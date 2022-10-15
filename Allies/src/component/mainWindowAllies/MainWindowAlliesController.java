package component.mainWindowAllies;

import component.AlliesDashboard.AlliesDashboardController;
import component.alliesContest.AlliesContestController;
import component.alliesContest.AlliesThreadTask;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

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
    String selectedBattleFieldName;

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

    public void updateAgentsTableView() {
        alliesDashboardController.startAgentsTableViewRefresher();
    }

    public void updateUBoatContestTableView() {
        alliesDashboardController.startUBoatContestsTableViewRefresher();
    }

    public void setSelectedBattleFieldName(String selectedBattleFieldName) {
        alliesContestController.setSelectedBattleFieldName(selectedBattleFieldName);

    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
        alliesDashboardController.setAlliesTeamName(alliesTeamName);
        alliesContestController.setAlliesTeamName(alliesTeamName);
    }

    public void setConvertedString(String convertedString) {
        alliesContestController.setConvertedString(convertedString);
    }

    public void changeToContestTab() throws IOException {


        contestTabButton.setDisable(false);
        alliesTabPane.getSelectionModel().select(contestTabButton);
        alliesContestController.startAlliesInfoTableViewRefresher();
        alliesContestController.startContestResultsTableViewRefresher();
        alliesContestController.startContestInfoTableViewRefresher();
        alliesContestController.startAgentsTableViewRefresher();
        alliesContestController.startDMAmountOfCreatedMissionsRefresherRefresher();
        alliesContestController.updateMaxAmountOfMissions();
        alliesDashboardController.close();
    }
public void disableReadyButton(){
        alliesDashboardController.disableReadyButton();
}
    public void changeToAlliesDashboardTab() throws IOException {
        dashboardTabButton.setDisable(false);
        alliesTabPane.getSelectionModel().select(dashboardTabButton);
        alliesDashboardController.startUBoatContestsTableViewRefresher();
        alliesDashboardController.startAgentsTableViewRefresher();
        alliesContestController.close();
        contestTabButton.setDisable(true);
    }
    public void startContestStatusRefresher(){
        alliesContestController.startContestStatusRefresher();
    }

    public AlliesContestController getAlliesContestController() {
        return alliesContestController;
    }
public void setMissionSize(int missionSize){
        alliesContestController.setMissionSize(missionSize);

}
    public void setThreadTask(AlliesThreadTask threadTask) {
        System.out.println("setThreadTask");
        alliesContestController.setThreadTask(threadTask);
    }
}