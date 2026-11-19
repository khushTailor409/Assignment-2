package org.khush.assignmen2again;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage raceStage = fxmlLoader.load();
        raceStage.setTitle("Car Race");
        raceStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}