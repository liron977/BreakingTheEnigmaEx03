package component.uBoatContestTab;

import bruteForce.*;
import com.google.gson.Gson;
import component.login.UBoatLoginController;
import component.mainWindowUBoat.MainWindowUBoatController;
import component.uBoatMachineTab.machineTab.CurrentConfigurationTableViewController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import machineEngine.EngineManager;
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
import javafx.scene.input.MouseEvent;
import machineDTO.ConvertedStringProcessDTO;
import okhttp3.*;
import uiMediator.Mediator;
import constants.Constants;
import utils.EventsHandler;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.REFRESH_RATE;

public class UBoatContestTabController implements EventsHandler, Closeable {

    @FXML
    private ListView<String> listView;
    @FXML
    private Spinner<Integer> missionSizeSpinner;
    @FXML
    private TextField searchTextField;
    EngineManager engineManager;
    @FXML
    private TextArea stringToConvertTextArea;
    @FXML
    private TextArea convertedStringTextArea;
    // private BruteForceResultsTabController bruteForceResultsTabController;
    @FXML
    private CurrentConfigurationTableViewController currentCodeConfigurationController;

    @FXML
    private ScrollPane currentCodeConfiguration;
    @FXML
    private Button decryptButton;
    @FXML
    private Button readyButton;
    @FXML
    private TableView<AlliesDTO> activeTeamsDetailsTableView;
    @FXML
    private TableView<BruteForceResultDTO> contestCandidatesTableView;
    @FXML
    private TableColumn<AlliesDTO, String> missionSizeColumn;
    @FXML
    private TableColumn<AlliesDTO, String> agentsAmountColumn;
    @FXML
    private TableColumn<AlliesDTO, String> alliesTeamNameColumn;

    @FXML
    private TableColumn<BruteForceResultDTO, String> stringColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> alliesNameColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> codeConfigurationColumn;

    private Timer timer;

    /* @FXML
     private Label amountOfMissions;
   */  /*@FXML
    private Button resetButton;
    */
    @FXML
    private Label currentConfigurationLabel;
    String battleName;
    @FXML
    private TextArea currentConfigurationValueTextArea;
    @FXML
    private Slider amountOfAgentsSlider;
    @FXML
    private ComboBox<String> difficultyLevelComboBox;
    /*  @FXML
      private TextField sizeOfMission;
  */
    @FXML
    private List<String> dictionary;
    @FXML
    private MainWindowUBoatController mainWindowUBoatController;
    @FXML
    private Button logoutButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label contestStatusLabel;

    BruteForceSettingsDTO bruteForceSettingsDTO;
    Mediator mediator;

    private Alert alert;
    private boolean isStringToConvertIsLegal;
    private boolean isMissionSizeIsValid;
    private boolean isDifficultyLevelwasChosen;
    private boolean isConvertedStringIsLegal;
    public SimpleBooleanProperty isBruteForceSettingDefined;
    private List<EventsHandler> handlers = new ArrayList<>();
    ConvertedStringProcessDTO convertedStringProcessDTO;
    private IntegerProperty totalAlliesRegisteredTeamsAmount;
    private IntegerProperty totalBruteResultAmount;
    private TimerTask alliesRegisteredTeamsRefresher;
    private Timer startAlliesInfoTableViewTimer;
    private TimerTask BruteForceResultTableViewRefresher;
    private Timer BruteForceResultTableViewRefresherTimer;

    private TimerTask contestStatusRefresher;
    private IntegerProperty contestResultsInfoVersion;

    private  SimpleBooleanProperty autoUpdate;

    private SimpleBooleanProperty isContestEnded;
    private String alliesWinnerTeamName;
    private boolean isMessageDisplayedForFirstTime;
    public UBoatContestTabController() {
        alert = new Alert(Alert.AlertType.ERROR);
    }

    public void initialize() {
        //currentCodeConfigurationController.setUserInputBruteForceLogicTabController(this);
        addHandler(currentCodeConfigurationController);
        this.isStringToConvertIsLegal = true;
        this.isMissionSizeIsValid = true;
        this.isConvertedStringIsLegal = true;
        this.isBruteForceSettingDefined = new SimpleBooleanProperty(false);
        totalAlliesRegisteredTeamsAmount=new SimpleIntegerProperty(0);
        totalBruteResultAmount=new SimpleIntegerProperty(0);
        this.contestResultsInfoVersion =new SimpleIntegerProperty();
        isContestEnded=new SimpleBooleanProperty(false);
        alliesWinnerTeamName="";
        contestStatusLabel.setText("");
        isMessageDisplayedForFirstTime=false;
        logoutButton.setVisible(false);//todo remove from note
        this.dictionary=new ArrayList<>();
        autoUpdate=new SimpleBooleanProperty(true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchTextFieldOnListener();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
     /*   difficultyLevelComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->{
            updateAmountOfMissionOnActionListener(newValue,sizeOfMission.getText());
        });
        sizeOfMission.textProperty().addListener((observable, oldValue, newValue) -> {
            if(difficultyLevelComboBox.getSelectionModel().getSelectedItem()!=null) {
                updateAmountOfMissionOnActionListener(difficultyLevelComboBox.getSelectionModel().getSelectedItem(), newValue);
            }else{
                updateAmountOfMissionOnActionListener("Difficulty level", newValue);
            }
        });*/

    }

    /* public void setBruteForceResultsController(BruteForceResultsTabController bruteForceResultsTabController) {
         this.bruteForceResultsTabController = bruteForceResultsTabController;
     }*/
    @FXML
    void listViewClickOnItem(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if(selectedItem!=null) {
            int index = listView.getSelectionModel().getSelectedIndex();
            String currentString = stringToConvertTextArea.getText().toUpperCase();
            if (!currentString.equals("")) {
                currentString = currentString.concat(" ");
            }
            String updatedSting = currentString.concat(selectedItem);
            stringToConvertTextArea.setText(updatedSting);
            listView.getSelectionModel().clearSelection();
            searchTextField.clear();
        }
    }

    public BruteForceSettingsDTO getBruteForceSettingsDTO() {
        return bruteForceSettingsDTO;
    }

    @FXML
    void saveSettingsButtonOnActionListener(ActionEvent event) {
        if (isUserInputIsValid()) {
            isBruteForceSettingDefined.set(true);
            String convertedString = convertedStringTextArea.getText();
            int agentsAmount = (int) amountOfAgentsSlider.getValue();
            //int missionSize = Integer.parseInt(sizeOfMission.getText());
            String missionLevel = difficultyLevelComboBox.getValue();
            // bruteForceSettingsDTO = new BruteForceSettingsDTO(convertedString, agentsAmount, missionSize, missionLevel);
           /* bruteForceResultsTabController.setBruteForceSettingsDTO(bruteForceSettingsDTO);
            bruteForceResultsTabController.reset();
            bruteForceTabController.runTaskFromSuper();*/
        }
    }

    public void initDifficultyLevelComboBox() {
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList(
                new String("Low"),
                new String("Medium"),
                new String("High"),
                new String("Impossible")));
    }

    @FXML
    void searchTextFieldOnListener() throws Exception {
        int amountOfSignalToProcess = searchTextField.getText().length();
        if (amountOfSignalToProcess > 0) {
            listView.getItems().clear();
            listView.getItems().addAll(searchList(searchTextField.getText(), dictionary));
        } else {
            listView.getItems().addAll(dictionary);

        }
    }

    public void initValues() {
        //engineManager=mediator.getEngineManger().cloneEngineManager();
      /*  engineManager=mediator.getEngineManger();
        List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
        if(listOfExceptionsCode.size()==0) {
       */
        initUserInputBruteForceLogicTabController();

    }

    private void initListOfStringOfDictionary() {
        /*List<String> listOfStringOfDictionary = Arrays.asList(engineManager.getTheMachineEngine().getDictionary().getDictionary());
        this.dictionary = listOfStringOfDictionary;*/
        Gson gson = new Gson();
        String finalUrl = HttpUrl
                .parse(Constants.GET_DICTIONARY)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                try {
                    String[] dictionaryArray = Constants.GSON_INSTANCE.fromJson(response.body().string(), String[].class);
                    this.dictionary = Arrays.asList(dictionaryArray);

                } catch (IOException ignore) {
                }
            }
            else {
                this.dictionary=new ArrayList<>();
            }
        } catch (IOException e) {
        }
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
        this.currentCodeConfigurationController.setMediator(mediator);
    }

    /* public void setBruteForce(BruteForceTabController bruteForceTabController){
         this.bruteForceTabController = bruteForceTabController;
     }*/
    @FXML
    void convertedStringTextAreaOnListener(ActionEvent event) {

    }

    /* @FXML
     void sizeOfMissionOnActionListener(ActionEvent event) {
         if(!sizeOfMission.getText().equals("")&&isMissionSizeIsValid&&!difficultyLevelComboBox.getValue().equals("Difficulty level")){
             amountOfMissions.setText(String.valueOf(engineManager.setMaxAmountOfMissions(difficultyLevelComboBox.getValue(),Integer.parseInt(sizeOfMission.getText()))));

         }
     }*/
   /* void updateAmountOfMissionOnActionListener(String  comboNewValue, String  textNewValue) {
        boolean isOutputNeeded = false;
        isMissionSizeIsValid(isOutputNeeded);
        if (comboNewValue != null){
            if (!textNewValue.equals("") && isMissionSizeIsValid && !comboNewValue.equals("Difficulty level")) {
                long maxAmountOfMissions =(engineManager.setMaxAmountOfMissions(comboNewValue, Integer.parseInt(sizeOfMission.getText())));
                String maxAmountOfMissionsWithCommas=engineManager.displayMaxAmountOfMissionsWithCommas(maxAmountOfMissions);
                amountOfMissions.setText("Amount of missions: " + maxAmountOfMissionsWithCommas);
            } else {
                amountOfMissions.setText("");

            }
        }
        else

        {
            amountOfMissions.setText("");
        }

    }*/
    public void initUserInputBruteForceLogicTabController() {
        /*  engineManager=mediator.getEngineManger();*/
        initListOfStringOfDictionary();
        listView.getItems().clear();
        listView.getItems().addAll(dictionary);
        // initSlider();
        //initDifficultyLevelComboBox();
        //bruteForceResultsTabController.setMediator(mediator);
        // difficultyLevelComboBox.getSelectionModel().clearSelection();
        //  amountOfAgentsSlider.setValue(1);
        // sizeOfMission.setText("");
     /*   stringToConvertTextArea.setText("");
        convertedStringTextArea.setText("");*/
    }

    /*    @FXML
        void decryptButtonOnAction(ActionEvent event) throws Exception {
            String stringToConvert=stringToConvertTextArea.getText().toUpperCase();
            if(stringToConvert==null){
                displayErrorAlert("Please insert a string to convert");
            }
            else {
                String stringToConvertWithoutExcludedSignals = engineManager.getTheMachineEngine().getDictionary().removeExcludeCharsFromString(stringToConvert).toUpperCase();
                String stringWithoutLegalSignals = engineManager.isStringIncludeIllegalSignal(stringToConvertWithoutExcludedSignals).toUpperCase();
                if (stringWithoutLegalSignals.length() != 0) {
                    isStringToConvertIsLegal = false;
                    isBruteForceSettingDefined.set(false);
                    String msg = "There are illegal chars  in the string: ".concat(stringToConvertWithoutExcludedSignals);
                    displayErrorAlert(msg);
                } else {
                    isStringToConvertLegal(stringToConvertWithoutExcludedSignals);
                    if (isStringToConvertIsLegal) {
                        stringToConvertTextArea.setText(stringToConvertWithoutExcludedSignals);
                        PlugsBoard plugsBoard=engineManager.getTheMachineEngine().getPlugsBoard();
                        List<Pair> listOfPairsOfSwappingCharacter=new ArrayList<>();
                        if(plugsBoard!=null) {
                            listOfPairsOfSwappingCharacter = engineManager.getTheMachineEngine().getPlugsBoard().getPairsOfSwappingCharacter();
                            engineManager.getTheMachineEngine().initEmptyPlugBoard();
                        }
                        String convertedString = engineManager.getConvertedString(stringToConvertWithoutExcludedSignals).getConvertedString().toUpperCase();
                        convertedStringTextArea.setText(convertedString);
                        if(plugsBoard!=null) {
                            engineManager.getTheMachineEngine().getPlugsBoard().setPairsOfSwappingCharacter(listOfPairsOfSwappingCharacter);
                        }
                        isStringToConvertIsLegal = true;
                        fireEvent();
                    }
                }
            }
        }*/
    public void isStringToConvertLegal(String stringToConvert) {
        String[] stringToConvertArray = stringToConvert.split(" ");
        String IllegalWords = "";
        isStringToConvertIsLegal = true;
        // boolean isStringToConvertLegal = true;
        for (String str : stringToConvertArray) {
            if (!engineManager.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(str)) {
                if (isStringToConvertIsLegal) {
                    IllegalWords = str;
                } else {
                    IllegalWords = (IllegalWords.concat(", ")).concat(str);
                }
                isStringToConvertIsLegal = false;
            }
        }
        if (!isStringToConvertIsLegal) {
            isBruteForceSettingDefined.set(false);
            this.isStringToConvertIsLegal = false;
            displayErrorAlert("Please enter legal word from the dictionary \nThe illegal words: " + IllegalWords);
        }
    }

    @FXML
    void stringToConvertTextFieldOnAction(ActionEvent event) {

    }

    public void setCurrentConfigurationLabel() {
        engineManager.createCurrentCodeDescriptionDTO();
        String currentConfiguration = engineManager.getCurrentCodeDescription();
        currentConfigurationLabel.setText("The current code configuration is:");
    }

    public Label getCurrentConfigurationLabel() {
        return currentConfigurationLabel;
    }

    public TextArea getCurrentConfigurationValueTextArea() {
        return currentConfigurationValueTextArea;
    }

    public void initSlider() {
        amountOfAgentsSlider.minProperty().setValue(1);
        amountOfAgentsSlider.maxProperty().setValue(mediator.getEngineManger().getAgents().getAmountOfAgents());
        //newAmountOfThreadsProperty=new SimpleIntegerProperty(maxThreadsAmount);
    }

    /*private void isMissionSizeIsValid(boolean isOutputNeeded){
       // String missionSizeString=sizeOfMission.getText();
        Long amountOfPossibleStartingPositionList = mediator.getEngineManger().getAmountOfPossibleStartingPositionList();
        if(missionSizeString.equals("")){
            isMissionSizeIsValid=false;
            // isBruteForceSettingDefined.set(false);
            if(isOutputNeeded) {
                displayErrorAlert("Please insert a mission size");
            }
        }
        else {
            if(missionSizeString.charAt(0)=='0'){
                displayErrorAlert("Please enter only positive numbers");
                isMissionSizeIsValid=false;
            }
            else {
                if ((!isNumeric(missionSizeString)) && (isOutputNeeded)) {
                    displayErrorAlert("Please enter only numbers in the mission size field");
                } else {
                    try {
                        long missionSize = Long.valueOf(missionSizeString);
                        if ((missionSize < 0) || (missionSize > amountOfPossibleStartingPositionList)) {
                            isMissionSizeIsValid = false;
                            if (isOutputNeeded) {
                                String msg = "Please enter a mission size between 1-" + engineManager.displayMaxAmountOfMissionsWithCommas((Long.valueOf(amountOfPossibleStartingPositionList)));
                                displayErrorAlert(msg);
                            }
                            isBruteForceSettingDefined.set(false);
                        } else {
                            isMissionSizeIsValid = true;
                        }
                    } catch (Exception e) {
                        String msg = "Please enter a mission size between 1-" + engineManager.displayMaxAmountOfMissionsWithCommas((Long.valueOf(amountOfPossibleStartingPositionList)));
                        isMissionSizeIsValid = false;
                        isBruteForceSettingDefined.set(false);
                        displayErrorAlert(msg);
                    }
                }
            }
        }
    }*/
    private void isDifficultyLevelwasChosen() {
        if (difficultyLevelComboBox.getValue() == null) {
            isDifficultyLevelwasChosen = false;
            isBruteForceSettingDefined.set(false);
            displayErrorAlert("Please select difficulty level");
        } else {
            isDifficultyLevelwasChosen = true;
        }
    }

    private boolean isUserInputIsValid() {
        boolean isOutputNeeded = true;
        isDifficultyLevelwasChosen();
        //isMissionSizeIsValid(isOutputNeeded);
        isStringTOConvertFieldEmpty();
        isConvertedStringFieldEmpty();

        if ((isConvertedStringIsLegal) && (isStringToConvertIsLegal) && (isMissionSizeIsValid) && (isDifficultyLevelwasChosen)) {
            return true;
        }
        return false;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private void isStringTOConvertFieldEmpty() {
        if (stringToConvertTextArea.getText().equals("")) {
            isStringToConvertIsLegal = false;
            isBruteForceSettingDefined.set(false);
            displayErrorAlert("Please insert a string to convert");
        } else {
            isStringToConvertIsLegal = true;

        }
    }

    private void isConvertedStringFieldEmpty() {
        if (convertedStringTextArea.getText().equals("")) {
            isConvertedStringIsLegal = false;
            isBruteForceSettingDefined.set(false);
            displayErrorAlert("Please decrypt the message");
        } else {
            isConvertedStringIsLegal = true;

        }
    }

    private void displayErrorAlert(String errorMessage) {
        alert.setContentText(errorMessage + "\n");
        alert.getDialogPane().setExpanded(true);
        alert.setTitle("Error!");
        alert.showAndWait();
    }

    public void addHandler(EventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    /*    public void initDisplayConfiguration() throws Exception {
            List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
            if(listOfExceptionsCode.size()==0) {
                currentCodeConfigurationController.setCurrentCodeConfiguration();
            }
        }*/
    public void initDisplayConfiguration() throws Exception {

        currentCodeConfigurationController.setCurrentCodeConfiguration();
    }

    private void fireEvent() throws Exception {
        EventObject myEvent = new EventObject(this);
        List<EventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (EventsHandler handler : handlers) {
            handler.eventHappened(myEvent);
        }
    }

    @Override
    public void eventHappened(EventObject event) throws Exception {
        initDisplayConfiguration();
    }

    public void setMainController(MainWindowUBoatController mainWindowUBoatController) {
        this.mainWindowUBoatController = mainWindowUBoatController;
    }

    public void setBattleName(String battleName) {
        currentCodeConfigurationController.setBattleName(battleName.trim());
        this.battleName = battleName.trim();
    }

    @FXML
    void decryptButtonOnAction(ActionEvent event) throws Exception {
        String stringToConvert = stringToConvertTextArea.getText();
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("stringToConvert", stringToConvert)
                        .build();
        String finalUrl = HttpUrl
                .parse(Constants.ENCRYPT)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
                /*Platform.runLater(() -> {
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
            } else {
                try {
                    ConvertedStringProcessDTO convertedStringProcessDTOFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), ConvertedStringProcessDTO.class);
                    convertedStringProcessDTO = convertedStringProcessDTOFromGson;
                } catch (IOException ignore) {
                }
                if (convertedStringProcessDTO.getExceptionList().size()==0) {
                    Platform.runLater(() -> {
                        {
                            mainWindowUBoatController.setDisableUBoatMachineTabButton(true);
                            stringToConvertTextArea.setText(convertedStringProcessDTO.getStringToConvertWithoutExcludedSignals());
                            convertedStringTextArea.setText(convertedStringProcessDTO.getConvertedString());
                            currentCodeConfigurationController.setCurrentCodeConfiguration();
                        }
                    });
                } else {
                    displayExceptions(convertedStringProcessDTO.getExceptionList());
                }
            }

        } catch (IOException e) {
        }

    }
    private void displayExceptions(List<String> exceptionList){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String message="";
        for (String exception:exceptionList) {
            message=message.concat(exception);
            message=message.concat("\n");
        }
        alert.setContentText(message);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
    @FXML
    void readyButtonOnAction(ActionEvent event) throws Exception {
        startContestStatusRefresher();
        logoutButton.setVisible(false);
        if(convertedStringTextArea.getText().isEmpty()){
            List<String> error=new ArrayList<>();
            error.add("Please decrypt message");
            displayExceptions(error);
        }else {
            String stringToConvert = convertedStringTextArea.getText();
            String stringToConvertGson = Constants.GSON_INSTANCE.toJson(stringToConvert);
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), stringToConvertGson);
            String finalUrl = HttpUrl
                    .parse(Constants.UBOATS_CONTESTS_SETTINGS)
                    .newBuilder()
                    .addQueryParameter("battlefield", battleName.trim())
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();
            Call call = HttpClientUtil.getOkHttpClient().newCall(request);
            try {
                Response response = call.execute();
                if (response.code() != 200) {
              /*  Platform.runLater(() -> {
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
                } else {
                    startContestTableViewRefresher();
                    readyButton.setDisable(true);
                 // mainWindowUBoatController.setDisableUBoatMachineTabButton(true);

                    Platform.runLater(() -> {
                        {
                          /*  stringToConvertTextArea.setText(convertedStringProcessDTO.getStringToConvertWithoutExcludedSignals());
                            convertedStringTextArea.setText(convertedStringProcessDTO.getConvertedString());
                            currentCodeConfigurationController.setCurrentCodeConfiguration();*/
                        }
                    });
                }

            } catch (IOException e) {
            }
        }

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


    private void updateRegisteredAlliesInfoList(List<AlliesDTO> alliesInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AlliesDTO> alliesDTOObservableList =getTeamsAgentsDataTableViewDTOList(alliesInfoDTOList);
            createAlliesInfoDTOTableView(alliesDTOObservableList);
            totalAlliesRegisteredTeamsAmount.set(alliesInfoDTOList.size());
        });
    }
    private void createAlliesInfoDTOTableView(ObservableList<AlliesDTO> alliesInfoDTOList ) {
        activeTeamsDetailsTableView.setItems(alliesInfoDTOList);
        activeTeamsDetailsTableView.getColumns().clear();
        activeTeamsDetailsTableView.getColumns().addAll(alliesTeamNameColumn, agentsAmountColumn, missionSizeColumn);
    }
    public void startAlliesInfoTableViewRefresher() {
        alliesRegisteredTeamsRefresher = new AlliesRegisteredTeamsInfoTablesViewRefresher(
                this::clearTableViewValues,
                this::updateRegisteredAlliesInfoList,
                autoUpdate,
                battleName.trim());
        startAlliesInfoTableViewTimer = new Timer();
        startAlliesInfoTableViewTimer.schedule(alliesRegisteredTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void clearTableViewValues(Integer i){
      /* if(!activeTeamsDetailsTableView.getItems().isEmpty()){*/
           activeTeamsDetailsTableView.getItems().clear();
      /* }*/

    }
    private void updateBruteForceResultsTableView(BruteForceResultAndVersion bruteForceResultAndVersionWithVersion) {
        if (bruteForceResultAndVersionWithVersion != null) {
            if (bruteForceResultAndVersionWithVersion.getVersion() != contestResultsInfoVersion.get()) {
                contestResultsInfoVersion.set(bruteForceResultAndVersionWithVersion.getVersion());
                Platform.runLater(() -> {
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
    public void startContestTableViewRefresher() {
        BruteForceResultTableViewRefresher = new UboatBruteForceResultTableViewRefresher(
                battleName.trim(),
                contestResultsInfoVersion,
                autoUpdate,
                this::updateBruteForceResultsTableView);
        BruteForceResultTableViewRefresherTimer= new Timer();
        BruteForceResultTableViewRefresherTimer.schedule(BruteForceResultTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void startContestStatusRefresher() {
        contestStatusRefresher = new ContestStatusRefresher("UBoat",battleName.trim(),
                this::updateContestStatus,autoUpdate,"","");
        timer = new Timer();
        timer.schedule(contestStatusRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void updateContestStatus(ContestStatusInfoDTO contestStatusInfoDTO) {

        if (!isContestEnded.getValue()) {
            Platform.runLater(() -> {
                this.isContestEnded.setValue(contestStatusInfoDTO.isContestEnded());
                this.alliesWinnerTeamName = contestStatusInfoDTO.getAlliesWinnerTeamName();
                if(contestStatusInfoDTO.getContestStatus().equals("Active")){
                    contestStatusLabel.setText("The contest started!");
                }
                // this.alliesWinnerTeamName = contestStatusInfoDTO.getAlliesWinnerTeamName();
                if (isContestEnded.getValue()&&!isMessageDisplayedForFirstTime) {
                    isMessageDisplayedForFirstTime=true;
                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.NONE);
                    String message = "The contest ended" + "\n" + "The winning team is " + alliesWinnerTeamName;
                    alert.setContentText(message);
                    alert.setTitle("UBoat");
                    alert.getDialogPane().setExpanded(true);
                   // alert.showAndWait();
                    //activeTeamsDetailsTableView.getItems().clear();

                   // readyButton.setDisable(false);
                   // mainWindowUBoatController.setDisableUBoatMachineTabButton(false);
                    //logoutButton.setVisible(true);
                    contestStatusLabel.setText("");
                    Optional<ButtonType> result=alert.showAndWait();
                    if(result.get()==ButtonType.OK) {
                        clearContestValues();
                        mainWindowUBoatController.setDisableUBoatMachineTabButton(false);
                        readyButton.setDisable(false);
                        logoutButton.setVisible(true);

                        try {
                            close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                   // mainWindowUBoatController.setDisableUBoatMachineTabButton(false);
                    //mainWindowUBoatController.changeToMachineTab();
                }});
        }
    }
    public void clearContestValues(){

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), "");
            String finalUrl = HttpUrl
                    .parse(Constants.CLEAR_CONTEST_UBOAT)
                    .newBuilder()
                    .addQueryParameter("battlefield", battleName.trim())
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();
            Call call = HttpClientUtil.getOkHttpClient().newCall(request);
            try {
                Response response = call.execute();
                if (response.code() != 200) {
               /* Platform.runLater(() -> {
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
                } else {

                }
            } catch (IOException e) {
            }


    }
    @FXML
    void logoutButtonOnAction(ActionEvent event){
        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_LOGOUT)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                 Platform.runLater(() -> {{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Logout Successfully");
                        alert.getDialogPane().setExpanded(true);
                        alert.showAndWait();

                      mainWindowUBoatController.getPrimaryStage().close();
                     try {
                         openLoginForm();
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }


                       /* mainWindowUBoatController.initLoadFileValues();
                        mainWindowUBoatController.initCodeConfiguration();
                        mainWindowUBoatController.initMachineDetails();
                     mainWindowUBoatController.changeToMachineTab();*/

                    }
                 });

            } else{
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
        } catch (IOException e) {
        }
    }
    public void openLoginForm() throws IOException {
        Stage primaryStage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/component/login/Login.fxml");
        //URL url = getClass().getResource("/component/mainWindowUBoat/MainWindowUBoat.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        UBoatLoginController uBoatLoginController = fxmlLoader.getController();
        //uBoatLoginController.setMediator(mediator);
        uBoatLoginController.setPrimaryStageAndLoadInTheBackgroundSuperScreen(primaryStage);
        primaryStage.setTitle("UBoat");
        primaryStage.getIcons().add(new Image("/Resources/uboatIcon.jpg"));
        Scene scene = new Scene(root);
        primaryStage.setMinHeight(300f);
        primaryStage.setMinWidth(400f);
        scene.getStylesheets().add(getClass().getResource("/utils/CSS/BlueStyle.css").toExternalForm());
        scene.getStylesheets().add("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    void resetButtonOnAction(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.RESET_CODE_CONFIGURATION)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                currentCodeConfigurationController.setCurrentCodeConfiguration();
            }
            else {
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
        } catch (IOException e) {
        }
    }
    @Override
    public void close() throws IOException {
        activeTeamsDetailsTableView.getItems().clear();
        contestCandidatesTableView.getItems().clear();
        totalAlliesRegisteredTeamsAmount.set(0);
        contestResultsInfoVersion.set(0);
        convertedStringTextArea.clear();
        stringToConvertTextArea.clear();
        isMessageDisplayedForFirstTime=false;
        isContestEnded.set(false);
        readyButton.setDisable(false);
       // mainWindowUBoatController.setDisableUBoatMachineTabButton(false);
        if (BruteForceResultTableViewRefresher!=null&&contestStatusRefresher!=null&&alliesRegisteredTeamsRefresher != null && timer!= null && BruteForceResultTableViewRefresherTimer!= null) {
           /* alliesRegisteredTeamsRefresher.cancel();
            alliesRegisteredTeamsRefresher.cancel();*/
            BruteForceResultTableViewRefresher.cancel();
           contestStatusRefresher.cancel();
            BruteForceResultTableViewRefresherTimer.cancel();
           /* startAlliesInfoTableViewTimer.cancel();*/
            timer.cancel();

        }
    }
}