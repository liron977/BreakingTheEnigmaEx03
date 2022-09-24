package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import mediator.Mediator;

import java.util.List;

public class EncryptDecryptTabController {

    public Mediator mediator;

    @FXML
    private BorderPane border;
    @FXML
    private AnchorPane firstAnchorPane;
    @FXML
  private ScrollPane encryptScrollerPane;

   /* @FXML
    private ScrollPane displayCodeConfiguration;*/

    @FXML
    private ScrollPane keyboard;
   @FXML
    KeyboardController keyboardController;
 /*   @FXML
    private DisplayCodeConfigurationController displayCodeConfigurationController;*/

    @FXML
    private Pane encryptDecrypt;
    @FXML
    private ScrollPane firstScrollerPane;
    @FXML
    private GridPane grid;
    @FXML
    private HBox decryptHbox;
    @FXML
    private EncryptDecryptController encryptDecryptController;
    @FXML
    private StatisticsController statisticsController;
    @FXML
    private ScrollPane statistics;
    /*@FXML
    private AnchorPane fileLoadAnchorPane;*/
 /*   @FXML
    private AnchorPane loadFileComponent;*/
    //@FXML
    //private LoadFileController loadFileComponentController;
    @FXML
    private MachineTabController machineTabController;
    @FXML
    private MainWindowController mainWindowController;
    @FXML
    private  VBox codeConfigurationVbox;
    @FXML
    ConfigurationDisplayController codeConfigurationVboxController;
    @FXML
    private UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    @FXML
    public void initialize() throws Exception {
        if (((encryptDecryptController != null) && (codeConfigurationVboxController != null) &&(statisticsController != null))) {
            codeConfigurationVboxController.setEncryptDecryptWindowController(this);
            encryptDecryptController.setEncryptDecryptWindowController(this);
            statisticsController.setEncryptDecryptWindowController(this);
            keyboardController.setEncryptDecryptWindowController(this);
            encryptDecryptController.addHandler(statisticsController);
            encryptDecryptController.addHandler(codeConfigurationVboxController);
encryptDecryptController.getModeToggleGroup().selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
                if (!encryptDecryptController.getProcessOrDoneButton().equals("Done")) {
                    keyboardController.unDisableKeyboard();
                } else {
                    keyboardController.disableKeyboard();
                }
            });
        }
    }
    public SimpleBooleanProperty getIsEnableAnimationsPropertyFromEncryptController(){
        return encryptDecryptController.getIsEnableAnimationsProperty();
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    private void setStatistics(){
        statisticsController.displayHistory();
    }

    public ScrollPane getFirstScrollerPane() {
        return firstScrollerPane;
    }

    public AnchorPane getFirstAnchorPane() {
        return firstAnchorPane;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    public void setMachineTabController(MachineTabController machineTabController) {
        this.machineTabController = machineTabController;
    }
    public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabControllerHandler) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabControllerHandler;
    }
    public void addMachineHandler(){
        encryptDecryptController.addHandler(machineTabController);
    }
    public void addUserInputBruteForceLogicTabControllerHandler(){
        encryptDecryptController.addHandler(userInputBruteForceLogicTabController);
    }
    public void setMediator(Mediator mediator) throws Exception{
        this.mediator = mediator;
        encryptDecryptController.setMediator(mediator);
         statisticsController.setMediator(mediator);
        keyboardController.setMediator(mediator);
        codeConfigurationVboxController.setMediator(mediator);

    }
    void changeButtonColorByString(String str){
        keyboardController.changeButtonColorByString(str);
    }
    public void setMainController(MachineTabController mainController) {
        this.machineTabController = mainController;
    }

    public EncryptDecryptController getEncryptDecryptController() {
        return encryptDecryptController;
    }

    public void initDisplayConfiguration() throws Exception {
        List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
        if(listOfExceptionsCode.size()==0) {
          codeConfigurationVboxController.initConfigurations();
        }
    }
    public StatisticsController getStatisticsController(){
        return statisticsController;
    }
}