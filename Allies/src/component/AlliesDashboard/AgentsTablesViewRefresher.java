package component.AlliesDashboard;

import bruteForce.AgentInfoDTO;
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

public class AgentsTablesViewRefresher extends TimerTask {
    private final Consumer<List<AgentInfoDTO>> updateAgentsInfoList;
    private final BooleanProperty shouldUpdate;
    private String alliesTeamName;

    public AgentsTablesViewRefresher(Consumer<List<AgentInfoDTO>> updateAgentsInfoList, BooleanProperty shouldUpdate,String alliesTeamName) {
        this.updateAgentsInfoList = updateAgentsInfoList;
        this.shouldUpdate = shouldUpdate;
        this.alliesTeamName=alliesTeamName;
    }
    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.AGENTS_INFO_TABLE_VIEW)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName.trim())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
              /*  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(e.getMessage());
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //String jsonArrayOfUsersNames = response.body().string();
                Type agentInfoDTOList = new TypeToken<ArrayList<AgentInfoDTO>>() {}.getType();
                List<AgentInfoDTO> dtoFromGson=Constants.GSON_INSTANCE.fromJson(response.body().string(),agentInfoDTOList);
                if(dtoFromGson!=null) {
                    updateAgentsInfoList.accept((dtoFromGson));
                }
            }
        });
    }
}
