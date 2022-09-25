package main;

import component.login.UBoatLoginController;
import engineManager.EngineManager;
import engineManager.EngineManagerInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uiMediator.Mediator;

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
            Scene scene = new Scene(root);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }