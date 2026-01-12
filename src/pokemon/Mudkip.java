package pokemon;

import java.util.EnumMap;

import main.GamePanel;

public class Mudkip extends Pokemon{

    public Mudkip(GamePanel gp){
        super(gp);

        ID = 0257;
        name = "水跃鱼";
        setImage("mudkip");
        statistic = new EnumMap<>(BaseStat.class);
        setStat();
    }

    public void setStat(){
        statistic.put(BaseStat.HP,12);
        statistic.put(BaseStat.ATTACK,6);
        statistic.put(BaseStat.DEFENSE,6);
        statistic.put(BaseStat.SP_ATTACK,6);
        statistic.put(BaseStat.SP_DEFENSE,6);
        statistic.put(BaseStat.SPEED,5);
        
        tempStatistic = new EnumMap<>(statistic);
        curHp = statistic.get(BaseStat.HP);

        maxHp = curHp;
    }
}
