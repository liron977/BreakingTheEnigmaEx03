package component.login;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AlliesTeamsRefresher extends TimerTask {
    private final Consumer<List<String>> updateAlliesTeamNamesComboBox;
    private final Consumer<String> showAlliesTeamNamesErrors;

    private String alliesTeamName;

    public AlliesTeamsRefresher(Consumer<List<String>> updateAlliesTeamNamesComboBox, Consumer<String> showAlliesTeamNamesErrors) {
        this.updateAlliesTeamNamesComboBox = updateAlliesTeamNamesComboBox;
        this.showAlliesTeamNamesErrors=showAlliesTeamNamesErrors;

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

                Platform.runLater(() -> {
                    showAlliesTeamNamesErrors.accept(e.getMessage());
                });

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                //String jsonArrayOfUsersNames = response.body().string();
                try {
                    Type alliesTeamNamesListType = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    List<String> dtoFromGson = Constants.GSON_INSTANCE.fromJson(response.body().string(), alliesTeamNamesListType);
                    if (dtoFromGson != null) {
                        updateAlliesTeamNamesComboBox.accept(dtoFromGson);
                    }
                    response.body().close();

                } catch (Exception e) {

                }

            } });


    }
}