package move;

import java.net.URL;


import java.awt.image.BufferedImage;

public class Ciallo extends Move{
    //音效
    private String soundEffect = "ciallo~(∠・ω )⌒.wav";
    //动画
    private BufferedImage animationImage;

    public Ciallo(){
        ID = 0721;
        name = "Ciallo";
        maxPP = 5;
        PP = maxPP;
        power = 150;
        accuracy = 90;
        moveType = MoveType.SPECIAL;
        isHitMove = true;
        isSoundEffect = true;
        isAnimated = true;
        //加载动画图像
        setup("/res/battle/effect/ciallo.png");
    }
    public URL getSoundEffect(){
        return getClass().getResource("/res/sound/" + soundEffect);
    }
    private void setup(String filePath){
        animationImage = null;
        try{
            animationImage = javax.imageio.ImageIO.read(getClass().getResourceAsStream(filePath));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public BufferedImage getAnimationImage(){
        return animationImage;
    }


}
