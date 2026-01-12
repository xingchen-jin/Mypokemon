package battle;


import inventory.PokemonParty;
import item.Item;
import main.GamePanel;
import move.*;
import pokemon.Pokemon;
import pokemon.Pokemon.BaseStat;
public class BattleManger {

    public enum BattleType{
        WILD,       //野外
        TRAINER;    //训练家对战
    }
    public enum BattleState{
        Start,          //战斗开始
        MAIN_MENU,      //主菜单
        FIGHT_MENU,     //战斗菜单
        BAG_MENU,       //背包菜单
        POKEMON_MENU,   //宝可梦菜单
        RUN_ATTEMPT,    //尝试逃跑

        AWAITING_ENEMY_DECISION, // 玩家已选动作，等待敌方决策

        RESOLVE_TURN,       // 判定回合顺序
        PLAYER_ACTION,      // 正在执行玩家动作
        ENEMY_ACTION,       // 正在执行敌方动作
        PLAYER_SWITCHING,   // 玩家切换宝可梦
        ENEMY_SWITCHING,    // 敌方切换宝可梦

        APPLY_ITEM,         // 使用道具
        CHECK_FAINT,        // 检查宝可梦是否濒死
        AWAITING_FAINT,     //等待濒死确认
        END                 //结束

        // ACTION,         //行动
        // END;            //结束
    }

    // 待执行动作的封装类
    public static class PendingAction{
        public enum ActionType{
            NONE,           //无动作
            MOVE,           //使用招式
            SWITCH,         //切换宝可梦    
            USE_ITEM,        //使用道具
            RUN             //逃跑
        }
        public ActionType actionType = ActionType.NONE;
        public Move move;           // 使用的招式
        public int itemIndex;       // 使用的道具索引
        public Item item;           // 使用的道具
        public int switchIndex;     // 切换到的宝可梦索引
        public Pokemon switchTo;    // 切换到的宝可梦

        public boolean forPlayer; // 是否为玩家的动

        public PendingAction(ActionType actionType){
            this.actionType = actionType;
        }

        public static PendingAction createMoveAction(Move move, boolean forPlayer){
            PendingAction action = new PendingAction(ActionType.MOVE);
            action.move = move;
            action.forPlayer = forPlayer;
            return action;
        }
        public static PendingAction createRunAction(boolean forPlayer){
            PendingAction action = new PendingAction(ActionType.RUN);
            action.forPlayer = forPlayer;
            return action;
        }

        public static PendingAction createSwitchAction(int switchIndex, Pokemon switchTo, boolean forPlayer){
            PendingAction action = new PendingAction(ActionType.SWITCH);
            action.switchIndex = switchIndex;
            action.switchTo = switchTo;
            action.forPlayer = forPlayer;
            return action;
        }
        public static PendingAction createUseItemAction(Item item, int itemIndex, boolean forPlayer){
            PendingAction action = new PendingAction(ActionType.USE_ITEM);
            action.item = item;
            action.itemIndex = itemIndex;
            action.forPlayer = forPlayer;
            return action;
        }
        

    }
    public BattleType battleType;
    // public BattleState preBattleState;
    public BattleState curBattleState;
    private BattleState nextBattleState;


    private GamePanel gp;
    public PokemonParty enemyTrainer;
    public Pokemon enemyWild;

    public Pokemon playerPokemon;
    public Pokemon enemyPokemon;

    //玩家和敌方动作暂存
    public PendingAction pendingPlayerAction; 
    public PendingAction pendingEnemyAction;
    
    private PendingAction currentExecuting; // 当前正在执行的动作
    public boolean isWin = false; // 战斗胜利标志

    //动作执行控制
    private boolean actionInProgress = false;       // 是否有动作正在进行
    public int actionTimer = 0;                    // 帧计时或动画时长
    private boolean waitingForDialog = false;       // 是否等待对话/确认
    private boolean animationFinished = true;      // 动画完成标志

   
    private boolean lastActionFromPlayer = false;  // 记录最后一个动作是否来自玩家
    // 濒死对话等待处理
    public boolean awaitingFaintDialog = false; // 是否正在等待濒死对话确认
    private int faintNextIndex = -1; // 下一只替换的索引，-1表示无替换（战斗结束）
    private boolean faintWasEnemy = false; // 濒死的是敌方还是玩家方
    
    //逃跑结果
    private boolean hasRunAway = false; // 是否成功逃跑
    public BattleManger(GamePanel gp){
        this.gp = gp;

    }

    public PendingAction getCurrentExecutingAction(){
        return this.currentExecuting;
    }
    public boolean isAnimationFinished(){
        return this.animationFinished;
    }
    //请求对话暂停
    public void requestDialogPause(){
        this.waitingForDialog = true;
    }

    //对话完成确认
    public void actionDialogConfirmed(){
        this.waitingForDialog = false;
    }

    //动画完成确认
    public void actionAnimationFinished(){
        this.animationFinished = true;
    }

    //战斗初始化
    public void BattleInitialize(PokemonParty enemyTrainer,BattleType battleType){
        this.enemyTrainer = enemyTrainer;
        this.battleType = battleType;
        curBattleState = BattleState.Start;
        gp.player.party.setCurPokemon(gp.player.party.getFirstAblePokemonIndex());
        playerPokemon = gp.player.party.getCurPokemon();
        enemyPokemon = enemyTrainer.getCurPokemon();
        gp.battleUI.setDialogues(); 
    }
    public void BattleInitialize(Pokemon enemyWild,BattleType battleType){
        this.enemyWild = enemyWild;
        this.battleType = battleType;
        curBattleState = BattleState.Start;
        gp.player.party.setCurPokemon(gp.player.party.getFirstAblePokemonIndex());
        playerPokemon = gp.player.party.getCurPokemon();
        enemyPokemon = enemyWild;
        gp.battleUI.setDialogues();

    }

    // 玩家动作队列
    public void queuePlayerAction(PendingAction action){
        this.pendingPlayerAction = action;
        this.curBattleState = BattleState.AWAITING_ENEMY_DECISION;
    }
    // 敌方动作队列
    public void queueEnemyAction(PendingAction action){
        this.pendingEnemyAction = action;
        if(this.curBattleState == BattleState.AWAITING_ENEMY_DECISION)
            this.curBattleState = BattleState.RESOLVE_TURN;
    }

    // 决定敌方动作
    private void decideEnemyIfNeeded(){
        if(this.pendingEnemyAction == null){
            //随机选择一个可用招式
            // 确保 enemyPokemon 已设置
            if(enemyPokemon == null){
                if(enemyTrainer != null){
                    int idx = enemyTrainer.getFirstAblePokemonIndex();
                    if(idx != -1) enemyPokemon = enemyTrainer.getPoke(idx);
                }else if(enemyWild != null){
                    enemyPokemon = enemyWild;
                }
            }
            Move move = (enemyPokemon != null)? enemyPokemon.getRandomAvailableMove() : null;
            if(move != null){
                this.queueEnemyAction(PendingAction.createMoveAction(move, false));
            }
        }
        // 确保敌方动作已决定后进入回合判定
        this.curBattleState = BattleState.RESOLVE_TURN;
    }
    //先手判断（速度）
    private boolean isPlayerFirst(){
        // 保证双方当前宝可梦存在的安全获取
        if(playerPokemon == null){
            if(gp != null && gp.player != null && gp.player.party != null){
                playerPokemon = gp.player.party.getCurPokemon();
            }
        }
        if(enemyPokemon == null){
            if(enemyTrainer != null){
                int idx = enemyTrainer.getFirstAblePokemonIndex();
                if(idx != -1) enemyPokemon = enemyTrainer.getPoke(idx);
            }else if(enemyWild != null){
                enemyPokemon = enemyWild;
            }
        }

        if(playerPokemon == null && enemyPokemon == null) return true;
        if(playerPokemon == null) return false;
        if(enemyPokemon == null) return true;

        int playerSpeed = 0;
        int enemySpeed = 0;
        try{
            playerSpeed = playerPokemon.tempStatistic.get(BaseStat.SPEED);
        }catch(Exception e){
            playerSpeed = 0;
        }
        try{
            enemySpeed = enemyPokemon.tempStatistic.get(BaseStat.SPEED);
        }catch(Exception e){
            enemySpeed = 0;
        }
        return playerSpeed >= enemySpeed;
    }

    
    //战斗更新
    public void update(){
        switch(curBattleState){
            case Start:break;
            case MAIN_MENU:break;
            case FIGHT_MENU:break;
            case BAG_MENU:break;
            case POKEMON_MENU:break;
            case RUN_ATTEMPT:run_attempt();break;
            case AWAITING_ENEMY_DECISION:
                // 玩家已选动作，决定敌方动作并进入回合判定
                decideEnemyIfNeeded();
                break;
            case RESOLVE_TURN:
                // 判定回合顺序
                boolean playerFirst = isPlayerFirst();
                if(playerFirst){
                    curBattleState = BattleState.PLAYER_ACTION;
                    actionInProgress = false;    // 重置动作状态
                }else{
                    curBattleState = BattleState.ENEMY_ACTION;
                    actionInProgress = false;       // 重置动作状态
                }
                break;
            case PLAYER_ACTION:
                // 执行玩家动作
                if(actionInProgress){
                    if(actionTimer >0)actionTimer--;
                    if(actionTimer <=0 ){
                        animationFinished = true;   // 标记动画完成

                    }
                    if(animationFinished && (!waitingForDialog)){
                        actionInProgress = false;   // 动作执行完毕
                        //每次行动完后检查濒死，若对方濒死跳过对方行动
                        if(nextBattleState != null){
                            curBattleState = nextBattleState;
                            nextBattleState = null;
                        }else{
                            curBattleState =  BattleState.CHECK_FAINT;
                        }
                        currentExecuting = null;
                    }

                }else{
                    if(pendingPlayerAction == null){
                        curBattleState = (pendingEnemyAction != null)? BattleState.ENEMY_ACTION : BattleState.MAIN_MENU;
                    
                    }else{
                        currentExecuting = pendingPlayerAction; // 当前执行动作
                        pendingPlayerAction = null;             // 清空待执行动作
                        startExecution(currentExecuting);
                    }
                }
                
                break;
            case ENEMY_ACTION:
                // 执行敌方动作
                if(actionInProgress){
                    if(actionTimer >0)actionTimer--;
                    if(actionTimer <=0 ){
                        animationFinished = true;   // 标记动画完成
                        // actionInProgress = false;   // 动作执行完毕
                        // animationFinished = true;   // 标记动画完成
                        // waitingForDialog = false;   
                        
                        // //每次行动完后检查濒死，若对方濒死跳过对方行动
                        // curBattleState =  BattleState.CHECK_FAINT;
                        // currentExecuting = null;
                    }

                    if(animationFinished && (!waitingForDialog)){
                        actionInProgress = false;   // 动作执行完毕
                        //每次行动完后进入下一状态，检查濒死，若对方濒死跳过对方行动,或结束游戏
                        curBattleState =  (nextBattleState != null) ? nextBattleState : BattleState.CHECK_FAINT;
                        nextBattleState = null;
                        currentExecuting = null;
                    }
                }else{
                    if(pendingEnemyAction == null){
                        curBattleState =  (pendingPlayerAction != null)? BattleState.PLAYER_ACTION : BattleState.MAIN_MENU;
                        break;
                    }else{
                        currentExecuting = pendingEnemyAction;
                        pendingEnemyAction = null;
                        startExecution(currentExecuting);
                    }
                }
                    
                break;
            case PLAYER_SWITCHING: switching(); break;
            case ENEMY_SWITCHING: break;
            case APPLY_ITEM: break;
            case CHECK_FAINT:  checkFaint();break;
            case AWAITING_FAINT:AWAITING_FAINT();break;
            case END: end();break;
            }
    }

    //动作初始化
    private void startExecution(PendingAction action){
        if(action == null) return;

        this.lastActionFromPlayer = action.forPlayer;

        int duration = 60; // 默认帧数
        switch(action.actionType){
            case MOVE:
                if(action.forPlayer){
                    if(action.move != null) gp.battleSystem.useMove(playerPokemon, enemyPokemon, action.move);
                }else{
                    if(action.move != null) gp.battleSystem.useMove(enemyPokemon, playerPokemon, action.move);
                }
                nextBattleState = BattleState.CHECK_FAINT;
                duration = 30;  
                break;
            case SWITCH:
                if(action.forPlayer){
                    gp.player.party.setCurPokemon(action.switchIndex);
                    playerPokemon = gp.player.party.getCurPokemon();
                    gp.battleSystem.switchPokemon(playerPokemon);
                    curBattleState = BattleState.PLAYER_SWITCHING;
                }else{
                    if(enemyTrainer != null){
                        enemyTrainer.setCurPokemon(action.switchIndex);
                        enemyPokemon = enemyTrainer.getCurPokemon();
                        curBattleState = BattleState.ENEMY_SWITCHING;
                    }else{
                        enemyPokemon = action.switchTo;
                        curBattleState = BattleState.ENEMY_SWITCHING;
                    }
                    gp.battleSystem.switchPokemon(enemyPokemon);
                }
                duration = 30;
                break;
            case USE_ITEM:
                duration = 40;
                if(action.forPlayer && action.item.isBallItem){
                    if(gp.battleSystem.useBallItem((item.ITEM_Poke_Ball)action.item, enemyPokemon)){
                        // 捕捉成功，结束战斗
                        nextBattleState = BattleState.END;
                        isWin = true;
                        gp.player.party.add(enemyPokemon);
                    }else{
                        // 捕捉失败，继续战斗
                        nextBattleState = BattleState.CHECK_FAINT;
                    }
                    gp.player.inventory.removeItem(action.item); // 从背包中移除精灵球
                }
            
                break;
            case RUN:

                curBattleState = BattleState.RUN_ATTEMPT;
                duration = 30;
                break;
            default:
                duration = 30;
                break;
        }
        // 标记执行中；UI 若需等待确认可调用 requestDialogPause()
        this.actionTimer = duration;
        this.animationFinished = false;
        this.actionInProgress = true;
        requestDialogPause();  

    }

    public void switching(){
        // 切换完成后检查濒死
        curBattleState = BattleState.CHECK_FAINT;
    }

    // public void executePlayerMove(Move move,boolean isPlayer){
    //     if(playerPokemon == null){
    //         playerPokemon = gp.player.party.getCurPokemon();
    //     }
    //     if(enemyPokemon == null){
    //         enemyPokemon = (enemyTrainer != null)? enemyTrainer.getPoke(enemyTrainer.getFirstAblePokemonIndex()) : enemyWild;
    //     }

    //     if(isPlayer)
    //         gp.battleSystem.useMove(playerPokemon, enemyPokemon, move);
    //     else
    //         gp.battleSystem.useMove(enemyPokemon, playerPokemon, move);

    // }

    //濒死检查
    public void checkFaint(){
        if(curBattleState != BattleState.CHECK_FAINT) return;
        if(this.awaitingFaintDialog){
            curBattleState = BattleState.AWAITING_FAINT;
            return;
        }

        boolean enemyFainted = enemyPokemon != null && enemyPokemon.curHp <= 0;     //敌方宝可梦濒死
        boolean playerFainted = playerPokemon != null && playerPokemon.curHp <= 0;  //玩家宝可梦濒死
        boolean checkEnemyFirst = lastActionFromPlayer; // 玩家动作后优先检查敌方

        if(checkEnemyFirst && enemyFainted){
            handleFaint(true);
            return;
        }
        if(!checkEnemyFirst && playerFainted){
            handleFaint(false);
            return;
        }
        if(enemyFainted){
            handleFaint(true);
            return;
        }
        if(playerFainted){
            handleFaint(false);
            return;
        }

        if(lastActionFromPlayer){
            curBattleState = (pendingEnemyAction != null)? BattleState.ENEMY_ACTION : BattleState.MAIN_MENU;
        }else{
            curBattleState = (pendingPlayerAction != null)? BattleState.PLAYER_ACTION : BattleState.MAIN_MENU;
        }
    }

    // 在对话确认后调用，处理濒死后的切换或结束
    public void processFaintConfirmation(){
        if(!awaitingFaintDialog) return;

        // 清理对话等待状态
        this.awaitingFaintDialog = false;
        this.actionDialogConfirmed(); // 解除对话暂停

        if(faintWasEnemy){
            if(faintNextIndex >= 0){    
                enemyTrainer.setCurPokemon(faintNextIndex);
                enemyPokemon = enemyTrainer.getCurPokemon();
                // preBattleState = curBattleState;
                curBattleState = BattleState.ENEMY_SWITCHING;
            }else{
                // 战斗结束，玩家获胜
                curBattleState = BattleState.END;
                isWin = true;
            }
        }else{
            if(faintNextIndex >= 0){
                // preBattleState = curBattleState;
                curBattleState = BattleState.POKEMON_MENU; // 玩家自主切换
            }else{
                // 战斗结束，玩家失败
                // preBattleState = curBattleState;
                curBattleState = BattleState.END;
                isWin = false;
            }
        }

        // 重置临时数据
        faintNextIndex = -1;
    }

    // 统一的濒死进入流程：设置对话、记录替换目标并进入等待状态
    private void handleFaint(boolean wasEnemy){
        if(wasEnemy){
            if(enemyPokemon != null) enemyPokemon.curHp = 0;
            int nextIndex = (enemyTrainer != null)? enemyTrainer.getFirstAblePokemonIndex() : -1;
            String faintedName = (enemyPokemon != null)? enemyPokemon.name : "对手的宝可梦";
            if(nextIndex != -1){
                String nextName = enemyTrainer.getPoke(nextIndex).name;
                gp.battleUI.setDialogues(faintedName + "濒死了！", "训练家派出了\n" + nextName + "！");
                this.faintNextIndex = nextIndex;
            }else{
                gp.battleUI.setDialogues(faintedName + "濒死了！", "你赢得了战斗！");
                this.faintNextIndex = -1;
            }
            this.faintWasEnemy = true;
        }else{
            if(playerPokemon != null) playerPokemon.curHp = 0;
            int nextIndex = gp.player.party.getFirstAblePokemonIndex();
            String faintedName = (playerPokemon != null)? playerPokemon.name : "你的宝可梦";
            if(nextIndex != -1){
                gp.battleUI.setDialogues(faintedName + "濒死了！", "请更换宝可梦！");
                this.faintNextIndex = nextIndex;
            }else{
                gp.battleUI.setDialogues(faintedName + "濒死了！", "你败北了！");
                this.faintNextIndex = -1;
            }
            this.faintWasEnemy = false;
        }

        this.requestDialogPause();
        this.awaitingFaintDialog = true;
        this.animationFinished = false;
        // this.actionTimer = 40;
        // this.preBattleState = curBattleState;
        this.curBattleState = BattleState.AWAITING_FAINT;


    }
    
    private void AWAITING_FAINT(){

    }
    private void run_attempt(){
        // 野外战斗可逃跑，训练家战斗不可逃跑
        if(!hasRunAway){
            if(battleType == BattleType.WILD){
                if(gp.battleSystem.runAway()){
                
                    hasRunAway = true;
                }else{

                    curBattleState = BattleState.CHECK_FAINT;
                }
                isWin = false;
            }else{
                gp.battleUI.setDialogues("训练家战斗不能逃跑！");
            }
        }

        //判断对话框等待
        if(!waitingForDialog ){
            if(battleType == BattleType.WILD){
                curBattleState = BattleState.END;
            }else{
                curBattleState = BattleState.MAIN_MENU;
            }
            hasRunAway = false;
        }
    }

    private void end(){
        if(waitingForDialog) return;
        gp.gameState = gp.playState;
        gp.playMusic(gp.getPreMusicIndex());
        // gp.battleM = null;
    }
}
