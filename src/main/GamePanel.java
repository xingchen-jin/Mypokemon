package main;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManger;
import battle.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    //游戏面板设置
    public final int originalTileSize = 16; //原始瓦片大小
    public final int scale = 3;    //缩放倍数

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;    //瓦片列数
    public final int maxScreenRow = 12;    //瓦片行数
    public final int screenWidth = tileSize * maxScreenCol;    
    public final int screenHeight = tileSize * maxScreenRow;

    //世界设置
    public final int maxWorldRow = 72;     //世界地图行数
    public final int maxWorldCol = 36;     //世界地图列数

    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    //最大参数设置
    public final int MAX_OBJ = 10;
    public final int MAX_NPC = 10;

    //帧率
    final int FPS = 60;

    //系统组件
    TileManger tileManger = new TileManger(this);   //瓦片管理器
    public KeyHandler keyH = new KeyHandler(this);             //键盘输入处理
    public Sound music = new Sound();               //音乐播放器
    public Sound se = new Sound();                  //音效播放器

    public CollisionChecker cChecker = new CollisionChecker(this);  //碰撞检测器
    public AssetSetter aSetter = new AssetSetter(this);             //物体设置器

    public BattleManger battleM = new BattleManger(this); //战斗管理器

    public UI ui = new UI(this);    //用户界面
    public BattleUI battleUI = new BattleUI(this,battleM); //战斗界面
    
    public BattleSystem battleSystem = new BattleSystem(this);
    public Font UniFont; //统一字体支持多语言
                                     
    public EventHandler eHandler = new EventHandler(this);
    
    Thread gamThread;               //游戏线程

    public Player player = new Player(this, keyH);                  //玩家实体
    public Entity[] obj = new Entity[MAX_OBJ];            //物体数组
    public Entity[] npc = new Entity[MAX_NPC];
    ArrayList<Entity> entityList = new ArrayList<>();

    //音乐
    private int preMusicIndex = -1; 
    private int curMusicIndex = -1;

    //游戏状态机
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int menuState = 2;
    public final int dialogState = 3; 
    public final int characterState = 4;
    public final int battleState = 5;

    public Object random;
  
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));     //设置面板大小
        this.setBackground(Color.BLACK);                  //设置背景颜色
        this.setDoubleBuffered(true);               //开启双缓冲

        this.addKeyListener(keyH);                    //添加键盘监听器
        this.setFocusable(true);           //面板可聚焦

        InputStream is = getClass().getResourceAsStream("/res/font/Unifont.ttf");
        
        if (is != null) {
            try {
                UniFont = Font.createFont(Font.TRUETYPE_FONT, is);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            } finally {
                try { is.close(); } catch (IOException ignored) {}
            }
        }else {
            System.out.println("无法加载字体文件，使用默认字体。");
        }
        

    }

    public int getPreMusicIndex(){
        return preMusicIndex;
    }
    public int getCurrentMusicIndex(){
        return curMusicIndex;
    }
    //游戏初始化(设置物体,播放音乐等)
    public void setupGame(){

        //音量设置
        music.setVolume(0.8f);  //实例clip还未创建
        se.setVolume(1.0f);
        se.volumeIndex = 4;
        // playMusic(0);

        gameState = titleState;

    }

    //启动游戏线程
    public void startGameThread(){
        gamThread = new Thread(this);
        gamThread.start();

    }

    @Override
    public void run(){

        double drawInterval = 1000000000/FPS;   //ns每帧
        double delta = 0;                       //时间差

        long lastTime = System.nanoTime();      //上次时间
        long currentTime;   //当前时间
        long timer = 0;     //计时器
        int drawCount = 0;  //帧数计数器

        while (gamThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            //执行下一帧
            if(delta >= 1){    
                update();   //刷新
                repaint();  //重绘，线程调用paintComponent方法
                delta--;
                drawCount++;
            }

            //FPS计数器
            if(timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }
    public void update(){
        if(gameState == playState){
            player.update();

            for(int i=0;i<npc.length;i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            
        }else if(gameState == menuState){

        }else if(gameState == battleState){
            //战斗更新
            battleM.update();
        }
        
        
    }

    //绘制
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //DEBUG
        long drawStart = 0L;
        if(keyH.showDebug){
            
            drawStart = System.nanoTime();
        }

        //标题界面
        if(gameState == titleState){
            ui.draw(g2);
        }else if(gameState == battleState){
            battleUI.draw(g2);
        }else{

            //绘制低图层
            tileManger.draw(g2,tileManger.mapTileNum); 

            //添加实体
            entityList.add(player);
            for(int i=0;i<npc.length;i++){
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }
            for(int i=0;i<obj.length;i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            //按高度顺序绘制
            Collections.sort(entityList,new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {
                   
                   int result = Integer.compare(e1.worldY + e1.solidArea.y, e2.worldY + e2.solidArea.y);
                   return result;
                }
            });

            for(int i=0;i<entityList.size();i++){
                if( entityList.get(i) != null)
                entityList.get(i).draw(g2);
            }

            entityList.clear();
            //绘制高图层
            tileManger.draw(g2,tileManger.mapTileNum2);

            ui.draw(g2);           
        }

        //DEBUG
        if (keyH.showDebug) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial",Font.PLAIN,20));
            g2.setColor(Color.white);
            int x = 10;int y=400;
            int lineHeight = 20;
            int tempSolidAreaX = player.worldX+player.solidArea.x;
            int tempSolidAreaY = player.worldY+player.solidArea.y;

            g2.drawString("worldX:"+player.worldX,x,y); y+=lineHeight;
            g2.drawString("worldY:"+player.worldY,x,y);y+=lineHeight;
            g2.drawString("Col:"+(tempSolidAreaX)/tileSize + " /orign: "+(player.worldX)/tileSize , x, y);y+=lineHeight;
            g2.drawString("Row:"+(tempSolidAreaY)/tileSize + " /orign: "+(player.worldY)/tileSize, x, y);y+=lineHeight;
            
            //绘制碰撞箱
            // ui.drawSubWindow(tempSolidAreaX,tempSolidAreaY, player.solidArea.width, player.solidArea.height);
            

            g2.drawString("Draw Time: " + passed + " ns", x, y);

            ui.Debug_drawCollisionBox(player.screenX+player.solidArea.x,player.screenY+player.solidArea.y, player.solidArea.width, player.solidArea.height);
            for(int i=0;i<npc.length;i++){
                if(npc[i]!=null){
                    ui.Debug_drawCollisionBox(npc[i].worldX-player.worldX+player.screenX +npc[i].solidArea.x,npc[i].worldY-player.worldY+player.screenY+npc[i].solidArea.y, npc[i].solidArea.width, npc[i].solidArea.height);
                }
            }

            for(int i=0;i<obj.length;i++){
                if(obj[i]!=null){
                    ui.Debug_drawCollisionBox(obj[i].worldX-player.worldX+player.screenX +obj[i].solidArea.x,obj[i].worldY-player.worldY+player.screenY+obj[i].solidArea.y, obj[i].solidArea.width, obj[i].solidArea.height);
                }
            }

            // ui.showMessage("Draw Time: " + passed + " ns");
            System.out.println("Draw Time: " + passed + " ns");
        }
        

        g2.dispose();

    }

    //播放音乐
    public void playMusic(int i){
        if(i<0 || i>=music.maxIndex){ 
            System.out.println("音乐索引错误");
            return; 
        }
        preMusicIndex = curMusicIndex;
        curMusicIndex = i;
        music.stop();
        music.setFile(i);
        music.play();
        music.loop();
    }
    //停止音乐
    public void stopMusic(){
        preMusicIndex = curMusicIndex;
        curMusicIndex = -1;
        music.stop();

    }
    //播放音效
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }

}
