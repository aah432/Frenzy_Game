//Amber Harding
package model;
import java.util.Random;

public class Enemy extends Cell implements Runnable{

    private char name;
    private String direction;
    private Thread thread = new Thread();
    private Board board;
    private boolean alive;
    private double speed;

    public Enemy(Board board, char name) {
        this.board = board;
        alive = true;
        Random rand = new Random();
        //get random character to represent enemy name.
        this.name = name;
        //If the random char generated happens to be a P get a new random char
        while (name == 'P') {
            this.name = (char) (rand.nextInt(126 - 33) + 33);
        }

        switch (rand.nextInt(4)) {
            case 0:
                //UpLeft
                direction = "UpLeft";
                break;
            case 1:
                //UpLeft
                direction = "UpRight";
                break;
            case 2:
                //UpLeft
                direction = "DownLeft";
                break;
            case 3:
                //UpLeft
                direction = "DownRight";
                break;
        }

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

        while(alive) {
            try {
                Thread.sleep((int)Math.round(speed));
                if(!alive)
                    break;
                switch(direction){
                    case "UpLeft":
                        board.move(this, 0);
                        Thread.sleep((int)Math.round(speed));
                        if(!alive)
                            break;
                        board.move(this, 2);
                        break;
                    case "UpRight":
                        board.move(this, 0);
                        Thread.sleep((int)Math.round(speed));
                        if(!alive)
                            break;
                        board.move(this, 3);
                        break;
                    case "DownLeft":
                        board.move(this, 1);
                        Thread.sleep((int)Math.round(speed));
                        if(!alive)
                            break;
                        board.move(this, 2);
                        break;
                    case "DownRight":
                        board.move(this, 1);
                        Thread.sleep((int)Math.round(speed));
                        if(!alive)
                            break;
                        board.move(this, 3);
                        break;
                }
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

    public String getDirection() {return direction;}

    public String getType() {
        return "enemy";
    }

    @Override
    public String getColor(){return "green";}

}
