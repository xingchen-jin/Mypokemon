package move;



public class Scratch extends Move{
    public Scratch(){
        ID = 10;
        name = "抓";
        maxPP = 35;
        PP = maxPP;
        power = 40;
        accuracy = 100;
        moveType = MoveType.PHYSICAL;
        isHitMove = true;
    }

}
