package org.khush.assignmen2again;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class HelloController {

    @FXML
    private Pane car1, car2, car3, car4;

    @FXML
    private Button startButton, pauseButton, resetButton;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label speedValueLabel;

    @FXML
    private Rectangle startLine, finishLine;

    @FXML
    private Label resultLabel, raceStatusLabel;

    @FXML
    private Label place1Label, place2Label, place3Label, place4Label;

    private final CarRaceModel model = new CarRaceModel();
    private final TranslateTransition[] movements = new TranslateTransition[4];
    private int finishedCount = 0;
    private boolean slideshowPlaying = true;
    private AudioClip slideshowSound;

    @FXML
    private void initialize() {
        raceStatusLabel.setText("Ready to race!");
        startButton.setDisable(true);
        speedSlider.setMin(0.5);
        speedSlider.setMax(3.0);
        speedSlider.setValue(1.0);
        speedValueLabel.setText(String.format("%.1fx", speedSlider.getValue()));
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            speedValueLabel.setText(String.format("%.1fx", newValue.doubleValue()));
            for (TranslateTransition move : movements) {
                if (move != null) {
                    move.setRate(newValue.doubleValue());
                }
            }
        });
        startButton.setOnAction(event -> startRace());
        pauseButton.setOnAction(event -> pauseRace());
        resetButton.setOnAction(event -> resetRace());

        Platform.runLater(this::showSlideshow);
    }

    private void showSlideshow() {
        AnchorPane root = (AnchorPane) car1.getParent();
        Pane[] cars = {car1, car2, car3, car4};

        Pane slideshow = new Pane();
        slideshow.setPrefSize(1261, 630);
        slideshow.setStyle("-fx-background-color: white;");

        Label greeting = new Label("Welcome to the Car Race!");
        greeting.setFont(Font.font(30));
        greeting.setAlignment(Pos.CENTER);
        greeting.setPrefWidth(1261);
        greeting.setLayoutY(110);

        Label carName = new Label();
        carName.setFont(Font.font(22));
        carName.setAlignment(Pos.CENTER);
        carName.setPrefWidth(1261);
        carName.setLayoutY(435);

        ImageView preview = new ImageView();
        preview.setFitWidth(387);
        preview.setFitHeight(210);
        preview.setPreserveRatio(true);
        preview.setLayoutX(437);
        preview.setLayoutY(200);

        slideshow.getChildren().addAll(greeting, preview, carName);
        root.getChildren().add(slideshow);

        URL soundFile = getClass().getResource("mfcc-intro-intro-music-632943.mp3");
        if (soundFile != null) {
            slideshowSound = new AudioClip(soundFile.toExternalForm());
            slideshowSound.play();
        }

        SnapshotParameters snapshotSettings = new SnapshotParameters();
        snapshotSettings.setFill(Color.TRANSPARENT);

        Timeline slides = new Timeline();
        for (int i = 0; i < cars.length; i++) {
            final int index = i;
            slides.getKeyFrames().add(new KeyFrame(Duration.seconds(i * 1.2), event -> {
                // The previews are generated directly from the four existing JavaFX cars.
                preview.setImage(cars[index].snapshot(snapshotSettings, null));
                carName.setText("Car " + model.getNumber(index) + " - " + model.getColor(index));
            }));
        }
        slides.getKeyFrames().add(new KeyFrame(Duration.seconds(4.8), event -> {
            if (slideshowSound != null) {
                slideshowSound.stop();
            }
            root.getChildren().remove(slideshow);
            slideshowPlaying = false;
            startButton.setDisable(false);
            raceStatusLabel.setText("Ready to race!");
        }));
        slides.play();
    }

    @FXML
    private void startRace() {
        if (slideshowPlaying || finishedCount == model.getCarCount()) {
            return;
        }

        if (movements[0] != null) {
            if (movements[0].getStatus() == javafx.animation.Animation.Status.RUNNING) {
                return;
            }
            for (TranslateTransition move : movements) {
                if (move != null) {
                    move.play();
                }
            }
            raceStatusLabel.setText("Race resumed!");
            return;
        }

        Pane[] cars = {car1, car2, car3, car4};
        double[] times = {4, 5, 6, 7};
        for (int i = times.length - 1; i > 0; i--) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            double temp = times[i];
            times[i] = times[j];
            times[j] = temp;
        }

        for (int i = 0; i < cars.length; i++) {
            TranslateTransition move = new TranslateTransition();
            move.setNode(cars[i]);
            move.setDuration(Duration.seconds(times[i]));
            double distance = finishLine.getLayoutX() - (cars[i].getLayoutX() + cars[i].getPrefWidth());
            move.setToX(distance);
            move.setRate(speedSlider.getValue());
            final int carIndex = i;
            move.setOnFinished(event -> carFinished(carIndex));
            movements[i] = move;
            move.play();
        }

        raceStatusLabel.setText("Race started!");
    }

    private void carFinished(int index) {
        Pane[] cars = {car1, car2, car3, car4};
        Label[] places = {place1Label, place2Label, place3Label, place4Label};

        model.moveCar(index, cars[index].getTranslateX());
        places[finishedCount].setText("Car " + model.getNumber(index) + " (" + model.getColor(index) + ")");
        finishedCount++;

        if (finishedCount == 1) {
            resultLabel.setText("Winner: Car " + model.getNumber(index) + " (" + model.getColor(index) + ")");
            raceStatusLabel.setText("Congratulations! Car " + model.getNumber(index) +
                    " (" + model.getColor(index) + ") wins!");
        } else if (finishedCount == model.getCarCount()) {
            raceStatusLabel.setText("Race finished! All cars crossed the line.");
        }
    }

    @FXML
    private void pauseRace() {
        if (slideshowPlaying || movements[0] == null) {
            return;
        }
        for (TranslateTransition move : movements) {
            if (move != null) {
                move.pause();
            }
        }
        raceStatusLabel.setText("Race paused!");
    }

    @FXML
    private void resetRace() {
        if (slideshowPlaying) {
            return;
        }
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
        finishedCount = 0;
        place1Label.setText("Waiting...");
        place2Label.setText("Waiting...");
        place3Label.setText("Waiting...");
        place4Label.setText("Waiting...");
        resultLabel.setText("Race Results");
        raceStatusLabel.setText("Ready to race!");
    }
}