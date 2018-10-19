//Amber Harding CSPAX Frenzy project

package frenzyui;

import controller.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import java.util.*;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Board;

import java.util.Observable;
import java.util.Observer;
import java.util.List;

/**
 * AppSkeleton is an empty, 'main' class for javafx programs.
 * Code skips javadocs to force focus on the code.
 * Printed messages show the calling sequence at runtime.
 */
public class Frenzy extends Application implements Observer {

    private int size;                       //Size of board
    private gameController game;            //Game Controller Object to communicate between View and Model tiers
    private Label[][] labels;               //Double array of labels
    private GridPane board;                 //Gridpane which the board will be created on
    private Random rand = new Random();     //Random is used for generating random integers
    private Board boardUpdate;              //boardUpdate used as Observable object
    private Text score = new Text("0");     //Text to display score
    private VBox textFields = new VBox();   //Vbox used to hold game messages
    private Button plus = new Button("++");
    private Button minus = new Button("--");
    private Button restart = new Button("restart");
    private Button help = new Button("help");
    private Label playerLabel;

    public Frenzy() {
        super();
        System.out.println("0-argument constructor called.");
    }

    @Override
    public void init() throws Exception {

        super.init();
        System.out.println("init called.");
        System.out.println("init may process the command line.");

        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();

        size = Integer.parseInt(parameters.get(0));
        labels = new Label[size][size];

        //Check for empty parameters
        if (parameters.isEmpty()) {
            System.out.println("Failure: Must have size parameter");
            System.exit(-1);
        }
        //Check if size is valid
        if (size < 7) {
            System.out.println("Failure: Size of board cannot be less than 7");
            System.exit(-1);
        }
        //Create board
        board = makeGridPane();
        //Create gameController object
        game = new gameController(size);

        boardUpdate = game.getBoard();
        boardUpdate.addObserver(this);
    }

    @Override
    public void start(Stage primaryStage) {

        System.out.println("start called.");
        System.out.println("start may process the command line.");
        System.out.println("start builds and shows the GUI.");

        primaryStage.setTitle("Frenzy");

        BorderPane mainPane = new BorderPane();

        mainPane.setPrefHeight(500);
        mainPane.setPrefWidth(700);

        BorderPane pane = new BorderPane();
        pane.setCenter(this.makeControlPane());
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        //create player
        playerLabel = createPlayerLabel();
        board.add(playerLabel, labelPosition(playerLabel)[0], labelPosition(playerLabel)[1]);

        //add 3 initial enemies
        for (int i = 0; i < 3; i++) {
            Label enemyLabel = createEnemyLabel();
            board.add(enemyLabel, labelPosition(enemyLabel)[0], labelPosition(enemyLabel)[1]);
        }

        //Spawn new enemy every 6 seconds
        Timeline sixSeconds = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
            Label enemyLabel = createEnemyLabel();
            board.add(enemyLabel, labelPosition(enemyLabel)[0], labelPosition(enemyLabel)[1]);
        }));
        sixSeconds.setCycleCount(Timeline.INDEFINITE);
        sixSeconds.play();

        //Spawn new blue enemy every 10 seconds
        Timeline tenSeconds = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            Label blueEnemyLabel = createblueEnemyLabel();
            board.add(blueEnemyLabel, labelPosition(blueEnemyLabel)[0], labelPosition(blueEnemyLabel)[1]);
        }));
        tenSeconds.setCycleCount(Timeline.INDEFINITE);
        tenSeconds.play();

        //Spawn new purple enemy every 7 seconds
        Timeline sevenSeconds = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            Label purpleEnemyLabel = createpurpleEnemyLabel();
            board.add(purpleEnemyLabel, labelPosition(purpleEnemyLabel)[0], labelPosition(purpleEnemyLabel)[1]);
        }));
        sevenSeconds.setCycleCount(Timeline.INDEFINITE);
        sevenSeconds.play();

        //Spawn new red enemy every 12 seconds
        Timeline twelveSeconds = new Timeline(new KeyFrame(Duration.seconds(12), event -> {
            Label redEnemyLabel = createRedEnemyLabel();
            board.add(redEnemyLabel, labelPosition(redEnemyLabel)[0], labelPosition(redEnemyLabel)[1]);
        }));
        twelveSeconds.setCycleCount(Timeline.INDEFINITE);
        twelveSeconds.play();

        //Keyevent handler to move Player
        board.setOnKeyPressed(event -> {
            final int x = game.getPlayerPosition()[0];
            final int y = game.getPlayerPosition()[1];

            if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.UP)
                    || event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.RIGHT)) {
                if (game.isPlayerAlive()) {
                    game.movePlayer(event.getCode().toString());
                    event.consume();

                    board.getChildren();

                }
            }
        });

        plus.setOnAction(event -> {
            game.changeEnemySpeed(1);
        });
        minus.setOnAction(event -> {
            game.changeEnemySpeed(0);
        });
        restart.setOnAction(event -> {
            //recreate player
            if(!game.isPlayerAlive()) {

                score.setText("0");
                getMessage("Respawning player");
                playerLabel = createPlayerLabel();
                board.add(playerLabel, labelPosition(playerLabel)[0], labelPosition(playerLabel)[1]);
            }else{
                getMessage("Player is still alive \n cannot respawn.");
            }

        });

        mainPane.setRight(pane);
        mainPane.setCenter(board);

        Scene scene = new Scene(mainPane);

        primaryStage.setScene(scene);
        primaryStage.show();

        board.setOnMouseClicked(event -> {
            board.requestFocus();
        });
    }

    /**
     * updates the text object, Score when Score is incremented.
     * @param newScore
     * */
    public void updateScore(int newScore) {
        score.setText(Integer.toString(newScore));
    }

    /**
     * Creates Gridpane which the board will be displayed onto.
     * */
    private GridPane makeGridPane() {
        GridPane gridBoard = new GridPane();
        gridBoard.setPadding(new Insets(5, 5, 5, 5));

        for (int i = 0; i < size; i++) {
            RowConstraints con = new RowConstraints();
            ColumnConstraints columnConstraints = new ColumnConstraints();
            con.setPercentHeight(20);
            columnConstraints.setPercentWidth(20);

            gridBoard.getRowConstraints().add(con);
            gridBoard.getColumnConstraints().add(columnConstraints);
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Label label = new Label("");
                gridBoard.add(label, x, y);
                labels[x][y] = label;
            }
        }

        gridBoard.setGridLinesVisible(true);

        return gridBoard;
    }

    /*
    * creates rightPane which buttons and game info will be displayed onto.*/
    private Pane makeControlPane() {
        VBox right = new VBox();

        Text text1 = new Text("Score: ");
        TextFlow textFlow = new TextFlow(text1, score);

        TilePane pane = new TilePane();

        pane.setPadding(new Insets(10, 5, 5, 5));

        textFields.setPrefHeight(300);
        textFields.setPrefWidth(150);

        ScrollPane scrollPane = new ScrollPane();

        Text text = new Text("Game Running!");
        textFields.getChildren().add(text);

        scrollPane.setContent(textFields);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.vvalueProperty().bind(textFields.heightProperty());
        scrollPane.hvalueProperty().bind(textFields.widthProperty());

        right.getChildren().addAll(textFlow, this.makeButtonPane(), scrollPane);

        pane.setOrientation(Orientation.VERTICAL);
        pane.getChildren().add(right);

        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return pane;
    }

    /*
    * Creates buttonPane for buttons.*/
    private Pane makeButtonPane() {

        GridPane grid = new GridPane();

        // use "Percentage sizing" so each column gets even % of width.
        // This will make the grid fill the space.
        int rows = 2;
        int cols = 2;
        ColumnConstraints[] cc = new ColumnConstraints[cols];
        for (int i = 0; i < cols; ++i) {
            cc[i] = new ColumnConstraints();
            cc[i].setPercentWidth(100.0 / cols);
        }
        grid.getColumnConstraints().addAll(cc);

        RowConstraints[] rc = new RowConstraints[rows];
        for (int i = 0; i < rows; ++i) {
            rc[i] = new RowConstraints();
            rc[i].setPercentHeight(100.0 / rows);
        }
        grid.getRowConstraints().addAll(rc);

        plus.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(plus, 0, 0);

        minus.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(minus, 1, 0);

        restart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(restart, 0, 1);
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        help.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(help, 1, 1);

        return grid;
    }

    /*
    * Creates yellow Player Label to keep track of the players movement*/
    private Label createPlayerLabel() {
        //Create player label
        Label player = new Label();
        player.setText("P");
        javafx.scene.text.Font font = new Font("Arial", 30);
        player.setFont(font);
        javafx.scene.paint.Color color = Color.rgb(255, 254, 88);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
                Insets.EMPTY);
        Background background = new Background(fill);
        player.setBackground(background);
        player.setAlignment(Pos.CENTER);
        player.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);
        game.createPlayer(x, y);

        labels[x][y] = player;
        return player;
    }

    /*
    * Creates green Enemy Label to keep track of the enemy's movement*/
    private Label createEnemyLabel() {

        Label enemy = new Label();

        //get random character to represent enemy name.
        char name = (char) (rand.nextInt(126 - 33) + 33);
        //create enemy label
        enemy.setText(String.valueOf(name));
        Font font = new Font("Arial", 30);
        enemy.setFont(font);
        Color color = Color.rgb(10, 238, 0);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
                Insets.EMPTY);
        Background background = new Background(fill);
        enemy.setBackground(background);
        enemy.setAlignment(Pos.CENTER);
        enemy.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        while (!labels[x][y].getText().equals("")) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }

        final int xf = x;
        final int yf = y;

        game.createEnemy(xf, yf, name);

        labels[x][y] = enemy;

        return enemy;
    }

    /*
    * Creates blue Enemy Label to keep track of the enemy's movement*/
    private Label createblueEnemyLabel(){
        Label enemy = new Label();

        //get random character to represent enemy name.
        char name = (char) (rand.nextInt(126 - 33) + 33);
        //create enemy label
        enemy.setText(String.valueOf(name));
        Font font = new Font("Arial", 30);
        enemy.setFont(font);
        Color color = Color.rgb(37, 75, 238);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
                Insets.EMPTY);
        Background background = new Background(fill);
        enemy.setBackground(background);
        enemy.setAlignment(Pos.CENTER);
        enemy.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        while (!labels[x][y].getText().equals("")) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }

        game.createblueEnemy(x, y, name);

        labels[x][y] = enemy;

        return enemy;
    }

    /*
    * Creates purple Enemy Label to keep track of the enemy's movement*/
    public Label createpurpleEnemyLabel(){
        Label enemy = new Label();

        //get random character to represent enemy name.
        char name = (char) (rand.nextInt(126 - 33) + 33);
        //create enemy label
        enemy.setText(String.valueOf(name));
        Font font = new Font("Arial", 30);
        enemy.setFont(font);
        Color color = Color.rgb(222, 51, 238);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
                Insets.EMPTY);
        Background background = new Background(fill);
        enemy.setBackground(background);
        enemy.setAlignment(Pos.CENTER);
        enemy.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        while (!labels[x][y].getText().equals("")) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }

        game.createpurpleEnemy(x, y, name);

        labels[x][y] = enemy;

        return enemy;
    }

    /*
    * Creates red Enemy Label to keep track of the enemy's movement*/
    public Label createRedEnemyLabel(){
        Label enemy = new Label();

        //get random character to represent enemy name.
        char name = (char) (rand.nextInt(126 - 33) + 33);
        //create enemy label
        enemy.setText(String.valueOf(name));
        Font font = new Font("Arial", 30);
        enemy.setFont(font);
        Color color = Color.rgb(238, 0, 17);
        BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY,
                Insets.EMPTY);
        Background background = new Background(fill);
        enemy.setBackground(background);
        enemy.setAlignment(Pos.CENTER);
        enemy.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        while (!labels[x][y].getText().equals("")) {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }

        game.createRedEnemy(x, y, name);

        labels[x][y] = enemy;

        return enemy;
    }

    /*
    * Finds the position of a label
    * @param label, the label that will be searched for
    * @returns the position in an int array*/
    private int[] labelPosition(Label label) {
        int[] position = new int[2];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (labels[x][y] == label) {
                    position[0] = x;
                    position[1] = y;
                }
            }
        }
        return position;
    }

    /*
    * Updates position of Label when its moved
    * @param oldx, an integer representing the old x position
    * @param oldy, an integer representing the old y position
    * @param newx, an integer representing the new x position
    * @param oldy, an integer representing the new y position
    * */
    private void updateLabel(int oldx, int oldy, int newx, int newy) {

        if (labels[newx][newy] == null || labels[oldx][oldy] == null) {
            System.out.println("Null");
            System.out.println(newx + " " + newy);
        } else {
            labels[newx][newy].setText(labels[oldx][oldy].getText());
            labels[newx][newy].setFont(labels[oldx][oldy].getFont());
            labels[newx][newy].setBackground(labels[oldx][oldy].getBackground());
            labels[newx][newy].setAlignment(labels[oldx][oldy].getAlignment());
            labels[newx][newy].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            labels[oldx][oldy].setText("");
            labels[oldx][oldy].setBackground(null);

            board.getChildren();

        }
    }

    /* adds message to the text field where the gameMessages will be displayed
    * @params message, a String to represent game message
    * */
    private void getMessage(String message) {
        Text newMessage = new Text(message);
        //Platform.runLater(()->{
        textFields.getChildren().add(newMessage);
        //});
    }

    /*
    * Sets label to empty text and background color to null*/
    private void killPlayer() {
        labels[labelPosition(playerLabel)[0]][labelPosition(playerLabel)[1]].setText("");
        labels[labelPosition(playerLabel)[0]][labelPosition(playerLabel)[1]].setBackground(null);
        board.getChildren();
        playerLabel = null;

    }

    /*
    * updates the UI based on changes to the model tier.*/
    public void update(Observable observable, Object arg) {

        boardUpdate = (Board) observable;

        Platform.runLater(() -> {
            if (arg instanceof int[]) {
                //If label needs to be updated
                int[] array = (int[]) arg;
                //Platform.runLater(() -> {
                if (array.length == 4) {
                    updateLabel(array[0], array[1], array[2], array[3]);
                }
                //});
            } else if (arg instanceof String[]) {
                //If a new game message needs to be displayed
                String[] strings = (String[]) arg;
                getMessage(strings[0]);
                if (strings[1] != null) {
                    getMessage(strings[1]);
                    killPlayer();
                    game.killPlayer();
                }
            } else if(arg instanceof String){
                //If a new game message needs to be displayed
                String string = (String) arg;
                getMessage(string);
            } else {
                //If score needs to be updated

                int score = (int) arg;
                updateScore(score);
            }
        });

    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("stop called. Do termination cleanup.");
        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println("main launching application...");
        Application.launch(args);
    }


}
