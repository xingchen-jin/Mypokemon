package move;

public class Ember extends Move {
    public Ember(){
        ID = 52;
        name = "火焰喷射";
        maxPP = 25;
        PP = maxPP;
        power = 40;
        accuracy = 100;
        moveType = MoveType.SPECIAL;
        isHitMove = true;
    }

}
