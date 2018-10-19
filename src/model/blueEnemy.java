//Amber Harding
package model;
import java.util.Random;

public class blueEnemy extends Cell implements Runnable{

    private char name;
    private Thread thread = new Thread();
    private Board board;
    private boolean alive;
    private double speed;
    private String type;

    public blueEnemy(Board board, char name) {
        this.board = board;
        alive = true;
        Random rand = new Random();
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

    @Override
    public void kill(){
        alive = false;
    }

    @Override
    public Thread getThread(){return thread;}

    public void run(){
        int level = 0;
        while(alive) {
            try {

                for(int i = 0; i <= level; i++) {
                    Thread.sleep((int)Math.round(speed));
                    if(!alive) {
                        break;
                    }
                    board.move(this, 3);
                }
                level++;

                for(int i = 0; i <= level; i++) {
                    Thread.sleep((int)Math.round(speed));
                    if(!alive) {
                        break;
                    }
                    board.move(this, 1);
                }
                level++;

                for(int i = 0; i <= level; i++) {
                    Thread.sleep((int)Math.round(speed));
                    if(!alive) {
                        break;
                    }
                    board.move(this, 2);
                }
                level++;

                for(int i = 0; i <= level; i++) {
                    Thread.sleep((int)Math.round(speed));
                    if(!alive) {
                        break;
                    }
                    board.move(this, 0);
                }
                level++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
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
    @Override
    public String getColor(){return "blue";}

}