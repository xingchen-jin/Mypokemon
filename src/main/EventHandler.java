package main;

import entity.Direction;

//事件处理
public class EventHandler {

    GamePanel gp;
    EventRect[][] eventRect;    //触发点

    int previousEventX,previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldRow][gp.maxWorldCol];
        int col;
        int row;
        
        for(row=0;row<gp.maxWorldRow;row++){
            for(col=0;col<gp.maxWorldCol;col++){
                eventRect[row][col] = new EventRect();
                eventRect[row][col].x = 22;
                eventRect[row][col].y = 22;
                eventRect[row][col].width = 4;
                eventRect[row][col].height = 4;
                eventRect[row][col].eventRectDefaultX = eventRect[row][col].x;
                eventRect[row][col].eventRectDefaultY = eventRect[row][col].y;
            }
        }


    }

    public void checkEvent(){

        //检测玩家距离上次触发事件的位置是否超过一定范围
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance,yDistance);
        if(distance>gp.tileSize){
            canTouchEvent = true;
        }
        if(canTouchEvent){
            if(hit(55,9,Direction.ANY)){
                damagePit(55,9,gp.dialogState);
            }
            if(hit(55,13,Direction.UP)){
                healingPool(55,13,gp.dialogState);   
            }
        }

    }

    public boolean hit(int row,int col,Direction reqDirection){
        boolean hit = false;

        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;
        eventRect[row][col].x += col*gp.tileSize;
        eventRect[row][col].y += row*gp.tileSize;

        //碰撞检测
        if(gp.player.solidArea.intersects(eventRect[row][col]) && !eventRect[row][col].eventDone){

            //方向检测
            if(gp.player.direction == reqDirection || reqDirection == Direction.ANY){
                System.out.println(gp.player.direction);
                System.out.println(reqDirection);
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldX;

            }

        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[row][col].x = eventRect[row][col].eventRectDefaultX;
        eventRect[row][col].y = eventRect[row][col].eventRectDefaultY;

        return hit;
    }

    public void damagePit(int row,int col,int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialog = "生命值降低";
        gp.player.life--;
        // eventRect[row][col].eventDone = true; //一次性事件
        canTouchEvent = false;
    }

    public void healingPool(int row,int col,int gameState){
        System.out.println("check");
        if(gp.keyH.enterPressed){
            gp.player.fishingCanceled = true;
            gp.gameState = gameState;
            gp.ui.currentDialog = "你的宝可梦都恢复健康了!";
            gp.player.party.recoverAllPokemon();
        }

    }


}
