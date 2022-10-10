package component.AgentDashboard;

import BruteForce.AgentDecryptionManager;
import bruteForce.*;
import com.google.gson.reflect.TypeToken;
import engine.theEnigmaEngine.SchemaGenerated;
import engine.theEnigmaEngine.TheMachineEngine;
import engine.theEnigmaEngine.UBoatBattleField;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import machineDTO.TheMachineEngineDTO;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import schemaGenerated.CTEEnigma;
import constants.Constants;
import utils.http.HttpClientUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static constants.Constants.REFRESH_RATE;


public class AgentDashboardController implements Closeable {
    @FXML
    private Button getMissions;

    private String selectedAlliesTeamName;
    private String amountOfMissionsPerAgent;
    private int amountOfThreads;
    private BlockingQueue<Runnable> missionsInfoBlockingQueue;

    private ThreadPoolExecutor threadPoolExecutor;
    private List<String> listOfPossiblePosition;
    private Stage primaryStage;
    @FXML
    private TableView contestInfo;

    @FXML
    ContestInfoController contestInfoController;
    AgentDecryptionManager decryptionManager;
    @FXML
    private TableColumn<BruteForceResultDTO, String> stringColumn;
    @FXML
    private TableColumn<BruteForceResultDTO, String> codeConfigurationColumn;

    @FXML
    private TableColumn<BruteForceResultDTO, Integer> missionNumberColumn;
    @FXML
    private TableView<BruteForceResultDTO> bruteForceResultTableView;

    @FXML
    private Label alliesTeamNameLabel;
    @FXML
    private Label amountDoneMissionsPerAgentLabel;
  @FXML
    private Label amountOfAskedMissionsLabel;
  @FXML
    private Label currentAmountOfMissionsInTheQueue;
    @FXML
    private Label amountOfCandidatesStrings;

    private final String JAXB_XML_GAME_PACKAGE_NAME = "schemaGenerated";
    private SimpleBooleanProperty isMissionEndedProperty;
    private List<BruteForceResultDTO> resultDTOList;
    private List<BruteForceResultDTO> resultDTOListForAgent;
    private SimpleIntegerProperty amountOfAskedMissionsProperty;
    private SimpleIntegerProperty amountOfDoneMissions;
    private SimpleIntegerProperty amountOfMissionsInTheQueue;
    private AgentInfoDTO agentInfoDTO;
    private Timer timer;
    private TimerTask contestStatusRefresher;
    BooleanProperty autoUpdate;
    private SimpleBooleanProperty isContestEnded;
    private String alliesWinnerTeamName;
    private ObservableList<BruteForceResultDTO> bruteForceResultsDTOObservableList;
    private boolean isMessageDisplayedForFirstTime;

    @FXML
    public void initialize() {
        isMissionEndedProperty=new SimpleBooleanProperty(false);
        amountOfAskedMissionsProperty =new SimpleIntegerProperty(0);
        amountOfDoneMissions=new SimpleIntegerProperty(0);
        resultDTOList=new ArrayList<>();
        resultDTOListForAgent=new ArrayList<>();
        isContestEnded=new SimpleBooleanProperty(false);
        alliesWinnerTeamName="";
        isMessageDisplayedForFirstTime=false;
        amountOfMissionsInTheQueue=new SimpleIntegerProperty(0);
        this.autoUpdate=new SimpleBooleanProperty(true);
        bruteForceResultsDTOObservableList=getTeamsAgentsDataTableViewDTOList(resultDTOList);
        bruteForceResultTableView.setItems(bruteForceResultsDTOObservableList);
        amountOfMissionsInTheQueue.addListener((observ)->updateMissionsStatus());
        amountOfCandidatesStrings.textProperty().addListener((observ)->{
            if(!String.valueOf(agentInfoDTO.getAmountOfCandidatesStrings()).equals(amountOfCandidatesStrings.getText()))
        {
            updateMissionsStatus();
        }
        });
    }
    public synchronized void saveResultsInServer(List<BruteForceResultDTO> bruteForceResultDTOBlockingQueue){
        String bruteForceResultDTOListGson = Constants.GSON_INSTANCE.toJson(bruteForceResultDTOBlockingQueue);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), bruteForceResultDTOListGson);

        String finalUrl = HttpUrl
                .parse(Constants.BRUTE_FORCE_RESULTS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            HttpClientUtil.runAsyncPost(finalUrl,body, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("not ok");
                            alert.getDialogPane().setExpanded(true);
                            alert.showAndWait();
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                   // amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
                  String res = response.body().string();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void updateMissionsStatus(){
        agentInfoDTO.setAmountOfMissionsToExecute(amountOfMissionsInTheQueue.getValue());
        agentInfoDTO.setAmountOfReceivedMissions(amountOfAskedMissionsProperty.getValue());
        agentInfoDTO.setAmountOfCandidatesStrings(bruteForceResultsDTOObservableList.size());
        String agentInfoDTOGson = Constants.GSON_INSTANCE.toJson(agentInfoDTO);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), agentInfoDTOGson);

        String finalUrl = HttpUrl
                .parse(Constants.AGENTS_MISSIONS_STATUS)
                .newBuilder()
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            HttpClientUtil.runAsyncPost(finalUrl,body, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("not ok");
                            alert.getDialogPane().setExpanded(true);
                            alert.showAndWait();
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    // amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
                    String res = response.body().string();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setSelectedAlliesTeamName(String selectedAlliesTeamName) {
        this.selectedAlliesTeamName = selectedAlliesTeamName;
        contestInfoController.setAlliesTeamName(selectedAlliesTeamName);
        alliesTeamNameLabel.setText(selectedAlliesTeamName);

    }

    public void setAmountOfMissionsPerAgent(String amountOfMissionsPerAgent) {
        this.amountOfMissionsPerAgent = amountOfMissionsPerAgent;

    }

    public void setAmountOfThreads(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
        this.listOfPossiblePosition = new ArrayList<>();
        setThreadPoolSize(amountOfThreads);
    }

    public void setThreadPoolSize(int amountOfThreads) {
        missionsInfoBlockingQueue = new LinkedBlockingQueue<Runnable>(1000);
        this.threadPoolExecutor = new ThreadPoolExecutor(amountOfThreads, amountOfThreads, 0L, TimeUnit.MILLISECONDS, missionsInfoBlockingQueue);
    }

    public boolean getMissions() {
        boolean isMissionsEnded = true;
        if(!isContestEnded.getValue()) {
            isMissionsEnded = false;
            System.out.println("Im here");
            String finalUrl = HttpUrl
                    .parse(Constants.AGENT_GET_MISSIONS)
                    .newBuilder()
                    .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                    .addQueryParameter("amountOfMissionsPerAgent", amountOfMissionsPerAgent)
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .build();
            Call call = HttpClientUtil.getOkHttpClient().newCall(request);
            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    Type theMissionInfoList = new TypeToken<ArrayList<TheMissionInfoDTO>>() {
                    }.getType();
                    List<TheMissionInfoDTO> theMissionInfoListFromGson = null;
                    try {
                        theMissionInfoListFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), theMissionInfoList);
                        amountOfAskedMissionsProperty.setValue(amountOfAskedMissionsProperty.getValue() + theMissionInfoListFromGson.size());
                        Platform.runLater(() -> {
                            amountOfAskedMissionsLabel.setText("Amount Of Asked Missions : " + displayTextWithCommas(amountOfAskedMissionsProperty.getValue()));
                        });
                        TheMachineEngine theMachineEngine = getTheMachineEngineInputstream();
                        setTheMachineEngine(theMachineEngine);
                        UIAdapter UIAdapter = createUIAdapter();
                        decryptionManager = new AgentDecryptionManager(amountOfMissionsInTheQueue, amountOfAskedMissionsProperty, amountOfDoneMissions, UIAdapter, isMissionEndedProperty, threadPoolExecutor, theMachineEngine
                                , selectedAlliesTeamName,
                                theMissionInfoListFromGson
                                , missionsInfoBlockingQueue
                                 ,isContestEnded);
                        decryptionManager.createMission();
                        threadPoolExecutor.shutdown();
                        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
                        setThreadPoolSize(amountOfThreads);
                        isMissionsEnded = false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(() -> {
                        {

                        }

                    });

                }
                if (response.code() != 200) {

                    if (response.code() == 409) {
                        Platform.runLater(() -> {
                            {
                                if(isContestEnded.getValue()){
                                    return;
                                }
                                String message = "The Missions ended";
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                try {
                                    alert.setContentText(response.body().string() + message);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                alert.getDialogPane().setExpanded(true);
                                alert.showAndWait();

                            }
                        });
                        isMissionsEnded = true;
                    }
                }
            } catch (IOException e) {

            }
        }
return isMissionsEnded;
    }

    public void setTheMachineEngine(TheMachineEngine theMachineEngine){
        TheMachineEngineDTO theMachineEngineDTO=getTheMachineEngineInfo();
        theMachineEngine.setUsedRotorsById(theMachineEngineDTO.getUsedRotorsId());
        theMachineEngine.setSelectedReflectorById(theMachineEngineDTO.getReflectorId());

    }
    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                bruteForceResultDTOList -> {
                    try {
                        saveResultsOnServer(bruteForceResultDTOList);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }},
                    amountDoneMissionsPerAgent->{
                        amountDoneMissionsPerAgentLabel.setText("Amount Of Done Missions : "+displayTextWithCommas(amountDoneMissionsPerAgent));

                    },
                amountOfMissionsInTheQueue->{
                    currentAmountOfMissionsInTheQueue.setText("Amount Of Missions In The Queue : "+displayTextWithCommas(amountOfMissionsInTheQueue));
                });
    }
    private  void saveResultsOnServer(List<BruteForceResultDTO> bruteForceResultDTOBlockingQueue) throws InterruptedException {
        saveResultsInServer(bruteForceResultDTOBlockingQueue);
           for (BruteForceResultDTO brute:resultDTOList) {
                System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber()+"BEFORE TABLE VIEW");
            }
            Platform.runLater(() -> {
                createAlliesInfoDTOTableView(bruteForceResultDTOBlockingQueue);

            });

    }

    private synchronized void createAlliesInfoDTOTableView(List<BruteForceResultDTO> alliesInfoDTOListFromMission ) {
        bruteForceResultsDTOObservableList.addAll(alliesInfoDTOListFromMission);
        amountOfCandidatesStrings.setText("Amount Of Candidates : "+bruteForceResultsDTOObservableList.size());

    }

    private ObservableList<BruteForceResultDTO> getTeamsAgentsDataTableViewDTOList(List<BruteForceResultDTO> alliesDTO) {
        try {
            bruteForceResultsDTOObservableList = FXCollections.observableArrayList(alliesDTO);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        missionNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("theMissionNumber")
        );
        stringColumn.setCellValueFactory(
                new PropertyValueFactory<>("convertedString")
        );
        codeConfigurationColumn.setCellValueFactory(
                new PropertyValueFactory<>("codeDescription")
        );

        return bruteForceResultsDTOObservableList;
    }
    public TheMachineEngine getTheMachineEngineInputstream(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_ENGINE_INPUTSTREAM)
                .newBuilder()
                .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
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
            } else {
                ByteArrayInputStream byteArrayInputStream = Constants.GSON_INSTANCE.fromJson(response.body().string(), ByteArrayInputStream.class);

                TheMachineEngine theMachineEngine= buildTheMachineEngineUboat(byteArrayInputStream);

                return theMachineEngine;

            }
        } catch (IOException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public TheMachineEngineDTO getTheMachineEngineInfo(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_MACHINE_INFO)
                .newBuilder()
                .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
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
            } else {
                TheMachineEngineDTO theMachineEngineDTO = Constants.GSON_INSTANCE.fromJson(response.body().string(), TheMachineEngineDTO.class);
                return theMachineEngineDTO;
            }
        } catch (IOException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private CTEEnigma deserializeFrom(InputStream in) throws Exception {
        in.reset();
        Unmarshaller unmarshaller = JAXBContext.newInstance("schemaGenerated")
                .createUnmarshaller();
        return (CTEEnigma)unmarshaller.unmarshal(in);
    }
    public TheMachineEngine buildTheMachineEngineUboat(InputStream inputStream) throws Exception {
        CTEEnigma cteEnigma=deserializeFrom(inputStream);
        SchemaGenerated schemaGenerated = new SchemaGenerated(cteEnigma);
        UBoatBattleField battleField=schemaGenerated.createBattleField();
        TheMachineEngine theMachineEngine = new TheMachineEngine(schemaGenerated.createRotorsSet()
                , schemaGenerated.createReflectorsSet(), schemaGenerated.createKeyboard(),
                schemaGenerated.getAmountOfUsedRotors(), schemaGenerated.createDictionary(),battleField);
        return theMachineEngine;
    }

/*    public void createRunnableMissions(List<TheMissionInfoDTO> theMissionInfoFromGson,EngineManager engineManager) throws InterruptedException {
        int sizeOfMission;
        String initialStartingPosition;

        for (TheMissionInfoDTO theMissionInfo:theMissionInfoFromGson) {
            System.out.println("createRunnableMissions");
            sizeOfMission=theMissionInfo.getSizeOfMission();
           // engineManager=theMissionInfo.getEngineManager();
            engineManager=engineManager.cloneEngineManager();

            initialStartingPosition=theMissionInfo.getInitialStartingPosition();
            createPossiblePositionList(sizeOfMission,initialStartingPosition,engineManager);
            AgentMissionRunnable agentMissionRunnable=new AgentMissionRunnable(engineManager,
                    theMissionInfo.getStringToConvert()
                    ,selectedAlliesTeamName,listOfPossiblePosition);
            missionsInfoBlockingQueue.put(agentMissionRunnable);
        }


    }*/

    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
    }
    public void startContestTableViewRefresher(){
        contestInfoController.startContestTableViewRefresher();
    }

    public void setAgentInfoDTO(AgentInfoDTO agentInfoDTO) {
        this.agentInfoDTO = agentInfoDTO;
    }
    public void startContestStatusRefresher() {
        contestStatusRefresher = new ContestStatusRefresher("agent","",
                this::updateContestStatus,autoUpdate,selectedAlliesTeamName);
        timer = new Timer();
        timer.schedule(contestStatusRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void updateContestStatus(ContestStatusInfoDTO contestStatusInfoDTO) {
        if(isMessageDisplayedForFirstTime) {
            if (contestStatusInfoDTO.getIsAlliesConfirmedGameOver()) {
                    Platform.runLater(() -> {
                        try {
                            close();
                            initValues();
                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
               });
            }
        }
      else  if (!isContestEnded.getValue()) {
            Platform.runLater(() -> {
                this.isContestEnded.setValue(contestStatusInfoDTO.isContestEnded());
                this.alliesWinnerTeamName = contestStatusInfoDTO.getAlliesWinnerTeamName();
                if (isContestEnded.getValue()&&!isMessageDisplayedForFirstTime) {
                    isMessageDisplayedForFirstTime=true;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    String message = "The contest ended" + "\n" + "The winning team is " + alliesWinnerTeamName;
                    alert.setContentText(message);
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();
              /*      try {
                        close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }*/
                }});
        }
    }

    public String displayTextWithCommas(int amount){
        StringBuilder amountWithCommas= new StringBuilder("");
        int counter=0;
        if(amount==0){
            return "0";
        }
        while (amount>0){
            if((counter%3==0)&&(counter!=0)){
                amountWithCommas=amountWithCommas.append(",");
            }
            counter++;
            amountWithCommas=amountWithCommas.append(amount%10);
            amount=amount/10;
        }
        return amountWithCommas.reverse().toString();
    }
    public void initValues(){
        setThreadPoolSize(amountOfThreads);
        listOfPossiblePosition=new ArrayList<>();
        contestInfo.getItems().clear();
        bruteForceResultTableView.getItems().clear();
        amountDoneMissionsPerAgentLabel.setText("");
        amountOfAskedMissionsLabel.setText("");
        currentAmountOfMissionsInTheQueue.setText("");
        amountOfCandidatesStrings.setText("");
        isMissionEndedProperty.setValue(false);
        resultDTOList=new ArrayList<>();
        amountOfAskedMissionsProperty.setValue(0);
        amountOfDoneMissions.setValue(0);
        amountOfMissionsInTheQueue.setValue(0);
        isContestEnded.setValue(false);
        alliesWinnerTeamName="";
        bruteForceResultsDTOObservableList= FXCollections.observableArrayList();
       isMessageDisplayedForFirstTime=false;

    }
    @Override
    public void close() throws IOException {
      //  this.isContestEnded.set(false);
      //  this.alliesWinnerTeamName = "";
        if (contestStatusRefresher != null) {
            contestStatusRefresher.cancel();
            contestInfoController.close();
            timer.cancel();
        }
    }
}