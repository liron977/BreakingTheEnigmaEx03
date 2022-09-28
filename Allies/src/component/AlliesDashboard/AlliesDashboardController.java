package component.AlliesDashboard;


import bruteForce.AgentInfoDTO;
import bruteForce.UBoatContestInfoDTO;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;

public class AlliesDashboardController implements Closeable {

    private Timer timer;
    private TimerTask agentsTableViewRefresher;
    private TimerTask uBoatContestsRefresher;
    private  SimpleBooleanProperty autoUpdate;
    private  IntegerProperty totalAgentsAmount;
    private  IntegerProperty totalUBoatContestsAmount;

    @FXML
    private TableView<UBoatContestInfoDTO> contestsDataTableView;
    @FXML
    private TableView<AgentInfoDTO> teamsAgentsDataTableView;
    @FXML
    private TableColumn<AgentInfoDTO, String> agentNameColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> threadsAmountColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> missionsAmountColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> amountOfActiveDecryptionTeamsColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> amountOfNeededDecryptionTeamsColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> battleFieldNameColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> contestLevelColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> contestStatusColumn;
    @FXML
    private TableColumn<UBoatContestInfoDTO, String> uBoatUserNameColumn;

    private MainWindowAlliesController mainWindowAlliesController;

    AgentInfoDTO agentInfoDTO;
    String alliesTeamName="";

    public AlliesDashboardController() {
        totalAgentsAmount = new SimpleIntegerProperty(0);
        totalUBoatContestsAmount = new SimpleIntegerProperty(0);
        autoUpdate=new SimpleBooleanProperty(true);
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    public void setMainWindowAlliesController(MainWindowAlliesController mainWindowAlliesController) {
        this.mainWindowAlliesController = mainWindowAlliesController;
    }
    private ObservableList<AgentInfoDTO> getTeamsAgentsDataTableViewDTOList(List<AgentInfoDTO> agentInfoDTO) {

        ObservableList<AgentInfoDTO> agentInfoDTOList;

        agentInfoDTOList = FXCollections.observableArrayList(agentInfoDTO);
        agentNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentName")
        );
        threadsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("threadsAmount")
        );
        missionsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("missionsAmount")
        );

        return agentInfoDTOList;
    }

    @Override
    public void close() throws IOException {
        teamsAgentsDataTableView.getItems().clear();
        totalAgentsAmount.set(0);
        if (agentsTableViewRefresher != null && timer != null) {
            agentsTableViewRefresher.cancel();
            timer.cancel();
        }
    }
    private void updateAgentsInfoList(List<AgentInfoDTO> agentInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AgentInfoDTO> agentInfoDTOObservableList =getTeamsAgentsDataTableViewDTOList(agentInfoDTOList);
            createAgentsInfoDTOTableView(agentInfoDTOObservableList);
            totalAgentsAmount.set(agentInfoDTOList.size());
        });
    }
    private void createAgentsInfoDTOTableView(ObservableList<AgentInfoDTO> agentInfoDTOList ) {
        teamsAgentsDataTableView.setItems(agentInfoDTOList);
        teamsAgentsDataTableView.getColumns().clear();
        teamsAgentsDataTableView.getColumns().addAll(missionsAmountColumn, threadsAmountColumn, agentNameColumn);
    }
    public void startAgentsTableViewRefresher() {
        agentsTableViewRefresher = new AgentsTablesViewRefresher(
                this::updateAgentsInfoList,
                autoUpdate,
                alliesTeamName);
        timer = new Timer();
        timer.schedule(agentsTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private ObservableList<UBoatContestInfoDTO> getUBoatContestInfoTableViewDTOList(List<UBoatContestInfoDTO> uBoatContestInfoDTO) {
        ObservableList<UBoatContestInfoDTO> uBoatContestInfoDTOList;
        uBoatContestInfoDTOList = FXCollections.observableArrayList(uBoatContestInfoDTO);
        battleFieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("battleFieldName")
        );
        uBoatUserNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("uBoatUserName")
        );
        contestStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("contestStatus")
        );
        contestLevelColumn.setCellValueFactory(
                new PropertyValueFactory<>("contestLevel")
        );
        amountOfNeededDecryptionTeamsColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfNeededDecryptionTeams")
        );
        amountOfActiveDecryptionTeamsColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfActiveDecryptionTeams")
        );
        return uBoatContestInfoDTOList;
    }
    private void updateUBoatContestsList(List<UBoatContestInfoDTO> uBoatContestInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<UBoatContestInfoDTO> uBoatContestsInfoDTOObservableList = getUBoatContestInfoTableViewDTOList(uBoatContestInfoDTOList);
            createUBoatContestsInfoDTOTableView(uBoatContestsInfoDTOObservableList);
            totalUBoatContestsAmount.set(uBoatContestsInfoDTOObservableList.size());
        });
    }
    private void createUBoatContestsInfoDTOTableView(ObservableList<UBoatContestInfoDTO> uBoatContestInfoDTOList ) {
        contestsDataTableView.setItems(uBoatContestInfoDTOList);
        contestsDataTableView.getColumns().clear();
        contestsDataTableView.getColumns().addAll(battleFieldNameColumn,
                uBoatUserNameColumn, contestStatusColumn,contestLevelColumn,
                amountOfNeededDecryptionTeamsColumn,amountOfActiveDecryptionTeamsColumn);
    }
    public void startUBoatContestsTableViewRefresher() {
        uBoatContestsRefresher = new UBoatContestsRefresher(
                this::updateUBoatContestsList,autoUpdate);
        timer = new Timer();
        timer.schedule(uBoatContestsRefresher, REFRESH_RATE, REFRESH_RATE);
    }


}