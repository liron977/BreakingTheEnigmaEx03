package main;

import component.UBoatLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class UBoatMain extends Application {
        @Override public void start(Stage primaryStage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/component/Login.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());
            UBoatLoginController workerLoginController = fxmlLoader.getController();
            workerLoginController.setPrimaryStageAndLoadInTheBackgroundSuperScreen(primaryStage);
            primaryStage.setTitle("Worker");
            Scene scene = new Scene(root);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }