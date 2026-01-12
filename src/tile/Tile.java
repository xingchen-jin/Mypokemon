package tile;
import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage[] images;      //用于存储动画瓦片的多帧图像
    public BufferedImage image;
    public boolean collision = false;   //false表示可通行
    public boolean isAnimated = false;  //是否为动画瓦片
    public int animationSpeed = 10;     //动画速度，单位为帧数
    public boolean encounter = false;   //false表示不可遇敌
    
}
