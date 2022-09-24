package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import machineDTO.CodeDescriptionDTO;
import mediator.Mediator;

import java.util.EventObject;

public class DisplayCodeConfigurationController  implements EventsHandler {
    Mediator mediator;
    @FXML
    private ScrollPane displayCodeConfiguration;
    @FXML
    private Label titleLable;
    @FXML
    private Label currentConfigurationLabel;
    @FXML
    private Label originalConfigurationLabel;
    @FXML
    private Label currentConfigurationValueLabel;
    @FXML
    private Label originalConfigurationValueLabel;
    @FXML
    private MachineTabController mainController;
    CodeDescriptionDTO codeDescriptionDTO;
    @FXML
    private EncryptDecryptTabController encryptDecryptTabController;
    @FXML
    private UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void setTitleLable() {
        //mediator.getCodeDescription(codeDescriptionDTO,codeDescriptionDTO.get)

            this.titleLable.setText("The Machine Configurations:");

    }
    public void setCurrentConfigurationLabel() {
        mediator.getEngineManger().createCurrentCodeDescriptionDTO();

            String currentConfiguration = mediator.getEngineManger().getCurrentCodeDescription();
            currentConfigurationLabel.setText("The current code configuration is:");
            this.currentConfigurationValueLabel.setText(currentConfiguration);

    }
    public void setOriginalConfigurationLabel() throws Exception {

            String originalConfiguration = mediator.getEngineManger().getOriginalCodeDescription();
            originalConfigurationLabel.setText("The original code configuration is:");
            this.originalConfigurationValueLabel.setText(originalConfiguration);

    }
    public Label getCurrentConfigurationLabel() {
        return currentConfigurationLabel;
    }
    public Label getOriginalConfigurationLabel() {
        return originalConfigurationLabel;
    }
    public Label getOriginalConfigurationValueLabel() {
        return originalConfigurationValueLabel;
    }
    public Label getCurrentConfigurationValueLabel() {
        return currentConfigurationValueLabel;
    }
    public Label getTitleLable() {
        return titleLable;
    }
    public void setMainController(MachineTabController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
        if(mediator.getEngineManger().getIsCodeConfigurationSet()) {
            setTitleLable();
            setCurrentConfigurationLabel();
            setOriginalConfigurationLabel();
        }
        else{
            this.originalConfigurationValueLabel.setText("");
            originalConfigurationLabel.setText("");
            currentConfigurationValueLabel.setText("");
            currentConfigurationLabel.setText("");
            this.titleLable.setText("");

        }
    }
    public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;
    }
    public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabController) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabController;
    }
}