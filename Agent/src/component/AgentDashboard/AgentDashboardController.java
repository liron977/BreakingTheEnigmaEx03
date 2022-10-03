package component.AgentDashboard;

import bruteForceLogic.TheMissionInfo;
import com.google.gson.reflect.TypeToken;
import component.AgentDashboard.AgentMissionRunnable;
import component.AgentDashboard.ContestInfoController;
import engineManager.EngineManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @FXML
    private Label alliesTeamNameLabel;



    @FXML
    public void initialize() {


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

    public Button getGetMissions() {
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
                Type theMissionInfoList = new TypeToken<ArrayList<TheMissionInfo>>() {}.getType();
                List<TheMissionInfo> theMissionInfoFromGson = null;
                try {
                    threadPoolExecutor.prestartAllCoreThreads();
                    System.out.println("started threadpool");
                    theMissionInfoFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), theMissionInfoList);
                    EngineManager engineManager=getEngineManager();
                    createRunnableMissions(theMissionInfoFromGson,engineManager);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                threadPoolExecutor.shutdown();
                threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
                Platform.runLater(() -> {
                    {

                    }

                });

            }
        } catch (IOException e) {

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public EngineManager getEngineManager(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_ENGINE_MANAGER)
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
             InputStream objectInputStream = response.body().byteStream();
               /* EngineManager engineManager= (EngineManager) objectInputStream.read()*/
                EngineManager engineManager=null;

                return engineManager;

            }
        } catch (IOException e) {

        }
        return null;

    }
    public void createRunnableMissions(List<TheMissionInfo> theMissionInfoFromGson,EngineManager engineManager) throws InterruptedException {
        int sizeOfMission;
        String initialStartingPosition;

        for (TheMissionInfo theMissionInfo:theMissionInfoFromGson) {
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


    }
    public void createPossiblePositionList(int sizeOfMission, String initialStartingPosition, EngineManager engineManager){
        String[] keyboard =engineManager.getKeyboardAsArray();
        listOfPossiblePosition.add(initialStartingPosition);
        int[] currentPosition = getIndexArrayFromString(initialStartingPosition, keyboard);
        getNextStartingPosition(sizeOfMission, currentPosition, keyboard,listOfPossiblePosition);
    }

    public static int[] getNextStartingPosition(int missionSize, int[] currentPosition, String[] keyboard, List<String> listOfPossiblePosition) {
        while (missionSize != 0) {
            while (currentPosition[currentPosition.length - 1] < keyboard.length - 1) {

                currentPosition[currentPosition.length - 1] = currentPosition[currentPosition.length - 1] + 1;
                missionSize--;
                if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                    listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                    break;
                }
                listOfPossiblePosition.add(getRes(currentPosition, keyboard));

            }
            if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                // listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                break;
            }
            for (int i = currentPosition.length - 1; i > 0; i--) {
                if (currentPosition[i] == (keyboard.length - 1)) {
                    currentPosition[i] = 0;
                    if (currentPosition[i - 1] < keyboard.length - 1) {
                        currentPosition[i - 1] = currentPosition[i - 1] + 1;
                        missionSize--;
                        listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                        break;
                    }

                    if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                        listOfPossiblePosition.add(getRes(currentPosition, keyboard));
                        break;
                    }

                }

            }

        }
        return currentPosition;
    }

    public static boolean isTheLastStartingPosition(int[] currentPosition, int keyboardSize) {
        for (int i = 0; i < currentPosition.length; i++) {
            if (!(currentPosition[i] == keyboardSize - 1)) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> initialStartingPosition(int rotorAmount) {
        List<Integer> currentPosition = new ArrayList<>();
        currentPosition.add(0);
        for (int i = 0; i < rotorAmount; i++) {
            currentPosition.add(0);
        }
        return currentPosition;
    }
    public static void printRes(int[] arr, String[] keyboard) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(keyboard[arr[i]]);
        }
        System.out.println();
        System.out.println("*****");
    }
    public static String getRes(int[] arr, String[] keyboard) {
        String currentPosition="";
        for (int i = 0; i < arr.length; i++) {
            currentPosition=currentPosition.concat(keyboard[arr[i]]);
        }
        return currentPosition;
    }
    public static int[] getIndexArrayFromString(String position, String[] keyboard) {
        int[] indexArrayFromString = new int[position.length()];
        for (int i = 0; i < position.length(); i++) {
            indexArrayFromString[i] = Arrays.asList(keyboard).indexOf(String.valueOf(position.charAt(i)));
        }
        return indexArrayFromString;
    }



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
                        Type theMissionInfoList = new TypeToken<ArrayList<bruteForceLogic.TheMissionInfo>>() {
                        }.getType();
                        List<bruteForceLogic.TheMissionInfo> theMissionInfoFromGson = null;
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