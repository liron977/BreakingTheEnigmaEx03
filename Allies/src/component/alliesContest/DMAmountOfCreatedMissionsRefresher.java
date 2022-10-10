package component.alliesContest;

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

public class DMAmountOfCreatedMissionsRefresher extends TimerTask {
    private final Consumer<String> updateAmountOfCreatedMissionConsumer;
    private final BooleanProperty shouldUpdate;
    private String alliesTeamName;

    public DMAmountOfCreatedMissionsRefresher(Consumer<String> updateAmountOfCreatedMissionConsumer, BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateAmountOfCreatedMissionConsumer = updateAmountOfCreatedMissionConsumer;
        this.shouldUpdate = shouldUpdate;
        this.alliesTeamName=alliesTeamName;
    }
    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.DM_GET_AMOUNT_OF_CREATED_MISSIONS)
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
                String amountOfCreatedMissions=response.body().string();
                if((amountOfCreatedMissions!=null)) {
                    updateAmountOfCreatedMissionConsumer.accept((amountOfCreatedMissions));
                }
            }
        });
    }
}
