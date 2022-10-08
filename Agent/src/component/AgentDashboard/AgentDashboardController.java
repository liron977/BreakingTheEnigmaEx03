package component.AgentDashboard;

import BruteForce.AgentDecryptionManager;
import bruteForce.AlliesDTO;
import bruteForce.BruteForceResultDTO;
import com.google.gson.reflect.TypeToken;
import engine.theEnigmaEngine.SchemaGenerated;
import engine.theEnigmaEngine.TheMachineEngine;
import engine.theEnigmaEngine.UBoatBattleField;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import machineDTO.TheMachineEngineDTO;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import schemaGenerated.CTEEnigma;
import utils.Constants;
import utils.http.HttpClientUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import bruteForce.TheMissionInfoDTO;


public class AgentDashboardController {
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

    private final String JAXB_XML_GAME_PACKAGE_NAME = "schemaGenerated";
    private SimpleBooleanProperty isMissionEndedProperty;
    private List<BruteForceResultDTO> resultDTOList;
    private List<BruteForceResultDTO> resultDTOListForAgent;
    private SimpleIntegerProperty amountOfAskedMissions;
    private SimpleIntegerProperty amountOfDoneMissions;
    private ObservableList<BruteForceResultDTO> bruteForceResultsDTOObservableList;

    @FXML
    public void initialize() {
        isMissionEndedProperty=new SimpleBooleanProperty(false);
        amountOfAskedMissions=new SimpleIntegerProperty(0);
        amountOfDoneMissions=new SimpleIntegerProperty(0);
        resultDTOList=new ArrayList<>();
        resultDTOListForAgent=new ArrayList<>();

        bruteForceResultsDTOObservableList=getTeamsAgentsDataTableViewDTOList(resultDTOList);
        bruteForceResultTableView.setItems(bruteForceResultsDTOObservableList);
      //  bruteForceResultTableView.getColumns().addAll(missionNumberColumn, stringColumn, codeConfigurationColumn);

       /* isMissionEndedProperty.addListener((obser)->{
            if(isMissionEndedProperty.getValue())

            });
*/

    }
    public synchronized void saveResultsInServer(List<BruteForceResultDTO> bruteForceResultDTOBlockingQueue){
      //  resultDTOListForAgent=resultDTOList;
       // List<BruteForceResultDTO> bruteForceResultDTOList=resultDTOList;
       // resultDTOList=new ArrayList<>();
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
            // Response response = call.execute();
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
                    amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
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

    public void getMissions() {

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

                  /*  System.out.println("started threadpool");
                    System.out.println("check");
*/
                    theMissionInfoListFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), theMissionInfoList);
                    amountOfAskedMissions.setValue(amountOfAskedMissions.getValue()+theMissionInfoListFromGson.size());
                    TheMachineEngine theMachineEngine = getTheMachineEngineInputstream();
                    setTheMachineEngine(theMachineEngine);
                    UIAdapter UIAdapter =  createUIAdapter();
                    decryptionManager = new AgentDecryptionManager(UIAdapter,isMissionEndedProperty,threadPoolExecutor, theMachineEngine
                            , selectedAlliesTeamName,
                            theMissionInfoListFromGson
                            , missionsInfoBlockingQueue);
                    decryptionManager.createMission();

                    threadPoolExecutor.shutdown();
                    threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
                    setThreadPoolSize(amountOfThreads);
                    getMissions();
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

                if(response.code()==409) {
                    Platform.runLater(() -> {
                        {
                            String  message = "The Missions ended";
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
                }
            }
        }catch(IOException e){

        }

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
                        amountDoneMissionsPerAgentLabel.setText(String.valueOf(amountDoneMissionsPerAgent));

                    });
    }
    private  void saveResultsOnServer(List<BruteForceResultDTO> bruteForceResultDTOBlockingQueue) throws InterruptedException {
        /*new Thread(()->{*/
        //resultDTOList=new ArrayList<>();
       // resultDTOListForAgent=new ArrayList<>();

        saveResultsInServer(bruteForceResultDTOBlockingQueue);


      /*  for (BruteForceResultDTO brute:resultDTOList) {
           // System.out.println("in runnable"+Thread.currentThread().getName());
            System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber()+"AFTER");
        }*/
            for (BruteForceResultDTO brute:resultDTOList) {
                // System.out.println("in runnable"+Thread.currentThread().getName());
                System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber()+"BEFORE TABLE VIEW");
            }
            Platform.runLater(() -> {
               // bruteForceResultsDTOObservableList.addAll(bruteForceResultDTOBlockingQueue);
                createAlliesInfoDTOTableView(bruteForceResultDTOBlockingQueue);

            });

        /*  }).start();*/

    }
    private void updateResultsOnAgent() throws InterruptedException {
      /*  List<BruteForceResultDTO> resultDTOList=new ArrayList<>();
        while (bruteForceResultDTOBlockingQueue.size()!=0) {
            resultDTOList.add(bruteForceResultDTOBlockingQueue.poll());
            //saveResultsInServer(resultDTOList);
        }
*/
   /*        for (BruteForceResultDTO brute:resultDTOList) {
                    System.out.println("in runnable"+Thread.currentThread().getName());
                    System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber());
                }*/
            Platform.runLater(() -> {
   /*             for (BruteForceResultDTO brute : resultDTOList) {
                    // System.out.println("in runnable"+Thread.currentThread().getName());
                    if(brute.getConvertedString().equals("OR")){
                        int x=0;
                    }
                    System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "before ");
                }*/
               // ObservableList<BruteForceResultDTO> alliesDTOObservableList = getTeamsAgentsDataTableViewDTOList(resultDTOListForAgent);
                createAlliesInfoDTOTableView(bruteForceResultsDTOObservableList);
            });
    }
    private synchronized void createAlliesInfoDTOTableView(List<BruteForceResultDTO> alliesInfoDTOListFromMission ) {
        bruteForceResultsDTOObservableList.addAll(alliesInfoDTOListFromMission);
         amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
        //updateAmountDoneMissionsPerAgent(amountOfDoneMissions.getValue());



      /*  if(bruteForceResultTableView.getItems().size()==0) {

            bruteForceResultTableView.getColumns().clear();
            bruteForceResultTableView.getColumns().addAll(missionNumberColumn, stringColumn, codeConfigurationColumn);
            bruteForceResultTableView.setItems(alliesInfoDTOList);
        }
        else{
            bruteForceResultTableView.getItems().addAll(alliesInfoDTOList);
        }
        resultDTOListForAgent=new ArrayList<>();
        resultDTOList=new ArrayList<>();*/
    }

    private ObservableList<BruteForceResultDTO> getTeamsAgentsDataTableViewDTOList(List<BruteForceResultDTO> alliesDTO) {

//        for (BruteForceResultDTO brute:alliesDTO) {
//            // System.out.println("in runnable"+Thread.currentThread().getName());
//            System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber()+"After ");
//        }
       // ObservableList<BruteForceResultDTO> alliesDTOList =null;
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

            /*JAXBContext jc = JAXBContext.newInstance("schemaGenerated");
            Unmarshaller u = jc.createUnmarshaller();
            return (CTEEnigma) u.unmarshal(in);*/
       /* } catch (JAXBException e) {
            throw new Exception("The file is not valid,please enter other file");
        }*/
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




    @FXML
    void getMissionsOnAction(ActionEvent event) {

    /*    String finalUrl = HttpUrl
                .parse(Constants.AGENT_GET_MISSIONS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                .addQueryParameter("amountOfMissionsPerAgent",amountOfMissionsPerAgent)
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
                Platform.runLater(() -> {
                    {
                        Type theMissionInfoList = new TypeToken<ArrayList<bruteForce.TheMissionInfo>>() {
                        }.getType();
                        List<bruteForce.TheMissionInfo> theMissionInfoFromGson = null;
                        try {
                            theMissionInfoFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), theMissionInfoList);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

        }
            }
        catch (IOException e) {

            }*/
    }
    public void setPrimaryStage(Stage primaryStageIn) {
        primaryStage = primaryStageIn;
        //scene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
    }
    public void startContestTableViewRefresher(){
        contestInfoController.startContestTableViewRefresher();
    }

}