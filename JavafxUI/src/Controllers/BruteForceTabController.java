package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import mediator.Mediator;

import java.util.EventObject;

public class BruteForceTabController implements EventsHandler{
    Mediator mediator;
    @FXML
    private MainWindowController mainWindowController;
    @FXML
    private ScrollPane userInputBruteForceLogic;
    @FXML
    private ScrollPane bruteForceResultsTab;
    @FXML
    private BruteForceResultsTabController bruteForceResultsTabController;
    @FXML
    private UserInputBruteForceLogicTabController userInputBruteForceLogicController;
    @FXML
    private GridPane bruteForceResultsGridPane;
    @FXML
    private GridPane bruteForceSettingsGridPane;
    @FXML
    private Tab bruteForceSettingsTabButton;
    @FXML
    private Tab bruteForceResultsTabButton;
    @FXML
    private TabPane bruteForceTabPane;

    public void initialize() {
        if (bruteForceResultsTabController != null && userInputBruteForceLogicController != null) {
            userInputBruteForceLogicController.setBruteForceResultsController(bruteForceResultsTabController);
            userInputBruteForceLogicController.setBruteForce(this);
            bruteForceResultsTabButton.disableProperty().bind(userInputBruteForceLogicController.isBruteForceSettingDefined.not());

            bruteForceResultsTab.prefWidthProperty().bind(bruteForceTabPane.widthProperty());
            bruteForceResultsTab.prefHeightProperty().bind(bruteForceTabPane.heightProperty());
            userInputBruteForceLogic.prefWidthProperty().bind(bruteForceTabPane.widthProperty());
            userInputBruteForceLogic.prefHeightProperty().bind(bruteForceTabPane.heightProperty());

        }
    }
    public TabPane getBruteForceTabPane() {
        return bruteForceTabPane;
    }


    public BruteForceResultsTabController getBruteForceResultsTabController() {
        return bruteForceResultsTabController;
    }



    public void initBruteForceValues(){
        try {
            userInputBruteForceLogicController.initDisplayConfiguration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userInputBruteForceLogicController.initValues();
    }

    public void setMediator(Mediator mediator) throws Exception {
        this.mediator = mediator;
        bruteForceResultsTabController.setMediator(mediator);
        userInputBruteForceLogicController.setMediator(mediator);

    }
    public UserInputBruteForceLogicTabController getUserInputBruteForceLogicController(){
        return userInputBruteForceLogicController;
    }

    public Tab getBruteForceSettingsTabButton() {
        return bruteForceSettingsTabButton;
    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    public void runTaskFromSuper() {
        bruteForceResultsTab.setDisable(false);
        bruteForceTabPane.getSelectionModel().select(bruteForceResultsTabButton);


    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
        bruteForceTabPane.getSelectionModel().select(bruteForceSettingsTabButton);
        bruteForceResultsTabController.reset();
        userInputBruteForceLogicController.isBruteForceSettingDefined.setValue(false);



    }
}