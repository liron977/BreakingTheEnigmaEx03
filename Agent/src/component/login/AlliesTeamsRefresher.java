package component.login;

import bruteForce.AgentInfoDTO;
import bruteForce.DecryptionInfoDTO;
import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AlliesTeamsRefresher extends TimerTask {
    private final Consumer<List<String>> updateAlliesTeamNamesComboBox;

    private String alliesTeamName;

    public AlliesTeamsRefresher(Consumer<List<String>> updateAlliesTeamNamesComboBox) {
        this.updateAlliesTeamNamesComboBox = updateAlliesTeamNamesComboBox;

    }
    @Override
    public void run() {

        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_TEAM_NAMES)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*Platform.runLater(() -> {
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
                Type alliesTeamNamesListType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),alliesTeamNamesListType);
                if(dtoFromGson!=null) {
                    updateAlliesTeamNamesComboBox.accept(dtoFromGson);
                }
            }
        });
    }
}