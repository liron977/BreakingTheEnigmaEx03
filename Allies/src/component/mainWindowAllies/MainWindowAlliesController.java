package component.mainWindowAllies;

import component.AlliesDashboard.AlliesDashboardController;
import component.alliesContest.AlliesContestController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainWindowAlliesController {

    @FXML
    ScrollPane alliesDashboard;

    @FXML
    ScrollPane alliesContest;


    @FXML
    AlliesDashboardController alliesDashboardController;

    @FXML
    AlliesContestController alliesContestController;

    String alliesTeamName;
    private Stage primaryStage;
    @FXML
    private TabPane alliesTabPane;
    @FXML
    private Tab dashboardTabButton;
     @FXML
    private Tab contestTabButton;


    @FXML
    public void initialize() {
        if (alliesDashboardController != null) {
            alliesDashboardController.setMainWindowAlliesController(this);
            alliesContestController.setMainWindowAlliesController(this);
            contestTabButton.setDisable(true);

        }
    }
    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
    }
    public void updateAgentsTableView(){
        alliesDashboardController.startAgentsTableViewRefresher();
    }
    public void updateUBoatContestTableView(){
        alliesDashboardController.startUBoatContestsTableViewRefresher();
    }
public void setSelectedBattleFieldName(String selectedBattleFieldName){
        alliesContestController.setSelectedBattleFieldName(selectedBattleFieldName);

}
    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
        alliesDashboardController.setAlliesTeamName(alliesTeamName);
        alliesContestController.setAlliesTeamName(alliesTeamName);
    }
    public void setConvertedString(String convertedString){
        alliesContestController.setConvertedString(convertedString);
    }
    public void changeToContestTab(){
        contestTabButton.setDisable(false);
        alliesTabPane.getSelectionModel().select(contestTabButton);
        alliesContestController.startAlliesInfoTableViewRefresher();
        alliesContestController.startContestResultsTableViewRefresher();
        alliesContestController.startContestInfoTableViewRefresher();

    }
}