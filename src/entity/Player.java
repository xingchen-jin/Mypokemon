package entity;

import main.GamePanel;
import main.KeyHandler;
import utils.UtilityTool;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import battle.BattleManger.BattleType;
import pokemon.*;
import move.*;
import inventory.Inventory;
import inventory.PokemonParty;
import item.*;

public class Player extends Entity {
    KeyHandler keyH;
    
    public final int screenX;   //屏幕上的固定坐标
    public final int screenY;
    public boolean fishingCanceled = false;
    public boolean isInitialized = false;       //玩家是否已初始化完成
    public BufferedImage[][] fishingImg = new BufferedImage[4][5];

    public int gender = 0; //0代表男生，1代表女生

    public int coin;
    public int onGrassCounter =0;   //在草丛中的计数器
    public int onGrassMax = 60;    //在草丛中触发遇敌的最大计数值

    //库存系统
    public Inventory inventory;
    public PokemonParty party;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.keyH = keyH;
        
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - gp.tileSize;     //玩家由1*2个瓦片构成，去除其影响，保证位于中心

        solidArea = new Rectangle(6,60,36,36);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;        

    }
    
    public void setDefaultValues() {
        worldX = gp.tileSize * (5+8);
        worldY = gp.tileSize * (48+6);
        speed = 4;
        direction = Direction.DOWN;

        //创建库存
        inventory = new Inventory(gp);
        party = new PokemonParty(gp);

        inventory.add(new ITEM_Old_Rod(gp));//可优化
        inventory.add(new ITEM_Poke_Ball(gp));
        inventory.add(new ITEM_Poke_Ball(gp));
        inventory.add(new ITEM_Poke_Ball(gp));
        inventory.add(new ITEM_Poke_Ball(gp),99);
        // inventory.add(new ITEM_Shield_Wood(gp));
        Pokemon torchic = new Torchic(gp);
        Pokemon ralts = new Ralts(gp);
        
        torchic.addMove(new Scratch());
        torchic.addMove(new Growl());
        ralts.addMove(new Tackle());
        ralts.addMove(new Growl());
        ralts.addMove(new Ciallo());
        torchic.moves[0].PP = 0; //测试招式PP耗尽

        //添加初始宝可梦到队伍
        party.add(torchic);
        party.add(ralts); 

        //加载精灵图
        if(this.gender == 0){
            name = "brendan";
        }else{
            name = "may";
        }
        getplayerImage(name);
        getplayerFishingImage(name);
        this.isInitialized = true;
        //玩家生命值（废案）
        maxLife = 6;
        life = maxLife;

    }

    public void getplayerImage(String name) {

        //移动_行走图片
        up1 = setup("player/" + name + "/waking/up_01",1,2);
        up2 = setup("player/" + name + "/waking/up_02",1,2);
        down1 = setup("player/" + name + "/waking/down_01",1,2);
        down2 = setup("player/" + name + "/waking/down_02",1,2);
        left1 = setup("player/" + name + "/waking/left_01",1,2);
        left2 = setup("player/" + name + "/waking/left_02",1,2);
        right1 = setup("player/" + name + "/waking/right_01",1,2);
        right2 = setup("player/" + name + "/waking/right_02",1,2);

        //待机图片
        idle_up = setup("player/" + name + "/waking/idle_up",1,2);
        idle_down = setup("player/" + name + "/waking/idle_down",1,2);
        idle_left = setup("player/" + name + "/waking/idle_left",1,2);
        idle_right = setup("player/" + name + "/waking/idle_right",1,2);

    }

    public void getplayerFishingImage(String name){

        for(int i=1;i<=4;i++){
            fishingImg[Direction.UP.ordinal()][i] = setup("player/"+name+"/fishing/up_0"+i,2,2);
        }
        for(int i=1;i<=4;i++){
            fishingImg[Direction.DOWN.ordinal()][i] = setup("player/"+name+"/fishing/down_0"+i,2,2);
        }
        for(int i=1;i<=4;i++){
            fishingImg[Direction.LEFT.ordinal()][i] = setup("player/"+name+"/fishing/left_0"+i,2,2);
        }
        for(int i=1;i<=4;i++){
            fishingImg[Direction.RIGHT.ordinal()][i] = setup("player/"+name+"/fishing/right_0"+i,2,2);
        }

    }
    public void update() {

        if(isFishing){
            fishing();
        }
        else if(keyH.isPressed || keyH.enterPressed){
            //移动方向判断
            if (keyH.upPressed) {
                direction = Direction.UP;
            }
            else if (keyH.downPressed) {
                direction = Direction.DOWN;
            }
            else if (keyH.leftPressed) {
                direction = Direction.LEFT;
            }
            else if (keyH.rightPressed) {
                direction = Direction.RIGHT;
            }
            
            //碰撞检测
            collisiOn = false;  //重置碰撞标志,false表示未碰撞
            gp.cChecker.checkTile(this);
            
            //检测物品
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            //检测npc
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //检测事件
            gp.eHandler.checkEvent();

            

            //如果没有碰撞，执行移动
            if((!collisiOn) && (!keyH.enterPressed)){
                switch(direction){
                    case UP:worldY -= speed; break;
                    case DOWN: worldY += speed; break;
                    case LEFT: worldX -= speed; break;
                    case RIGHT: worldX += speed; break;

                }
            }
            interGrass();

            if(keyH.enterPressed && !fishingCanceled){
                isFishing = true;
                spriteCounter = 0;
            }
            fishingCanceled = false;

            gp.keyH.enterPressed = false;
            //精灵动画
            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNum ==2){
                    spriteNum =0;
                }
                spriteNum++;
                spriteCounter =0;
            }     

            if(keyH.isPressed)
                isIdle = false;

        }else{
            //静止状态下显示待机图片
            spriteNum =1;
            isIdle= true;

        }
    }
    public void fishing(){
        spriteCounter++;
        if(spriteCounter <= 5){
            fishSpriteNum = 1;
        }else if(spriteCounter > 5 && spriteCounter <= 10){
            fishSpriteNum = 2;
        }else if(spriteCounter > 10 && spriteCounter <= 15){
            fishSpriteNum = 3;
        }else if(spriteCounter > 15 && spriteCounter <= 50){
            fishSpriteNum = 4;
        }else{
            fishSpriteNum = 1;
            spriteCounter = 0;
            isFishing = false;
        }
    }
    //处理物体碰撞结果
    public void pickUpObject(int i) {
        if(i != -1){
    
            String text;
            if(keyH.enterPressed){
                switch (gp.obj[i].name) {
                    case "item ball":
                        if(inventory.add(gp.obj[i].getItem())){
                            gp.playSE(1);
                            text = "获得一个"+gp.obj[i].getItem().name + "!";
                            gp.obj[i] = null;
                        }else{
                            text = "背包容量已满!";
                        }
                        break;
                    case "SignBoard":
                        text = ((object.OBJ_SignBoard)gp.obj[i]).getMessage();
                        break;
                    default:
                        text = "NULL";
                        break;
                }

                    gp.ui.currentDialog = text;
                    gp.gameState = gp.dialogState;
                    fishingCanceled = true;
                
            }

            
        }


    }
    public void interactNPC(int i){
        if(gp.keyH.enterPressed){
            if(i != -1){
                gp.gameState = gp.dialogState;
                gp.npc[i].speak();
                fishingCanceled = true;
            }
        }
        

    }
    
    public void interGrass(){
        
        //全员濒死或队伍无宝可梦不触发遇敌
        if(party.checkAllFainted() || party.size() ==0){
            return;
        }
        //检测是否在草丛中
        if(onGrass){
            onGrassCounter++;
            //遇敌检测
            if(onGrassCounter > onGrassMax){
                int rand = UtilityTool.getRandomNumber(1,100);
                if(rand > 50){   //50%的概率遇敌
                    Pokemon wildPokemon = getRandomWildPokemon();
                    gp.gameState = gp.battleState;
                    gp.battleM.BattleInitialize(wildPokemon, BattleType.WILD);
                    onGrass = false;
                    gp.playMusic(6);
                }
                onGrassCounter = 0;
            }
        }else{
            onGrassCounter = 0;
        }
    }
    private Pokemon getRandomWildPokemon(){
        //此处可根据不同地图设置不同的野生宝可梦
        Pokemon wildPokemon;
        int rand = UtilityTool.getRandomNumber(1,100);
        if(rand <= 70){
            wildPokemon = new Poochyena(gp);   
            wildPokemon.addMove(new Tackle());  
            return wildPokemon;   //70%的概率遇到小狼犬
        }else{
            wildPokemon = new Ralts(gp);
            wildPokemon.addMove(new Tackle());
            wildPokemon.addMove(new Growl());
            return wildPokemon;       //30%的概率遇到拉鲁拉丝
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int tempScreenX = screenX,tempScreenY = screenY;

        if(!isFishing){
            switch(direction){
                case UP:
                    if(isIdle){
                        image = idle_up;
                    }else if(spriteNum ==1){
                        image = up1;
                    }else if(spriteNum ==2){
                        image = up2;
                    }

                    break;
                case DOWN:
                        if(isIdle) {
                            image = idle_down;
                        }else if(spriteNum ==1){
                            image = down1;
                        }else if(spriteNum ==2){
                            image = down2;
                        }
                    break;
                case LEFT:

                        if(isIdle){
                            image = idle_left;
                        }else if(spriteNum ==1){
                            image = left1;
                        }else if(spriteNum ==2){
                            image = left2;
                        }
                    break;
                case RIGHT:

                        if(isIdle){
                            image = idle_right;
                        }else if(spriteNum ==1){
                            image = right1;
                        }else if(spriteNum ==2){
                            image = right2;
                        }
 
                    break;
            }
        }else if(isFishing){
            image = fishingImg[direction.ordinal()][fishSpriteNum];
            if(direction == Direction.UP || direction == Direction.DOWN )
                tempScreenX -=gp.tileSize/2;
        }
   
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

    }
}
