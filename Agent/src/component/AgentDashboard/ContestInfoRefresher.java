package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ContestInfoRefresher extends TimerTask {
    private final Consumer<UBoatContestInfoWithoutCheckBoxDTO> updateContestInfoTableView;
    private final Consumer<String> deleteDataFromContestTableView;
    private final BooleanProperty shouldUpdate;

    private String alliesTeamName;


    public ContestInfoRefresher(Consumer<String> deleteDataFromContestTableView,Consumer<UBoatContestInfoWithoutCheckBoxDTO> updateContestInfoTableView, BooleanProperty shouldUpdate, String alliesTeamName) {
        this.updateContestInfoTableView = updateContestInfoTableView;
        this.shouldUpdate=shouldUpdate;
        this.alliesTeamName=alliesTeamName;
        this.deleteDataFromContestTableView=deleteDataFromContestTableView;
    }



    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.CONTEST_INFO)
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
                if(response.code()==409){
                    deleteDataFromContestTableView.accept("");
                }


                UBoatContestInfoWithoutCheckBoxDTO dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),UBoatContestInfoWithoutCheckBoxDTO.class);

                if(dtoFromGson!=null) {

                    updateContestInfoTableView.accept(dtoFromGson);
                }
            }
        });
    }
}