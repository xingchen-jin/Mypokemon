package utils;

import main.GamePanel;

public class CameraViewport{
    public int wMinX,wMinY,wMaxX,wMaxY;         //世界在摄像机上的边界
    public int wMinCol,wMinRow,wMaxCol,wMaxRow;
    private GamePanel gp;
    public CameraViewport(GamePanel gp,int worldMinX,int worldMinY,int worldMaxX,int worldMaxY){
        this.gp = gp;
        this.wMinX=worldMinX;this.wMinY = worldMinY;
        this.wMaxX = worldMaxX;this.wMaxY = worldMaxY;
        this.wMinCol = wMinX/gp.tileSize;
        this.wMinRow = wMinY/gp.tileSize;
        this.wMaxCol = wMaxX/gp.tileSize;
        this.wMaxRow = wMaxY/gp.tileSize;
    }

    public static CameraViewport getBorder(GamePanel gp){
        int wMinX,wMinY,wMaxX,wMaxY;
        wMinX = gp.player.worldX - gp.player.screenX; wMinX = (wMinX>0)?wMinX:0;
        wMinY = gp.player.worldY - gp.player.screenY; wMinY = (wMinY>0)?wMinY:0;
        wMaxX = gp.player.worldX + gp.screenWidth - gp.player.screenX;  wMaxX = (wMaxX < gp.worldWidth)?wMaxX : gp.worldWidth;
        wMaxY = gp.player.worldY + gp.screenHeight - gp.player.screenY; wMaxY = (wMaxY < gp.worldHeight)?wMaxY : gp.worldHeight;
        
        return new CameraViewport(gp,wMinX,wMinY,wMaxX,wMaxY);
    }

}
