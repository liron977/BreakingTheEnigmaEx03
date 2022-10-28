package component.mainWindowAllies;

import component.AlliesDashboard.AlliesDashboardController;
import component.alliesContest.AlliesContestController;
import component.alliesContest.AlliesThreadTask;
import component.bonus.ChatAreaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainWindowAlliesController {

    @FXML
    ScrollPane alliesDashboard;

    @FXML
    ScrollPane alliesContest;

    @FXML
    private Button createAgentButton;
    @FXML
    AlliesDashboardController alliesDashboardController;

    @FXML
    AlliesContestController alliesContestController;

    String alliesTeamName;
    private Stage primaryStage;
    @FXML
    private Button chatButton;
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
            dashboardTabButton.setOnSelectionChanged(e -> {
                startDashboardRefreshersOnAction();
            });

        }
    }

    @FXML
    void createAgentButtonOnAction(ActionEvent event) throws IOException {
       /*   ProcessBuilder pb=new ProcessBuilder("AgentRun.bat");
        pb.start();*/

        ProcessBuilder pb = new ProcessBuilder();
        java.util.Map<String, String> env = pb.environment();

        env.put("alliesTeamName", alliesTeamName);

        pb.command("AddAgentBonus.bat", "/c", "echo", "%mode%");

        pb.inheritIO().start();

    }

    @FXML
    void chatButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage=new Stage();
        URL url = getClass().getResource("/component/bonus/chat-area.fxml");
        //URL url = getClass().getResource("/component/mainWindowUBoat/MainWindowUBoat.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        ChatAreaController chatAreaController = fxmlLoader.getController();
        //uBoatLoginController.setMediator(mediator);
        chatAreaController.startListRefresher();
        stage.setTitle("Allies Chat");
        stage.getIcons().add(new Image("/Resources/power.jpg"));
        Scene scene = new Scene(root);
        stage.setMinHeight(300f);
        stage.setMinWidth(400f);
        scene.getStylesheets().add(getClass().getResource("/utils/CSS/BlueStyle.css").toExternalForm());
        scene.getStylesheets().add("");
        stage.setScene(scene);
        stage.show();
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


        //alliesContestController.close();

        contestTabButton.setDisable(false);
        dashboardTabButton.setDisable(true);
        alliesTabPane.getSelectionModel().select(contestTabButton);
        alliesContestController.startAlliesInfoTableViewRefresher();
        alliesContestController.startContestResultsTableViewRefresher();
        alliesContestController.startContestInfoTableViewRefresher();
        alliesContestController.startAgentsTableViewRefresher();
        alliesContestController.updateMaxAmountOfMissions();
        alliesContestController.startGetNotAvailableAgentsRefresher();
        alliesDashboardController.close();
    }
public void disableReadyButton(){
        alliesDashboardController.disableReadyButton();
}
    public void changeToAlliesDashboardTab() throws IOException {
        dashboardTabButton.setDisable(false);
      // alliesContestController.cancelNotAvailableAgentsRefresher();
        alliesTabPane.getSelectionModel().select(dashboardTabButton);
        alliesDashboardController.startUBoatContestsTableViewRefresher();
        alliesDashboardController.startAgentsTableViewRefresher();
        alliesContestController.close();
        alliesContestController.initNotAvailableAgentsValues();
        contestTabButton.setDisable(true);
    }
    public void startDashboardRefreshersOnAction(){
        alliesDashboardController.startUBoatContestsTableViewRefresher();
        alliesDashboardController.startAgentsTableViewRefresher();
    }
    public void startContestStatusRefresher(){
        alliesContestController.startContestStatusRefresher();
        alliesContestController.startDMAmountOfCreatedMissionsRefresherRefresher();
    }
    public void alliesContestControllerDeleteValues() throws IOException {
        alliesContestController.close();
        alliesContestController.reset();
    }


    public AlliesContestController getAlliesContestController() {
        return alliesContestController;
    }
public void setMissionSize(int missionSize){
        alliesContestController.setMissionSize(missionSize);

}
  /*  public void setThreadTask(AlliesThreadTask threadTask) {
        System.out.println("setThreadTask");
        alliesContestController.setThreadTask(threadTask);
    }*/
}