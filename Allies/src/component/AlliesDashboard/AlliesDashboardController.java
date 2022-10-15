package component.AlliesDashboard;


import bruteForce.AgentInfoDTO;
import bruteForce.AlliesDTO;
import bruteForce.UBoatContestInfoWithCheckBoxDTO;
import component.alliesContest.AlliesThreadTask;
import component.mainWindowAllies.MainWindowAlliesController;
import constants.Constants;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.TheMachineEngineDTO;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class AlliesDashboardController implements Closeable {

    private Timer timer;
    private TimerTask agentsTableViewRefresher;
    private TimerTask uBoatContestsRefresher;
    private  SimpleBooleanProperty autoUpdate;
    private  IntegerProperty totalAgentsAmount;
    private  IntegerProperty totalUBoatContestsAmount;
    private SimpleBooleanProperty isContestSelected;
private  List<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoWithCheckBoxDTOList;
    @FXML
    private TableView<UBoatContestInfoWithCheckBoxDTO> contestsDataTableView;
    @FXML
    private TableView<AgentInfoDTO> teamsAgentsDataTableView;
    @FXML
    private TableColumn<AgentInfoDTO, String> agentNameColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> threadsAmountColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> missionsAmountColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> amountOfActiveDecryptionTeamsColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> amountOfNeededDecryptionTeamsColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> battleFieldNameColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> contestLevelColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> contestStatusColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, String> uBoatUserNameColumn;
    @FXML
    private TableColumn<UBoatContestInfoWithCheckBoxDTO, CheckBox> selectedContestColumn;
    @FXML
    private Button readyButton;
    @FXML
    private TextField missionSizeTextField;
    AlliesThreadTask threadTask;

    private  UBoatContestInfoWithCheckBoxDTO selectedContestDTO;
@FXML
    private MainWindowAlliesController mainWindowAlliesController;

    AgentInfoDTO agentInfoDTO;
    String alliesTeamName="";
    String selectedBattleField;

    @FXML
    public void initialize(){
        isContestSelected=new SimpleBooleanProperty(true);
        readyButton.disableProperty().bind(isContestSelected);
        selectedBattleField="";
    }

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
/*    private ObservableList<UBoatContestInfoWithCheckBoxDTO> getUBoatContestInfoTableViewDTOList(List<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTO) {
        ObservableList<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList;
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
        selectedContestColumn.setCellValueFactory(
                new PropertyValueFactory<>("selectionContestColumn")
        );
        return uBoatContestInfoDTOList;
    }*/
private ObservableList<UBoatContestInfoWithCheckBoxDTO> getUBoatContestInfoTableViewDTOList(UBoatContestInfoWithCheckBoxDTO uBoatContestInfoDTO) {
    ObservableList<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList;
    uBoatContestInfoDTOList = FXCollections.observableArrayList(uBoatContestInfoDTO);
    selectedContestColumn.setCellValueFactory(
            new PropertyValueFactory<>("selectionContestColumn")
    );
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
    private void updateUBoatContestsList(List<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList) {
        Platform.runLater(() -> {
            boolean isDTDExistsInTableView;
            ObservableList<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOListTemp = FXCollections.observableArrayList();;
            for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO: uBoatContestInfoDTOList) {
              checkBoxChangedListener(uBoatContestInfoWithCheckBoxDTO);
                isDTDExistsInTableView=false;
                int index=0;
                    for (UBoatContestInfoWithCheckBoxDTO TableViewUBoatContestDTO : contestsDataTableView.getItems()) {
                        index++;
                        if (TableViewUBoatContestDTO.getBattleFieldName().equals(uBoatContestInfoWithCheckBoxDTO.getBattleFieldName())) {
                            updateContestsDataTableViewRow(uBoatContestInfoWithCheckBoxDTO,index);
                            isDTDExistsInTableView = true;
                            break;
                        }
                    }
                    if (!isDTDExistsInTableView) {
                        uBoatContestInfoDTOListTemp.addAll(getUBoatContestInfoTableViewDTOList(uBoatContestInfoWithCheckBoxDTO));
                        contestsDataTableView.getItems().add(uBoatContestInfoWithCheckBoxDTO);
                    }
            }
            uBoatContestInfoWithCheckBoxDTOList=contestsDataTableView.getItems();
 /*           if(contestsDataTableView.getItems()!=null) {
                //todo : To check
                for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO : contestsDataTableView.getItems()) {
                    if (!uBoatContestInfoDTOList.contains(uBoatContestInfoWithCheckBoxDTO)) {

                        contestsDataTableView.getItems().remove(uBoatContestInfoWithCheckBoxDTO);
                    }
                    if(contestsDataTableView.getItems()==null){
                        break;
                    }
                }
            }*/
            updateContest(uBoatContestInfoDTOList);
           // totalUBoatContestsAmount.set(uBoatContestInfoDTOList.size());
        });
    }
    public void updateContest(List<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList){
        List<UBoatContestInfoWithCheckBoxDTO> toRemove=new ArrayList<>();
        ObservableList<UBoatContestInfoWithCheckBoxDTO>  contestsDataTableViewList=contestsDataTableView.getItems();
       for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO:contestsDataTableViewList) {
           boolean flag=false;
           for (UBoatContestInfoWithCheckBoxDTO ubi2:uBoatContestInfoDTOList) {
               if(ubi2.getBattleFieldName().equals(uBoatContestInfoWithCheckBoxDTO.getBattleFieldName())){
                    flag=true;
               }
                  }
           if(!flag){
               toRemove.add(uBoatContestInfoWithCheckBoxDTO);
           }
        }
       uBoatContestInfoDTOList.removeAll(toRemove);
       /* contestsDataTableView.getItems().clear();
        int index=0;
        for (UBoatContestInfoWithCheckBoxDTO TableViewUBoatContestDTO : uBoatContestInfoDTOList) {
            index++;
                updateContestsDataTableViewRow(TableViewUBoatContestDTO,index);
            }*/
        if(uBoatContestInfoDTOList.size()==0){
            contestsDataTableView.getItems().clear();
        }

    }
private void updateContestsDataTableViewRow(UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO, int contestsDataTableViewRow ){
    TableColumn statusTableColumn = contestsDataTableView.getColumns().get(3);
    ObservableValue statusTableColumnObservableValue = statusTableColumn.getCellObservableValue(0);
StringProperty statusStringProperty=(StringProperty) statusTableColumnObservableValue;
        (statusStringProperty).set(uBoatContestInfoWithCheckBoxDTO.getContestStatus().getValue());

    TableColumn activeAlliesTableColumn = contestsDataTableView.getColumns().get(6);
    ObservableValue activeAlliesObservableValue = activeAlliesTableColumn.getCellObservableValue(0);

    IntegerProperty activeAlliesStringProperty=(IntegerProperty) activeAlliesObservableValue;
    (activeAlliesStringProperty).set(uBoatContestInfoWithCheckBoxDTO.getAmountOfActiveDecryptionTeams());
}

    private void checkBoxChangedListener(UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO) {

        uBoatContestInfoWithCheckBoxDTO.getSelectionContestColumn().setOnAction(event -> {
            uBoatContestInfoWithCheckBoxDTOList = contestsDataTableView.getItems();
            if (uBoatContestInfoWithCheckBoxDTO.getSelectionContestColumn().isSelected()) {
                checkboxWasSelected(uBoatContestInfoWithCheckBoxDTO);

            } else {
                checkboxWasNotSelected(uBoatContestInfoWithCheckBoxDTO);

            }
        });
    }

    private void checkboxWasNotSelected(UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO ) {
        uBoatContestInfoWithCheckBoxDTOList=contestsDataTableView.getItems();
        uBoatContestInfoWithCheckBoxDTO.setSelected(false);
        for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTOFromList : uBoatContestInfoWithCheckBoxDTOList) {
            if (uBoatContestInfoWithCheckBoxDTOFromList.getIsSelected()) {
                return;
            }
        }
        for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTOFromListToNotDisabled : uBoatContestInfoWithCheckBoxDTOList) {
            uBoatContestInfoWithCheckBoxDTOFromListToNotDisabled.getSelectionContestColumn().setDisable(false);
        }
        selectedContestDTO=null;
        isContestSelected.setValue(true);
    }
        private void checkboxWasSelected(UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO ){
    isContestSelected.setValue(false);
            selectedContestDTO=uBoatContestInfoWithCheckBoxDTO;
            mainWindowAlliesController.setConvertedString(selectedContestDTO.getConvertedString());
        uBoatContestInfoWithCheckBoxDTOList=contestsDataTableView.getItems();
        uBoatContestInfoWithCheckBoxDTO.setSelected(true);
        for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTOFromList : uBoatContestInfoWithCheckBoxDTOList) {
            if (!uBoatContestInfoWithCheckBoxDTOFromList.getIsSelected()) {
                uBoatContestInfoWithCheckBoxDTOFromList.getSelectionContestColumn().setDisable(true);
            }
        }
    }


   /* private void createUBoatContestsInfoDTOTableView(ObservableList<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList ) {
        contestsDataTableView.setItems(uBoatContestInfoDTOList);
*//*       // contestsDataTableView.getColumns().clear();
        contestsDataTableView.getColumns().addAll(selectedContestColumn,battleFieldNameColumn,
                uBoatUserNameColumn, contestStatusColumn,contestLevelColumn,
                amountOfNeededDecryptionTeamsColumn,amountOfActiveDecryptionTeamsColumn);*//*
    }*/
    public void startUBoatContestsTableViewRefresher() {
        uBoatContestsRefresher = new UBoatContestsRefresher(
                this::updateUBoatContestsList,autoUpdate);
        timer = new Timer();
        timer.schedule(uBoatContestsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @FXML
    void readyButtonOnAction(ActionEvent event) throws IOException {
        if(selectedContestDTO!=null) {
            mainWindowAlliesController.setSelectedBattleFieldName(selectedContestDTO.getBattleFieldName());
            selectedBattleField=selectedContestDTO.getBattleFieldName();
            if(isMissionSizeIsValid()) {
                if(registerAllies()) {
                    mainWindowAlliesController.startContestStatusRefresher();
                    mainWindowAlliesController.changeToContestTab();
                }
            }
        }

    }
    private boolean isMissionSizeIsValid(){
        if(selectedContestDTO==null){
            displayErrorAlert("Please select a contest");
            return false;
        }
        else {
            String missionSizeString = missionSizeTextField.getText();
            TheMachineEngineDTO theMachineEngineDTO = getTheMachineEngineInfo();
            Long amountOfPossibleStartingPositionList = (long) Math.pow(theMachineEngineDTO.getKeyboardSize(), theMachineEngineDTO.getAmountOfUsedRotors());
            if (missionSizeString.equals("")) {
                displayErrorAlert("Please insert a mission size");
                return false;
            } else {
                if (missionSizeString.charAt(0) == '0') {
                    displayErrorAlert("Please enter only positive numbers");
                    return false;
                } else {
                    if (!isNumeric(missionSizeString)) {
                        displayErrorAlert("Please enter only numbers in the mission size field");
                        return false;
                    } else {
                        try {
                            long missionSize = Long.valueOf(missionSizeString);
                            if ((missionSize < 0) || (missionSize > amountOfPossibleStartingPositionList)) {
                                String msg = "Please enter a mission size between 1-" + displayTextWithCommas(amountOfPossibleStartingPositionList);
                                displayErrorAlert(msg);
                                return false;

                            } else {
                                return true;
                            }
                        } catch (Exception e) {
                            String msg = "Please enter a mission size between 1-" + displayTextWithCommas(amountOfPossibleStartingPositionList);
                            displayErrorAlert(msg);
                            return false;
                        }
                    }
                }
            }
        }
    }
    public String displayTextWithCommas(Long amount){
        StringBuilder amountWithCommas= new StringBuilder("");
        int counter=0;
        if(amount==0){
            return "0";
        }
        while (amount>0){
            if((counter%3==0)&&(counter!=0)){
                amountWithCommas=amountWithCommas.append(",");
            }
            counter++;
            amountWithCommas=amountWithCommas.append(amount%10);
            amount=amount/10;
        }
        return amountWithCommas.reverse().toString();
    }
    private void displayErrorAlert(String errorMessage){
      Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMessage + "\n");
        alert.getDialogPane().setExpanded(true);
        alert.setTitle("Error!");
        alert.showAndWait();
    }
    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }

    public void setSelectedBattleField(String selectedBattleField) {
        this.selectedBattleField = selectedBattleField;
    }

    private boolean registerAllies() throws IOException {
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
                System.out.println("registerAllies");
                mainWindowAlliesController.setMissionSize(Integer.valueOf(missionSizeTextField.getText()));
                /* threadTask=new AlliesThreadTask(mainWindowAlliesController.getAlliesContestController(),stringToConvert,Integer.parseInt(missionSizeTextField.getText()),alliesTeamName);
                 mainWindowAlliesController.setThreadTask(threadTask);
                System.out.println("before start");*/
                new Thread(threadTask).start();

            } else {
                if (response.code() == 409) {
                    alert.setContentText("The contest " + selectedBattleField + " is full, please select another one");
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();
                    return false;
                }
            }
      return true;
    }
    public TheMachineEngineDTO getTheMachineEngineInfo(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_MACHINE_INFO)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .addQueryParameter("battlefield", selectedContestDTO.getBattleFieldName())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
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
            } else {
                TheMachineEngineDTO theMachineEngineDTO = Constants.GSON_INSTANCE.fromJson(response.body().string(), TheMachineEngineDTO.class);
                return theMachineEngineDTO;
            }
        } catch (IOException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private void displayErrors(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
    public void disableReadyButton(){
        isContestSelected.setValue(true);
    }

}