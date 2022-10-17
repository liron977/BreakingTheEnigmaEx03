package component.alliesContest;

import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import okhttp3.*;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;

public class AlliesThreadTask extends Task<Boolean> {
    // private UiAdapterInterface UiAdapter;
    private String stringToConvert;
    private int sizeOfMission;
  //  private EngineManager engineManager;
    private String alliesTeamName;
    private boolean isErrorOccurred =false;
    private ContestStatusInfoDTO contestStatusInfoDTO;
    private boolean isUboatSettingsCompleted;
    private boolean isContestEnded;


    public AlliesThreadTask(String stringToConvert, int sizeOfMission, String alliesTeamName) {
        //this.UiAdapter = uiAdapt;
       // this.engineManager = engineManager;
        this.sizeOfMission = sizeOfMission;
        this.alliesTeamName = alliesTeamName;
        this.stringToConvert = stringToConvert;
       this.isUboatSettingsCompleted=false;
        this.isContestEnded=false;

    }
public void setStringToConvert(String stringToConvert){
        this.stringToConvert=stringToConvert;
}
  public void setIsUboatSettingsCompleted(boolean isUboatSettingsCompleted){
        this.isUboatSettingsCompleted=isUboatSettingsCompleted;
  }
  public void setIsContestEnded(boolean isContestEnded){
        this.isContestEnded=isContestEnded;
  }


    public void setContestStatusInfoDTO(ContestStatusInfoDTO contestStatusInfoDTO) {
        this.contestStatusInfoDTO = contestStatusInfoDTO;
    }

    public Boolean call() throws Exception {
        //stringToConvert=alliesContestController.getConvertedString();

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
                if(!isErrorOccurred) {
                    //todo:kill the timeout
                    isErrorOccurred=true;
                    /*Platform.runLater(() -> {
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
                    });*/
                }
            } else {
                System.out.println("thread task 200");
                String res = response.body().string();
           /*     Platform.runLater(() -> {
                    {

                    }
                });*/
            }
        } catch (IOException e) {
        }
        System.out.println("thread task out");
        return Boolean.TRUE;
    }
}