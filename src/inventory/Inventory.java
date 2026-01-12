package inventory;

import item.Item;
import main.GamePanel;

public class Inventory {

    //设置物品单元类
    private class ItemEntity {
        private Item item;
        private int quantity;    //数量

        public ItemEntity(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }
    private final int MAX_CAP = 40;

    private ItemEntity[] items;
    private int curCap;
    public int capacity;
    private GamePanel gp;

    public Inventory(GamePanel gp){
        this.gp = gp;
        items = new ItemEntity[MAX_CAP];
        curCap = 0;
        capacity = MAX_CAP;
    }
    //添加物品
    public Boolean add(Item item){
        int i;
        for(i=0; i<curCap; i++){
            if(items[i].item.getName().equals(item.getName())){
                items[i].quantity += 1;
           return true;
            }
        }
        if(curCap >= capacity){
            return false;
        }
        items[curCap] = new ItemEntity(item, 1);
        curCap++;

        return true;
    }
    public Boolean add(Item item, int quantity){
        int i;
        for(i=0; i<curCap; i++){
            if(items[i].item.getName().equals(item.getName())){
                items[i].quantity += quantity;
                return true;
            }
        }
        if(curCap >= capacity){
            return false;
        }
        items[curCap] = new ItemEntity(item, quantity);
        curCap++;

        return true;
    }

    //获取物品种数
    public int size(){
        return this.curCap;
    }
    //获取物品
    public Item get(int index){
        if(index < 0 || index >= curCap)
            return null;
        return items[index].item;
    }
    //获取物品数量
    public int getQuantity(int index){
        if(index < 0 || index >= curCap)
            return 0;
        return items[index].quantity;
    }
    //移除物品
    public void removeItem(Item item){
        for(int i=0; i<curCap; i++){
            if(items[i].item == item){
                items[i].quantity--;
                if(items[i].quantity <= 0){
                    //移除该物品
                    for(int j=i; j<curCap-1; j++){
                        items[j] = items[j+1];
                    }
                    items[curCap-1] = null;
                    curCap--;
                }
                break;
            }
        }
    }
}
