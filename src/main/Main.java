package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        //创建游戏窗口
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("MyPokemon-ZUST-金嘉炜-1240696034");
        
        //创建游戏面板
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gp.setupGame();
        gp.startGameThread();
    }
}
