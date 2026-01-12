package item;

import java.awt.image.BufferedImage;

import main.GamePanel;
import utils.UtilityTool;

public class ITEM_Poke_Ball extends Item{
    protected BufferedImage[] frameImages;

    private int captureRate;        //捕捉概率,百分制

    public ITEM_Poke_Ball(GamePanel gp){
        super(gp);
        name = "精灵球";
        description = "用于捕捉野生宝可梦的道具。";
        image = setup("item/poke_ball");
        setFrameImages("/res/item/balls/poke.png");
        isBallItem = true;
        isBattleItem = true;
        captureRate = 50;
    }
    public void setFrameImages(String imagePath){
        UtilityTool uTool = new UtilityTool(gp);
        frameImages = uTool.splitImage(imagePath, 16,16,gp.tileSize);
    }
    public int getCaptureRate(){
        return captureRate;
    }

    public void setCaptureRate(int rate){
        this.captureRate = rate;
    }
    public BufferedImage getFrame(int frameIndex){
        if(frameImages == null || frameIndex < 0 || frameIndex >= frameImages.length){
            return null;
        }
        return frameImages[frameIndex];
    }

}
