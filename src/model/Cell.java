//Amber Harding
package model;

public abstract class Cell {

    private char name;
    private int[] position = new int[2];
    private String type;
    private Thread thread;
    private String color;

    public char getName(){ return name; }

    public String getType(){return type;}

    public Thread getThread(){
        return thread;
    }

    public void kill(){ }

    public int getScore(){
        return 0;
    }

    public void incrementScore(){ }

    public int[] getPosition(){return position;}

    public void setPosition(int x,int y){
        position[0] = x;
        position[1] = y;
    }

    public String getColor(){ return color;}

    public void setSpeed(double speed){}

}
