package item;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.UtilityTool;

public class Item {
    public  GamePanel gp;
    public BufferedImage image;
    public String name;
    public String description;
    public boolean isBattleItem;    //是否为战斗中使用的道具
    public boolean isBallItem;       //是否为精灵球类道具
    public int animationSpeed = 10;         //动画速度，单位为帧数

    public Item(GamePanel gp){
        this.gp = gp;
    }

    public String getName(){
        return name;
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/" + imagePath + ".png"));
             image = uTool.scaleImage(image,gp.tileSize,gp.tileSize); //gp.tileSize, gp.tileSize      //玩家由1*2个瓦片构成
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
    public BufferedImage getFrame(int frameIndex){
        return null;
    }
}
