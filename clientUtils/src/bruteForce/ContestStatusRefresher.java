package bruteForce;

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

public class ContestStatusRefresher  extends TimerTask {
    private final Consumer<ContestStatusInfoDTO> updateContestStatus;
    private final BooleanProperty shouldUpdate;
    private String alliesTeamName;
    public ContestStatusRefresher(Consumer<ContestStatusInfoDTO> updateContestStatus , BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateContestStatus = updateContestStatus;
        this.shouldUpdate=shouldUpdate;
        this.alliesTeamName=alliesTeamName;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_CONTEST_STATUS)
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
                ContestStatusInfoDTO contestStatusInfoDTOFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),ContestStatusInfoDTO.class);
                if(contestStatusInfoDTOFromGson!=null) {
                    updateContestStatus.accept(contestStatusInfoDTOFromGson);
                }
            }
        });
    }
}
