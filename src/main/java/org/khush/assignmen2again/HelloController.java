package org.khush.assignmen2again;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private Pane car1, car2, car3, car4;

    @FXML
    private Button startButton, pauseButton, resetButton;

    @FXML
    private Slider speedSlider;

    @FXML
    private Rectangle startLine, finishLine;

    @FXML
    private Label resultLabel, raceStatusLabel;

    @FXML
    private Label place1Label, place2Label, place3Label, place4Label;

    private final CarRaceModel model = new CarRaceModel();

    private TranslateTransition[] movements = new TranslateTransition[4];

    @FXML
    private void initialize() {
        raceStatusLabel.setText("Ready to race!");
    }

    @FXML
    private void startRace() {
        if (movements[0] != null) {
            return;
        }

        Pane[] cars = {car1, car2, car3, car4};
        double[] times = {5, 6, 4, 7};

        for (int i = 0; i < cars.length; i++) {
            TranslateTransition move = new TranslateTransition();

            move.setNode(cars[i]);
            move.setDuration(Duration.seconds(times[i]));
            move.setToX(950);

            movements[i] = move;
            move.play();
        }

        raceStatusLabel.setText("Race started!");
    }
}