package tile;
import java.awt.image.BufferedImage;
import utils.UtilityTool;
import main.GamePanel;
import javax.imageio.ImageIO;

public class UITileManger{
    GamePanel gp;
    public Tile[][] tile;  
    int scale =2; 
    public int tileSize;
    private UtilityTool uTool;
    public UITileManger(GamePanel gp) {
        this.gp = gp;
        uTool = new UtilityTool(gp);
        InitTile();

    }

    private void InitTile(){
        
        this.tileSize = gp.tileSize/scale;
        tile = new Tile[1][];

        tile[0]=getTileImage("/res/text_window/9.png");

    }

    //创建瓦片图块
    public Tile[] getTileImage(String imagePath) {
        BufferedImage originImage;
        try {
            originImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            return new Tile[0];
        }

        int cols = originImage.getWidth() / 8;
        int rows = originImage.getHeight() / 8;
        int count = Math.max(0, rows * cols);
        Tile[] tiles = new Tile[count];

        int index = 0;
        for (int y = 0; y + 8 <= originImage.getHeight(); y += 8) {
            for (int x = 0; x + 8 <= originImage.getWidth(); x += 8) {
                if (index >= tiles.length) break; // 防御，避免越界
                Tile t = new Tile();
                t.image = originImage.getSubimage(x, y, 8, 8);
                t.image = uTool.scaleImage(t.image, this.tileSize, this.tileSize);
                tiles[index++] = t;
            }
        }

        originImage.flush();
        return tiles;
    }

    
}
