package pokemon;

import java.util.EnumMap;

import main.GamePanel;


//木守宫
public class Treecko extends Pokemon {
        public Treecko(GamePanel gp){
        super(gp);
        ID = 252;
        name = "木守宫";
        setImage("treecko");
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
