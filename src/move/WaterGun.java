package move;

public class WaterGun extends Move {
    public WaterGun(){
        ID = 55;
        name = "水枪";
        maxPP = 25;
        PP = maxPP;
        power = 40;
        accuracy = 100;
        moveType = MoveType.SPECIAL;
        isHitMove = true;
    }
}
