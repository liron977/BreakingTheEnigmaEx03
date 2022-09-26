package component.mainWindowUBoat;

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



//        @FXML
//        private BruteForceTabController bruteForceTabPaneController;
//
//        @FXML
//        private ScrollPane encryptDecryptTab;
//
//        @FXML
//        private EncryptDecryptTabController encryptDecryptTabController;
        /*
            @FXML
            private ScrollPane mainScroller;*/
     /*   @FXML
        BorderPane mainBorder;*/
   /*     @FXML
        private TabPane bruteForceTabPane;*/

/*
        @FXML
        private TabPane  mainTabPane;

        @FXML
        private Tab encryptDecryptTabButton;
        @FXML
        private Tab machineTabButton;
        @FXML
        private Tab bruteForceTab;
*/

    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());

    }

       @FXML
        public void initialize() {
            if (machineTabController != null) {
                loadFileComponentController.setMainWindowUBoatController(this);
                machineTabController.setMainController(this);
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
                loadFileComponentController.setAnimationsBinding();
/*                encryptDecryptTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
                });
                bruteForceTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        bruteForceTabButtonOnListener();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                encryptDecryptTabButton.disableProperty().bind(machineTabController.getSetCodeConfigurationController().isCodeDefinedProperty().not());
                bruteForceTab.disableProperty().bind(machineTabController.getSetCodeConfigurationController().isCodeDefinedProperty().not());*/
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
    public void setMachineDetails(){
        machineTabController.setMachineDetails();
    }
        public void setBattleName(String battleName){

            machineTabController.setBattleName(battleName);
        }
//        public void bruteForceTabButtonOnListener() {
//            SimpleBooleanProperty isInitNeeded=machineTabController.getSetCodeConfigurationController().getIsInitNeeded();
//            if(isInitNeeded.getValue()) {
//                bruteForceTabPaneController.initBruteForceValues();
//                isInitNeeded.setValue(false);
//            }
//        }
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
    }