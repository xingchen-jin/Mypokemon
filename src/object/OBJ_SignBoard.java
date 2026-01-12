package object;
import entity.Entity;

public class OBJ_SignBoard extends Entity {
    private String message;
    public OBJ_SignBoard(main.GamePanel gp) {
        super(gp);
        name = "SignBoard";  //告示牌
        collision = true;
    }
    public void setMessage(String text){
        this.message = text;
    }
    public String getMessage(){
        return this.message;
    }
    

}
