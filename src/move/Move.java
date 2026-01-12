package move;

import java.awt.image.BufferedImage;
import java.net.URL;


import pokemon.Pokemon;

public abstract class  Move {
    int ID;
    protected String name;
    public int maxPP,PP;

    protected int power;      //威力
    protected int accuracy;   //命中

    public  boolean isHitMove = false;      //是否为命中招式;
    public boolean  isSoundEffect = false;  //是否有音效
    public boolean  isAnimated = false;     //是否有动画

    public enum MoveType{
        PHYSICAL,   //物理
        SPECIAL,    //特殊
        STATUS      //变化
    }
    protected MoveType moveType;
    public MoveType getMoveType(){
        return this.moveType;
    }
    

    public String use(Pokemon own,Pokemon enemy){
        return "";
    }

    public int getID(){
        return ID;
    }
    public String getName(){
        return name;
    }
    public int getMaxPP(){
        return maxPP;
    }
    public int getPower(){
        return power;
    }
    public int getAccuracy(){
        return accuracy;
    }
    public MoveType getMoveCategory(){
        return moveType;
    }
    public URL getSoundEffect(){
        return null;
    }
    public BufferedImage getAnimationImage(){
        return null;
    }
    // int type;   //属性

}
