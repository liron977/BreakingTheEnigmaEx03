package component.alliesContest;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;

public class AlliesThreadTask extends Task<Boolean> {
    // private UiAdapterInterface UiAdapter;
    private String stringToConvert;
    private int sizeOfMission;
  //  private EngineManager engineManager;
    private String alliesTeamName;

    public AlliesThreadTask(String stringToConvert, int sizeOfMission, String alliesTeamName) {
        //this.UiAdapter = uiAdapt;
       // this.engineManager = engineManager;
        this.sizeOfMission = sizeOfMission;
        this.alliesTeamName = alliesTeamName;
        this.stringToConvert = stringToConvert;
    }

    public Boolean call() throws Exception {
      RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("stringToConvert", stringToConvert)
                        .build();
        Gson gson = new Gson();
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_CREATE_MISSIONS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .addQueryParameter("stringToConvert", stringToConvert)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
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
                    }
                });
            }
        } catch (IOException e) {
        }
        return Boolean.TRUE;
    }
}