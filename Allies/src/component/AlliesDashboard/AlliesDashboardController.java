package component.AlliesDashboard;


import bruteForce.AgentInfoDTO;
import com.google.gson.Gson;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;

public class AlliesDashboardController implements Closeable {

    private Timer timer;
    private TimerTask agentsTableViewRefresher;
    private  SimpleBooleanProperty autoUpdate;
    private  IntegerProperty totalAgentsAmount;
    @FXML
    private TableView<AgentInfoDTO> contestsDataTableView;
    @FXML
    private TableView<AgentInfoDTO> teamsAgentsDataTableView;

    @FXML
    private TableColumn<AgentInfoDTO, String> agentNameColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> threadsAmountColumn;
    @FXML
    private TableColumn<AgentInfoDTO, String> missionsAmountColumn;

    private MainWindowAlliesController mainWindowAlliesController;

    AgentInfoDTO agentInfoDTO;
    String alliesTeamName="";

    public AlliesDashboardController() {
        totalAgentsAmount = new SimpleIntegerProperty(0);
        autoUpdate=new SimpleBooleanProperty(true);
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    public void setMainWindowAlliesController(MainWindowAlliesController mainWindowAlliesController) {
        this.mainWindowAlliesController = mainWindowAlliesController;
    }

    public void setCurrentCodeConfiguration() {
        Gson gson = new Gson();
        String finalUrl = HttpUrl
                .parse(Constants.DISPLAY_CURRENT_CODE_CONFIGURATION)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
                Platform.runLater(() -> {
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        try {
                            alert.setContentText(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        alert.getDialogPane().setExpanded(true);
                        alert.showAndWait();
                    }
                });
            } else {
                try {
                    AgentInfoDTO agentInfoDTOFromGson =Constants.GSON_INSTANCE.fromJson(response.body().string(),AgentInfoDTO.class);
                    agentInfoDTO=agentInfoDTOFromGson;
                }
                catch (IOException ignore){}
                Platform.runLater(() -> {
                    {
                       /* //ObservableList<AgentInfoDTO> agentInfoDTOList = getTeamsAgentsDataTableViewDTOList(agentInfoDTO);
                        teamsAgentsDataTableView.setItems(agentInfoDTOList);
                        teamsAgentsDataTableView.getColumns().clear();
                        teamsAgentsDataTableView.getColumns().addAll(missionsAmountColumn,threadsAmountColumn,agentNameColumn);
*/
                    }
                });
            }
        } catch (IOException e) {
        }
    }
    private ObservableList<AgentInfoDTO> getTeamsAgentsDataTableViewDTOList(List<AgentInfoDTO> agentInfoDTO) {

        ObservableList<AgentInfoDTO> agentInfoDTOList;

        agentInfoDTOList = FXCollections.observableArrayList(agentInfoDTO);
        agentNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentName")
        );
        threadsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("threadsAmount")
        );
        missionsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("missionsAmount")
        );

        return agentInfoDTOList;
    }

    @Override
    public void close() throws IOException {
        teamsAgentsDataTableView.getItems().clear();
        totalAgentsAmount.set(0);
        if (agentsTableViewRefresher != null && timer != null) {
            agentsTableViewRefresher.cancel();
            timer.cancel();
        }
    }
    private void updateAgentsInfoList(List<AgentInfoDTO> agentInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AgentInfoDTO> agentInfoDTOObservableList =getTeamsAgentsDataTableViewDTOList(agentInfoDTOList);
            createAgentsInfoDTOTableView(agentInfoDTOObservableList);
            totalAgentsAmount.set(agentInfoDTOList.size());
        });
    }
    private void createAgentsInfoDTOTableView(ObservableList<AgentInfoDTO> agentInfoDTOList ) {
        teamsAgentsDataTableView.setItems(agentInfoDTOList);
        teamsAgentsDataTableView.getColumns().clear();
        teamsAgentsDataTableView.getColumns().addAll(missionsAmountColumn, threadsAmountColumn, agentNameColumn);
    }
    public void startListRefresher() {
        agentsTableViewRefresher = new AgentsTablesViewRefresher(
                this::updateAgentsInfoList,
                autoUpdate,
                alliesTeamName);
        timer = new Timer();
        timer.schedule(agentsTableViewRefresher, REFRESH_RATE, REFRESH_RATE);
    }

}