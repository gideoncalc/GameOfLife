/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflifefxhw1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author glevy
 */
public class GameOfLifeFXHW1 extends Application {

    final static int SQUARE_WIDTH = 30;
    final static int SQUARE_HEIGHT = 30;
    final static int MAX_ROWS = 40;
    final static int MAX_COLS = 40;

    boolean[][] lifeField = new boolean[MAX_ROWS][MAX_COLS];

    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        seedLifeArray(lifeField);

        Group group = new Group();

        loadGroupWithLifeArray(group, lifeField);

        Scene scene = new Scene(group, MAX_COLS * SQUARE_WIDTH, MAX_ROWS * SQUARE_HEIGHT);
        scene.setFill(Color.WHITESMOKE);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000), (ActionEvent event) -> {
            this.lifeField = mutateLifeArray(this.lifeField);
            loadGroupWithLifeArray(group, this.lifeField);

            if (!isThereAnyLifeInThisArray(this.lifeField)) {
                this.timeline.stop();
            }
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void seedLifeArray(boolean[][] lifeArray) {

        for (boolean[] lifeArrayRow : lifeArray) {
            for (int col = 0; col < lifeArrayRow.length; col++) {
                lifeArrayRow[col] = (Math.random() > 0.7);
            }
        }

    }

    public void loadGroupWithLifeArray(Group group, boolean[][] lifeArray) {

        group.getChildren().removeAll(group.getChildren());
        for (int row = 0; row < lifeArray.length; row++) {
            for (int col = 0; col < lifeArray[row].length; col++) {
                int x = col * SQUARE_WIDTH;
                int y = row * SQUARE_HEIGHT;
                Rectangle square = new Rectangle(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
                square.setStroke(Color.BLUE);
                square.setFill(lifeArray[row][col] ? Color.BLUE : Color.WHITE);
                group.getChildren().add(square);
            }

        }
    }

    public boolean[][] mutateLifeArray(boolean[][] lifeArray) {
        if (lifeArray == null || lifeArray.length == 0 || lifeArray[0].length == 0) {
            throw new IllegalArgumentException("Array must have at least one row and one column to mutate.");
        }

        boolean[][] mutatedArray = new boolean[lifeArray.length][lifeArray[0].length];

        for (int row = 0; row < lifeArray.length; row++) {
            for (int col = 0; col < lifeArray[row].length; col++) {
                boolean isAliveW = (col > 0 ? lifeArray[row][col - 1] : false);
                boolean isAliveNW = (col > 0 && row > 0 ? lifeArray[row - 1][col - 1] : false);
                boolean isAliveN = (row > 0 ? lifeArray[row - 1][col] : false);
                boolean isAliveNE = (row > 0 && col < lifeArray[row].length - 1 ? lifeArray[row - 1][col + 1] : false);
                boolean isAliveE = (col < lifeArray[row].length - 1 ? lifeArray[row][col + 1] : false);
                boolean isAliveSE = (row < lifeArray.length - 1 && col < lifeArray[row].length - 1 ? lifeArray[row + 1][col + 1] : false);
                boolean isAliveS = (row < lifeArray.length - 1 ? lifeArray[row + 1][col] : false);
                boolean isAliveSW = (row < lifeArray.length - 1 && col > 0 ? lifeArray[row + 1][col - 1] : false);

                int neighbors = numberOfNeighbors(isAliveW, isAliveNW, isAliveN, isAliveNE, isAliveE, isAliveSE, isAliveS, isAliveSW);

                boolean hasLife = lifeArray[row][col];

                if (hasLife) {
                    boolean stillAlive = (neighbors >= 2 && neighbors <= 3);
                    mutatedArray[row][col] = stillAlive;
                } else {
                    boolean stillAlive = (neighbors == 3);
                    mutatedArray[row][col] = stillAlive;

                }

            }
        }

        return mutatedArray;
    }

    public boolean isThereAnyLifeInThisArray(boolean[][] lifeArray) {
        boolean isThereAnyLife = false;
        for (int row = 0; row < lifeArray.length && !isThereAnyLife; row++) {
            for (int col = 0; col < lifeArray[row].length && !isThereAnyLife; col++) {
                isThereAnyLife = lifeArray[row][col];
            }
        }

        return isThereAnyLife;
    }

    private int numberOfNeighbors(boolean w, boolean nw, boolean n, boolean ne, boolean e, boolean se, boolean s, boolean sw) {
        boolean[] neighbors = {w, nw, n, ne, e, se, s, sw};
        int numNeighbors = 0;
        for (int i = 0; i < neighbors.length; i++) {

            if (neighbors[i]) {
                numNeighbors += 1;
            }
        }

        return numNeighbors;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
