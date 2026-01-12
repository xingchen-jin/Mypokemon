package main;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;
    private int preGrasstileX, preGrasstileY;//上一个草丛瓦片的坐标
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    
    //检查实体与地图瓦片的碰撞
    public void checkTile(Entity entity){

        //获取实体的边界坐标
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        
        //获取实体边界的行列
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        
        //定义两个变量存储将要检测的瓦片编号
        int tileNum1=0, tileNum2=0;

        //获取地图的最大行列数
        int maxRow = gp.maxWorldRow;
        int maxCol = gp.maxWorldCol;


        //根据实体的移动方向，检查对应方向的瓦片是否有碰撞属性
        switch(entity.direction){
            case UP:
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if(entityTopRow < 0 || entityTopRow >= maxRow || entityLeftCol < 0 || entityLeftCol >= maxCol || entityRightCol < 0 || entityRightCol >= maxCol) return;    //防止数组越界
                //获取将要检测的两个瓦片编号
                tileNum1 = gp.tileManger.mapTileNum[entityTopRow][entityLeftCol]-gp.tileManger.firstgid[0];
                tileNum2 = gp.tileManger.mapTileNum[entityTopRow][entityRightCol]-gp.tileManger.firstgid[0];

                break;
            case DOWN:
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;

                if(entityBottomRow < 0 || entityBottomRow >= maxRow || entityLeftCol < 0 || entityLeftCol >= maxCol || entityRightCol < 0 || entityRightCol >= maxCol) return;
                
                tileNum1 = gp.tileManger.mapTileNum[entityBottomRow][entityLeftCol]-gp.tileManger.firstgid[0];
                tileNum2 = gp.tileManger.mapTileNum[entityBottomRow][entityRightCol]-gp.tileManger.firstgid[0];

                break;
            case LEFT:
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if(entityLeftCol < 0 || entityLeftCol >= maxCol || entityTopRow < 0 || entityTopRow >= maxRow || entityBottomRow < 0 || entityBottomRow >= maxRow) return;
                tileNum1 = gp.tileManger.mapTileNum[entityTopRow][entityLeftCol]-gp.tileManger.firstgid[0];
                tileNum2 = gp.tileManger.mapTileNum[entityBottomRow][entityLeftCol]-gp.tileManger.firstgid[0];
                break;
            case RIGHT:
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if(entityRightCol < 0 || entityRightCol >= maxCol || entityTopRow < 0 || entityTopRow >= maxRow || entityBottomRow < 0 || entityBottomRow >= maxRow) return;
                tileNum1 = gp.tileManger.mapTileNum[entityTopRow][entityRightCol]-gp.tileManger.firstgid[0];
                tileNum2 = gp.tileManger.mapTileNum[entityBottomRow][entityRightCol]-gp.tileManger.firstgid[0];
                break;

        }
        
        if(gp.tileManger.tile[0][tileNum1].collision || gp.tileManger.tile[0][tileNum2].collision){
            entity.collisiOn = true;
        }
        if(gp.tileManger.tile[0][tileNum1].encounter || gp.tileManger.tile[0][tileNum2].encounter){
            if(gp.tileManger.tile[0][tileNum1].encounter) gp.tileManger.tile[0][tileNum1].isAnimated = true;    //切换为动画草丛
            if(gp.tileManger.tile[0][tileNum2].encounter) gp.tileManger.tile[0][tileNum2].isAnimated = true;
            entity.onGrass = true;
        }else{
            entity.onGrass = false;
        }
        

        
    }

    //检查实体与物体的碰撞
    public int checkObject(Entity entity, boolean player){
        int index = -1;

        for(int i=0; i<gp.obj.length; i++){
            if(gp.obj[i] != null){

                //获取实体的碰撞箱位置
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //获取物体的碰撞箱位置
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
                switch(entity.direction){
                    case UP:
                        entity.solidArea.y -= entity.speed;
                        break;
                    case DOWN:
                        entity.solidArea.y += entity.speed;
                        break;
                    case LEFT:
                        entity.solidArea.x -= entity.speed;
                        break;
                    case RIGHT:
                        entity.solidArea.x += entity.speed;
                        break;
                    default: break;
                }
                //检测碰撞
                if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                    
                    if(gp.obj[i].collision){
                        entity.collisiOn = true;
                    }
                    if(player){
                        index = i;
                    }
                }

                //恢复原始坐标
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    //NPC碰撞检测
    public int checkEntity(Entity entity,Entity[] target){
        int index = -1;

        for(int i=0; i<target.length; i++){
            if(target[i] != null){

                //获取实体的碰撞箱位置
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //获取物体的碰撞箱位置
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
                switch(entity.direction){
                    
                    case UP:
                        entity.solidArea.y -= entity.speed;
                        break;
                    case DOWN:
                        entity.solidArea.y += entity.speed;
                        break;
                    case LEFT:
                        entity.solidArea.x -= entity.speed;
                        break;
                    case RIGHT:
                        entity.solidArea.x += entity.speed;
                        break;
                }
                
                //检测碰撞
                if(entity.solidArea.intersects(target[i].solidArea)){
                    if(target[i] != entity){       
                        entity.collisiOn = true;
                        index = i;
                    }
                }

                //恢复原始坐标
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public void checkPlayer(Entity entity){

        //获取实体的碰撞箱位置
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        //获取玩家的碰撞箱位置
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        switch(entity.direction){
            case UP:
                entity.solidArea.y -= entity.speed;
                break;
            case DOWN:
                entity.solidArea.y += entity.speed;
                break;
            case LEFT:
                entity.solidArea.x -= entity.speed;
                break;
            case RIGHT:
                entity.solidArea.x += entity.speed;
                break;
        }
                
        //检测碰撞
        if(entity.solidArea.intersects(gp.player.solidArea)){
                    
            entity.collisiOn = true;
        }
        //恢复原始坐标
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        
    }


}
