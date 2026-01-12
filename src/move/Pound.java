package move;


public class Pound extends Move {
    public Pound(){
        ID = 10;
        name = "拍打";
        maxPP = 35;
        PP = maxPP;
        power = 40;
        accuracy = 100;
        moveType = MoveType.PHYSICAL;
        isHitMove = true;
    }
}
