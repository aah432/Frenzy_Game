//Amber Harding
package model;

public class Player extends Cell{

    private int score;
    private char name;

    public Player(){
        this.name = 'P';
        //Score is initially set to 0
        this.score = 0;
    }

    public String getType() { return "player"; }

    @Override
    public int getScore(){return score;}

    public void resetScore(){score = 0;}

    public char getName(){ return name; }

    public void incrementScore(){ score++; }
    @Override
    public String getColor(){return "yellow";}
}
