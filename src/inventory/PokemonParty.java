package inventory;

import main.GamePanel;
import pokemon.Pokemon;

public class PokemonParty {
    private GamePanel gp;

    public Pokemon[] pokemons;
    int curCap;
    final int curPokeIndex = 0;
    Pokemon curPokemon;
    public PokemonParty(GamePanel gp){
        this.gp = gp;
        pokemons = new Pokemon[6];
        curCap = 0;
    }

    public Boolean add(Pokemon pokemon){
        if(curCap >=6 ) return false;

        pokemons[curCap] = pokemon;
        curCap++;
        return true;
    }

    //获取队伍宝可梦
    public Pokemon getPoke(int i){
        if(i<curCap && i>=0)return pokemons[i];
        else return null;
    }

    //删除队伍宝可梦，并返回删除对象
    public Pokemon delPoke(int i){
        if(i<curCap && i>=0 && pokemons[i]!=null){
            Pokemon poke = pokemons[i];
            for(;i<=curCap-2;i++){
                pokemons[i] = pokemons[i+1];
            }
            return poke;
        }
        return null;
    }
    public int size(){
        return curCap;
    }
    //获取第一个可用宝可梦的索引
    public int getFirstAblePokemonIndex(){
        for(int i=0;i<curCap;i++){
            if(pokemons[i]!=null && pokemons[i].curHp>0){
                return i;
            }
        }
        return -1;
    }
    //获取第一个可用宝可梦对象
    public Pokemon getFirstAblePokemon(){
        for(int i=0;i<curCap;i++){
            if(pokemons[i]!=null && pokemons[i].curHp>0){
                return pokemons[i];
            }
        }
        return null;
    }

    //设置当前宝可梦对象
    public void setCurPokemon(int index) {
        Pokemon poke = pokemons[index];
        if(poke==null || poke.curHp<=0){
            return;
        }
        pokemons[index] = pokemons[0];
        pokemons[0] = poke;
    }
    //获取当前宝可梦对象
    public Pokemon getCurPokemon() {

        curPokemon = pokemons[curPokeIndex];
        return curPokemon;
    }

    //交换两个宝可梦位置
    public void swapPokemon(int index1,int index2){
        Pokemon temp = pokemons[index1];
        pokemons[index1] = pokemons[index2];
        pokemons[index2] = temp;
    }
    
    //恢复所有宝可梦状态
    public void recoverAllPokemon(){
        for(int i=0;i<curCap;i++){
            if(pokemons[i]!=null){
                pokemons[i].recoverAllStatus();
            }
        }
    }
    //检测队伍状态
    public boolean checkAllFainted(){
        for(int i=0;i<curCap;i++){
            if(pokemons[i]!=null && pokemons[i].curHp>0){
                return false;
            }
        }
        return true;
    }
}
