package pokemon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import move.Move;
import utils.UtilityTool;

public class Pokemon {

    private GamePanel gp;
    public int ID;  //精灵编号  0***
    public String name;
    
    public BufferedImage back,front;
    public BufferedImage[] anim_front,icon;

    public enum BaseStat {
        HP("HP","生命值"),
        ATTACK("ATK", "攻击"),
        DEFENSE("DEF", "防御"),
        SP_ATTACK("SPA", "特攻"),
        SP_DEFENSE("SPD", "特防"),
        SPEED("SPE", "速度");
        private String code,name;
        BaseStat(String code,String name){
            this.code = code;
            this.name = name;
        }

        public String getCode(){
            return code;
        }
        public String getName(){
            return name;
        }
    }

    public EnumMap<BaseStat,Integer> iv;              //个体值
    public EnumMap<BaseStat,Integer> ev;              //努力值
    public EnumMap<BaseStat,Integer> statistic;       //能力值
    public EnumMap<BaseStat,Integer> tempStatistic;   //临时能力值


    public int curHp;
    public int maxHp;
    public int level = 5;

    public Move[] moves = new Move[4];
    public int capacity = 0;   //已学会招式数
    // public int index = 0;      //下一个招式位置
    
    public Pokemon(GamePanel gp){
        this.gp = gp;
    }
    
    public String getName(){
        return name;
    } 
    public void addExp(int exp){
        // this.exp += exp;
    }
    public void levelUp(){
        level++;
        
    }

    public void addMove(Move move){
        if(capacity>=0 && capacity<4){
            moves[capacity] = move;
            capacity++;
        }
    }

    public void setImage(String name){

        String imagePath = "/res/pokemon/" +name+"/";

        this.anim_front = getImages(imagePath+"anim_front");
        this.icon = getImages(imagePath+"icon");
        this.back = getImage(imagePath+"back");
        this.front = getImage(imagePath+"front");
    }

    //获取多张图
    private BufferedImage[] getImages(String imagePath){
        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage orignImage = null;
        BufferedImage[] images = new BufferedImage[2];

        try{
            orignImage = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        int oImageWidth = orignImage.getWidth();
        int oImageHeight = orignImage.getHeight();
        //单精灵长宽
        int width = oImageWidth;
        int height = oImageHeight/2;
        int scale = width/gp.originalTileSize;  //该图的瓦片数目（大小）
        try{
            images[0] = orignImage.getSubimage(0,0,width,height);
            images[1] = orignImage.getSubimage(0,height,width,height);
            //图片大小修正
            images[0] = uTool.scaleImage(images[0], gp.tileSize*scale, gp.tileSize*scale);
            images[1] = uTool.scaleImage(images[1], gp.tileSize*scale, gp.tileSize*scale);
        }catch(Exception e){
            e.printStackTrace();
        }
        return images;
    }
    
    //获取单图
    private BufferedImage getImage(String imagePath){
        UtilityTool uTool = new UtilityTool(gp);
        BufferedImage image = null;
        int scale;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            //图片大小修正
            scale = image.getWidth()/gp.originalTileSize;   
            image = uTool.scaleImage(image, gp.tileSize*scale, gp.tileSize*scale);     
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    // 获取随机可用招式
    public Move getRandomAvailableMove(){
        Move move = null;
        int availableMoveCount = 0;
        Random  random = new Random();
        for(int i=0;i<4;i++){
            if(moves[i]!=null){
                availableMoveCount++;
            }
        }
        
        if(availableMoveCount>0){
            int randIndex = random.nextInt(availableMoveCount);
            move = moves[randIndex];
        }
        return move;
    }

    //恢复所有状态
    public void recoverAllStatus(){
        this.curHp = this.maxHp;
        this.tempStatistic = new EnumMap<BaseStat,Integer>(this.statistic);
        for(int i=0;i<4;i++){
            if(this.moves[i]!=null){
                this.moves[i].PP = this.moves[i].maxPP;
            }
        }
    }
}
