package component.alliesContest;

import bruteForce.AgentInfoDTO;
import bruteForce.AlliesDTO;
import bruteForce.BruteForceResultDTO;
import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import component.AlliesDashboard.AgentsTablesViewRefresher;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import bruteForce.BruteForceResultAndVersion;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class AlliesContestController implements Closeable {
    MainWindowAlliesController mainWindowAlliesController;

    @FXML
    private Button submitButton;

    @FXML
    private TextField missionSizeTextField;

    private String alliesTeamName;
    private String selectedBattleField="";
    @FXML
    private TableView<AlliesDTO> activeTeamsDetailsTableView;
    @FXML
    private TableColumn<AlliesDTO, String> missionSizeColumn;
    @FXML
    private TableColumn<AlliesDTO, String> agentsAmountColumn;
    @FXML
    private TableColumn<AlliesDTO, String> alliesTeamNameColumn;
    private Timer timer;
    private TimerTask alliesRegisteredTeamsRefresher;
    private Timer agentsTableViewTimer;
    private TimerTask agentsTableViewRefresher;
    private SimpleBooleanProperty autoUpdate;

    @FXML
    private TableColumn<BruteForceResultDTO, String> stringColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> alliesNameColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> codeConfigurationColumn;
    @FXML
    private TableView<BruteForceResultDTO> contestCandidatesTableView;

    @FXML
    private TableView<UBoatContestInfoWithoutCheckBoxDTO> contestDataTableView;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> contestLevelColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> encryptedStringColumn;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> contestStatusColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> battleFieldNameColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> uBoatUserNameColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> amountOfActiveDecryptionTeamsColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> amountOfNeededDecryptionTeamsColumn;

    @FXML
    private TableView<AgentInfoDTO> agentsMissionsStatusTableView;
    @FXML
    private TableColumn<AgentInfoDTO, String> agentNameColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> amountOfCandidatesStringsColumn;

    @FXML
    private TableColumn<AgentInfoDTO, String> amountOfMissionsReceivedColumn;

    @FXML
    private TableColumn<AgentInfoDTO, String> amountOfMissionsToExecuteColumn;

    private IntegerProperty totalBruteResultAmount;
    private TimerTask alliesBruteForceResultTableViewRefresher;
    private Timer alliesBruteForceResultTableViewRefresherTimer;
    private IntegerProperty contestResultsInfoVersion;
    private Timer contestInfoRefresherTimer;
    private TimerTask contestInfoRefresher;
  private   String convertedString;

    @FXML
    public void initialize(){
        autoUpdate=new SimpleBooleanProperty(true);
        totalBruteResultAmount=new SimpleIntegerProperty(0);
        this.contestResultsInfoVersion =new SimpleIntegerProperty();


    }

    public void setConvertedString(String convertedString) {
        this.convertedString = convertedString;

    }

    public  void setSelectedBattleFieldName(String selectedBattleFieldName){
        this.selectedBattleField=selectedBattleFieldName;

    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }
    @FXML
    void submitButtonOnActionListener(ActionEvent event) throws IOException {
        AlliesDTO alliesDTO = new AlliesDTO(Integer.parseInt(missionSizeTextField.getText()), alliesTeamName);
        String alliesDTOGson = Constants.GSON_INSTANCE.toJson(alliesDTO);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), alliesDTOGson);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String finalUrl = HttpUrl
                .parse(Constants.REGISTER_ALLIES_TO_CONTEST)
                .newBuilder()
                .addQueryParameter("battlefield", selectedBattleField)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        Response response = call.execute();
        if (response.code() == 200) {

            Platform.runLater(() -> {

                alert.setContentText("You registered to the contest " + selectedBattleField + " successfully");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            });

            String stringToConvert=Constants.GSON_INSTANCE.fromJson(response.body().string(),String.class);
            if(stringToConvert!=null) {
                AlliesThreadTask threadTask=new AlliesThreadTask(stringToConvert,Integer.parseInt(missionSizeTextField.getText()),alliesTeamName);
                new Thread(threadTask).start();
            }
        } else {
            if (response.code() == 409) {
                alert.setContentText("The contest " + selectedBattleField + " is full, please select another one");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            }
        }
    }

    public void setMainWindowAlliesController(MainWindowAlliesController mainWindowAlliesController) {
        this.mainWindowAlliesController = mainWindowAlliesController;
    }
    public void startAlliesInfoTableViewRefresher() {
        alliesRegisteredTeamsRefresher = new AlliesRegisteredTeamsInfoTablesViewRefresher(
                this::updateRegisteredAlliesInfoList,
                autoUpdate,
                selectedBattleField);
        timer = new Timer();
        timer.schedule(alliesRegisteredTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void createAlliesInfoDTOTableView(ObservableList<AlliesDTO> alliesInfoDTOList ) {
        activeTeamsDetailsTableView.setItems(alliesInfoDTOList);
        activeTeamsDetailsTableView.getColumns().clear();
        activeTeamsDetailsTableView.getColumns().addAll(alliesTeamNameColumn, agentsAmountColumn, missionSizeColumn);
    }
    private void updateRegisteredAlliesInfoList(List<AlliesDTO> alliesInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AlliesDTO> alliesDTOObservableList =getTeamsAgentsDataTableViewDTOList(alliesInfoDTOList);
            createAlliesInfoDTOTableView(alliesDTOObservableList);
           // totalAlliesRegisteredTeamsAmount.set(alliesInfoDTOList.size());
        });
    }
    private ObservableList<AlliesDTO> getTeamsAgentsDataTableViewDTOList(List<AlliesDTO> alliesDTO) {

        ObservableList<AlliesDTO> alliesDTOList;
        alliesDTOList = FXCollections.observableArrayList(alliesDTO);
        alliesTeamNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("alliesName")
        );
        agentsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentsAmount")
        );
        missionSizeColumn.setCellValueFactory(
                new PropertyValueFactory<>("missionSize")
        );

        return alliesDTOList;
    }

    private void updateBruteForceResultsTableView(BruteForceResultAndVersion bruteForceResultAndVersionWithVersion) {
        if (bruteForceResultAndVersionWithVersion != null) {
            if (bruteForceResultAndVersionWithVersion.getVersion() != contestResultsInfoVersion.get()) {

                Platform.runLater(() -> {
                    contestResultsInfoVersion.set(bruteForceResultAndVersionWithVersion.getVersion());
                    updateBruteForceResultInfoList(bruteForceResultAndVersionWithVersion.getEntries());
                });
            }
        }
    }
    private ObservableList<BruteForceResultDTO> getBruteForceResultDataTableViewDTOList(List<BruteForceResultDTO> bruteForceResult) {

        ObservableList<BruteForceResultDTO> bruteForceResultDTOObservableList;
        bruteForceResultDTOObservableList = FXCollections.observableArrayList(bruteForceResult);
        stringColumn.setCellValueFactory(
                new PropertyValueFactory<>("convertedString")
        );
        alliesNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("alliesTeamName")
        );
        codeConfigurationColumn.setCellValueFactory(
                new PropertyValueFactory<>("codeDescription")
        );

        return bruteForceResultDTOObservableList;
    }
    private void updateBruteForceResultInfoList(List<BruteForceResultDTO> bruteForceResultDTOList) {
        ObservableList<BruteForceResultDTO> bruteForceResultDTOObservableList =getBruteForceResultDataTableViewDTOList(bruteForceResultDTOList);
        createContestInfoDTOTableView(bruteForceResultDTOObservableList);
        totalBruteResultAmount.set(bruteForceResultDTOList.size());
    }
    private void createContestInfoDTOTableView(ObservableList<BruteForceResultDTO> bruteForceResultDTOList ) {
        if (contestCandidatesTableView.getItems().isEmpty()) {
            contestCandidatesTableView.setItems(bruteForceResultDTOList);
            contestCandidatesTableView.getColumns().clear();
            contestCandidatesTableView.getColumns().addAll(stringColumn, alliesNameColumn, codeConfigurationColumn);
        }
        else {
            contestCandidatesTableView.getItems().addAll(bruteForceResultDTOList);
        }
    }
    public void startContestResultsTableViewRefresher() {
        alliesBruteForceResultTableViewRefresher = new AlliesBruteForceResultTableViewRefresher(
                alliesTeamName.trim(),
                contestResultsInfoVersion,
                autoUpdate,
                this::updateBruteForceResultsTableView);
        alliesBruteForceResultTableViewRefresherTimer = new Timer();
        timer.schedule(alliesBruteForceResultTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    private ObservableList<UBoatContestInfoWithoutCheckBoxDTO> getUBoatContestInfoTableViewDTOList(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTO) {
        ObservableList<UBoatContestInfoWithoutCheckBoxDTO> uBoatContestInfoDTOList;
        uBoatContestInfoDTOList = FXCollections.observableArrayList(uBoatContestInfoDTO);

        battleFieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("battleFieldName")
        );
        encryptedStringColumn.setCellValueFactory(
                new PropertyValueFactory<>("convertedString")
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
    private void updateContestInfoTableView(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTOList) {
        Platform.runLater(() -> {
                    ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList =getUBoatContestInfoTableViewDTOList(uBoatContestInfoDTOList);
                    createContestInfoTableView(contestInfoObservableList);
                }

        );
    }
    private void createContestInfoTableView( ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList) {
        contestDataTableView.setItems(contestInfoObservableList);
        contestDataTableView.getColumns().clear();
        contestDataTableView.getColumns().addAll(battleFieldNameColumn,encryptedStringColumn ,uBoatUserNameColumn,contestStatusColumn,contestLevelColumn,amountOfNeededDecryptionTeamsColumn,amountOfActiveDecryptionTeamsColumn);
    }
    public void startContestInfoTableViewRefresher() {
        contestInfoRefresher = new AlliesSelectedContestInfoTablesViewRefresher(
                this::updateContestInfoTableView,autoUpdate,alliesTeamName);
        contestInfoRefresherTimer = new Timer();
        timer.schedule(contestInfoRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @Override
    public void close() throws IOException {
        activeTeamsDetailsTableView.getItems().clear();
        contestResultsInfoVersion.set(0);
        if (alliesRegisteredTeamsRefresher != null && timer!= null && alliesBruteForceResultTableViewRefresherTimer != null &&alliesRegisteredTeamsRefresher!=null) {
            alliesRegisteredTeamsRefresher.cancel();
            alliesRegisteredTeamsRefresher.cancel();
            timer.cancel();
            alliesBruteForceResultTableViewRefresherTimer.cancel();
        }
    }

    private ObservableList<AgentInfoDTO> getTeamsAgentsMissionsStatusTableViewDTOList(List<AgentInfoDTO> agentInfoDTO) {

        ObservableList<AgentInfoDTO> agentInfoDTOList;

        agentInfoDTOList = FXCollections.observableArrayList(agentInfoDTO);
        agentNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentName")
        );
        amountOfCandidatesStringsColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfCandidatesStrings")
        );
        amountOfMissionsToExecuteColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfMissionsToExecute")
        );
        amountOfMissionsReceivedColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfReceivedMissions")
        );

        return agentInfoDTOList;
    }
    private void updateAgentsInfoList(List<AgentInfoDTO> agentInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AgentInfoDTO> agentInfoDTOObservableList =getTeamsAgentsMissionsStatusTableViewDTOList(agentInfoDTOList);
            createAgentsInfoDTOTableView(agentInfoDTOObservableList);
            //totalAgentsAmount.set(agentInfoDTOList.size());
        });
    }
    private void createAgentsInfoDTOTableView(ObservableList<AgentInfoDTO> agentInfoDTOList ) {
        agentsMissionsStatusTableView.setItems(agentInfoDTOList);
        agentsMissionsStatusTableView.getColumns().clear();
        agentsMissionsStatusTableView.getColumns().addAll(agentNameColumn, amountOfCandidatesStringsColumn, amountOfMissionsToExecuteColumn,amountOfMissionsReceivedColumn);
    }
    public void startAgentsTableViewRefresher() {
        agentsTableViewRefresher = new AgentsTablesViewRefresher(
                this::updateAgentsInfoList,
                autoUpdate,
                alliesTeamName);
        agentsTableViewTimer = new Timer();
        agentsTableViewTimer.schedule(agentsTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }

}