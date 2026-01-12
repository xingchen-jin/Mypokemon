package pokemon;

import java.util.EnumMap;

import main.GamePanel;

public class Torchic extends Pokemon{
    public Torchic(GamePanel gp){
        super(gp);

        ID = 255;
        name = "火稚鸡";
        setImage("torchic");
        statistic = new EnumMap<>(BaseStat.class);
        setStat();
    }

    public void setStat(){
        statistic.put(BaseStat.HP,13);
        statistic.put(BaseStat.ATTACK,7);
        statistic.put(BaseStat.DEFENSE,6);
        statistic.put(BaseStat.SP_ATTACK,7);
        statistic.put(BaseStat.SP_DEFENSE,7);
        statistic.put(BaseStat.SPEED,6);
        
        tempStatistic = new EnumMap<>(statistic);
        curHp = statistic.get(BaseStat.HP);
        maxHp = curHp;
    }

    

}
