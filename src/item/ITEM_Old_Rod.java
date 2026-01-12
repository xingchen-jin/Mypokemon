package item;

import main.GamePanel;

public class ITEM_Old_Rod extends Item{

    public ITEM_Old_Rod(GamePanel gp){
        super(gp);

        name = "旧钓竿";
        image = setup("item/old_rod");
        description = "["+name+"]\n一个老旧的钓鱼竿";
    }
}
