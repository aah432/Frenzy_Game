//Amber Harding Board.java
package model;

import java.util.Observable;

import java.util.Random;

public class Board extends Observable {

    private int size;
    private Cell cells[][];
    private Cell player;
    private Random rand = new Random();
    private double speed = 1000;

    public Board(int size){
        super();
        this.size = size;
        this.cells = new Cell[size][size];

    }

    public void killPlayer(){
        cells[player.getPosition()[0]][player.getPosition()[1]] = null;
        player = null;
    }

    public void addPiece(Cell cell, int x, int y){

        cell.setPosition(x,y);
        if(cell.getColor().equals("blue")){
            blueEnemy blue;
            blue = (blueEnemy) cell;
            blue.setSpeed(speed);
            blue.setPosition(x,y);
            Thread thread = new Thread(blue);
            thread.start();
            cells[x][y] = blue;
        } else if(cell.getColor().equals("red")){
            redEnemy red;
            red = (redEnemy) cell;
            red.setSpeed(speed);
            red.setPosition(x,y);
            Thread thread = new Thread(red);
            thread.start();
            cells[x][y] = red;
        } else if(cell.getColor().equals("purple")){
            purpleEnemy purple;
            purple = (purpleEnemy) cell;
            purple.setSpeed(speed);
            purple.setPosition(x,y);
            Thread thread = new Thread(purple);
            thread.start();
            cells[x][y] = purple;
        } else if(cell.getType().equals("enemy")){
            Enemy enemy;
            enemy = (Enemy) cell;
            enemy.setSpeed(speed);
            enemy.setPosition(x,y);
            Thread thread = new Thread(enemy);
            thread.start();
            cells[x][y] = enemy;
        } else{
            player = cell;
            player.setPosition(x,y);
            cells[x][y] = player;
        }
    }

    private boolean Occupied(int x, int y){
        return cells[x][y] != null;
    }

    synchronized public void move(Cell cell, int move){
        if(cell != null) {

            int oldcol = cell.getPosition()[0];
            int oldrow = cell.getPosition()[1];

            int col = 0;
            int row = 0;

            switch (move) {
                case 0:
                    //up

                    //when moving up col doesn't change
                    col = oldcol;

                    //if the player is on top of board and tries to move up
                    // it should wrap around to other side
                    if (oldrow != 0) {
                        row = oldrow - 1;
                    } else {
                        row = size - 1;
                    }

                    break;

                case 1:
                    //down
                    //when moving down col doesn't change
                    col = oldcol;

                    //if the player is on bottom of board and tries to move down
                    // it should wrap around to other side
                    if (oldrow != size - 1) {
                        row = oldrow + 1;
                    } else {
                        row = 0;
                    }
                    break;

                case 2:
                    //left

                    //when moving left row doesn't change
                    row = oldrow;

                    col = oldcol - 1;

                    //if the player is on left side of board and tries to move left
                    // it should wrap around to other side
                    if (oldcol != 0) {
                        col = oldcol - 1;
                    } else {
                        col = size - 1;
                    }

                    break;

                case 3:
                    //right
                    //when moving left row doesn't change
                    row = oldrow;

                    col = oldcol + 1;

                    //if the player is on right side of board and tries to move left
                    // it should wrap around to other side
                    if (oldcol != size - 1) {
                        col = oldcol + 1;
                    } else {
                        col = 0;
                    }

                    break;
                case 5:
                    Random rand = new Random();
                    row = rand.nextInt(size);
                    col = rand.nextInt(size);
            }

            //If the space is occupied
            if (Occupied(col, row)) {
                eat(cells[col][row], cells[oldcol][oldrow]);
            }

            cell.setPosition(col, row);
            cells[col][row] = cell;
            cells[oldcol][oldrow] = null;

            int[] xy = new int[]{oldcol, oldrow, col, row};

            super.setChanged();
            super.notifyObservers(xy);

        }
    }

    private synchronized void eat(Cell cellA, Cell cellB) {
        //cellA is the one that gets eaten
        if(cellB != null && cellA != null) {
            String[] messages = new String[2];
            if (cellB.getType().equals("player")) {
                player.incrementScore();
                setChanged();
                notifyObservers(player.getScore());
            } else if (cellA.getType().equals("player")) {

                    killPlayer();

                messages[1] = "Game Over!";
            }

            messages[0] = Character.toString(cellB.getName()) + " ate "
                    + Character.toString(cellA.getName());

            cells[cellA.getPosition()[0]][cellA.getPosition()[1]] = null;
            if(cellA.getType().equals("enemy")) {
                cellA.kill();
            }

            setChanged();
            notifyObservers(messages);
        }
    }

    public void changeSpeed(int speedChange){
        if(speedChange == 0){
            //decrease speed
            speed *= 2;
        }else{
            //increase speed
            if(speed-100 < 100){
                setChanged();
                notifyObservers("Speed at maximum\n value can not\n increase speed \npast max");
            }else{
                speed -= 100;
            }
        }
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(cells[x][y] != null && cells[x][y].getType().equals("enemy")){
                    cells[x][y].setSpeed(speed);
                }
            }
        }
    }

}
