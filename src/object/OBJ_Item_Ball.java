package object;

import entity.Entity;
import item.Item;
import main.GamePanel;

public class OBJ_Item_Ball extends Entity{
    Item item;

    public OBJ_Item_Ball(GamePanel gp){
        super(gp);

        name = "item ball";
        down1 = setup("objects/item_ball");
        collision = true;

    }
    
    public void setItem(Item item){
        this.item = item;
    }
    public Item getItem(){

        return this.item;
    }
}
