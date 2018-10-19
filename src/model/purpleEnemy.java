//Amber Harding
package model;
import java.util.Random;

public class purpleEnemy extends Cell implements Runnable{

    private char name;
    private Thread thread = new Thread();
    private Board board;
    private boolean alive;
    private double speed;
    private String type;
    Random rand = new Random();

    public purpleEnemy(Board board, char name) {
        this.board = board;
        alive = true;

        //get random character to represent enemy name.
        this.name = name;
        //If the random char generated happens to be a P get a new random char
        while (name == 'P') {
            this.name = (char) (rand.nextInt(126 - 33) + 33);
        }
        type = "blue";
    }

    public char getName(){
        return name;
    }


    public void kill(){
        alive = false;
    }


    public Thread getThread(){return thread;}

    public void run(){
        while (alive) {
            try {

                Thread.sleep((int) Math.round(speed));
                if (!alive) {
                    break;
                }
                int move = rand.nextInt(4);
                board.move(this, move);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setSpeed(double speed){
        this.speed = speed;
    }

    public void stop() throws Exception {
        thread.stop();
        System.out.println( "stop called. Do termination cleanup." );
    }

    public String getType() {
        return "enemy";
    }

    public String getColor(){return "purple";}

}