package item;

import main.GamePanel;

public class ITEM_Shield_Wood extends Item{
    public ITEM_Shield_Wood(GamePanel gp){
        super(gp);

        name = "旧盾牌";
        image = setup("item/shield_wood");
        description = "["+name+"]\n一个老旧的盾牌";
    }
}
