package move;

public class Growl extends Move {
    public Growl(){
        ID = 45;
        name = "叫声";
        maxPP = 40;
        PP = maxPP;
        power = 0;
        accuracy = 100;
        moveType = MoveType.STATUS;
        isHitMove = false;
    }
    
    @Override
    public String use(pokemon.Pokemon own,pokemon.Pokemon enemy){
        this.PP--;
        //降低对方攻击力一级
        int currentStage = enemy.tempStatistic.get(pokemon.Pokemon.BaseStat.ATTACK);
        if(currentStage > -6){
            enemy.tempStatistic.put(pokemon.Pokemon.BaseStat.ATTACK, currentStage - 1);
            System.out.println(enemy.getName() + "的攻击力下降了！");
        }
        return enemy.getName() + "的攻击力下降了！";
    }
}
