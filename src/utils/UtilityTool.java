package utils;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;

//辅助工具类
public class UtilityTool {

    private GamePanel gp;

    public UtilityTool(GamePanel gp){
        this.gp = gp;
    }

    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        // 创建新的图像对象，支持透明度
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = scaledImage.createGraphics();
        
        // 关键：设置透明度混合模式
        g2.setComposite(AlphaComposite.Src);
        
        // 绘制前清除为透明
        g2.setBackground(new Color(0, 0, 0, 0));
        g2.clearRect(0, 0, width, height);
        
        // 绘制图像
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        
        return scaledImage;
    }

    public static int getRandomNumber(int min, int max){
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    // 对原图像进行分割，返回分割后的图块数组,缩放成tileSize大小
    // 注：缩放结果是正方形
    public  BufferedImage[] splitImage(String imagePath, int tileWidth, int tileHeight,int tileSize) {
        BufferedImage original;
        try {
            original = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }

        int cols = original.getWidth() / tileWidth;
        int rows = original.getHeight() / tileHeight;
        BufferedImage[] tiles = new BufferedImage[cols * rows];
        int index = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                tiles[index] = original.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                tiles[index] = scaleImage(tiles[index], tileSize, tileSize);
                index++;
            }
        }
        return tiles;
    }

}
