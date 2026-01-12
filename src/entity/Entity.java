package entity;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import item.*;
import main.GamePanel;
import utils.UtilityTool;


public class Entity {

    GamePanel gp;
    public int worldX,worldY;   //实体在世界中的坐标,区别于screenX,screenY;
    public int speed;

    public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2,idle_up,idle_down,idle_left,idle_right;
    public Direction direction = Direction.DOWN;    //精灵方向
    public Boolean isIdle = false;  //判断是否静止
    
    public int spriteCounter = 0;   //动画计数器
    public int spriteNum = 1;       //当前精灵表图片编号
    public int fishSpriteNum = 1;

    public Rectangle solidArea = new Rectangle(0,0,48,48);     //碰撞箱
    public int solidAreaDefaultX = solidArea.x, solidAreaDefaultY=solidArea.y; //碰撞箱默认坐标
    public boolean collisiOn = false;
    public boolean onGrass = false;     //是否在草丛中
    public int actionLockCounter = 0;   

    String dialogues[] = new String[20];
    int dialogueIndex = 0;

    public BufferedImage image,image2,image3;
    public String name;
    public boolean collision = false;
    public boolean isFishing = false;

    //角色状态(带删除)
    public int maxLife;
    public int life;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){}

    //基本交互处理
    public void speak(){
        if(dialogues[dialogueIndex] == null)  
            dialogueIndex=0;
        gp.ui.currentDialog = dialogues[dialogueIndex];
        dialogueIndex++;
        switch(gp.player.direction){
            case UP: direction = Direction.DOWN;break;
            case DOWN: direction = Direction.UP;break;
            case LEFT: direction = Direction.RIGHT;break;
            case RIGHT: direction = Direction.LEFT;break;
        }
    }
    

    public void update(){
        setAction();

        collisiOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkPlayer(this);

        // //如果没有碰撞，执行移动
        if(!collisiOn){

           switch(direction){
            case UP: worldY -= speed; break;
            case DOWN: worldY += speed; break;
            case LEFT: worldX -= speed; break;
            case RIGHT: worldX += speed; break;

            }
        }

        //精灵动画
        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum ==2){
                spriteNum =0;
            }
            spriteNum++;
            spriteCounter =0;
        }     

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int x = worldX - gp.player.worldX + gp.player.screenX;    //屏幕上的坐标,保证玩家在屏幕中央
        int y = worldY - gp.player.worldY + gp.player.screenY;
        if(x + gp.tileSize > 0 && x - gp.tileSize < gp.screenWidth &&
            y + gp.tileSize > 0 && y - gp.tileSize < gp.screenHeight){
            
            switch(direction){
            case UP:
                if(spriteNum ==1){
                    image = up1;
                }else if(spriteNum ==2){
                    image = up2;
                }

                break;
            case DOWN:
                if(spriteNum ==1){
                    image = down1;
                }else if(spriteNum ==2){
                    image = down2;
                }
                break;
            case LEFT:
                if(spriteNum ==1){
                    image = left1;
                }else if(spriteNum ==2){
                    image = left2;
                }
                break;
            case RIGHT:
                if(spriteNum ==1){
                    image = right1;
                }else if(spriteNum ==2){
                    image = right2;
                }
                break;

            // case "idle_up": image = idle_up; break;
            // case "idle_down": image = idle_down; break;
            // case "idle_left": image = idle_left; break;
            // case "idle_right": image = idle_right; break;

        }
            if(image != null){
                g2.drawImage(image, x, y,  null);
            }
        }
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/" + imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);      //玩家由1*2个瓦片构成
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage setup(String imagePath,int col,int row){

        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/" + imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize*col, gp.tileSize*row);      //玩家由1*2个瓦片构成
        }catch(IOException e){
           
            e.printStackTrace();
            
        }
        return image;
    }

    public void setItem(Item item) {
        
    }
    public Item getItem(){
        return null;
    }
}
