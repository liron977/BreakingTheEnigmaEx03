package component.mainWindowAllies;

import component.AlliesDashboard.AlliesDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class MainWindowAlliesController {

    @FXML
    ScrollPane alliesDashboard;

    @FXML
    AlliesDashboardController alliesDashboardController;

    String alliesTeamName;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        if (alliesDashboardController != null) {
            alliesDashboardController.setMainWindowAlliesController(this);
        }
    }
    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
    }
    public void updateAgentsTableView(){
        alliesDashboardController.startListRefresher();
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
        alliesDashboardController.setAlliesTeamName(alliesTeamName);
    }
}