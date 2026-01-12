package main;

import entity.NPC_Twin;
import item.*;
import object.*;
import entity.*;
//负责初始化实体
public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gamePanel){
        this.gp = gamePanel;
    }

    public void setObject(){
        int i=0;
        Entity item_ball = new OBJ_Item_Ball(gp);
        item_ball.setItem(new ITEM_Old_Rod(gp));
        
        gp.obj[i] = item_ball;
        gp.obj[i].worldX = 25 * gp.tileSize;
        gp.obj[i].worldY = 48 * gp.tileSize;
        i++;

        OBJ_SignBoard signBoard = new OBJ_SignBoard(gp);
        signBoard.setMessage("祐树的家");
        gp.obj[i] = signBoard;
        gp.obj[i].worldX = 15 * gp.tileSize;
        gp.obj[i].worldY = 54 * gp.tileSize;
        i++;

        signBoard = new OBJ_SignBoard(gp);
        signBoard.setMessage("小瑶的家");
        gp.obj[i] = signBoard;
        gp.obj[i].worldX = 20 * gp.tileSize;
        gp.obj[i].worldY = 54 * gp.tileSize;
        i++;
        
        signBoard = new OBJ_SignBoard(gp);
        signBoard.setMessage("欢迎来到未白镇\n一座祥和的小镇");
        gp.obj[i] = signBoard;
        gp.obj[i].worldX = 23 * gp.tileSize;
        gp.obj[i].worldY = 59 * gp.tileSize;
    
        
        // gp.obj[1] = new object.OBJ_Door(gp);
        // gp.obj[1].worldX = 10 * gp.tileSize;
        // gp.obj[1].worldY = 41 * gp.tileSize;

        // gp.obj[2] = new object.OBJ_Door(gp);
        // gp.obj[2].worldX = 11 * gp.tileSize;
        // gp.obj[2].worldY = 41 * gp.tileSize;



    }
    public void setNPC(){
        gp.npc[0] = new NPC_Twin(gp);
        gp.npc[0].worldX = gp.tileSize * (16+8);
        gp.npc[0].worldY = gp.tileSize * (49+6);


    }

}
