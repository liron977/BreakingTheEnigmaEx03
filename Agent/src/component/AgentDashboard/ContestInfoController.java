package component.AgentDashboard;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<String, String> AlliesTeamNameColumn;
    String alliesTeamName;

    @FXML
    private TableColumn<UBoatContestInfoWithoutCheckBoxDTO, String> uBoatUserNameColumn;

    private ObservableList<UBoatContestInfoWithoutCheckBoxDTO> getUBoatContestInfoTableViewDTOList(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTO) {
        ObservableList<UBoatContestInfoWithoutCheckBoxDTO> uBoatContestInfoDTOList;
        uBoatContestInfoDTOList = FXCollections.observableArrayList(uBoatContestInfoDTO);
        AlliesTeamNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("selectionContestColumn")
        );
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
 /*   private void updateUBoatContestsList(List<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOList) {


        Platform.runLater(() -> {
            boolean isDTDExistsInTableView;
            ObservableList<UBoatContestInfoWithCheckBoxDTO> uBoatContestInfoDTOListTemp = FXCollections.observableArrayList();;
            for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO: uBoatContestInfoDTOList) {
                checkBoxChangedListener(uBoatContestInfoWithCheckBoxDTO);
                isDTDExistsInTableView=false;
                int index=0;
                for (UBoatContestInfoWithCheckBoxDTO TableViewUBoatContestDTO : contestsDataTableView.getItems()) {
                    index++;
                    if (TableViewUBoatContestDTO.getBattleFieldName().equals(uBoatContestInfoWithCheckBoxDTO.getBattleFieldName())) {
                        updateContestsDataTableViewRow(uBoatContestInfoWithCheckBoxDTO,index);
                        isDTDExistsInTableView = true;
                        break;
                    }
                }
                if (!isDTDExistsInTableView) {
                    uBoatContestInfoDTOListTemp.addAll(getUBoatContestInfoTableViewDTOList(uBoatContestInfoWithCheckBoxDTO));
                    contestsDataTableView.getItems().add(uBoatContestInfoWithCheckBoxDTO);
                }
            }
            uBoatContestInfoWithCheckBoxDTOList=contestsDataTableView.getItems();
            //todo : To check
          *//*  for (UBoatContestInfoWithCheckBoxDTO uBoatContestInfoWithCheckBoxDTO: contestsDataTableView.getItems()) {
                if (!uBoatContestInfoDTOList.contains(uBoatContestInfoWithCheckBoxDTO)) {

                    contestsDataTableView.getItems().remove(uBoatContestInfoWithCheckBoxDTO);
                }
           }*//*
            totalUBoatContestsAmount.set(uBoatContestInfoDTOList.size());
        });
    }
    public void startUBoatContestsTableViewRefresher() {
        uBoatContestsRefresher = new UBoatContestsRefresher(
                this::updateUBoatContestsList,autoUpdate);
        timer = new Timer();
        timer.schedule(uBoatContestsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
*/
}