package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import constants.Constants;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class StartNewContestStatusRefresher extends TimerTask {
    private final Consumer<Boolean> updateNewContestStatus;

    private final BooleanProperty shouldUpdate;

    private String alliesTeamName;


    public StartNewContestStatusRefresher(Consumer<Boolean>  updateNewContestStatus, BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateNewContestStatus = updateNewContestStatus;
        this.shouldUpdate=shouldUpdate;
        this.alliesTeamName=alliesTeamName;
    }



    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.START_NEW_CONTEST_STATUS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
              /*  Platform.runLater(() -> {
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(e.getMessage());
                        alert.getDialogPane().setExpanded(true);
                        alert.showAndWait();
                    }
                });*/
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //String jsonArrayOfUsersNames = response.body().string();
                //Type UBoatContestInfoType = new TypeToken<ArrayList<UBoatContestInfoWithoutCheckBoxDTO>>() {}.getType();

                Boolean dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),Boolean.class);

                if(dtoFromGson!=null) {

                    updateNewContestStatus.accept(dtoFromGson);
                }
            }
        });
    }
}