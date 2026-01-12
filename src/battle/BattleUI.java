package battle;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import item.Item;
import main.GamePanel;
import pokemon.Pokemon;
import tile.BattleTileManger;
import tile.BattleTileManger.TileType;
import tile.Tile;
import main.SuperUI;
import move.Move;

// 战斗界面
public class BattleUI extends SuperUI {
    BattleManger battleM;

    public BattleTileManger battleTileM;
    // 对话指针：记录当前应显示哪一段文本
    public int dialogueIndex = 0;
    public String[] dialogueLines = new String[0];
    public String[] options = new String[]{"战斗","背包","宝可梦","逃跑"};  //0-战斗 1-背包 2-宝可梦 3-逃跑
    public int selectedOptionIndex = 0;     // 当前选中的菜单选项索引

    //动画用计数器
    int animationCounter = 0;
    int animationIndex = 0; 
    final int animationFrameDuration = 10;   //每帧持续时间
    int xOffset = 0;    //图像X偏移
    int yOffset = 0;    //图像Y偏移

    int imageHeight, imageWidth;     //图像绘制区域大小

    Font UniFont;

    public BattleUI(GamePanel gp, BattleManger battleM) {
        super(gp);
        this.battleM = battleM;
        this.battleTileM = new BattleTileManger(gp);
        this.UniFont = gp.UniFont;
    }
    public BattleUI(GamePanel gp) {
        super(gp);
        this.battleTileM = new BattleTileManger(gp);
        this.UniFont = gp.UniFont;
    }

    public void setupBattle() {
        // 初始化对话指针
        dialogueIndex = 0;
        dialogueLines = new String[0];
        selectedOptionIndex = 0;

        // 播放战斗音乐
        gp.playMusic(6);
    }

    // 设定当前对话内容，并重置指针
    public void setDialogues(String... lines) {
        this.dialogueLines = (lines == null) ? new String[0] : lines;
        this.dialogueIndex = 0;
    }
    // 添加对话内容到现有对话末尾
    public void addDialogue(String dialogue) {
        String[] newLines = new String[dialogueLines.length + 1];
        System.arraycopy(dialogueLines, 0, newLines, 0, dialogueLines.length);
        newLines[dialogueLines.length] = dialogue;
        this.dialogueLines = newLines;
    }

    // 手动推进对话指针（可在按键事件里调用）
    public void nextDialogue() {
        if (dialogueIndex + 1 < dialogueLines.length) {
            dialogueIndex++;
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // 抗锯齿设置
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        float baseFontSize = 27f;
        
        if (gp.UniFont != null) {
            g2.setFont(gp.UniFont.deriveFont(Font.BOLD, baseFontSize));
        } else {
            g2.setFont(new Font(Font.SERIF, Font.BOLD, (int)baseFontSize));
        }
        g2.setColor(Color.white);

        switch (battleM.curBattleState) {
            case Start: drawBattleStartScreen() ;break;
            case MAIN_MENU: drawBattleMainMenuScreen(); break;
            case FIGHT_MENU: drawBattleFightMenuScreen(); break;
            case BAG_MENU: drawInventory(); break;
            case POKEMON_MENU: drawPokemonScreen(); break;
            case PLAYER_ACTION: drawActionScreen(); break;
            case ENEMY_ACTION: drawActionScreen(); break;
            case END:
            case AWAITING_FAINT: drawFaintScreen(); break;
            
            
            default:drawActionScreen();
                break;
            }
    }
    // 绘制对话框
    private void drawDialogueScreen(int x,int y,int width,int height,String currentDialog) {

        drawSubWindowLocal(x, y, width, height);
      
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialog.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }

    }

    private void drawSubWindowLocal(int x,int y,int width,int height){
        Color c = new Color(0,0,0,100);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    //绘制菜单选项
    public void drawMenuOptions(String[] options,int selectedIndex,int col,int row,int width,int height){
        int optionX = (int)((col + 1.8) * battleTileM.tileSize);
        int optionY = (int)((row + 1.5) * battleTileM.tileSize);
        //步长
        int stepX = (width/2) * battleTileM.tileSize;
        int stepY = (height/3) * battleTileM.tileSize;
        // g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            // 计算文本高度以垂直居中
            FontMetrics fm = g2.getFontMetrics();
            int textHeight = fm.getHeight();
            int ascent = fm.getAscent();
            int baselineY = optionY + (battleTileM.tileSize - textHeight) / 2 + ascent;

            // 绘制选项文本            
            g2.setColor(new Color(normalColor.getRed(),normalColor.getGreen(),normalColor.getBlue(),50));
            g2.drawString(option, optionX + 2, baselineY + 2);
            g2.setColor(normalColor);
            g2.drawString(option, optionX, baselineY);


            // 绘制选中指示符
            if (i == selectedIndex) {
                String pointer = "▶";               
                g2.setColor(new Color(shadowColor.getRed(),shadowColor.getGreen(),shadowColor.getBlue(),50));
                g2.drawString(pointer, optionX - battleTileM.tileSize + 2, baselineY + 2);
                g2.setColor(normalColor);
                g2.drawString(pointer, optionX - battleTileM.tileSize, baselineY);
 
            }

            optionX += stepX;
            // 换行显示选项
            if (i == 1) {
                optionX = (int)((col + 1.8) * battleTileM.tileSize);
                optionY += stepY;
            }
        }
    }
    //绘制宝可梦立绘
    public void drawPokemonImage(int x, int y,BufferedImage image){ 
        g2.drawImage(image, x + xOffset, y + yOffset, null);
    }

    //闪烁效果,透明显示
    public void drawPokemonImage(int x, int y,BufferedImage image,Boolean isFishing){ 

        if(isFishing){
            long time = System.currentTimeMillis();
            // 每隔300毫秒改变一次X偏移方向
            if((time/300)%2==0){
                xOffset = -3;
            }else{
                xOffset = 3;
            }
            if ((time / 200) % 2 == 0) {
                g2.drawImage(image, x + xOffset, y + yOffset, null);
            }else {
                // 透明绘制
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
                g2.drawImage(image, x + xOffset, y + yOffset, null);
                // 恢复不透明绘制
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));
            }

        }else{
            g2.drawImage(image, x, y, null);
        }
    }
    //技能动画
    private void drawMoveAnimation(Move move,Boolean forPlayer){
        if(move == null) return;
        if(move.isAnimated){
            BufferedImage animationImage = move.getAnimationImage();
            if(animationImage == null) return;
            int x,y;
            if(forPlayer){
                x = 168 * gp.scale;
                y = 8 * gp.scale;
            }else{
                x = 32 * gp.scale;
                y = 85 * gp.scale;
            }
            g2.drawImage(animationImage, x, y, null);

        }

    }
    //绘制血条
    public void drawHpBar(int curHp,int maxHp,Boolean isPlayer) {

        //HP起始位置
        int x,y,cols;
        if(isPlayer){
            x = 22*battleTileM.tileSize;
            y = 15*battleTileM.tileSize;
        }else{
            x = 4*battleTileM.tileSize;
            y = 4*battleTileM.tileSize;
        }
        Tile tempTile = battleTileM.tile[TileType.HPBAR.ordinal()][1];
        g2.drawImage(tempTile.image, x, y, null);
        x+= battleTileM.tileSize;
        tempTile = battleTileM.tile[TileType.HPBAR.ordinal()][2];
        g2.drawImage(tempTile.image, x, y, null);
        x+= battleTileM.tileSize;

        //计算血条格数
        cols = 6;
        float hpPerCol = (float)(maxHp)/cols;  //每格血量
        int fullCols = (int)(curHp/hpPerCol);  //满格数d
        // int partialHp = curHp % hpPerCol;   //部分格血量

        int i;
        //绘制满格血条
        for(i=0;i<fullCols;i++){
            tempTile = battleTileM.tile[TileType.HPBAR.ordinal()][11];
            g2.drawImage(tempTile.image, x, y, null);
            x+= battleTileM.tileSize;
        }

        //绘制空格
        for(;i<cols;i++){
            tempTile = battleTileM.tile[TileType.HPBAR.ordinal()][3];
            g2.drawImage(tempTile.image, x, y, null);

            x+= battleTileM.tileSize;
        }
        
        
    }
    //绘制等级
    public void drawLevel(int level,Boolean isPlayer) {
        // 格子左上角
        int cellX, cellY;
        if(isPlayer){
            cellX = 28*battleTileM.tileSize;
            cellY = 14*battleTileM.tileSize;
        }else{
            cellX = 10*battleTileM.tileSize;
            cellY = 3*battleTileM.tileSize;
        }
        String levelStr = "Lv"+level;

        // 计算使文本垂直居 中于该格的基线位置
        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();
        int baselineY = cellY + (battleTileM.tileSize - textHeight)/2 + ascent;

        // 轻微阴影 + 正常字，仍以格子左上角为参考

        g2.setColor(shadowColor);
        g2.drawString(levelStr, cellX + 2, baselineY + 2);
        g2.setColor(normalColor);
        g2.drawString(levelStr, cellX, baselineY);
    }

    //绘制名字
    private void drawName(String name,Boolean isPlayer) {  
        //名字起始位置
        int x,y;
        if(isPlayer){
            x = 18*battleTileM.tileSize;
            y = 14*battleTileM.tileSize;
        }else{
            x = 2*battleTileM.tileSize;
            y = 3*battleTileM.tileSize;
        }

        // 计算使文本垂直居 中于该格的基线位置
        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();
        int baselineY = y + (battleTileM.tileSize - textHeight)/2 + ascent;

        // 起点即文本开头：不再右下偏移     
        g2.setColor(shadowColor);
        g2.drawString(name, x+2, baselineY+2);
        g2.setColor(normalColor);
        g2.drawString(name, x, baselineY);
    }
    
    //绘制血量数值,只绘制玩家方
    private void drawHpAmount(int curHp,int maxHp) {
        //HP起始位置
        int x,y;
        x = 26*battleTileM.tileSize;
        y = 16*battleTileM.tileSize;

        String hpStr = curHp + "/ " + maxHp;    //血量文本，可以占位符优化
        // 计算使文本垂直居 中于该格的基线位置
        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();
        int baselineY = y + (battleTileM.tileSize - textHeight)/2 + ascent;

        // 轻微阴影 + 正常字
        g2.setColor(shadowColor);
        g2.drawString(hpStr, x + 2, baselineY + 2);
        g2.setColor(normalColor);
        g2.drawString(hpStr, x, baselineY);
        
    }
    //绘制状态信息
    public void drawStatusInfo(Pokemon pokemon,Boolean isPlayer) {

        //绘制血条
        drawHpBar(pokemon.curHp, pokemon.maxHp, isPlayer);
        
        //绘制等级
        drawLevel(pokemon.level, isPlayer);
        //绘制名字
        drawName(pokemon.name, isPlayer);
        if(isPlayer){
            //绘制血量
            drawHpAmount(pokemon.curHp, pokemon.maxHp);
        }

    }

    //绘制技能信息
    private void drawMoveInfo() {
        // 技能信息起始位置
        int x = 22 * battleTileM.tileSize;
        int y = 20 * battleTileM.tileSize;
  
  
        
        Move moove = (battleM.playerPokemon != null) ? battleM.playerPokemon.moves[selectedOptionIndex] : null;
        if (moove == null) return;

        String powerStr = "威力: " + moove.getPower();
        String accuracyStr = "命中: " + moove.getAccuracy();
        String ppStr = "PP   " + moove.PP + "/" + moove.getMaxPP();
        // 绘制威力
        g2.setColor(normalColor);
        g2.drawString(powerStr, x, y);
        // 绘制命中
        g2.drawString(accuracyStr, x, y + battleTileM.tileSize);
        // 绘制PP
        g2.drawString(ppStr, x, y + 2 * battleTileM.tileSize);
        
    }

    private void drawUseItemEffect(Item item ,boolean forPlayer){
        if(item == null) return;
        if(forPlayer){
            if(item.isBallItem){ 
                // 精灵球捕捉动画效果
                animationCounter++;
                if(animationCounter >= item.animationSpeed){
                    animationCounter = 0;
                    animationIndex++;
                    if(animationIndex>=3){
                        animationCounter =2;
                    }
                }                  
                int x,y;
                x = 24 * battleTileM.tileSize;
                y = battleTileM.tileSize + gp.tileSize*animationIndex;
                BufferedImage ballImage = item.getFrame(2-animationIndex);
                g2.drawImage(ballImage, x, y, null);
            }else{
                drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.playerPokemon.front);
            }
            drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
        }


    
    }
    // 绘制战斗开始界面
    public void drawBattleStartScreen() {
        this.selectedOptionIndex = 0;
        int[][] frameMain = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.FRAME_MAIN.ordinal()];
        if (frameMain != null) {
            battleTileM.drawOverlay(g2, frameMain);
        }
        if(battleM.enemyWild != null && battleM.battleType == BattleManger.BattleType.WILD){
            if (dialogueLines.length == 0) {
                dialogueIndex = 0;
                String myPokemonName = "";
                if (battleM.playerPokemon != null) {
                    myPokemonName = battleM.playerPokemon.name;
                } else if (gp.player != null && gp.player.party != null && battleM.playerPokemon != null) {
                    myPokemonName = battleM.playerPokemon.name;
                }
                setDialogues("野生的"+battleM.enemyWild.name+"出现了！","就决定是你了！"+ myPokemonName+"!");
            }
            if (dialogueIndex < dialogueLines.length) {
                drawDialogueScreen(0, 18*battleTileM.tileSize, 32*battleTileM.tileSize, 6*battleTileM.tileSize, dialogueLines[dialogueIndex]);
            }
        }
        
        // g2.drawString("", 0, 0);
    }

    // 绘制战斗主菜单界面
    public void drawBattleMainMenuScreen() {
        animationIndex = 0;
        int[][] frameMain = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.FRAME_MAIN.ordinal()];
        if (frameMain != null) battleTileM.drawOverlay(g2, frameMain);

        // 绘制对话框
        if(battleM.enemyWild != null){
            if (dialogueLines.length == 0) {
                dialogueIndex = 0;
                String myPokemonName = "";
                if (battleM.playerPokemon != null) {
                    myPokemonName = battleM.playerPokemon.name;
                } else if (gp.player != null && gp.player.party != null && battleM.playerPokemon != null) {
                    myPokemonName = battleM.playerPokemon.name;
                }
                setDialogues("想要\n"+myPokemonName+"做什么？");
            }
            if (dialogueIndex < dialogueLines.length) {
                drawDialogueScreen(0, 18*battleTileM.tileSize, 32*battleTileM.tileSize, 6*battleTileM.tileSize, dialogueLines[dialogueIndex]);
            }
        }



        int[][] frameInfo = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.INFO.ordinal()];
        if (frameInfo != null) battleTileM.drawOverlay(g2, frameInfo);
        //绘制玩家宝可梦立绘
        if (battleM.playerPokemon != null) {
            if (battleM.playerPokemon.back != null) drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
            drawStatusInfo(battleM.playerPokemon, true);
        }

        // 绘制敌方立绘
        if (battleM.enemyPokemon != null) {
            if (battleM.enemyPokemon.front != null) drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front);
            drawStatusInfo(battleM.enemyPokemon, false);
        } 

        // 绘制信息和血条框架
        drawMenu(16,18,16,6);
        //绘制菜单选项
        drawMenuOptions(options, selectedOptionIndex, 16, 18,16,6);

    }

    // 绘制战斗菜单界面
    public void drawBattleFightMenuScreen() {
    
        int[][] frameMain = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.FRAME_MAIN.ordinal()];
        if (frameMain != null) battleTileM.drawOverlay(g2, frameMain);

        // 绘制信息和血条框架
        int[][] frameInfo = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.INFO.ordinal()];
        if (frameInfo != null) battleTileM.drawOverlay(g2, frameInfo);

        // 立绘与状态信息
        if (battleM.playerPokemon != null) {
            if (battleM.playerPokemon.back != null) drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
            drawStatusInfo(battleM.playerPokemon, true);
        } else if (gp.player != null && gp.player.party != null && gp.player.party.getCurPokemon() != null) {
            drawPokemonImage(32*gp.scale, 85*gp.scale, gp.player.party.getCurPokemon().back);
            drawStatusInfo(gp.player.party.getCurPokemon(), true);
        }

        if (battleM.enemyPokemon != null) {
            if (battleM.enemyPokemon.front != null) drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front);
            drawStatusInfo(battleM.enemyPokemon, false);
        }
        // 绘制对话框
        if(this.dialogueLines.length != 0 && dialogueIndex < dialogueLines.length){
            drawDialogueScreen(0, 18*battleTileM.tileSize, 32*battleTileM.tileSize, 6*battleTileM.tileSize, dialogueLines[dialogueIndex]);
            return;
        }
        //技能选项框与信息框
        drawMenu(0, 18, 21, 6); 
        drawMenu(21, 18, 11, 6);
        //绘制菜单选项
        String[] fightOptions = new String[4];
        Pokemon curPoke = null;
        if (battleM.playerPokemon != null) curPoke = battleM.playerPokemon;


        if (curPoke != null) {
            for (int i = 0; i < 4; i++) {
                if (curPoke.moves[i] != null) {
                    fightOptions[i] = curPoke.moves[i].getName();
                } else {
                    fightOptions[i] = "---";
                }
            }
        }
        drawMenuOptions(fightOptions, selectedOptionIndex, 0, 18, 21, 6);
        //绘制技能信息
        drawMoveInfo();
    }

    // 绘制行动
    public void drawActionScreen() {

        int[][] frameMain = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.FRAME_MAIN.ordinal()];
        if (frameMain != null) battleTileM.drawOverlay(g2, frameMain);

        // 绘制信息面板
        int[][] frameInfo = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.INFO.ordinal()];
        if (frameInfo != null) battleTileM.drawOverlay(g2, frameInfo);

                // 状态信息
        if (battleM.playerPokemon != null) {
            // if (battleM.playerPokemon.back != null) drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back, false);
            drawStatusInfo(battleM.playerPokemon, true);

        } else if (gp.player != null && gp.player.party != null && gp.player.party.getCurPokemon() != null) {
            // drawPokemonImage(32*gp.scale, 85*gp.scale, gp.player.party.getCurPokemon().back, false);
            drawStatusInfo(gp.player.party.getCurPokemon(), true);
        }

        if (battleM.enemyPokemon != null) {
            // if (battleM.enemyPokemon.front != null) drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front, false);
            drawStatusInfo(battleM.enemyPokemon, false);
        }
        if (dialogueIndex < dialogueLines.length) {
            drawDialogueScreen(0, 18*battleTileM.tileSize, 32*battleTileM.tileSize, 6*battleTileM.tileSize, dialogueLines[dialogueIndex]);
        }

        //特效动画(暂时只显示闪烁)
        // //闪烁
        battle.BattleManger.PendingAction curAction = battleM.getCurrentExecutingAction();
        if(curAction != null && !battleM.isAnimationFinished()){
            switch (curAction.actionType) {
                case MOVE:
                    if(curAction.forPlayer){
                        drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
                        drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front,true);
                        drawMoveAnimation(curAction.move,true);
                    }else{
                        drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front);
                        drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back,true);
                        drawMoveAnimation(curAction.move,false);
                    }
                    break;
                case USE_ITEM:
                    drawUseItemEffect(curAction.item,curAction.forPlayer);
                    break;
                case SWITCH:
                    if(curAction.forPlayer){
                        drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front);
                    }else{
                        drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
                    }
                    break;
                default:break;
            }

        }else {
            drawPokemonImage(32*gp.scale, 85*gp.scale, battleM.playerPokemon.back);
            drawPokemonImage(168*gp.scale, 8*gp.scale, battleM.enemyPokemon.front);
        }
        

        drawStatusInfo(battleM.playerPokemon, true);
        drawStatusInfo(battleM.enemyPokemon, false);
    }
    
    // 绘制濒死确认界面
    public void drawFaintScreen() {
        int[][] frameMain = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.FRAME_MAIN.ordinal()];
        if (frameMain != null) battleTileM.drawOverlay(g2, frameMain);
        // 绘制信息面板
        int[][] frameInfo = battleTileM.frameTileNum[BattleTileManger.BattleUIElemnt.INFO.ordinal()];
        if (frameInfo != null) battleTileM.drawOverlay(g2, frameInfo);

                // 状态信息
        if (battleM.playerPokemon != null) {
            drawStatusInfo(battleM.playerPokemon, true);

        } else if (gp.player != null && gp.player.party != null && gp.player.party.getCurPokemon() != null) {
            drawStatusInfo(gp.player.party.getCurPokemon(), true);
        }

        if (battleM.enemyPokemon != null) {
            drawStatusInfo(battleM.enemyPokemon, false);
        }

        // 绘制对话框
        if (dialogueIndex < dialogueLines.length) {
            drawDialogueScreen(0, 18*battleTileM.tileSize, 32*battleTileM.tileSize, 6*battleTileM.tileSize, dialogueLines[dialogueIndex]);
        }

        // 绘制立绘 
        // if(animationCounter < animationFrameDuration){
        //     animationCounter++;
        // }else{
        //     imageHeight += 1;
        //     animationCounter = 0;
        // }

        // Image backImage = (battleM.playerPokemon != null) ? battleM.playerPokemon.back : null;
        // Image frontImage = (battleM.enemyPokemon != null) ? battleM.enemyPokemon.front : null;

        // if (backImage != null && battleM.playerPokemon.curHp <= 0) {
        //     int bw = Math.max(0, backImage.getWidth(null));
        //     int bh = Math.max(0, backImage.getHeight(null));
        //     int crop = Math.min(imageHeight, Math.max(0, bh - 1));
        //     int sx1 = 0;
        //     int sy1 = crop;
        //     int sx2 = bw;
        //     int sy2 = bh;
        //     int dx1 = 32 * gp.scale;
        //     int dy1 = 85 * gp.scale + imageHeight;
        //     int destH = Math.max(0, bh - crop);
        //     int dx2 = dx1 + bw;
        //     int dy2 = dy1 + destH;
        //     if (bw > 0 && destH > 0) {
        //         g2.drawImage(backImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        //     }
        //     if (frontImage != null) g2.drawImage(frontImage, 168 * gp.scale, 8 * gp.scale, null);
        // } else if (frontImage != null) {
        //     int fw = Math.max(0, frontImage.getWidth(null));
        //     int fh = Math.max(0, frontImage.getHeight(null));
        //     int crop = Math.min(imageHeight, Math.max(0, fh - 1));
        //     int sx1 = 0;
        //     int sy1 = crop;
        //     int sx2 = fw;
        //     int sy2 = fh;
        //     int dx1 = 168 * gp.scale;
        //     int dy1 = 8 * gp.scale + imageHeight;
        //     int destH = Math.max(0, fh - crop);
        //     int dx2 = dx1 + fw;
        //     int dy2 = dy1 + destH;
        //     if (fw > 0 && destH > 0) {
        //         g2.drawImage(frontImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        //     }
        //     if (backImage != null) {
        //         if (backImage instanceof BufferedImage) {
        //             drawPokemonImage(32 * gp.scale, 85 * gp.scale, (BufferedImage) backImage);
        //         } else {
        //             g2.drawImage(backImage, 32 * gp.scale, 85 * gp.scale, null);
        //         }
        //     }
        // }

        if (battleM.playerPokemon.curHp<=0) {
            drawPokemonImage(168 * gp.scale, 8 * gp.scale, battleM.enemyPokemon.front);
        }else{
            drawPokemonImage(32 * gp.scale, 85 * gp.scale, battleM.playerPokemon.back);
        }
    }
}
