package tile;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import main.GamePanel;

public class BattleTileManger extends TileManger{
    public enum TileType{
        PLAIN,
        TEXT_WINDOW_09,
        HEALTHBOX_SINGLES_PLAYER,
        HEALTHBOX_SINGLES_OPPONENT,
        HPBAR;
    }

    public enum BattleUIElemnt{
        FRAME_MAIN,
        INFO,
        HP;
    }

    public int[][][] frameTileNum;  
    public BattleTileManger(GamePanel gp){
       super(gp);
    }

    @Override
    public void InitTile(){
        this.scale =2;
        updateTileSize();
        firstgid = new int[]{1,113,122,250,314};
        tile = new Tile[TileType.values().length][];
        frameTileNum = new int[BattleUIElemnt.values().length][gp.screenHeight/tileSize][gp.screenWidth/tileSize];
        
        tile[TileType.PLAIN.ordinal()]=getTileImage("/res/battle/ui/tiles.png");
        tile[TileType.TEXT_WINDOW_09.ordinal()]=getTileImage("/res/text_window/9.png");
        tile[TileType.HEALTHBOX_SINGLES_PLAYER.ordinal()]=getTileImage("/res/battle/ui/healthbox_singles_player.png");
        tile[TileType.HEALTHBOX_SINGLES_OPPONENT.ordinal()]=getTileImage("/res/battle/ui/healthbox_singles_opponent.png");
        tile[TileType.HPBAR.ordinal()]=getTileImage("/res/battle/ui/hpbar.png");

        loadMap(frameTileNum[BattleUIElemnt.FRAME_MAIN.ordinal()],"/res/battle/scene/scene_001_plain/frame_main.csv");
        loadMap(frameTileNum[BattleUIElemnt.INFO.ordinal()],"/res/battle/scene/scene_001_plain/info.csv");     


    }


    //创建瓦片图块
    public Tile[] getTileImage(String imagePath) {
 
        int i, j, index;
        try {
            originImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        
        originImage.flush();    //释放内存
        return tile;

    }

    public void drawOverlay(Graphics2D g2, int[][] layout) {
        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                int tileNum = layout[row][col];
                if (tileNum == 0) continue;
                for (int gid = 0; gid < firstgid.length; gid++) {
                    if (firstgid[gid] + tile[gid].length > tileNum) {
                        int x = col * tileSize;
                        int y = row * tileSize;
                        g2.drawImage(tile[gid][tileNum - firstgid[gid]].image, x, y, null);
                        break;
                    }
                }
            }
        }
    }
    
}
