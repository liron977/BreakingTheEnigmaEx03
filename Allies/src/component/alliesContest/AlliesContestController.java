package component.alliesContest;

import bruteForce.AlliesDTO;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;

public class AlliesContestController {
    MainWindowAlliesController mainWindowAlliesController;

    @FXML
    private Button submitButton;

    @FXML
    private TextField missionSizeTextField;

    private String alliesTeamName;
    private String selectedBattleField="";
    @FXML
    private TableView<AlliesDTO> activeTeamsDetailsTableView;
    @FXML
    private TableColumn<AlliesDTO, String> missionSizeColumn;
    @FXML
    private TableColumn<AlliesDTO, String> agentsAmountColumn;
    @FXML
    private TableColumn<AlliesDTO, String> alliesTeamNameColumn;
    private Timer timer;
    private TimerTask alliesRegisteredTeamsRefresher;
    private SimpleBooleanProperty autoUpdate;


    @FXML
    public void initialize(){
        autoUpdate=new SimpleBooleanProperty(true);


    }

    public  void setSelectedBattleFieldName(String selectedBattleFieldName){
        this.selectedBattleField=selectedBattleFieldName;

    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }
    @FXML
    void submitButtonOnActionListener(ActionEvent event) throws IOException {
        AlliesDTO alliesDTO = new AlliesDTO(Integer.parseInt(missionSizeTextField.getText()), alliesTeamName);
        String alliesDTOGson = Constants.GSON_INSTANCE.toJson(alliesDTO);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), alliesDTOGson);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String finalUrl = HttpUrl
                .parse(Constants.REGISTER_ALLIES_TO_CONTEST)
                .newBuilder()
                .addQueryParameter("battlefield", selectedBattleField)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        Response response = call.execute();
        if (response.code() == 200) {

            Platform.runLater(() -> {

                alert.setContentText("You registered to the contest " + selectedBattleField + " successfully");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            });

            String stringToConvert=Constants.GSON_INSTANCE.fromJson(response.body().string(),String.class);
            if(stringToConvert!=null) {
                AlliesThreadTask threadTask=new AlliesThreadTask(stringToConvert,Integer.parseInt(missionSizeTextField.getText()),alliesTeamName);
                new Thread(threadTask).start();
            }
        } else {
            if (response.code() == 409) {
                alert.setContentText("The contest " + selectedBattleField + " is full, please select another one");
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            }
        }
    }

    public void setMainWindowAlliesController(MainWindowAlliesController mainWindowAlliesController) {
        this.mainWindowAlliesController = mainWindowAlliesController;
    }
    public void startAlliesInfoTableViewRefresher() {
        alliesRegisteredTeamsRefresher = new AlliesRegisteredTeamsInfoTablesViewRefresher(
                this::updateRegisteredAlliesInfoList,
                autoUpdate,
                selectedBattleField);
        timer = new Timer();
        timer.schedule(alliesRegisteredTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void createAlliesInfoDTOTableView(ObservableList<AlliesDTO> alliesInfoDTOList ) {
        activeTeamsDetailsTableView.setItems(alliesInfoDTOList);
        activeTeamsDetailsTableView.getColumns().clear();
        activeTeamsDetailsTableView.getColumns().addAll(alliesTeamNameColumn, agentsAmountColumn, missionSizeColumn);
    }
    private void updateRegisteredAlliesInfoList(List<AlliesDTO> alliesInfoDTOList) {
        Platform.runLater(() -> {
            ObservableList<AlliesDTO> alliesDTOObservableList =getTeamsAgentsDataTableViewDTOList(alliesInfoDTOList);
            createAlliesInfoDTOTableView(alliesDTOObservableList);
           // totalAlliesRegisteredTeamsAmount.set(alliesInfoDTOList.size());
        });
    }
    private ObservableList<AlliesDTO> getTeamsAgentsDataTableViewDTOList(List<AlliesDTO> alliesDTO) {

        ObservableList<AlliesDTO> alliesDTOList;
        alliesDTOList = FXCollections.observableArrayList(alliesDTO);
        alliesTeamNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("alliesName")
        );
        agentsAmountColumn.setCellValueFactory(
                new PropertyValueFactory<>("agentsAmount")
        );
        missionSizeColumn.setCellValueFactory(
                new PropertyValueFactory<>("missionSize")
        );

        return alliesDTOList;
    }

}