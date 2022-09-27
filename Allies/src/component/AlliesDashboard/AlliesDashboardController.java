package component.AlliesDashboard;


import bruteForce.AgentInfoDTO;
import com.google.gson.Gson;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.LimitedCodeConfigurationDTO;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;

public class AlliesDashboardController {

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
                        ObservableList<AgentInfoDTO> agentInfoDTOList = getTeamsAgentsDataTableViewDTOList(agentInfoDTO);
                        teamsAgentsDataTableView.setItems(agentInfoDTOList);
                        teamsAgentsDataTableView.getColumns().clear();
                        teamsAgentsDataTableView.getColumns().addAll(missionsAmountColumn,threadsAmountColumn,agentNameColumn);

                    }
                });
            }
        } catch (IOException e) {
        }
    }
    private ObservableList<AgentInfoDTO> getTeamsAgentsDataTableViewDTOList(AgentInfoDTO agentInfoDTO) {

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

    }