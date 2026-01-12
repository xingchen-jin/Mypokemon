package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;

import tile.Tile;
import tile.UITileManger;


public class SuperUI {
    public GamePanel gp;
    public Graphics2D g2;
    public int screenWidth;
    public int screenHeight;
    public UITileManger uiTileM;

    public Color shadowColor = new Color(216,208,176);       //阴影色
    public Color dialogColor   =   new Color(255,255,255);       //对话文本主色
    public Color normalColor =  new Color(64,64,64);     //正常文字色,颜色为深灰色
    
    // 图标动画
    private int iconSpriteCounter =0;
    private int iconSpriteCounterMax =10;
    private int iconSpriteNum =0;
    private int iconFrameMax =2;

    //菜单指针
    public int slotCol = 0;     
    public int slotRow = 0; 
    public final  int slotColMax =4;
    public final  int slotRowMax =7;
    public int pokemonSlotColMax =1;
    public int pokemonSlotRowMax =5;
    public int prePokeMenuIndex =-1;     //上一次菜单选中位置
    public int pokeMenuIndex =-1;        //当前菜单选中位置


    public SuperUI(GamePanel gp){
        this.gp = gp;
        this.screenWidth = gp.screenWidth;
        this.screenHeight = gp.screenHeight;
        uiTileM = new UITileManger(gp);

    }

    //绘制菜单框架
    public void drawMenu(int col,int row,int width,int height){
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                Tile tempTile;
                //四角
                if(i==0 && j==0){
                    // 左上角
                    tempTile = uiTileM.tile[0][0];
                }else if(i==0 && j==width-1){
                    // 右上角
                    tempTile = uiTileM.tile[0][2];
                }else if(i==height-1 && j==0){
                    // 左下角
                    tempTile = uiTileM.tile[0][6];
                }else if(i==height-1 && j==width-1){
                    // 右下角
                    tempTile = uiTileM.tile[0][8];
                }
                //边框
                else if(i==0){
                    // 上边框
                    tempTile = uiTileM.tile[0][1];
                }else if(i==height-1){
                    // 下边框
                    tempTile = uiTileM.tile[0][7];
                }else if(j==0){
                    // 左边框
                    tempTile = uiTileM.tile[0][3];
                }else if(j==width-1){
                    // 右边框
                    tempTile = uiTileM.tile[0][5];
                }
                // 最后是中间
                else{
                    tempTile = uiTileM.tile[0][4];
                }

                int x = (col + j) * uiTileM.tileSize;
                int y = (row + i) * uiTileM.tileSize;
                if (tempTile != null && tempTile.image != null) {
                    g2.drawImage(tempTile.image, x, y, null);
                }
            }
        }

        

    }

    public void drawSubWindow(int x,int y,int width,int height){
        Color c = new Color(0,0,0,100);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);   //圆矩形
        
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);

    }
    public void drawSubWindow(int x,int y,int width,int height, Color bgColor, Color borderColor){
        //带自定义颜色的子窗口 bgColor:背景色 borderColor:边框色 
        Color c = bgColor;
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);   //圆矩形
        
        c = borderColor;
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);



    }
    public void drawPokemonScreen(){

        //填充背景
        Color bgColor = new Color(100,149,237); // 淡蓝色背景
        g2.setColor(bgColor);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int tileSize = uiTileM.tileSize;
        //间隔
        int gap = tileSize/4;

        //左部分
        int leftX = tileSize *2;
        int leftY = tileSize*3;
        int leftWidth = tileSize*10;
        int leftHeight = tileSize*6;
        if(slotCol == 0 || prePokeMenuIndex ==0){
            drawSubWindow(leftX,leftY,leftWidth,leftHeight, new Color(255,255,0,150), Color.yellow);
        }else{
            drawSubWindow(leftX,leftY,leftWidth,leftHeight);
        }

        //右部分
        int rightX = 15*tileSize;
        int rightY = 0;
        int rightWidth = tileSize*16;
        int rightHeight = tileSize*4;
        //左边五个框
        for(int i=0;i<5;i++){
            int boxX = rightX;
            int boxY = rightY + i*rightHeight;
            if((slotCol == 1 && slotRow == i) || prePokeMenuIndex == i+1){
                drawSubWindow(boxX,boxY,rightWidth,rightHeight, new Color(255,255,0,150), Color.yellow);
            }else{  
                drawSubWindow(boxX,boxY,rightWidth,rightHeight);
            }
        }

        //左部分
        //宝可梦图标
        iconSpriteCounter++;
        if(iconSpriteCounter > iconSpriteCounterMax){
            iconSpriteNum++;
            if(iconSpriteNum >= iconFrameMax){
                iconSpriteNum =0;
            }
            iconSpriteCounter =0;
        }
        BufferedImage icon = gp.player.party.getCurPokemon().icon[iconSpriteNum];
        int iconX = leftX;
        int iconY = leftY + gap;
        g2.drawImage(icon, iconX, iconY, null);

        //宝可梦名称
        String pName = gp.player.party.getCurPokemon().name;
        int nameX = leftX+getXforCenteredText(pName, leftWidth);
        int nameY = leftY + leftHeight/2-gap;
        
        g2.setFont(gp.UniFont.deriveFont(Font.BOLD,24F));

        g2.setColor(Color.white);
        g2.drawString(pName, nameX, nameY);
        //等级
        String level = "Lv."+gp.player.party.getCurPokemon().level;
        int levelX = nameX + getXforCenteredText(level, g2.getFontMetrics().stringWidth(pName));
        int levelY = nameY+g2.getFontMetrics().getHeight();
        g2.drawString(level, levelX, levelY);

        //生命值(血条)
        int hpBarX = leftX + tileSize;
        int hpBarY = levelY + g2.getFontMetrics().getHeight()-gap;
        int hpBarWidth = leftWidth - 6*gap;
        int hpBarHeight = tileSize/2;
        //血条背景
        g2.setColor(Color.gray);
        g2.fillRect(hpBarX, hpBarY, hpBarWidth, hpBarHeight);
        //血条前景
        double hpRatio = (double)gp.player.party.getCurPokemon().curHp / gp.player.party.getCurPokemon().maxHp;
        int hpWidth = (int)(hpBarWidth * hpRatio);
        g2.setColor(Color.red);
        g2.fillRect(hpBarX, hpBarY, hpWidth, hpBarHeight);

        //血条信息
        String hpInfo = gp.player.party.getCurPokemon().curHp + "/" + gp.player.party.getCurPokemon().maxHp;
        int hpInfoX = leftX + getXforCenteredText(hpInfo, leftWidth);
        int hpInfoY = hpBarY + hpBarHeight;
        g2.setColor(Color.white);
        g2.drawString(hpInfo, hpInfoX, hpInfoY);

        //右部分
        //宝可梦
        for(int i=1;i<gp.player.party.size();i++){
            //宝可梦图标
            BufferedImage pIcon = gp.player.party.getPoke(i).icon[0];
            int pIconX = rightX + gap;
            int pIconY = rightY + (i-1)*rightHeight - gap;
            g2.drawImage(pIcon, pIconX, pIconY, null);
            //宝可梦名称
            String pkmName = gp.player.party.getPoke(i).name;
            int pkmNameX = pIconX + pIcon.getWidth() + gap;
            int pkmNameY = pIconY+ g2.getFontMetrics().getHeight()+gap+5;
            g2.setColor(Color.white);
            g2.drawString(pkmName, pkmNameX, pkmNameY);
            //宝可梦等级
            String pkmLevel = "Lv."+gp.player.party.getPoke(i).level;
            int pkmLevelX = pkmNameX+ getXforCenteredText(pkmLevel, g2.getFontMetrics().stringWidth(pkmName));
            int pkmLevelY = pkmNameY + g2.getFontMetrics().getHeight();
            g2.drawString(pkmLevel, pkmLevelX, pkmLevelY);
            //宝可梦血条
            int pkmHpBarX = pkmNameX+g2.getFontMetrics().stringWidth(pkmName)+gap;
            int pkmHpBarY = pkmNameY + gap;
            int pkmHpBarWidth = rightWidth - (pkmHpBarX - rightX) - 2*gap;
            int pkmHpBarHeight = 15;
            //血条背景
            g2.setColor(Color.gray);
            g2.fillRect(pkmHpBarX, pkmHpBarY, pkmHpBarWidth, pkmHpBarHeight);
            //血条前景
            double pkmHpRatio = (double)gp.player.party.getPoke(i).curHp / gp.player.party.getPoke(i).maxHp;
            int pkmHpWidth = (int)(pkmHpBarWidth * pkmHpRatio);
            g2.setColor(Color.red);
            g2.fillRect(pkmHpBarX, pkmHpBarY, pkmHpWidth, pkmHpBarHeight);

            //宝可梦血量信息
            String pkmHpInfo = gp.player.party.getPoke(i).curHp + "/" + gp.player.party.getPoke(i).maxHp;
            int pkmHpInfoX = pkmHpBarX + getXforCenteredText(pkmHpInfo, pkmHpBarWidth);
            int pkmHpInfoY = pkmHpBarY + pkmHpBarHeight;
            g2.setColor(Color.white);
            g2.drawString(pkmHpInfo, pkmHpInfoX, pkmHpInfoY);
        }
        //信息框
        int infoX = 0;
        int infoY = 20;
        int infoWidth = 24;
        int infoHeight = 4;
        drawMenu(infoX, infoY, infoWidth, infoHeight); 
        String infoText = "请选择你的宝可梦";
        int infoTextX = infoX + getXforCenteredText(infoText, infoWidth*tileSize);
        int infoTextY = infoY*(tileSize+3);
        g2.setColor(normalColor);
        g2.drawString(infoText, infoTextX, infoTextY);

        

    }
        public void drawInventory(){

        //框体
        int frameX = gp.screenWidth - gp.tileSize*6+gp.tileSize/2;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*5;
        int frameHeight = gp.tileSize*8;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        //槽位
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        slotCol = (slotCol>0)?slotCol:0; slotCol = (slotCol<=3)?slotCol:3;
        slotRow = (slotRow>0)?slotRow:0; slotRow = (slotRow<=6)?slotRow:6;

        //物品
        for(int i=0;i<gp.player.inventory.size();i++){
            if(gp.player.inventory.get(i) != null)
                g2.drawImage(gp.player.inventory.get(i).image,slotX,slotY,null);
                 int quantity = gp.player.inventory.getQuantity(i);
                 if(quantity > 1){
                    g2.setFont(g2.getFont().deriveFont(16F));
                    g2.setColor(Color.white);
                    g2.drawString("x"+quantity, slotX+(int)(0.6*gp.tileSize), slotY+gp.tileSize-4);
                 }
                slotX += gp.tileSize;
                if((slotX-slotXstart)/gp.tileSize>3){
                    slotX = slotXstart;
                    slotY+=gp.tileSize;
                } 
  
        }

        int cursorX = slotXstart + (gp.tileSize*slotCol);
        int cursorY = slotYstart + (gp.tileSize*slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth,cursorHeight, 10, 10);

        int dFrameX = frameX - gp.tileSize;
        int dFrameY = frameY+frameHeight+10;
        int dFrameWidth = frameWidth+gp.tileSize;
        int dFrameHeight = gp.tileSize*2;
        

        int textX = dFrameX+20;
        int textY = dFrameY+gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(18F));

        int itemIndex = getItemIndexOnSlot();
        if(itemIndex < gp.player.inventory.size()){
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(line, textX, textY);
                textY += 24;
            }
        }

    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + slotRow*4;
        return itemIndex;
    }

    
    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public int getXforCenteredText(String text,int customWidth){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = customWidth/2 - length/2;
        return x;
    }

}
