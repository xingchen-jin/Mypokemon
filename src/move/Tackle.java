package move;

public class Tackle extends Move {
    public Tackle(){
        ID = 10;
        name = "撞击";
        maxPP = 35;
        PP = maxPP;
        power = 40;
        accuracy = 100;
        moveType = MoveType.PHYSICAL;
        isHitMove = true;
    }
}
