package pokemon;

import java.util.EnumMap;

import main.GamePanel;

//拉鲁拉丝
public class Ralts extends Pokemon {
        public Ralts(GamePanel gp){
        super(gp);

        ID = 279;
        name = "拉鲁拉丝";
        setImage("ralts");
        statistic = new EnumMap<>(BaseStat.class);
        setStat();
    }

    public void setStat(){
        statistic.put(BaseStat.HP,28);
        statistic.put(BaseStat.ATTACK,9);
        statistic.put(BaseStat.DEFENSE,6);
        statistic.put(BaseStat.SP_ATTACK,9);
        statistic.put(BaseStat.SP_DEFENSE,7);
        statistic.put(BaseStat.SPEED,6);
        
        tempStatistic = new EnumMap<>(statistic);
        curHp = statistic.get(BaseStat.HP);
        maxHp = curHp;
    }
}
