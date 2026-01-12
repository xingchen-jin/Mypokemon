package tile;


import main.GamePanel;
import utils.CameraViewport;
import utils.UtilityTool;

import javax.imageio.ImageIO;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManger {

    GamePanel gp;    //游戏面板引用
    public Tile[][] tile;            //瓦片数组,tile[ID][tile]

    //各图块集的全局ID前置值,firstgid[ID]
    public int[] firstgid;
    int scale =1;
    public int tileSize;
    public int frameCounter =0; 

    //图层
    public int mapTileNum[][];    
    public int mapTileNum2[][];     
    BufferedImage originImage;

    public TileManger(GamePanel gp) {
        this.gp = gp;
        InitTile();   

    }

    protected void updateTileSize(){
        tileSize = gp.tileSize/scale;
    }

    public void InitTile(){
        scale =1;
        updateTileSize();
        firstgid = new int[]{1,657};
        tile = new Tile[2][];
        
        mapTileNum = new int[gp.maxWorldRow*scale][gp.maxWorldCol*scale];    //双图层
        mapTileNum2 = new int[gp.maxWorldRow*scale][gp.maxWorldCol*scale];    

        tile[0]=getTileImage("/res/tiles/tileSet.txt","tiles_01",0);
        loadMap(mapTileNum,"/res/maps/world_01_01.csv");       //world_世界编号_图层编号
        tile[1]=getTileImage("/res/tiles/tileSet.txt","tiles_02",1);
        loadMap(mapTileNum2,"/res/maps/world_01_02.csv");
    }

    //创建瓦片图块，通过读取整合图块及碰撞箱子设置用文本来进行初始化
    public Tile[] getTileImage(String tileSetFilePath,String imageName,int id) {
 
        int i, j, index;
        java.util.List<Integer> tileSetList = new java.util.ArrayList<>();
        try {
            originImage = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imageName + ".png"));
            InputStream is = getClass().getResourceAsStream(tileSetFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    part = part.trim();
                    if (!part.isEmpty()) {
                        tileSetList.add(Integer.parseInt(part));
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] tileSet = tileSetList.stream().mapToInt(Integer::intValue).toArray();

        index = 0;
        int tileWidth = gp.originalTileSize/scale;  //原图单图块大小
        int tileHeight = gp.originalTileSize/scale;
        int imgWidth = originImage.getWidth();
        int imgHeight = originImage.getHeight();
        int n = (imgHeight/tileHeight) * (imgWidth/tileWidth);
        Tile[] tile = new Tile[n];
        for (i = 0; i + tileHeight <= imgHeight; i += tileHeight) {
            for (j = 0; j + tileWidth <= imgWidth; j += tileWidth) {
                setup(tile,index, j, i,tileWidth,tileHeight);
                index++;
            }
        }
        for(i=0;i<tileSet.length;i++){

            //只有id为0图块集需设置碰撞
            if(tileSet[i] != 0 && tileSet[i] < tile.length && id == 0){
                tile[tileSet[i]].collision = true;
            }else{
                break;
            }
        }

        if(id == 0 && tileSet.length > 13){
            UtilityTool utils = new UtilityTool(gp);
            tile[13].encounter = true;
            tile[13].isAnimated = false;     //初始设置为非动画瓦片
            tile[13].animationSpeed = 80;   //动画速度，单位为帧数
            tile[13].images = utils.splitImage("/res/tiles/grass.png",16,16,gp.tileSize/scale);
            tile[4].isAnimated = true;         //设置为动画瓦片
            tile[4].animationSpeed = 70;     //动画速度，单位为帧数
            tile[4].images = utils.splitImage("/res/tiles/flower.png",16,16,gp.tileSize/scale);
            
        }
        
        originImage.flush();    //释放内存
            // setup(5, "water", true);
        return tile;

    }

    //设置瓦片
    public void setup(Tile[] tile,int index,int x,int y,int tileWidth,int tileHeight) {
        UtilityTool uTool = new UtilityTool(gp);
        try {
            tile[index] = new Tile();
            tile[index].image = originImage.getSubimage(x,y,tileWidth,tileHeight);
            tile[index].image = uTool.scaleImage(tile[index].image, this.tileSize, this.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
            // System.err.println("  "+index);   
        }

    }

    //加载地图
    public void loadMap(int[][] mapTileNum,String filePath) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row, col;
            for(row = 0; row < mapTileNum.length; row++){
                String line = br.readLine();
                if(line == null) break;
                String numbers[] = line.split(",");
                for(col = 0; col < mapTileNum[row].length; col++){
                    if(col < numbers.length) {
                        int num = Integer.parseInt(numbers[col].trim());
  
                        mapTileNum[row][col] = num;
                    } else {
                        mapTileNum[row][col] = 0; // 默认填充为0，防止越界
                    }
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 绘制瓦片
    public void draw(Graphics2D g2,int[][] mapTileNum) {

        int worldRow,worldCol,worldX,worldY,x,y;

        CameraViewport cameraViewport =  CameraViewport.getBorder(gp);
        for(worldRow = cameraViewport.wMinRow-1;worldRow<cameraViewport.wMaxRow+1;worldRow++){
            for(worldCol = cameraViewport.wMinCol-1;worldCol<cameraViewport.wMaxCol+1;worldCol++){
                worldX = worldCol * gp.tileSize;
                worldY = worldRow * gp.tileSize;
                x = worldX - gp.player.worldX + gp.player.screenX;    //屏幕上的坐标,保证玩家在屏幕中央
                y = worldY - gp.player.worldY + gp.player.screenY;
                int tileNum = 0;
                if (worldRow >= 0 && worldRow < mapTileNum.length && 
                    worldCol >= 0 && worldCol < mapTileNum[worldRow].length) {
                    tileNum = mapTileNum[worldRow][worldCol];
                } else {
                    // 跳过这个图块
                    continue;
                }

                //仅绘制在屏幕范围内的瓦片    
                if(x + gp.tileSize > 0 && x - gp.tileSize < gp.screenWidth &&
                   y + gp.tileSize > 0 && y - gp.tileSize < gp.screenHeight && 
                   tileNum != 0){
                    for(int i = 0;i<firstgid.length;i++){
                        //寻找对应图块
                        if(firstgid[i]+tile[i].length > tileNum){
                            
                            g2.drawImage(tile[i][tileNum-firstgid[i]].image, x, y,null);
                             if(tile[i][tileNum-firstgid[i]].isAnimated){
                                drawAnimatedTile(g2,x,y,tile[i][tileNum-firstgid[i]]);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void drawAnimatedTile(Graphics2D g2, int x, int y, Tile tile) {
        // 计算当前动画帧   
        if(frameCounter > tile.animationSpeed * tile.images.length) {
            frameCounter = 0; // 重置计数器以循环动画
            if(tile.encounter){
                tile.isAnimated = false; // 停止动画
            }
        }
        int frameIndex = (frameCounter / tile.animationSpeed) % tile.images.length;
        frameCounter++;

        if(frameIndex < 0 || frameIndex >= tile.images.length) {
            frameIndex = 0; // 安全检查，防止数组越界
        }
        BufferedImage currentFrame = tile.images[frameIndex];
        g2.drawImage(currentFrame, x, y, null);
       
    }
}




