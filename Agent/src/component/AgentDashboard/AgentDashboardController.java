package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithCheckBoxDTO;
import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import bruteForceLogic.TheMissionInfo;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private BlockingQueue<Runnable> missionsInfoBlockingQueue;

    private ThreadPoolExecutor threadPoolExecutor;
    public void setThreadPoolSize(int amountOfThreads){
        missionsInfoBlockingQueue = new LinkedBlockingQueue<Runnable>(1000);
        this.threadPoolExecutor = new ThreadPoolExecutor(amountOfThreads, amountOfThreads, 0L, TimeUnit.MILLISECONDS, missionsInfoBlockingQueue);
    }

    @FXML
    void getMissionsOnAction(ActionEvent event) {

        String finalUrl = HttpUrl
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
                        Type theMissionInfoList = new TypeToken<ArrayList<TheMissionInfo>>() {
                        }.getType();
                        List<TheMissionInfo> theMissionInfoFromGson = null;
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

            }
        }
    }

