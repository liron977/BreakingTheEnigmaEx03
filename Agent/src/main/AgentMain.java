package main;

import component.login.AgentLoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AgentMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/component/login/Login.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AgentLoginController agentLoginController = fxmlLoader.getController();
        java.util.Map<String, String> env = System.getenv();
        String alliesTeamName = "";
        if (env.values() != null) {
            for (Map.Entry<String, String> map : env.entrySet()) {
           if(map.getKey().equals("alliesTeamName")) {
                alliesTeamName = map.getValue();
               agentLoginController.setAlliesName(alliesTeamName);
              }
                System.out.println(alliesTeamName + "Agent app");
            }
        }
      if(alliesTeamName.isEmpty()) {
            agentLoginController.startListRefresher();
        }
        agentLoginController.setPrimaryStageAndLoadInTheBackgroundSuperScreen(primaryStage);
        primaryStage.setTitle("Agent");
        primaryStage.getIcons().add(new Image("/Resources/agent.jpg"));
        Scene scene = new Scene(root);
        primaryStage.setMinHeight(300f);
        primaryStage.setMinWidth(400f);
        scene.getStylesheets().add(getClass().getResource("/utils/CSS/BlueStyle.css").toExternalForm());
        scene.getStylesheets().add("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}