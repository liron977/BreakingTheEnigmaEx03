package component.uBoatMachineTab;

import component.mainWindowUBoat.LoadFileController;
import component.mainWindowUBoat.MainWindowUBoatController;
import component.uBoatMachineTab.machineTab.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import machineDTO.TheMachineSettingsDTO;
import uiMediator.Mediator;
import utils.EventsHandler;

import java.util.EventObject;
import java.util.List;

public class UBoatMachineTabController implements EventsHandler {
    public Mediator mediator;

    @FXML
    private HBox bottomHbox;
    @FXML
    private AnchorPane machineDetails;
    @FXML
    private SplitPane setCodeConfiguration;

    @FXML
    private ScrollPane statistics;
    @FXML
    private VBox codeConfiguration;
   /* @FXML
    private ConfigurationDisplayController codeConfigurationController;
*/
    @FXML
    private SetCodeConfigurationController setCodeConfigurationController;
    @FXML
    private BorderPane border;
    @FXML
    private ScrollPane firstScroller;
   /* @FXML
    private DisplayCodeConfigurationController displayCodeConfigurationController;*/

   /* @FXML
    private StatisticsController statisticsController;
*/
    @FXML
    private EncryptDecryptController encryptDecryptController;
    @FXML
    private MachineDetailsController machineDetailsController;

  /*  @FXML
    private EncryptDecryptTabController encryptDecryptTabController;
*/    @FXML
    private MainWindowUBoatController mainWindowUBoatController;
String battleName;

  /*  @FXML
    private BruteForceTabController bruteForceTabController;
*/

    public void setBattleName(String battleName){
        machineDetailsController.setBattleName(battleName);
        setCodeConfigurationController.setBattleName(battleName);
    }
public void setMachineDetails(){
    machineDetailsController.setMachineDetailsValues();
}
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;

        machineDetailsController.setMediator(mediator);
        setCodeConfigurationController.setUBoatMachineTabController(this);
        setCodeConfigurationController.setMediator(mediator);
        machineDetailsController.setUBoatMachineTabController(this);

        // displayCodeConfigurationController.setMediator(mediator);
      //  codeConfigurationController.setMediator(mediator);
        // bruteForceController.setMediator(mediator);
        // bruteForceController.initBruteForceValues();
        bottomHbox.setMaxWidth(Double.MAX_VALUE);
        machineDetails.setMaxWidth(Double.MAX_VALUE);
        border.setMaxWidth(Double.MAX_VALUE);
    }

    public SimpleBooleanProperty getIsMachineDefined() {
        return setCodeConfigurationController.getIsMachineDefined();
    }
    public Mediator getMediator() {
        return mediator;
    }

    @FXML
    public void initialize() {
        if (machineDetailsController!=null) {
            machineDetailsController.setUBoatMachineTabController(this);
            setCodeConfigurationController.setUBoatMachineTabController(this);

            //machineDetailsController.setMainController(this);
            //encryptDecryptWindowController.setMainController(this);
            //setCodeConfigurationController.setMainController(this);
            //displayCodeConfigurationController.setMainController(this);
            //loadFileComponentController.addHandler();
            //setCodeConfigurationController.addHandler(displayCodeConfigurationController);
           // codeConfigurationController.setuBoatMachineTabController(this);
            //setCodeConfigurationController.addHandler(codeConfigurationController.getCodeConfigurationGridPaneController());
            //setCodeConfigurationController.addHandler(codeConfigurationController.getOriginalCodeConfigurationGridPaneController());
       /*     border.prefHeightProperty().bind(firstScroller.heightProperty());
            border.prefWidthProperty().bind(firstScroller.widthProperty());*/
            // bottomHbox.prefWidthProperty().bind(border.widthProperty());

        }
    }
    public void setMainController(MainWindowUBoatController mainWindowUBoatController) {
        this.mainWindowUBoatController = mainWindowUBoatController;
    }
  /*  public DisplayCodeConfigurationController getDisplayCodeConfigurationController() {
        return displayCodeConfigurationController;
    }*/

    public SetCodeConfigurationController getSetCodeConfigurationController() {
        return setCodeConfigurationController;
    }
    public MachineDetailsController getMachineDetailsController() {
        return machineDetailsController;
    }
    public void setTheMachineSettingsDTO(TheMachineSettingsDTO theMachineSettingsDTO){
        setCodeConfigurationController.setTheMachineSettingsDTO(theMachineSettingsDTO);
    }

    public void setMachineValues() throws Exception{
        machineDetailsController.setValues();
    }
    public void setHeaderComponentController(LoadFileController headerComponentController, MachineDetailsController machineDetailsController) {
        this.machineDetailsController=machineDetailsController;
        machineDetailsController.setuBoatMachineTabController(this);

    }
   /* public void initDisplayConfiguration() throws Exception {
        List<Exception> listOfExceptionsCode=(mediator.isCodeWasDefined().getListOfException());
        if(listOfExceptionsCode.size()==0) {
            codeConfigurationController.initConfigurations();
        }
    }*/
   /* public ConfigurationDisplayController getCodeConfigurationController() {
        return codeConfigurationController;
    }*/
    @Override
    public void eventHappened(EventObject event) throws Exception {
        machineDetailsController.setValues();
    }
}