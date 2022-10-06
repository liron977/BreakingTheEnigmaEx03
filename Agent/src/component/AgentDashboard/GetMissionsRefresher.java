package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GetMissionsRefresher  extends TimerTask {
 /*   private final Consumer<UBoatContestInfoWithoutCheckBoxDTO> updateContestInfoTableView;
    private final BooleanProperty shouldUpdate;

    private String alliesTeamName;*/
 AgentDashboardController agentDashboardController;


    public GetMissionsRefresher(AgentDashboardController agentDashboardController) {
        this.agentDashboardController=agentDashboardController;

    }



    @Override
    public void run() {

        AgentThreadTask agentThreadTask=new AgentThreadTask(agentDashboardController);
        new Thread(agentThreadTask).start();

    }
}