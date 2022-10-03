package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithCheckBoxDTO;
import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ContestInfoRefresher extends TimerTask {
    private final Consumer<List<UBoatContestInfoWithCheckBoxDTO>> updateUBoatContestsList;
    private final BooleanProperty shouldUpdate;

    private String alliesTeamName;

    public ContestInfoRefresher(Consumer<List<UBoatContestInfoWithCheckBoxDTO>> updateUBoatContestsList, BooleanProperty shouldUpdate) {
        this.updateUBoatContestsList = updateUBoatContestsList;
        this.shouldUpdate=shouldUpdate;
    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.CONTEST_INFO_FOR_AGENT)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(e.getMessage());
                        alert.getDialogPane().setExpanded(true);
                        alert.showAndWait();
                    }
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //String jsonArrayOfUsersNames = response.body().string();
                Type UBoatContestInfoType = new TypeToken<ArrayList<UBoatContestInfoWithoutCheckBoxDTO>>() {}.getType();
                List<UBoatContestInfoWithoutCheckBoxDTO> dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),UBoatContestInfoType);
                List<UBoatContestInfoWithCheckBoxDTO> dtoWithCheckBoxFromGson=new ArrayList<>();
                if(dtoFromGson!=null) {
                    for (UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoWithoutCheckBoxDTO:dtoFromGson) {
                        UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO = new UBoatContestInfoWithCheckBoxDTO(uBoatContestInfoWithoutCheckBoxDTO);
                        dtoWithCheckBoxFromGson.add(uBoatContestInfoWithCheckBoxDTO);
                    }
                    updateUBoatContestsList.accept(dtoWithCheckBoxFromGson);
                }
            }
        });
    }
}