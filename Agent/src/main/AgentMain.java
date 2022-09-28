package main;

import component.login.AgentLoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AgentMain extends Application {
    @Override public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/component/login/Login.fxml");
        //URL url = getClass().getResource("/component/mainWindowUBoat/MainWindowUBoat.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AgentLoginController agentLoginController = fxmlLoader.getController();
        //uBoatLoginController.setMediator(mediator);
        agentLoginController.startListRefresher();
        agentLoginController.setPrimaryStageAndLoadInTheBackgroundSuperScreen(primaryStage);

        primaryStage.setTitle("Agent");
        Scene scene = new Scene(root);
        primaryStage.setMinHeight(300f);
        primaryStage.setMinWidth(400f);
        scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
        scene.getStylesheets().add("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}