package component.alliesContest;

import bruteForce.*;
import component.AlliesDashboard.AgentsTablesViewRefresher;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class AlliesContestController implements Closeable {
    MainWindowAlliesController mainWindowAlliesController;

    @FXML
    private Button submitButton;
    @FXML
    private Label dmAmountOfCreatedMissionsLabel;
    @FXML
    private Label amountOfDoneMissions;
    @FXML
    private Label totalAmountOfCreatedMissionsLabel;


    private String alliesTeamName;
    private String selectedBattleField = "";
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
    AlliesThreadTask threadTask;

    @FXML
    private TableColumn<BruteForceResultDTO, String> stringColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> resultTableAgentNameColumn;
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
    private String convertedString;
    private int missionSize;
    private boolean isUboatSettingsCompleted;

    private TimerTask contestStatusRefresher;
    private Timer contestStatusRefresherTimer;
    private SimpleBooleanProperty isContestEnded;
    private String alliesWinnerTeamName;
    private boolean isMessageDisplayedForFirstTime;
    private TimerTask amountOfCreatedMissionsRefresher;
    private Timer amountOfCreatedMissionsRefresherTimer;
    boolean isMissionsCreated;
    // ObservableList<AgentInfoDTO> agentInfoDTOObservableList;
    @FXML
    private Label uBoatIsNotReadyLabel;
    private boolean isMessageUboatDontExistDisplayed;

    @FXML
    public void initialize() {
        autoUpdate = new SimpleBooleanProperty(true);
        totalBruteResultAmount = new SimpleIntegerProperty(0);
        this.contestResultsInfoVersion = new SimpleIntegerProperty();
        isContestEnded = new SimpleBooleanProperty(false);
        alliesWinnerTeamName = "";
        isMessageDisplayedForFirstTime = false;
        isMissionsCreated = false;
        isUboatSettingsCompleted = false;
        missionSize = 0;
        isMessageUboatDontExistDisplayed=false;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public void setConvertedString(String convertedString) {
        this.convertedString = convertedString;

    }

    public void setSelectedBattleFieldName(String selectedBattleFieldName) {
        this.selectedBattleField = selectedBattleFieldName;

    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    /* @FXML
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
                *//* AlliesThreadTask threadTask=new AlliesThreadTask(stringToConvert,Integer.parseInt(missionSizeTextField.getText()),alliesTeamName);
                new Thread(threadTask).start();*//*
            }
        } else {
            if (response.code() == 409) {
                alert.setContentText("The contest " + selectedBattleField + " is full, please select another one");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            }
        }
    }
*/
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

    private void createAlliesInfoDTOTableView(ObservableList<AlliesDTO> alliesInfoDTOList) {
        activeTeamsDetailsTableView.setItems(alliesInfoDTOList);
        activeTeamsDetailsTableView.getColumns().clear();
        activeTeamsDetailsTableView.getColumns().addAll(alliesTeamNameColumn, agentsAmountColumn, missionSizeColumn);
    }

    private void updateRegisteredAlliesInfoList(List<AlliesDTO> alliesInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AlliesDTO> alliesDTOObservableList = getTeamsAgentsDataTableViewDTOList(alliesInfoDTOList);
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
        resultTableAgentNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentName")
        );
        codeConfigurationColumn.setCellValueFactory(
                new PropertyValueFactory<>("codeDescription")
        );

        return bruteForceResultDTOObservableList;
    }

    private void updateBruteForceResultInfoList(List<BruteForceResultDTO> bruteForceResultDTOList) {
        ObservableList<BruteForceResultDTO> bruteForceResultDTOObservableList = getBruteForceResultDataTableViewDTOList(bruteForceResultDTOList);
        createContestInfoDTOTableView(bruteForceResultDTOObservableList);
        totalBruteResultAmount.set(bruteForceResultDTOList.size());
    }

    private void createContestInfoDTOTableView(ObservableList<BruteForceResultDTO> bruteForceResultDTOList) {
        if (contestCandidatesTableView.getItems().isEmpty()) {
            contestCandidatesTableView.setItems(bruteForceResultDTOList);
            contestCandidatesTableView.getColumns().clear();
            contestCandidatesTableView.getColumns().addAll(stringColumn, resultTableAgentNameColumn, codeConfigurationColumn);
        } else {
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
                    ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList = getUBoatContestInfoTableViewDTOList(uBoatContestInfoDTOList);
                    createContestInfoTableView(contestInfoObservableList);
                }

        );
    }

    private void createContestInfoTableView(ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList) {
        contestDataTableView.setItems(contestInfoObservableList);
        contestDataTableView.getColumns().clear();
        contestDataTableView.getColumns().addAll(battleFieldNameColumn, encryptedStringColumn, uBoatUserNameColumn, contestStatusColumn, contestLevelColumn, amountOfNeededDecryptionTeamsColumn, amountOfActiveDecryptionTeamsColumn);
    }

    public void startContestInfoTableViewRefresher() {
        contestInfoRefresher = new AlliesSelectedContestInfoTablesViewRefresher(
                this::updateContestInfoTableView, autoUpdate, alliesTeamName);
        contestInfoRefresherTimer = new Timer();
        contestInfoRefresherTimer.schedule(contestInfoRefresher, REFRESH_RATE, REFRESH_RATE);
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
            ObservableList<AgentInfoDTO> agentInfoDTOObservableList = getTeamsAgentsMissionsStatusTableViewDTOList(agentInfoDTOList);
            createAgentsInfoDTOTableView(agentInfoDTOObservableList);
            amountOfDoneMissions.setText("The amount of done missions: " + displayTextWithCommas(updateAmountOfDoneMissions(agentInfoDTOObservableList)));
            //totalAgentsAmount.set(agentInfoDTOList.size());
        });
    }

    private void createAgentsInfoDTOTableView(ObservableList<AgentInfoDTO> agentInfoDTOList) {
        agentsMissionsStatusTableView.setItems(agentInfoDTOList);
        agentsMissionsStatusTableView.getColumns().clear();
        agentsMissionsStatusTableView.getColumns().addAll(agentNameColumn, amountOfCandidatesStringsColumn, amountOfMissionsToExecuteColumn, amountOfMissionsReceivedColumn);
    }

    private Long updateAmountOfDoneMissions(ObservableList<AgentInfoDTO> agentInfoDTOList) {
        Long amountOfDoneMissions = 0L;
        for (AgentInfoDTO agentInfoDTO : agentInfoDTOList) {
            amountOfDoneMissions += agentInfoDTO.getAmountOfDoneMissions();
        }
        return amountOfDoneMissions;
    }

    public void startAgentsTableViewRefresher() {
        agentsTableViewRefresher = new AgentsTablesViewRefresher(
                this::updateAgentsInfoList,
                autoUpdate,
                alliesTeamName);
        agentsTableViewTimer = new Timer();
        agentsTableViewTimer.schedule(agentsTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void startContestStatusRefresher() {
        contestStatusRefresher = new ContestStatusRefresher("Allies", selectedBattleField
                , this::updateContestStatus, autoUpdate, alliesTeamName);
        contestStatusRefresherTimer = new Timer();
        contestStatusRefresherTimer.schedule(contestStatusRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public boolean getIsUboatSettingsCompleted() {
        return isUboatSettingsCompleted;
    }

    public String getConvertedString() {
        return convertedString;
    }


    private void updateContestStatus(ContestStatusInfoDTO contestStatusInfoDTO) {

       /* if(threadTask!=null){
            threadTask.setIsContestEnded(contestStatusInfoDTO.isContestEnded());
            threadTask.setIsUboatSettingsCompleted(contestStatusInfoDTO.isUboatSettingsCompleted());
            threadTask.setStringToConvert(convertedString);
        }*/
        if (contestStatusInfoDTO.isUboatSettingsCompleted() && !isMissionsCreated) {
            threadTask = new AlliesThreadTask(convertedString, missionSize, alliesTeamName);
            mainWindowAlliesController.setThreadTask(threadTask);
            System.out.println("before start");
            new Thread(threadTask).start();
            isMissionsCreated = true;
            /* isUboatSettingsCompleted = contestStatusInfoDTO.isUboatSettingsCompleted();*/
        }
        if (!isContestEnded.getValue()) {

            Platform.runLater(() -> {
                this.isContestEnded.setValue(contestStatusInfoDTO.isContestEnded());
                this.alliesWinnerTeamName = contestStatusInfoDTO.getAlliesWinnerTeamName();
                if (isContestEnded.getValue() && !isMessageDisplayedForFirstTime&&(!alliesWinnerTeamName.equals(""))&&!isMessageUboatDontExistDisplayed) {
                    isMessageDisplayedForFirstTime = true;
                    isMissionsCreated = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    String message = "The contest ended" + "\n" + "The winning team is " + alliesWinnerTeamName;
                    alert.setContentText(message);
                    alert.setTitle("Allies");
                    alert.getDialogPane().setExpanded(true);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        isMissionsCreated = false;
                        try {
                            setConfirmed();
                            close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        deleteValues();
                        try {
                            mainWindowAlliesController.changeToAlliesDashboardTab();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (result.get() == ButtonType.CANCEL) {
                        try {
                            close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //alert.showAndWait();
                    try {
                        close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public void close() throws IOException {
        // activeTeamsDetailsTableView.getItems().clear();
        contestResultsInfoVersion.set(0);
        if (contestStatusRefresher != null && agentsTableViewRefresher != null
                && alliesRegisteredTeamsRefresher != null && contestInfoRefresher != null
                && agentsTableViewTimer != null
                && alliesBruteForceResultTableViewRefresherTimer != null
                &&amountOfCreatedMissionsRefresher!=null) {
            contestStatusRefresher.cancel();
            agentsTableViewRefresher.cancel();
            alliesRegisteredTeamsRefresher.cancel();
            alliesBruteForceResultTableViewRefresher.cancel();
            contestInfoRefresher.cancel();
            agentsTableViewTimer.cancel();
            alliesBruteForceResultTableViewRefresherTimer.cancel();
            amountOfCreatedMissionsRefresher.cancel();
            timer.cancel();
            agentsTableViewTimer.cancel();
            alliesBruteForceResultTableViewRefresherTimer.cancel();
            contestInfoRefresherTimer.cancel();
            contestStatusRefresherTimer.cancel();
            amountOfCreatedMissionsRefresherTimer.cancel();
        }
    }

    public void startDMAmountOfCreatedMissionsRefresherRefresher() {
        isMessageUboatDontExistDisplayed=false;
        amountOfCreatedMissionsRefresher = new DMAmountOfCreatedMissionsRefresher(selectedBattleField,
                this::updateAmountOfCreatedMissions, autoUpdate, alliesTeamName);
        amountOfCreatedMissionsRefresherTimer = new Timer();
        amountOfCreatedMissionsRefresherTimer.schedule(amountOfCreatedMissionsRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAmountOfCreatedMissions(DMAmountOfMissionsInfoDTO dmAmountOfMissionsInfoDTO) {

            if (!dmAmountOfMissionsInfoDTO.getIsUboatExist() && !isMessageUboatDontExistDisplayed) {

                    Platform.runLater(() -> {
                        isMessageUboatDontExistDisplayed = true;
                        Alert alertToUb = new Alert(Alert.AlertType.INFORMATION);
                        alertToUb.setContentText("The selected UBoat Logged out.");
                        alertToUb.getDialogPane().setExpanded(true);
                        alertToUb.showAndWait();
                       /* alliesRegisteredTeamsRefresher.cancel();
                        agentsTableViewRefresher.cancel();
                        agentsTableViewTimer.cancel();
                        amountOfCreatedMissionsRefresher.cancel();*/
                        mainWindowAlliesController.disableReadyButton();
                        try {
                            close();
                            mainWindowAlliesController.changeToAlliesDashboardTab();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });



            } else if (!isContestEnded.getValue()) {
                Platform.runLater(() -> {
               /* if(!dmAmountOfMissionsInfoDTO.getIsUboatExist()&&!isMessageUboatDontExistDisplayed){
                    try {
                        isMessageUboatDontExistDisplayed=true;
                        Alert alertToUb = new Alert(Alert.AlertType.INFORMATION);
                        alertToUb.setContentText("The selected UBoat Logged out.");
                        alertToUb.getDialogPane().setExpanded(true);
                        alertToUb.showAndWait();
                       *//* alliesRegisteredTeamsRefresher.cancel();
                        agentsTableViewRefresher.cancel();
                        agentsTableViewTimer.cancel();
                        amountOfCreatedMissionsRefresher.cancel();*//*
                        mainWindowAlliesController.disableReadyButton();
                        close();
                        mainWindowAlliesController.changeToAlliesDashboardTab();


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }*/
                    /*else{*/
                    if (dmAmountOfMissionsInfoDTO.getAmountOfCreatedMissions() == 0l) {
                        uBoatIsNotReadyLabel.setText("The uboat is not ready yet");
                    } else {
                        uBoatIsNotReadyLabel.setText("");
                    }
                    dmAmountOfCreatedMissionsLabel.setText("The amount of created missions: "
                            + displayTextWithCommas(dmAmountOfMissionsInfoDTO.getAmountOfCreatedMissions()));
              /*  totalAmountOfCreatedMissionsLabel.setText("The maximum amount of missions: "
                        + displayTextWithCommas(dmAmountOfMissionsInfoDTO.getTotalAmountOfCreatedMissions()));
*/
                });
            }
        }


    public String displayTextWithCommas(Long amount) {
        StringBuilder amountWithCommas = new StringBuilder("");
        int counter = 0;
        if (amount == 0) {
            return "0";
        }
        while (amount > 0) {
            if ((counter % 3 == 0) && (counter != 0)) {
                amountWithCommas = amountWithCommas.append(",");
            }
            counter++;
            amountWithCommas = amountWithCommas.append(amount % 10);
            amount = amount / 10;
        }
        return amountWithCommas.reverse().toString();
    }

    public void deleteValues() {
        activeTeamsDetailsTableView.getItems().clear();
        contestCandidatesTableView.getItems().clear();
        dmAmountOfCreatedMissionsLabel.setText("0");
        amountOfDoneMissions.setText("0");
        totalAmountOfCreatedMissionsLabel.setText("0");
        /*    missionSizeTextField.setText("0");*/
        selectedBattleField = "";
        autoUpdate = new SimpleBooleanProperty(true);
        contestCandidatesTableView.getItems().clear();
        agentsMissionsStatusTableView.getItems().clear();
        totalBruteResultAmount.setValue(0);
        contestResultsInfoVersion.setValue(0);
        convertedString = "";
        isContestEnded.setValue(false);
        alliesWinnerTeamName = "";
        isMessageDisplayedForFirstTime = false;
        //  agentInfoDTOObservableList= FXCollections.observableArrayList();
    }

    public void setThreadTask(AlliesThreadTask threadTask) {
        this.threadTask = threadTask;
    }

    public void setConfirmed() throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), "");

        String finalUrl = HttpUrl
                .parse(Constants.CONTEST_STATUS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
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
            response.body().close();

        }
        else{
  /*          Platform.runLater(() -> {
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    try {
                        alert.setContentText(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();
                }
            });*/
        }
/*
            Platform.runLater(() -> {

                alert.setContentText("You registered to the contest " + selectedBattleField + " successfully");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            })*/
        ;

//            String stringToConvert=Constants.GSON_INSTANCE.fromJson(response.body().string(),String.class);
//            if(stringToConvert!=null) {
//                AlliesThreadTask threadTask=new AlliesThreadTask(stringToConvert,Integer.parseInt(missionSizeTextField.getText()),alliesTeamName);
//                new Thread(threadTask).start();
//            }
//        } else {
//            if (response.code() == 409) {
//                alert.setContentText("The contest " + selectedBattleField + " is full, please select another one");
//                alert.getDialogPane().setExpanded(true);
//                alert.showAndWait();
//            }
//        }
    }

    public void updateMaxAmountOfMissions() throws IOException {

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), "");

        String finalUrl = HttpUrl
                .parse(Constants.GET_MAXIMUM_AMOUNT_OF_MISSIONS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        Response response = call.execute();
        if (response.code() == 200) {
            String maxAmount=response.body().string().trim();
           // System.out.println(maxAmount+"updateMaxAmountOfMissions");
            totalAmountOfCreatedMissionsLabel.setText("The maximum amount of missions: "+
                 displayTextWithCommas(Long.parseLong(maxAmount)));

        } else {

                Platform.runLater(() -> {
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        try {
                            alert.setContentText(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        alert.getDialogPane().setExpanded(true);
                        alert.showAndWait();
                    }
                });

        }

    }
}