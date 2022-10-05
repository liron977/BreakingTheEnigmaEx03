package Controllers;

import Controllers.SingleDecryptionInfo.SingleDecryptionInfoController;
import animatefx.animation.Shake;
import bruteForce.BruteForceSettingsDTO;
import bruteForce.DecryptionInfoDTO;
import bruteForceUI.ThreadTask;
import bruteForceUI.UIAdapter;
import machineEngine.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import mediator.Mediator;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BruteForceResultsTabController {

    Mediator mediator;
    @FXML
    private ListView<String> listView;
    @FXML
    private MainWindowController mainWindowController;
    @FXML
    private Button startBruteForceButton;
    @FXML
    private Button pauseButton;

    @FXML
    private Label amountOfMissionsPerLevelLabel;

    private BruteForceSettingsDTO bruteForceSettingsDTO;
    Thread thread;
    @FXML
    private Button resumeButton;
    @FXML
    private Label TaskProgressLabel;
    @FXML
    private Button stopButton;

    @FXML
    private Label taskMessageLabel;

    @FXML
    private Label progressPercentLabel;
    @FXML
    private ProgressBar taskProgressBar;
    @FXML
    private FlowPane decryptionDtoInfoFlowPane;
    @FXML
    private Label timeProcessLabel;


       /* @FXML
        private Button searchButton;*/

    @FXML
    private Button bruteForceTabButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField stringToConvert;
    @FXML
    private AnchorPane userInputBruteForceLogic;
    @FXML
    private Label averageProcessTimeLabel;

    ThreadTask currentRunningTask;
    private SimpleBooleanProperty startProperty;
    private SimpleBooleanProperty pauseProperty;
    private SimpleBooleanProperty stopProperty;
    private SimpleBooleanProperty clearProperty;

    private SimpleBooleanProperty allMissionsDoneProperty;
    private SimpleBooleanProperty resumeProperty;
    private SimpleBooleanProperty pauseDisableProperty;
    private List<String> dictionary;
    private SimpleBooleanProperty isEnableAnimationsProperty;

    public SimpleBooleanProperty getIsEnableAnimationsProperty(){
        return isEnableAnimationsProperty;
    }


    private Map<String, SingleDecryptionInfoController> decryptionInfoToTileControllerMap;

    public void initialize() {
        pauseProperty = new SimpleBooleanProperty(false);
        startProperty = new SimpleBooleanProperty(true);
        stopProperty = new SimpleBooleanProperty(true);
        resumeProperty = new SimpleBooleanProperty(false);
        pauseDisableProperty = new SimpleBooleanProperty(true);
        allMissionsDoneProperty = new SimpleBooleanProperty(false);
        clearProperty=new SimpleBooleanProperty(false);
        isEnableAnimationsProperty=new SimpleBooleanProperty(true);
        startBruteForceButton.disableProperty().bind(startProperty.not());
        stopButton.disableProperty().bind(stopProperty);
        pauseButton.disableProperty().bind(pauseDisableProperty);
        resumeButton.disableProperty().bind(resumeProperty.not());
        allMissionsDoneProperty.addListener((obsev) -> {
            if (allMissionsDoneProperty.get()) {
                onFinish();
            }
        });

        // userInputBruteForceLogicController.setBruteForce(this);

    }



    public BruteForceResultsTabController() {
        decryptionInfoToTileControllerMap = new HashMap<>();
    }
    public void cancelMissions(){

        if(currentRunningTask!=null) {

            stopProperty.setValue(true);
        }
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void setBruteForceSettingsDTO(BruteForceSettingsDTO bruteForceSettingsDTO) {
        this.bruteForceSettingsDTO = bruteForceSettingsDTO;

    }
public void reset(){
    if(pauseProperty.getValue()){
        EventsHandler e=null;
        stopButtonOnActionListener((ActionEvent) e);


    }
    clearProperty.setValue(true);
    stopProperty.setValue(true);
    amountOfMissionsPerLevelLabel.setText("");
    timeProcessLabel.setText("");
    allMissionsDoneProperty.setValue(true);
    decryptionDtoInfoFlowPane.getChildren().clear();
    progressPercentLabel.setText("");
    taskProgressBar.setProgress(0.0);
    averageProcessTimeLabel.setText("");
}
    @FXML
    void stopButtonOnActionListener(ActionEvent event) {
        startProperty.setValue(true);
        resumeProperty.setValue(false);
        stopProperty.setValue(true);
        pauseProperty.setValue(false);
        pauseDisableProperty.setValue(true);
    }

    @FXML
    void pauseButtonOnActionListener(ActionEvent event) {
        pauseProperty.setValue(true);
        resumeProperty.setValue(true);
        pauseDisableProperty.setValue(true);
        stopProperty.setValue(false);
    }
    @FXML
    void resumeButtonOnActionListener(ActionEvent event) {
        pauseProperty.setValue(false);
        resumeProperty.setValue(false);
        pauseDisableProperty.setValue(false);
    }
    @FXML
    void startBruteForceButtonOnActionListener(ActionEvent event) throws Exception {
        initValues();
        stopProperty.setValue(false);
        UIAdapter UIAdapter = createUIAdapter();
        EngineManager engineManager = mediator.getEngineManger();
        int missionSize=bruteForceSettingsDTO.getMissionSize();
        String convertedString=bruteForceSettingsDTO.getConvertedString();
        String missionLevel=bruteForceSettingsDTO.getMissionLevel();
        int agentsAmount= bruteForceSettingsDTO.getAgentsAmount();
        engineManager.setUsedAgents(agentsAmount);
        currentRunningTask = new ThreadTask(clearProperty,stopProperty, allMissionsDoneProperty, convertedString, UIAdapter, pauseProperty, engineManager,missionSize,missionLevel);
        new Thread(currentRunningTask).start();
    }

    private void initValues() {
            clearProperty.setValue(false);
            startProperty.setValue(false);
            pauseProperty.setValue(false);
            pauseDisableProperty.setValue(false);
            taskProgressBar.setProgress(0);
            resumeProperty.setValue(false);
            pauseDisableProperty.setValue(false);
            decryptionDtoInfoFlowPane.getChildren().clear();
            timeProcessLabel.setText("");
            allMissionsDoneProperty.setValue(false);
            averageProcessTimeLabel.setText("");
    }

    private void onFinish() {
        startProperty.setValue(true);
        stopProperty.setValue(true);
        pauseProperty.setValue(true);
        resumeProperty.setValue(false);
        pauseDisableProperty.setValue(true);
    }
    public void onTaskFinished(Optional<Runnable> onFinish) {
        onFinish();
        this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    private void createTile(DecryptionInfoDTO decryptionInfoDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/Fxml/SingalBruteForceResult.fxml");
            loader.setLocation(url);
            Node singleWordTile = loader.load();
            SingleDecryptionInfoController singleDecryptionInfoController = loader.getController();
           singleDecryptionInfoController.setConvertedString(decryptionInfoDTO.getConvertedString());
            singleDecryptionInfoController.setCodeConfiguration(decryptionInfoDTO.getCodeDescription());
            singleDecryptionInfoController.setAgentId(decryptionInfoDTO.getAgentId());
            decryptionDtoInfoFlowPane.getChildren().add(singleWordTile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                decryptionInfoDTO -> {
                            createTile(decryptionInfoDTO);

                },
                count -> {
                },
                progress -> {
                    taskProgressBar.setProgress(progress);
                    String progressStr = String.format("%.2f", (progress * 100));
                    if((progressStr.equals("100.00"))&&(isEnableAnimationsProperty.getValue())){
                        new Shake(taskProgressBar).play();
                        new Shake(progressPercentLabel).play();
                    }
                    progressPercentLabel.setText(progressStr + "%");
                },
                processTime -> {
                    String stringToSet="";
                    if(processTime>0){

                        stringToSet="Process time: " + processTime + " ms";
                    }
                    timeProcessLabel.setText(stringToSet);

                },
                amountOfMissionsPerLevel->{
                    amountOfMissionsPerLevelLabel.setText("Amount Of Missions Left: "+amountOfMissionsPerLevel);
                },
                init->{
                    amountOfMissionsPerLevelLabel.setText(init);

                },
                averageProcessTime->{
                    String averageProcessTimeStr = String.format("%.2f",averageProcessTime);
                    averageProcessTimeLabel.setText("Average Missions Process Time: "+averageProcessTimeStr+" ms");
                 });
    }
}