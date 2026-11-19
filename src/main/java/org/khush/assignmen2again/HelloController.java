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
    private final TranslateTransition[] movements = new TranslateTransition[4];

    @FXML
    private void initialize() {
        raceStatusLabel.setText("Ready to race!");
        speedSlider.setMin(0.5);
        speedSlider.setMax(3.0);
        speedSlider.setValue(1.0);
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            for (TranslateTransition move : movements) {
                if (move != null) {
                    move.setRate(newValue.doubleValue());
                }
            }
        });
        startButton.setOnAction(event -> startRace());
        pauseButton.setOnAction(event -> pauseRace());
        resetButton.setOnAction(event -> resetRace());
    }

    @FXML
    private void startRace() {
        if (movements[0] != null) {
            for (TranslateTransition move : movements) {
                if (move != null) {
                    move.play();
                }
            }
            raceStatusLabel.setText("Race resumed!");
            return;
        }

        Pane[] cars = {car1, car2, car3, car4};
        double[] times = {5, 6, 4, 7};

        for (int i = 0; i < cars.length; i++) {
            TranslateTransition move = new TranslateTransition();
            move.setNode(cars[i]);
            move.setDuration(Duration.seconds(times[i]));
            move.setToX(950);
            move.setRate(speedSlider.getValue());
            movements[i] = move;
            move.play();
        }

        raceStatusLabel.setText("Race started!");
    }

    @FXML
    private void pauseRace() {
        for (TranslateTransition move : movements) {
            if (move != null) {
                move.pause();
            }
        }
        raceStatusLabel.setText("Race paused!");
    }

    @FXML
    private void resetRace() {
        for (int i = 0; i < movements.length; i++) {
            if (movements[i] != null) {
                movements[i].stop();
                movements[i] = null;
            }
        }

        car1.setTranslateX(0);
        car2.setTranslateX(0);
        car3.setTranslateX(0);
        car4.setTranslateX(0);

        model.reset();
        raceStatusLabel.setText("Ready to race!");
    }
}