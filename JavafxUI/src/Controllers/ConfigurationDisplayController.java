package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import mediator.Mediator;

import java.util.EventObject;

public class ConfigurationDisplayController implements EventsHandler{
    Mediator mediator;
    @FXML
    private CurrentConfigurationTableViewController codeConfigurationScrollPaneController;

   private EncryptDecryptTabController encryptDecryptTabController;
    private UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    @FXML
    private OriginalCodeConfigurationController originalCodeConfigurationScrollPaneController;
    @FXML
    private ScrollPane codeConfigurationScrollPane;
    @FXML
    private ScrollPane originalCodeConfigurationScrollPane;
    MachineTabController mainController;


    public CurrentConfigurationTableViewController getCodeConfigurationGridPaneController() {
        return codeConfigurationScrollPaneController;
    }

    public OriginalCodeConfigurationController getOriginalCodeConfigurationGridPaneController() {
        return originalCodeConfigurationScrollPaneController;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
        originalCodeConfigurationScrollPaneController.setMediator(mediator);
        codeConfigurationScrollPaneController.setMediator(mediator);
    }
    public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;
    }
    public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabController) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabController;
    }
    public void initConfigurations() throws Exception {
        originalCodeConfigurationScrollPaneController.setOriginalCodeConfiguration();
        codeConfigurationScrollPaneController.setCurrentCodeConfiguration();
    }

    @Override
    public void eventHappened(EventObject event) throws Exception {
        originalCodeConfigurationScrollPaneController.eventHappened(event);
        codeConfigurationScrollPaneController.eventHappened(event);

    }
    public void setMainController(MachineTabController mainController) {
        this.mainController = mainController;
    }

}