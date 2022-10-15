package component.mainWindowUBoat;

import component.uBoatContestTab.UBoatContestTabController;
import component.uBoatMachineTab.UBoatMachineTabController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import machineDTO.TheMachineSettingsDTO;
import uiMediator.Mediator;
import utils.EventsHandler;

import java.util.EventObject;

public class MainWindowUBoatController implements EventsHandler {
    private Stage primaryStage;
        public Mediator mediator;
        @FXML
        private ScrollPane machineTab;
        @FXML
        private ScrollPane contestTab;
        @FXML
        private UBoatContestTabController contestTabController;
        @FXML
        private UBoatMachineTabController machineTabController;
        @FXML
        private AnchorPane loadFileComponent;
        @FXML
        private LoadFileController loadFileComponentController;
        @FXML
        private TabPane  uBoatMainTabPane;
        @FXML
        private Tab uBoatMachineTabButton;
         @FXML
         private Tab uBoatContestButton;
         private SimpleBooleanProperty isMachineDefined;

    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
    }
    public void initLoadFileValues(){
        loadFileComponentController.initValues();
    }
    public void initMachineDetails(){
        machineTabController.initMachineDetails();
    }   public void initCodeConfiguration(){
        machineTabController.initCodeConfiguration();
    }
    public SimpleBooleanProperty getIsMachineDefined(){
        return machineTabController.getIsMachineDefined();
    }
    public SimpleBooleanProperty getIsCodeDefined(){
        return machineTabController.getIsCodeDefined();
    }
       @FXML
        public void initialize() {
            if ((machineTabController != null)&&(contestTabController != null)){
                loadFileComponentController.setMainWindowUBoatController(this);
                machineTabController.setMainController(this);
                contestTabController.setMainController(this);
                isMachineDefined=new SimpleBooleanProperty(false);
               /* encryptDecryptTabController.setMainWindowController(this);
                encryptDecryptTabController.setMachineTabController(machineTabController);
                encryptDecryptTabController.addMachineHandler();
                bruteForceTabPaneController.getUserInputBruteForceLogicController().addHandler(machineTabController);
                encryptDecryptTabController.setUserInputBruteForceLogicTabController(bruteForceTabPaneController.getUserInputBruteForceLogicController());
                encryptDecryptTabController.addUserInputBruteForceLogicTabControllerHandler();
                bruteForceTabPaneController.setMainController(this);*/
                loadFileComponent.setMaxWidth(Double.MAX_VALUE);
               // machineTabController.getSetCodeConfigurationController().addHandler(bruteForceTabPaneController);
               // loadFileComponentController.addHandler(machineTabController.getSetCodeConfigurationController());
                loadFileComponentController.addHandler(machineTabController.getMachineDetailsController());
                loadFileComponentController.addHandler(this);
/*                loadFileComponentController.addHandler(machineTabController.getCodeConfigurationController().getOriginalCodeConfigurationGridPaneController());
                loadFileComponentController.addHandler(machineTabController.getCodeConfigurationController().getCodeConfigurationGridPaneController());*/
              /*  loadFileComponentController.addHandler(machineTabController.getCodeConfigurationController().getOriginalCodeConfigurationGridPaneController());
                loadFileComponentController.addHandler(machineTabController.getCodeConfigurationController().getCodeConfigurationGridPaneController());
                loadFileComponentController.addHandler(encryptDecryptTabController.getKeyboardController());
                loadFileComponentController.setEncryptDecryptWindowController(encryptDecryptTabController);*/
                //loadFileComponentController.setAnimationsBinding();
   /*           encryptDecryptTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        encryptDecryptTabButtonSelectionListener();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                machineTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        machineTabButtonSelectionListener();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }); */
               /* encryptDecryptTabButton.disableProperty().bind(machineTabController.getSetCodeConfigurationController().isCodeDefinedProperty().not());
              bruteForceTab.disableProperty().bind(machineTabController.getSetCodeConfigurationController().isCodeDefinedProperty().not());*/
                uBoatContestButton.disableProperty().bind(getIsCodeDefined().not());
                uBoatContestButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        uBoatContestButtonOnListener();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                uBoatMachineTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        setMachineDetails();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
public void setTheMachineSettingsDTO(TheMachineSettingsDTO theMachineSettingsDTO){
        machineTabController.setTheMachineSettingsDTO(theMachineSettingsDTO);
}
  /*      @FXML
        void encryptDecryptTabButtonSelectionListener() throws Exception {
            encryptDecryptTabController.initDisplayConfiguration();
        }*/
 /*       void machineTabButtonSelectionListener() throws Exception {
            machineTabController.initDisplayConfiguration();
            machineTabController.setMachineValues();
        }*/
        public void setMediator(Mediator mediator) throws Exception {
           // this.mediator = mediator;
           // machineTabController.setMediator(mediator);
           // encryptDecryptTabController.setMediator(mediator);
           // bruteForceTabPaneController.setMediator(mediator);
          //  loadFileComponentController.setMediator(mediator);
        }
        public void setUserName(String uBoatUserName){

            loadFileComponentController.setUserName(uBoatUserName);
        }
        public void setCodeCalibration(){
            machineTabController.getSetCodeConfigurationController().setCodeCalibration();
        }
    public void setMachineDetails(){
            if(loadFileComponentController.getIsMachineDefinedProperty().getValue()) {
                machineTabController.setMachineDetails();
                isMachineDefined.setValue(true);
            }
    }
        public void setBattleName(String battleName){
            contestTabController.setBattleName(battleName);
            machineTabController.setBattleName(battleName);
        }
//        public void bruteForceTabButtonOnListener() {
//            SimpleBooleanProperty isInitNeeded=machineTabController.getSetCodeConfigurationController().getIsInitNeeded();
//            if(isInitNeeded.getValue()) {
//                bruteForceTabPaneController.initBruteForceValues();
//                isInitNeeded.setValue(false);
//            }
//        }

    public void uBoatContestButtonOnListener() throws Exception {
            contestTabController.initDisplayConfiguration();
            contestTabController.initValues();
            updateAlliesTableView();

    }

    @Override
        public void eventHappened(EventObject event) throws Exception {
          /*  bruteForceTabPaneController.getUserInputBruteForceLogicController().isBruteForceSettingDefined.setValue(false);
            bruteForceTabPaneController.getUserInputBruteForceLogicController().initUserInputBruteForceLogicTabController();
            bruteForceTabPaneController.getBruteForceTabPane().getSelectionModel().select(bruteForceTabPaneController.getBruteForceSettingsTabButton());
            bruteForceTabPaneController.getBruteForceResultsTabController().reset();
            bruteForceTabPaneController.getBruteForceResultsTabController().cancelMissions();*/
           // machineTabController.getSetCodeConfigurationController().isCodeDefinedProperty().set(false);
            uBoatMainTabPane.getSelectionModel().select(uBoatMachineTabButton);
        }
   /*     public BruteForceResultsTabController getBruteForceResultsTabController(){
            return bruteForceTabPaneController.getBruteForceResultsTabController();
        }*/
public void changeToMachineTab(){
    uBoatMainTabPane.getSelectionModel().select(uBoatMachineTabButton);

}
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void updateAlliesTableView(){
        contestTabController.startAlliesInfoTableViewRefresher();
        contestTabController.startContestTableViewRefresher();
       // contestTabController.startContestStatusRefresher();
    }
    }