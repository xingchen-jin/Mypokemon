package battle;
import main.GamePanel;
import item.*;
import pokemon.*;
import move.*;
public class BattleSystem {
    GamePanel gp;
    BattleUI battleUI;
    BattleManger battleManger;

    public BattleSystem(GamePanel gp){
        this.gp = gp;
        battleUI = gp.battleUI;
        battleManger = gp.battleM;
    }

    public boolean useBallItem(ITEM_Poke_Ball ball, Pokemon target){

        int rate = ball.getCaptureRate();
        int randomValue = (int)(Math.random() * 100);
        ball = null;  
        if(randomValue < rate){
            battleUI.setDialogues("捕捉成功！你捕捉到了 " + target.name + "！");
            return true;    //捕捉成功
        }else{
            battleUI.setDialogues("捕捉失败！" + target.name + " 逃脱了！");
            return false;   //捕捉失败
        }
    }
    public void useMove(Pokemon user, Pokemon target, Move move){
        if(move == null || move.PP <= 0) return;
        if(move.isSoundEffect){
            gp.se.setFile(move.getSoundEffect());
            gp.se.play();
        }
        //命中判定
        int randomValue = (int)(Math.random() * 100);
        if(randomValue >= move.getAccuracy()){
            move.PP--;
            battleUI.setDialogues(user.name + "使用了" + move.getName() + "，但是招式未命中！");
            return;
        }
        //非命中招式处理
        if(!move.isHitMove){
            String result = move.use(user, target);
            battleUI.setDialogues(user.name + "使用了" + move.getName() + "！");
            battleUI.addDialogue(result);
            return;
        }

        //伤害处理
        int damage = (int)(((2.0/5+2)*move.getPower()*(user.tempStatistic.get(Pokemon.BaseStat.ATTACK)/target.tempStatistic.get(Pokemon.BaseStat.DEFENSE))/50)+2);
        target.curHp -= damage;
        if(target.curHp < 0){
            target.curHp = 0;
        }
        move.PP--;
        //显示战斗信息
        battleUI.setDialogues(user.name + "使用了" + move.getName() + "，造成了" + damage + "点伤害！");


    }

    //尝试逃跑
    public boolean runAway(){
        int runChance = 50; // 50%的成功率
                int randomValue = (int)(Math.random() * 100);
        Boolean success = randomValue < runChance;
        battleUI.setDialogues(success ? "逃跑成功！" : "逃跑失败！");
        return success;
    }
    //切换宝可梦
    public void switchPokemon(Pokemon newPokemon){
        battleUI.setDialogues("你换上了" + newPokemon.name + "！");
    }
}
