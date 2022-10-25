package main;

import component.login.UBoatLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class UBoatMain extends Application {
        @Override public void start(Stage primaryStage) throws Exception {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/component/login/Login.fxml");
            //URL url = getClass().getResource("/component/mainWindowUBoat/MainWindowUBoat.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());
            UBoatLoginController uBoatLoginController = fxmlLoader.getController();
            //uBoatLoginController.setMediator(mediator);
            uBoatLoginController.setPrimaryStageAndLoadInTheBackgroundSuperScreen(primaryStage);
            primaryStage.setTitle("UBoat");
            primaryStage.getIcons().add(new Image("/Resources/uboatIcon.jpg"));
            Scene scene = new Scene(root);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            scene.getStylesheets().add(getClass().getResource("/utils/CSS/BlueStyle.css").toExternalForm());
            scene.getStylesheets().add("");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }