package main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import entity.Entity;
import object.OBJ_Heart;



import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class UI extends SuperUI{
    // GamePanel gp;
    // Font maruMonica,purisaB;
    // BufferedImage keyImage;   //钥匙图标

    //玩家生命值图标（待删除）
    BufferedImage heart_full,heart_half,heart_blank;

    public boolean messageOn = false;   //是否显示信息
    public String message = "";        //信息内容
    int messageCounter = 0;             //信息计时器
    public String currentDialog = "";
    public int commandNum = 1;          //菜单“指针”
    public int maxOptions = 2;          //设置选项最大数量

    public int titleScreenState = 0;  //区分标题状态 


    public enum menuState{
        main,       //主菜单
        options,    //选项菜单
        item,        //背包菜单
        pokemon,    //精灵菜单
    }
    public menuState currentMenuState = menuState.main;

    public UI(GamePanel gp){
        super(gp);

        //HUD OBJECT(待删除)
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    //传入信息内容,显示信息
    public void showMessage(String text){
        message = text;
        messageOn = true;

    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        //抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        if (gp.UniFont != null) {
            g2.setFont(gp.UniFont.deriveFont(Font.BOLD, 27F));
        } else {
            g2.setFont(new Font(Font.SERIF, Font.BOLD, 27));
        }
        g2.setColor(Color.white);

        //标题
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        else if(gp.gameState == gp.playState){
            drawPlayerLife();
            if(messageOn){
                g2.setFont(g2.getFont().deriveFont(30F));   //设置信息字体大小
                g2.drawString(message, gp.tileSize/2, gp.tileSize*3);
                messageCounter++;
                if(messageCounter >60){   //显示1秒后隐藏信息
                    messageCounter =0;
                    messageOn = false;
                }
            }
        }

        else if(gp.gameState == gp.menuState){
            switch (currentMenuState) {
                case main:
                    drawPlayerLife();
                    drawMenuScreen();
                    break;
                case item:
                    drawInventory();
                    break;
                case options:
                    drawOptionsScreen();
                    break;
                case pokemon:
                    drawPokemonScreen();
                    break;
                default:
                    break;
            }
 
        }

        else if(gp.gameState == gp.dialogState){
            drawDialogueScreen();
        }

        else if(gp.gameState == gp.characterState){
            drawInventory();
        }



    }

    public void drawPlayerLife(){
        int temp = gp.tileSize/2;
        int x = temp;
        int y = temp;

        //绘制空生命
        for(int i=0;i<gp.player.maxLife/2;i++,x+=gp.tileSize){
            g2.drawImage(heart_blank, x, y,null);
        }
        //绘制血量
        x = temp;
        y = temp;
        for(int i=0;i<gp.player.life;i+=2,x+=gp.tileSize){
            if(i+1 < gp.player.life){
                g2.drawImage(heart_full, x, y,null);
            }else{
                g2.drawImage(heart_half, x, y,null);
                break;
            }
            
        }
    }

    public void drawTitleScreen(){

        //标题主界面
        if(titleScreenState == 0){
            //背景色
            g2.setColor(new Color(70,120,80));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

            //标题名称
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,66F));
            String text = "口袋妖怪 绿宝石";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;
            //  阴影色
            g2.setColor(Color.black);
            g2.drawString(text, x+3, y+3);
            //  主体色
            g2.setColor(Color.white);
            g2.drawString(text, x, y); 
            //菜单
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize * 5;
            g2.drawString(text, x, y); 
            if(commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "CONTINUE";  
            x = getXforCenteredText(text);
            y += gp.tileSize+gp.tileSize/4;
            g2.drawString(text, x, y); 
            if(commandNum == 2){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "QUIT";
            x = getXforCenteredText(text);
            y += gp.tileSize+gp.tileSize/4;
            g2.drawString(text, x, y); 
            if(commandNum == 3){
                g2.drawString(">", x-gp.tileSize, y);
            }
        }

        else if(titleScreenState == 1){
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(32F));
            String text = "你是男孩还是女孩";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*2;
            g2.drawString(text, x, y);

            text = "男孩";
            x = getXforCenteredText(text)-gp.tileSize*3;
            y += gp.tileSize*3;
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }
            text = "女孩";
            x += gp.tileSize*6;
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "返回标题";
            x = getXforCenteredText(text);
            y = gp.screenHeight - gp.tileSize*2;
            g2.drawString(text, x, y);
            if(commandNum == 3){
                g2.drawString(">", x-gp.tileSize, y);
            }

        }


    }
    public void drawPauseScreen(){
        String text = "PAUSED";
        g2.setFont(g2.getFont().deriveFont(80F));
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2 + gp.tileSize/2;
        
        g2.drawString(text,x,y);
    }
    public void drawDialogueScreen(){
        //窗口
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int widht = gp.screenHeight;
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, widht, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,22F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialog.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }
    }    

    public void drawMenuScreen(){

        //菜单框体
        drawMenu(24,0,8,17);
        //绘制选项
        g2.setFont(g2.getFont().deriveFont(32F));
 

        String text;
        int x = uiTileM.tileSize*26;
        int y = uiTileM.tileSize*1 + 60;

        //选项间隔高度
        int height = uiTileM.tileSize * 2;  

        //绘制选项
        text = "宝可梦";
        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);
   
        if(commandNum == 1){
            g2.drawString(">", x - uiTileM.tileSize, y);
        }

        text = "背包";
        y += height;
        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);
        if(commandNum == 2){
            g2.drawString(">", x-uiTileM.tileSize, y);
        }
        
        text = "设置";
        y += height;
        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);

        if(commandNum == 3){
            g2.drawString(">", x - uiTileM.tileSize, y);
        }

        text = "返回";
        y += height;

        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);

        if(commandNum == 4){
            g2.drawString(">", x - uiTileM.tileSize, y);
        }
        
        text = "标题";
        y += height;

        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);

        if(commandNum == 5){
            g2.drawString(">", x - uiTileM.tileSize, y);
        }

        text = "退出";
        y += height;
        g2.setColor(shadowColor);
        g2.drawString(text, x+2, y+2);
        g2.setColor(normalColor);
        g2.drawString(text, x, y);
        if(commandNum == 6){
            g2.drawString(">", x - uiTileM.tileSize, y);
        }
    }


    //设置
    public void drawOptionsScreen(){

        int x = 4*gp.tileSize;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - 8*gp.tileSize;
        int height = gp.tileSize * 8;
        drawSubWindow(x, y, width, height);
        String text;
        text = "OPTIONS";
        g2.setFont(g2.getFont().deriveFont(32F));
        int tempX = getXforCenteredText(text, width);
        y += gp.tileSize;
        g2.drawString(text, x+tempX, y);

        //音乐
        g2.setFont(g2.getFont().deriveFont(22F));
        text = "音乐";
        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString(text, x, y);
        g2.drawRect(x + 2*gp.tileSize, y - gp.tileSize/3, 4*gp.tileSize, gp.tileSize/2);
        //音量指示条
        int volumeWidth = (int)(gp.music.volumeIndex * gp.tileSize);
        g2.fillRect(x + 2*gp.tileSize, y - gp.tileSize/3, volumeWidth, gp.tileSize/2);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize/2, y);
        }

        //音效
        text = "音效";
        y += gp.tileSize;
        g2.drawString(text, x, y);
        g2.drawRect(x + 2*gp.tileSize, y - gp.tileSize/3, 4*gp.tileSize, gp.tileSize/2);
        //音效指示条
        int seVolumeWidth = (int)(gp.se.volumeIndex * gp.tileSize);
        g2.fillRect(x + 2*gp.tileSize, y - gp.tileSize/3, seVolumeWidth, gp.tileSize/2);
        if(commandNum == 2){
            g2.drawString(">", x - gp.tileSize/2, y);
        }

    }

    //DEBUG
    public void Debug_drawCollisionBox(int x,int y,int width,int height){
        // System.out.println("x:"+x+"y"+y+);
        // g2.setColor(Color.red);
        g2.drawRect(x,y,width,height);
        g2.setColor(new Color(0, 255, 255, 100));
        g2.drawRect(x-1,y-1,width+2,height+2);
            //   g2.fillRoundRect(x,y,width,height,35,35);   //圆矩形
        
        
    }
}
