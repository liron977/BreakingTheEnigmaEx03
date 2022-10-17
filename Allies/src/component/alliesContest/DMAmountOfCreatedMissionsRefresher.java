package component.alliesContest;

import bruteForce.AlliesDTO;
import bruteForce.DMAmountOfMissionsInfoDTO;
import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
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

public class DMAmountOfCreatedMissionsRefresher extends TimerTask {
    private final Consumer<DMAmountOfMissionsInfoDTO> updateAmountOfCreatedMissionConsumer;
    private final BooleanProperty shouldUpdate;
    private String alliesTeamName;
    private String selectedBattleField;


    public DMAmountOfCreatedMissionsRefresher(String selectedBattleField,Consumer<DMAmountOfMissionsInfoDTO> updateAmountOfCreatedMissionConsumer, BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateAmountOfCreatedMissionConsumer = updateAmountOfCreatedMissionConsumer;
        this.shouldUpdate = shouldUpdate;
        this.alliesTeamName=alliesTeamName;
        this.selectedBattleField=selectedBattleField;
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
                .addQueryParameter("battlefield", selectedBattleField)
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
                Type dMAmountOfMissionsInfoDTOType = new TypeToken<DMAmountOfMissionsInfoDTO>() {}.getType();
                DMAmountOfMissionsInfoDTO dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),dMAmountOfMissionsInfoDTOType);
               // String amountOfCreatedMissions=response.body().string();
                if((dtoFromGson!=null)) {
                    updateAmountOfCreatedMissionConsumer.accept(dtoFromGson);
                }
            }
        });
    }
}