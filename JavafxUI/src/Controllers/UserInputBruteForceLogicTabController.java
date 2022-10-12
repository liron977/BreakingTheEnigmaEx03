package Controllers;

import bruteForce.BruteForceSettingsDTO;
import engine.theEnigmaEngine.Pair;
import engine.theEnigmaEngine.PlugsBoard;
import machineEngine.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import mediator.Mediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;

public class UserInputBruteForceLogicTabController implements EventsHandler{

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
    private BruteForceResultsTabController bruteForceResultsTabController;
    @FXML
    private CurrentConfigurationTableViewController currentCodeConfigurationController ;
    @FXML
    private ScrollPane currentCodeConfiguration;
    @FXML
    private Button decryptButton;
    @FXML
    private Label amountOfMissions;
    @FXML
    private Button resetButton;
    @FXML
    private Label currentConfigurationLabel;
    @FXML
    private TextArea currentConfigurationValueTextArea;
    @FXML
    private Slider amountOfAgentsSlider;
    @FXML
    private ComboBox<String> difficultyLevelComboBox;
    @FXML
    private TextField sizeOfMission;
    @FXML
    private List<String> dictionary;
    @FXML
    private Button saveSettingsButton;
 /*   @FXML
    private DisplayCodeConfigurationController displayCodeConfigurationController;
    @FXML
    private ScrollPane displayCodeConfiguration;*/
    @FXML
    BruteForceTabController bruteForceTabController;

    BruteForceSettingsDTO bruteForceSettingsDTO;
    Mediator mediator;

    private Alert alert;
    private boolean isStringToConvertIsLegal;
    private boolean isMissionSizeIsValid;
    private boolean isDifficultyLevelwasChosen;
    private boolean isConvertedStringIsLegal;
    public SimpleBooleanProperty isBruteForceSettingDefined;
    private List<EventsHandler> handlers=new ArrayList<>();

    public UserInputBruteForceLogicTabController (){
        alert = new Alert(Alert.AlertType.ERROR);
    }
    public void initialize() {
        currentCodeConfigurationController.setUserInputBruteForceLogicTabController(this);
        addHandler(currentCodeConfigurationController);
        this.isStringToConvertIsLegal=true;
        this.isMissionSizeIsValid=true;
        this.isConvertedStringIsLegal=true;
        this.isBruteForceSettingDefined=new SimpleBooleanProperty(false);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchTextFieldOnListener();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        difficultyLevelComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->{
            updateAmountOfMissionOnActionListener(newValue,sizeOfMission.getText());
        });
        sizeOfMission.textProperty().addListener((observable, oldValue, newValue) -> {
            if(difficultyLevelComboBox.getSelectionModel().getSelectedItem()!=null) {
                updateAmountOfMissionOnActionListener(difficultyLevelComboBox.getSelectionModel().getSelectedItem(), newValue);
            }else{
                updateAmountOfMissionOnActionListener("Difficulty level", newValue);
            }
        });

    }
    public void setBruteForceResultsController(BruteForceResultsTabController bruteForceResultsTabController) {
        this.bruteForceResultsTabController = bruteForceResultsTabController;
    }
    @FXML
    void listViewClickOnItem(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        String currentString = stringToConvertTextArea.getText().toUpperCase();
        if(!currentString.equals("")) {
            currentString = currentString.concat(" ");
        }
        String updatedSting = currentString.concat(selectedItem);
        stringToConvertTextArea.setText(updatedSting);
        listView.getSelectionModel().clearSelection();
        searchTextField.clear();
    }

    public BruteForceSettingsDTO getBruteForceSettingsDTO() {
        return bruteForceSettingsDTO;
    }

    @FXML
    void saveSettingsButtonOnActionListener(ActionEvent event) {
        if(isUserInputIsValid()) {
            isBruteForceSettingDefined.set(true);
            String convertedString = convertedStringTextArea.getText();
            int agentsAmount = (int) amountOfAgentsSlider.getValue();
            int missionSize = Integer.parseInt(sizeOfMission.getText());
            String missionLevel = difficultyLevelComboBox.getValue();
            bruteForceSettingsDTO = new BruteForceSettingsDTO(convertedString, agentsAmount, missionSize, missionLevel);
            bruteForceResultsTabController.setBruteForceSettingsDTO(bruteForceSettingsDTO);
           bruteForceResultsTabController.reset();
            bruteForceTabController.runTaskFromSuper();
        }
    }
public void initDifficultyLevelComboBox(){
    difficultyLevelComboBox.setItems(FXCollections.observableArrayList(
            new String("Low"),
            new String("Medium"),
            new String("High"),
            new String("Impossible")));
}
    @FXML
    void searchTextFieldOnListener() throws Exception {
        int amountOfSignalToProcess = searchTextField.getText().length();
        if (amountOfSignalToProcess >0) {
            listView.getItems().clear();
            listView.getItems().addAll(searchList(searchTextField.getText(), dictionary));
        }
        else{
            listView.getItems().addAll(dictionary);

        }
    }
    public void initValues() {
     //engineManager=mediator.getEngineManger().cloneEngineManager();
        engineManager=mediator.getEngineManger();
        List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
        if(listOfExceptionsCode.size()==0) {
            initUserInputBruteForceLogicTabController();
        }
    }
    private void initListOfStringOfDictionary() {
        List<String> listOfStringOfDictionary = Arrays.asList(engineManager.getTheMachineEngine().getDictionary().getDictionary());
        this.dictionary = listOfStringOfDictionary;
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
    public void setBruteForce(BruteForceTabController bruteForceTabController){
        this.bruteForceTabController = bruteForceTabController;
    }
 @FXML
 void convertedStringTextAreaOnListener(ActionEvent event) {

 }
 @FXML
 void sizeOfMissionOnActionListener(ActionEvent event) {
     if(!sizeOfMission.getText().equals("")&&isMissionSizeIsValid&&!difficultyLevelComboBox.getValue().equals("Difficulty level")){
         amountOfMissions.setText(String.valueOf(engineManager.maxAmountOfMissionscalculation(difficultyLevelComboBox.getValue(),Integer.parseInt(sizeOfMission.getText()))));

     }
 }
void updateAmountOfMissionOnActionListener(String  comboNewValue, String  textNewValue) {
    boolean isOutputNeeded = false;
    isMissionSizeIsValid(isOutputNeeded);
    if (comboNewValue != null){
        if (!textNewValue.equals("") && isMissionSizeIsValid && !comboNewValue.equals("Difficulty level")) {
            long maxAmountOfMissions =(engineManager.maxAmountOfMissionscalculation(comboNewValue, Integer.parseInt(sizeOfMission.getText())));
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

    }
 public void initUserInputBruteForceLogicTabController(){
     engineManager=mediator.getEngineManger();
     initListOfStringOfDictionary();
     listView.getItems().clear();
     listView.getItems().addAll(dictionary);
     initSlider();
     initDifficultyLevelComboBox();
     bruteForceResultsTabController.setMediator(mediator);
     difficultyLevelComboBox.getSelectionModel().clearSelection();
     amountOfAgentsSlider.setValue(1);
     sizeOfMission.setText("");
     stringToConvertTextArea.setText("");
     convertedStringTextArea.setText("");
 }
   @FXML
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
    }
    public void isStringToConvertLegal(String stringToConvert) {
        String[] stringToConvertArray = stringToConvert.split(" ");
        String IllegalWords = "";
        isStringToConvertIsLegal=true;
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
        if(!isStringToConvertIsLegal) {
            isBruteForceSettingDefined.set(false);
            this.isStringToConvertIsLegal=false;
            displayErrorAlert("Please enter legal word from the dictionary \nThe illegal words: "+IllegalWords);
        }
    }

    @FXML
    void stringToConvertTextFieldOnAction(ActionEvent event) {

    }
    @FXML
    void resetButtonOnAction(ActionEvent event) {
        engineManager.resetCurrentCode();
        try {
            fireEvent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCurrentConfigurationLabel() {
        engineManager.createCurrentCodeDescriptionDTO();
        String currentConfiguration=engineManager.getCurrentCodeDescription();
        currentConfigurationLabel.setText("The current code configuration is:");
    }
    public Label getCurrentConfigurationLabel() {
        return currentConfigurationLabel;
    }
    public TextArea getCurrentConfigurationValueTextArea() {
        return currentConfigurationValueTextArea;
    }
    public void initSlider(){
        amountOfAgentsSlider.minProperty().setValue(1);
        amountOfAgentsSlider.maxProperty().setValue(mediator.getEngineManger().getAgents().getAmountOfAgents());
        //newAmountOfThreadsProperty=new SimpleIntegerProperty(maxThreadsAmount);
    }
    private void isMissionSizeIsValid(boolean isOutputNeeded){
     String missionSizeString=sizeOfMission.getText();
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
    }
    private void isDifficultyLevelwasChosen(){
     if(difficultyLevelComboBox.getValue()==null){
         isDifficultyLevelwasChosen=false;
         isBruteForceSettingDefined.set(false);
         displayErrorAlert("Please select difficulty level");
     }
     else {
         isDifficultyLevelwasChosen=true;
     }
    }
    private boolean isUserInputIsValid(){
        boolean isOutputNeeded=true;
        isDifficultyLevelwasChosen();
        isMissionSizeIsValid(isOutputNeeded);
        isStringTOConvertFieldEmpty();
        isConvertedStringFieldEmpty();

     if ((isConvertedStringIsLegal)&&(isStringToConvertIsLegal)&&(isMissionSizeIsValid)&&(isDifficultyLevelwasChosen)){
         return true;
     }
     return false;
    }
    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }
    private void isStringTOConvertFieldEmpty(){
     if(stringToConvertTextArea.getText().equals("")){
         isStringToConvertIsLegal=false;
         isBruteForceSettingDefined.set(false);
         displayErrorAlert("Please insert a string to convert");
     }
     else{
         isStringToConvertIsLegal=true;

     }
    }
    private void isConvertedStringFieldEmpty(){
        if(convertedStringTextArea.getText().equals("")){
            isConvertedStringIsLegal=false;
            isBruteForceSettingDefined.set(false);
            displayErrorAlert("Please decrypt the message");
        }
        else{
            isConvertedStringIsLegal=true;

        }
    }
    private void displayErrorAlert(String errorMessage){
        alert.setContentText(errorMessage + "\n");
        alert.getDialogPane().setExpanded(true);
        alert.setTitle("Error!");
        alert.showAndWait();
    }
    public void addHandler (EventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void initDisplayConfiguration() throws Exception {
        List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
        if(listOfExceptionsCode.size()==0) {
            currentCodeConfigurationController.setCurrentCodeConfiguration();
        }
    }
    private void fireEvent () throws Exception {
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
}