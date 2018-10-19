//Amber Harding gameController.java
package controller;

import model.*;

public class gameController {

    private Board board;
    private int move;
    private Player player = new Player();
    private Boolean playerAlive = false;

    public gameController( int size) { board = new Board(size); }

    public boolean isPlayerAlive(){return playerAlive;}

    public Board getBoard(){return board;}

    public int[] getPlayerPosition(){ return player.getPosition(); }

    public void createPlayer(int x, int y)
    {
        board.addPiece(player, x, y);
        playerAlive = true;
        player.resetScore();
    }

    public void createEnemy(int x, int y, char name){
        Enemy enemy= new Enemy(board, name);
        board.addPiece(enemy, x, y);
    }
    public void createblueEnemy(int x, int y, char name){
        blueEnemy blue= new blueEnemy(board, name);
        board.addPiece(blue, x, y);
    }
    public void createpurpleEnemy(int x, int y, char name){
        purpleEnemy purple= new purpleEnemy(board, name);
        board.addPiece(purple, x, y);
    }

    public void createRedEnemy(int x, int y, char name){
        redEnemy red= new redEnemy(board, name);
        board.addPiece(red, x, y);
    }

    public void killPlayer(){
        playerAlive = false;
    }

    public void changeEnemySpeed(int changeValue){
        board.changeSpeed(changeValue);
    }

    public void movePlayer(String moveString) {
        if(moveString.equals("UP")){
            move = 0;
        }
        if(moveString.equals("DOWN")){
            move = 1;
        }
        if(moveString.equals("LEFT")){
            move = 2;
        }
        if(moveString.equals("RIGHT")){
            move = 3;
        }
        board.move(player,move);
    }
}
