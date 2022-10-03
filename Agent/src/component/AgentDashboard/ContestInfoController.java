package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;

public class ContestInfoController {
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> amountOfActiveDecryptionTeamsColumn;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> amountOfNeededDecryptionTeamsColumn;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> battleFieldNameColumn;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> contestLevelColumn;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> contestStatusColumn;

    @FXML
    private TableView<UBoatContestInfoWithoutCheckBoxDTO> contestsDataTableView;
    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> uBoatUserNameColumn;

    String alliesTeamName;
    private Timer timer;
    private TimerTask contestInfoRefresher;
    BooleanProperty autoUpdate;


    public void setAlliesTeamName(String selectedAlliesTeamName) {
        this.alliesTeamName = selectedAlliesTeamName;
    }
    private ObservableList<UBoatContestInfoWithoutCheckBoxDTO> getUBoatContestInfoTableViewDTOList(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTO) {
        ObservableList<UBoatContestInfoWithoutCheckBoxDTO> uBoatContestInfoDTOList;
        uBoatContestInfoDTOList = FXCollections.observableArrayList(uBoatContestInfoDTO);

        battleFieldNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("battleFieldName")
        );
        uBoatUserNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("uBoatUserName")
        );
        contestStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("contestStatus")
        );
        contestLevelColumn.setCellValueFactory(
                new PropertyValueFactory<>("contestLevel")
        );
        amountOfNeededDecryptionTeamsColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfNeededDecryptionTeams")
        );
        amountOfActiveDecryptionTeamsColumn.setCellValueFactory(
                new PropertyValueFactory<>("amountOfActiveDecryptionTeams")
        );

        return uBoatContestInfoDTOList;
    }
    private void updateContestInfoTableView(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTOList) {


        Platform.runLater(() -> {
                    ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList =getUBoatContestInfoTableViewDTOList(uBoatContestInfoDTOList);
                    createContestInfoTableView(contestInfoObservableList);
            }

        );
    }
    private void createContestInfoTableView( ObservableList<UBoatContestInfoWithoutCheckBoxDTO> contestInfoObservableList) {
        contestsDataTableView.setItems(contestInfoObservableList);
        contestsDataTableView.getColumns().clear();
        contestsDataTableView.getColumns().addAll(battleFieldNameColumn, uBoatUserNameColumn,contestStatusColumn,contestLevelColumn,amountOfNeededDecryptionTeamsColumn,amountOfActiveDecryptionTeamsColumn);
    }
    public void startContestTableViewRefresher() {
        contestInfoRefresher = new ContestInfoRefresher(
                this::updateContestInfoTableView,autoUpdate,alliesTeamName);
        timer = new Timer();
        timer.schedule(contestInfoRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}