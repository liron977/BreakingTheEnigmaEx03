package component.mainWindowAgent;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AgentMainWindowController {
    private Stage primaryStage;

    @FXML
    public void initialize() {
       /* if (alliesDashboardController != null) {
            alliesDashboardController.setMainWindowAlliesController(this);
        }*/
    }
    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
    }
}