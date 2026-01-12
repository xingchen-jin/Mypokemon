package main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import battle.BattleManger;
import battle.BattleManger.PendingAction;
import move.Move;
import move.Scratch;
import pokemon.Pokemon;

public class KeyHandler implements KeyListener{
    
    public GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed,isPressed,enterPressed;
    //DEBUG
    boolean showDebug = false;
    

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        else if(gp.gameState == gp.battleState){
            battleState(code);
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState){
            playState(code);
        }

        // MENU STATE
        else if(gp.gameState == gp.menuState){
            menuState(code);
        }

        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogState){
            dialogState(code);
        }

        else if(gp.gameState == gp.characterState){
            characterState(code);
        }
    }

    public void titleState(int code){
        if(gp.ui.titleScreenState == 0){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum = (gp.ui.commandNum > 1)?(gp.ui.commandNum-1) : 3;
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum = (gp.ui.commandNum < 3)?(gp.ui.commandNum+1) : 1;
            }
            if(code == KeyEvent.VK_ENTER){
                switch (gp.ui.commandNum) {
                    //NEW GAME
                    case 1:
                        gp.ui.titleScreenState = 1;
                        break;
                    //CONTINUE
                    case 2: 
                        if(gp.player.isInitialized){
                            gp.gameState = gp.playState;
                            gp.playMusic(0);
                        }
                    break;
                    //EXIT
                    case 3:
                        System.exit(0);
                }
            }
        }else if(gp.ui.titleScreenState == 1){
            if(code == KeyEvent.VK_A){
                gp.ui.commandNum = (gp.ui.commandNum > 1)?(gp.ui.commandNum-1) : 3;
            }
            if(code == KeyEvent.VK_D){
                gp.ui.commandNum = (gp.ui.commandNum < 3)?(gp.ui.commandNum+1) : 1;
            }
            if(code == KeyEvent.VK_S){
                if(gp.ui.commandNum == 3) gp.ui.commandNum = 1;
                else gp.ui.commandNum = 3;
            }
            if(code == KeyEvent.VK_W){
                if(gp.ui.commandNum < 3) gp.ui.commandNum = 3;
                else gp.ui.commandNum = 1;
            }
            if(code == KeyEvent.VK_ENTER){
                switch (gp.ui.commandNum) {
                    //男孩
                    case 1:
                        gp.player.gender = 0;
                        gp.player.setDefaultValues();
                        gp.gameState = gp.playState;
                         //加载实体
                        gp.aSetter.setObject();
                        gp.aSetter.setNPC();
                        gp.playMusic(0);
                        break;
                    //女孩
                    case 2:
                        gp.player.gender = 1;
                        gp.player.setDefaultValues();
                        gp.gameState = gp.playState;
                        //加载实体
                        gp.aSetter.setObject();
                        gp.aSetter.setNPC();
                        gp.playMusic(0);
                        break;
                    //返回标题
                    case 3:
                        gp.ui.titleScreenState = 0;
                        break;
                }
            }
        }
    }
    public void playState(int code){
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(upPressed || downPressed || leftPressed || rightPressed){
            isPressed = true;
        }
        //Esc键返回菜单
        if(code == KeyEvent.VK_ESCAPE){
            gp.ui.currentMenuState = UI.menuState.main;
            gp.gameState = gp.menuState;
            gp.ui.commandNum = 1;
        }
        //Enter键交互
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        //B键进入状态栏状态
        if(code == KeyEvent.VK_B){
            gp.gameState = gp.characterState;

        }
        // DEBUG
        if(code == KeyEvent.VK_I){
            if(!showDebug){
                showDebug = true;
            }else{
                showDebug = false;
            }
        }

        // F键进入战斗状态，调试用
        if(code == KeyEvent.VK_F){
           gp.gameState = gp.battleState;

           gp.battleM.curBattleState = BattleManger.BattleState.Start;
           Pokemon wildPokemon = new pokemon.Mudkip(gp);
           wildPokemon.addMove(new Scratch());

           gp.battleM.BattleInitialize(wildPokemon, BattleManger.BattleType.WILD);
           gp.playMusic(6);

        }
        
    }
    public void menuState(int code){
        switch(gp.ui.currentMenuState){
            case main:
                menuMainState(code);
                break;
            case item:
                menuItemState(code);
                break;
            case pokemon:
                menuPokemonState(code);
                break;
            case options:
                menuOptionsState(code);
                break;
        }
        // if(code == KeyEvent.VK_ESCAPE){
        //     gp.gameState = gp.playState;
        // }
    }

    public void menuMainState(int code){
        switch (code) {
            case KeyEvent.VK_W:
                gp.ui.commandNum = (gp.ui.commandNum > 1)?(gp.ui.commandNum-1) : 6;
                gp.playSE(5);
                break;
            case KeyEvent.VK_S:
                gp.ui.commandNum = (gp.ui.commandNum < 6)?(gp.ui.commandNum+1) : 1;
                gp.playSE(5);
                break;
            case KeyEvent.VK_ESCAPE:
                gp.gameState = gp.playState;
                gp.playSE(5);
                break;
            case KeyEvent.VK_ENTER:
                gp.playSE(5);
                switch (gp.ui.commandNum) {
                    case 1: gp.ui.currentMenuState = UI.menuState.pokemon; break;
                    case 2: gp.ui.currentMenuState = UI.menuState.item;gp.gameState = gp.characterState; break;
                    case 3: gp.ui.currentMenuState = UI.menuState.options;gp.ui.commandNum = 1; break;
                    case 4: gp.gameState = gp.playState; break;
                    case 5: gp.gameState = gp.titleState;gp.ui.commandNum = 1;gp.ui.titleScreenState = 0; break;
                    case 6: System.exit(0); break;
                }
                break;
            default:
                break;
        }
    }
    public void menuItemState(int code){
        
    }
    public void menuPokemonState(int code){

         switch (code) {  
            case KeyEvent.VK_ESCAPE:
                if(gp.gameState == gp.menuState){
                    gp.ui.currentMenuState = UI.menuState.main;
                    gp.ui.commandNum = 1;
                }
                gp.playSE(5);
                break;
        
            case KeyEvent.VK_ENTER:
                //换上宝可梦
                if(gp.ui.slotCol ==1){
                    int pokeIndex = gp.ui.slotRow+1;
                    if(pokeIndex < gp.player.party.size()){
                        gp.player.party.setCurPokemon(pokeIndex);
                    }
                }
                if(gp.gameState == gp.battleState){
                    gp.battleM.curBattleState = BattleManger.BattleState.AWAITING_ENEMY_DECISION;
                }
                gp.playSE(5);
                break;
            case KeyEvent.VK_W:
                if(gp.ui.slotRow >0){
                    gp.ui.slotRow--;
                    gp.playSE(5);
                }else if(gp.ui.slotRow ==0 && gp.ui.slotCol ==1){
                    gp.ui.slotRow = gp.ui.pokemonSlotRowMax-1;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_S:
                if(gp.ui.slotRow < gp.ui.pokemonSlotRowMax-1){
                    gp.ui.slotRow++;
                    gp.playSE(5);
                }else if(gp.ui.slotRow == gp.ui.pokemonSlotRowMax-1 && gp.ui.slotCol ==1){
                    gp.ui.slotRow = 0;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_A:
                if(gp.ui.slotCol >0){
                    gp.ui.slotCol = 0;
                    gp.playSE(5);
                }else{
                    gp.ui.slotCol = 1;
                    gp.ui.slotRow = 0;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_D:
                if(gp.ui.slotCol == 0){
                    gp.ui.slotCol = 1;
                    gp.ui.slotRow = 0;
                    gp.playSE(5);
                }else{
                    gp.ui.slotCol = 0;
                    gp.playSE(5);
                }
                break;
        }
    }
    public void menuOptionsState(int code){
        switch (code) {
            case KeyEvent.VK_W:
                gp.ui.commandNum = (gp.ui.commandNum > 1)?(gp.ui.commandNum-1) : gp.ui.maxOptions;
                break;
            case KeyEvent.VK_S:
                gp.ui.commandNum = (gp.ui.commandNum < gp.ui.maxOptions)?(gp.ui.commandNum+1) : 1;
                break;
            case KeyEvent.VK_A:
                if(gp.ui.commandNum == 1){
                    //音乐音量调节
                    if(gp.music.volumeIndex >0){
                        gp.music.volumeIndex--;
                        gp.music.setVolume(gp.music.volumeValues[gp.music.volumeIndex]);
                    }
                }
                if(gp.ui.commandNum == 2){
                    //音效音量调节
                    if(gp.se.volumeIndex >0){
                        gp.se.volumeIndex--;
                        gp.se.setVolume(gp.se.volumeValues[gp.se.volumeIndex]);
                    }
                }
                break;
            case KeyEvent.VK_D:
                if(gp.ui.commandNum == 1){
                    //音乐音量调节
                    if(gp.music.volumeIndex < gp.music.volumeValues.length -1){
                        gp.music.volumeIndex++;
                        gp.music.setVolume(gp.music.volumeValues[gp.music.volumeIndex]);
             
                    }
                }
                if(gp.ui.commandNum == 2){
                    //音效音量调节
                    if(gp.se.volumeIndex < gp.se.volumeValues.length -1){
                        gp.se.volumeIndex++;
                        gp.se.setVolume(gp.se.volumeValues[gp.se.volumeIndex]);
                    }
                }
                break;
            case KeyEvent.VK_ESCAPE:
                gp.ui.currentMenuState = UI.menuState.main;
                break;
        }
        gp.playSE(5);
    }
    public void dialogState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if(code == KeyEvent.VK_B || code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }

        if(code == KeyEvent.VK_W){
            gp.ui.slotRow--;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_A){
            gp.ui.slotCol--;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_S){
            gp.ui.slotRow++;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_D){
            gp.ui.slotCol++;
            gp.playSE(5);
        }
    }
    public void battleState(int code){

        switch(gp.battleM.curBattleState){
            case Start: battleStart(code); break;
            case MAIN_MENU: battleMainMenu(code);break;
            case FIGHT_MENU: battleFightMenu(code);break;
            case BAG_MENU: battleBagMenu(code);break;
            case POKEMON_MENU: battlePokemonMenu(code);break;
            case PLAYER_ACTION: battleActionState(code);break;
            case ENEMY_ACTION: battleActionState(code);break;
            case CHECK_FAINT:
            case AWAITING_FAINT: awaitFaintState(code);break;
            case RUN_ATTEMPT: battleRunAttempt(code);break;
            case END: battleEndAttempt(code);break;
            default:break;
        }

    }
    // 战斗状态处理函数
    // 开始状态
    private void battleStart(int code){
        if(code == KeyEvent.VK_ENTER && gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length-1){
            gp.battleUI.nextDialogue();
        }else if(code == KeyEvent.VK_ENTER && gp.battleUI.dialogueIndex >= gp.battleUI.dialogueLines.length-1){
            System.out.println("战斗开始对话结束");
            //进入战斗主菜单
            gp.battleM.curBattleState = BattleManger.BattleState.MAIN_MENU;
            gp.battleUI.setDialogues();     //清空对话
        }
    }
    // 主菜单状态
    private void battleMainMenu(int code){
        if(code == KeyEvent.VK_W){
            gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex >0)?(gp.battleUI.selectedOptionIndex -2):gp.battleUI.selectedOptionIndex;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_S){
            gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex <2)?(gp.battleUI.selectedOptionIndex +2):gp.battleUI.selectedOptionIndex;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_A){
            gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex == 1 || gp.battleUI.selectedOptionIndex == 3)?(gp.battleUI.selectedOptionIndex -1):gp.battleUI.selectedOptionIndex;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_D){
            gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex == 0 || gp.battleUI.selectedOptionIndex == 2)?(gp.battleUI.selectedOptionIndex +1):gp.battleUI.selectedOptionIndex;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_ENTER){
            gp.playSE(5);
            System.out.println("选中了选项："+gp.battleUI.options[gp.battleUI.selectedOptionIndex]);
            //进入对应菜单
            switch (gp.battleUI.selectedOptionIndex) {
                case 0: //战斗
                    gp.battleM.curBattleState = BattleManger.BattleState.FIGHT_MENU;
                    break;
                case 1: //背包
                    gp.battleM.curBattleState = BattleManger.BattleState.BAG_MENU;
                    break;
                case 2: //宝可梦
                    gp.battleM.curBattleState = BattleManger.BattleState.POKEMON_MENU;
                    break;
                case 3: //逃跑
                    PendingAction runAction = PendingAction.createRunAction(true);
                    gp.battleM.queuePlayerAction(runAction);
                    break;
            }
            gp.battleUI.setDialogues();// 清空对话框
        }
    }
    // 战斗菜单状态
    private void battleFightMenu(int code){
         if(code == KeyEvent.VK_W){
                if(gp.battleUI.selectedOptionIndex >1 && gp.battleM.playerPokemon.moves[gp.battleUI.selectedOptionIndex -2]!=null){
                    gp.battleUI.selectedOptionIndex -=2;
   
                }
                //  gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex >1)?(gp.battleUI.selectedOptionIndex -2):gp.battleUI.selectedOptionIndex;

                gp.playSE(5);
            }
            if(code == KeyEvent.VK_S){
                if(gp.battleUI.selectedOptionIndex <2 && gp.battleM.playerPokemon.moves[gp.battleUI.selectedOptionIndex + 2]!=null){
                    gp.battleUI.selectedOptionIndex +=2;

                }
                gp.playSE(5);
            }
            if(code == KeyEvent.VK_A){
                if((gp.battleUI.selectedOptionIndex == 1 || gp.battleUI.selectedOptionIndex == 3) && gp.battleM.playerPokemon.moves[gp.battleUI.selectedOptionIndex -1]!=null){
                    gp.battleUI.selectedOptionIndex -=1;
                }
            }
            if(code == KeyEvent.VK_D){
                if((gp.battleUI.selectedOptionIndex == 0 || gp.battleUI.selectedOptionIndex == 2) && gp.battleM.playerPokemon.moves[gp.battleUI.selectedOptionIndex +1]!=null){
                    gp.battleUI.selectedOptionIndex +=1;
                }

                // gp.battleUI.selectedOptionIndex = (gp.battleUI.selectedOptionIndex == 0 || gp.battleUI.selectedOptionIndex == 2 )?(gp.battleUI.selectedOptionIndex +1):gp.battleUI.selectedOptionIndex;
                gp.playSE(5);
            }
            if(code == KeyEvent.VK_ESCAPE){
                //返回主菜单
                gp.battleM.curBattleState = BattleManger.BattleState.MAIN_MENU;
                gp.battleUI.selectedOptionIndex = 0;
                gp.playSE(5);
            }
            if(code == KeyEvent.VK_ENTER){
                gp.playSE(5);
                // 优先使用 BattleManger 的 playerPokemon 引用
                if(gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length-1 && gp.battleUI.dialogueLines.length >0){
                    gp.battleUI.nextDialogue();
                    
                }else if(gp.battleUI.dialogueLines.length >0){
                        gp.battleUI.setDialogues();
                }else{
                    Pokemon active = gp.battleM.playerPokemon != null ? gp.battleM.playerPokemon : gp.player.party.getCurPokemon();
                    if(active != null && active.moves[gp.battleUI.selectedOptionIndex] != null && active.moves[gp.battleUI.selectedOptionIndex].PP > 0){
                        System.out.println("选中了招式：" + active.moves[gp.battleUI.selectedOptionIndex].getName());
                        // 队列玩家动作，进入等待敌方决策流程
                        Move selectedMove = active.moves[gp.battleUI.selectedOptionIndex];
                        PendingAction action = PendingAction.createMoveAction(selectedMove, true);
                        gp.battleM.queuePlayerAction(action);
                        gp.battleUI.setDialogues();// 清空对话框
                        gp.battleUI.selectedOptionIndex = 0;
                
                    }else if(active != null && active.moves[gp.battleUI.selectedOptionIndex] != null && active.moves[gp.battleUI.selectedOptionIndex].PP <= 0){
                        gp.battleUI.setDialogues("招式没有剩余PP了！");
                    }
                }
            }
    }

    private void battleActionState(int code){
        if(code == KeyEvent.VK_ENTER){
            if(gp.battleUI.dialogueLines.length > 0){
                if(gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length - 1){
                    gp.battleUI.nextDialogue();
                }else{
                    gp.battleM.actionDialogConfirmed();
                    gp.battleUI.setDialogues();
                }
            }
        }
    }
    
    
    // 背包菜单状态
    private void battleBagMenu(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.battleM.curBattleState = BattleManger.BattleState.MAIN_MENU;
            gp.battleUI.selectedOptionIndex = 0;    
            gp.playSE(5);
        }
        // Enter键使用物品
        if(code == KeyEvent.VK_ENTER){
            gp.playSE(5);
            int itemIndex = gp.battleUI.slotRow * gp.battleUI.slotColMax + gp.battleUI.slotCol;
            if(itemIndex < gp.player.inventory.size()){
                item.Item selectedItem = gp.player.inventory.get(itemIndex);
                if(selectedItem instanceof item.ITEM_Poke_Ball){
                    item.ITEM_Poke_Ball ball = (item.ITEM_Poke_Ball)selectedItem;
                    // 队列使用精灵球动作
                    battle.BattleManger.PendingAction action = battle.BattleManger.PendingAction.createUseItemAction(ball, itemIndex, true);
                    gp.battleM.queuePlayerAction(action);
                    gp.battleUI.setDialogues(); // 清空对话框
                }else{
                    gp.battleUI.setDialogues("只能在战斗中使用精灵球！");
                }
            }
        }
        // 上下左右选择物品b
        if(code == KeyEvent.VK_W){
            gp.battleUI.slotRow = (gp.battleUI.slotRow >0)?(gp.battleUI.slotRow -1):gp.battleUI.slotRow;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_A){
            gp.battleUI.slotCol= (gp.battleUI.slotCol >0)?(gp.battleUI.slotCol -1):gp.battleUI.slotCol;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_S){
            gp.battleUI.slotRow = (gp.battleUI.slotRow < gp.battleUI.slotRowMax-1)?(gp.battleUI.slotRow +1):gp.battleUI.slotRow;
            gp.playSE(5);
        }
        if(code == KeyEvent.VK_D){
            gp.battleUI.slotCol = (gp.battleUI.slotCol < gp.battleUI.slotColMax-1)?(gp.battleUI.slotCol +1):gp.battleUI.slotCol;
            gp.playSE(5);
        }
        

    }
    //
    private void battlePokemonMenu(int code){
        switch (code) {  
            case KeyEvent.VK_ESCAPE:
                if(gp.gameState == gp.battleState){
                    gp.battleM.curBattleState = BattleManger.BattleState.MAIN_MENU;
                    gp.battleUI.selectedOptionIndex = 0;    
                }
                gp.playSE(5);
                break;
        
            case KeyEvent.VK_ENTER:
                // 选择右侧队伍中的宝可梦进行切换（消耗一回合）
                if(gp.battleUI.slotCol == 1){
                    int pokeIndex = gp.battleUI.slotRow + 1; // 右侧列表从1开始，0为当前出战
                    if(pokeIndex < gp.player.party.size()){
                        pokemon.Pokemon target = gp.player.party.getPoke(pokeIndex);
                        // 仅允许切到存活且非当前出战的宝可梦
                        if(target != null && target.curHp > 0){
                            battle.BattleManger.PendingAction action = battle.BattleManger.PendingAction.createSwitchAction(pokeIndex, null, true);
                            gp.battleM.queuePlayerAction(action);
                            gp.battleUI.setDialogues();
                        }else{
                            gp.battleUI.setDialogues("不能选择濒死的宝可梦！");
                        }
                    }
                }

                gp.playSE(5);
                break;
            case KeyEvent.VK_W:
                if(gp.battleUI.slotRow >0){
                    gp.battleUI.slotRow--;
                    gp.playSE(5);
                }else if(gp.battleUI.slotRow ==0 && gp.battleUI.slotCol ==1){
                    gp.battleUI.slotRow = gp.battleUI.pokemonSlotRowMax-1;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_S:
                if(gp.battleUI.slotRow < gp.battleUI.pokemonSlotRowMax-1){
                    gp.battleUI.slotRow++;
                    gp.playSE(5);
                }else if(gp.battleUI.slotRow == gp.battleUI.pokemonSlotRowMax-1 && gp.battleUI.slotCol ==1){
                    gp.battleUI.slotRow = 0;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_A:
                if(gp.battleUI.slotCol >0){
                    gp.battleUI.slotCol = 0;
                    gp.playSE(5);
                }else{
                    gp.battleUI.slotCol = 1;
                    gp.battleUI.slotRow = 0;
                    gp.playSE(5);
                }
                break;
            case KeyEvent.VK_D:
                if(gp.battleUI.slotCol == 0){
                    gp.battleUI.slotCol = 1;
                    gp.battleUI.slotRow = 0;
                    gp.playSE(5);
                }else{
                    gp.battleUI.slotCol = 0;
                    gp.playSE(5);
                }
                break;
        }

    }
    //
    private void battleRunAttempt(int code){
        if(code == KeyEvent.VK_ENTER){
            if(gp.battleUI.dialogueLines.length > 0){
                if(gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length - 1){
                    gp.battleUI.nextDialogue();
                }else{
                    gp.battleM.actionDialogConfirmed();
                    gp.battleUI.setDialogues(); 
                }
            }
        }
    }
    private void awaitFaintState(int code){
        if(code == KeyEvent.VK_ENTER){
            if(gp.battleUI.dialogueLines.length > 0){
                if(gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length - 1){
                    gp.battleUI.nextDialogue();
                }else{
                    if(gp.battleM.awaitingFaintDialog){ 
                        gp.battleM.processFaintConfirmation();
                    }
                gp.battleUI.setDialogues(); 
                }
            }
        }
    }   
    private void battleEndAttempt(int code){
        if(code == KeyEvent.VK_ENTER){
            if(gp.battleUI.dialogueLines.length > 0){
                if(gp.battleUI.dialogueIndex < gp.battleUI.dialogueLines.length - 1){
                    gp.battleUI.nextDialogue();
                }else{
                    gp.battleM.actionDialogConfirmed();
                    gp.battleUI.setDialogues();
                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
         
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(!upPressed && !downPressed && !leftPressed && !rightPressed){
            isPressed = false;
        }

    }
}
