package component.uBoatContestTab;

import bruteForce.AlliesDTO;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
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

public class AlliesRegisteredTeamsInfoTablesViewRefresher extends TimerTask {
    private final Consumer<List<AlliesDTO>> updateRegisteredAlliesInfoList;
    private final Consumer<Integer> clearTableViewValues;
    private final BooleanProperty shouldUpdate;
    private String battleField;

    public AlliesRegisteredTeamsInfoTablesViewRefresher(Consumer<Integer> clearTableViewValues,Consumer<List<AlliesDTO>> updateAgentsInfoList, BooleanProperty shouldUpdate, String battleField) {
        this.updateRegisteredAlliesInfoList = updateAgentsInfoList;
        this.shouldUpdate = shouldUpdate;
        this.battleField=battleField;
        this.clearTableViewValues=clearTableViewValues;
    }
    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.DISPLAY_ALLIES_TEAMS_INFO)
                .newBuilder()
                .addQueryParameter("battlefield", battleField.trim())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                int x=0;
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
                Type alliesDTOListType = new TypeToken<ArrayList<AlliesDTO>>() {}.getType();
                List<AlliesDTO> dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),alliesDTOListType);
                if((dtoFromGson!=null) && (dtoFromGson.size()!=0)) {
                    updateRegisteredAlliesInfoList.accept((dtoFromGson));
                }
                if(dtoFromGson.size()==0){
                clearTableViewValues.accept(0);

                }
            }
        });
    }
}