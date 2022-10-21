package component.alliesContest;

import bruteForce.DMAmountOfMissionsInfoDTO;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GetNotAvailableAgentsRefresher extends TimerTask {
    private final Consumer<List<String>> updateNotAvailableAgents;
    private final BooleanProperty shouldUpdate;
    private String alliesTeamName;

    public GetNotAvailableAgentsRefresher( Consumer<List<String>> updateNotAvailableAgents
            , BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateNotAvailableAgents = updateNotAvailableAgents;
        this.shouldUpdate = shouldUpdate;
        this.alliesTeamName=alliesTeamName;
    }
    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.GET_NOT_AVAILABLE_AGENTS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               /* Platform.runLater(() -> {
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
                Type notAvailableAgentsListType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),notAvailableAgentsListType);
                // String amountOfCreatedMissions=response.body().string();
                if((dtoFromGson!=null)) {
                    updateNotAvailableAgents.accept(dtoFromGson);
                }
            }
        });
    }

}
