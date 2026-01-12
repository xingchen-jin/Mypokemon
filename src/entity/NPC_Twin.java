package entity;

import java.util.Random;

import java.awt.Rectangle;
import main.GamePanel;

public class NPC_Twin extends Entity{


    public NPC_Twin(GamePanel gp){
        super(gp);

        direction = Direction.DOWN;
        speed = 1;
        solidArea = new Rectangle(0,64,48,32);
        solidAreaDefaultX = solidArea.x;solidAreaDefaultY = solidArea.y; 

        getImage();
        setDialpgue();
    }

    public void getImage() {

        //移动_行走图片
        up1 = setup("npc/twin_up_1",1,2);
        up2 = setup("npc/twin_up_2",1,2);
        down1 = setup("npc/twin_down_1",1,2);
        down2 = setup("npc/twin_down_2",1,2);
        left1 = setup("npc/twin_left_1",1,2);
        left2 = setup("npc/twin_left_2",1,2);
        right1 = setup("npc/twin_right_1",1,2);
        right2 = setup("npc/twin_right_2",1,2);

        //待机图片
        // idle_up = setup("npc/twin_idle_up");
        // idle_down = setup("npc/twin_idle_down");
        // idle_left = setup("npc/twin_idle_left");
        // idle_right = setup("npc/twin_idle_right");
    }

    public void setDialpgue(){
        dialogues[0] = "先辈,Ciallo";
        dialogues[1] = "旮旯噶姆里不是这样的";
        dialogues[2] = "你怎么直接向我表白阿";
        dialogues[3] = "嘎啦噶姆里根本不是这样的";
        dialogues[4] = "不行，我不接受";

    }

    public void setAction(){
        actionLockCounter++;
        if(actionLockCounter == 120){

            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25){
                direction = Direction.UP;
            }else if(i > 25 && i <= 50){
                direction = Direction.DOWN;
            }else if(i > 50 && i <= 75){
                direction = Direction.LEFT;
            }else{
                direction = Direction.RIGHT;
            }

            actionLockCounter = 0;
        }


    }

    //交互处理
    public void speak(){
        super.speak();
    }

}
