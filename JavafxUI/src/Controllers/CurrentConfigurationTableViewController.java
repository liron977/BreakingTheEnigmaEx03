package Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.LimitedCodeConfigurationDTO;
import mediator.Mediator;

import java.util.EventObject;

public class CurrentConfigurationTableViewController implements EventsHandler {

    Mediator mediator;
    @FXML
    private TableView<LimitedCodeConfigurationDTO> codeConfigurationTableView;


    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> plugBoardColumn;
    @FXML
    private Label currentCodeConfigurationLabel;
    UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> positionsAndNotchColumn;
    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> chosenReflectorColumn;

    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> rotorsColumn;
    SimpleIntegerProperty plugBoardColumnWidthProperty;
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void setCurrentCodeConfiguration() {
        LimitedCodeConfigurationDTO currentLimitedCodeConfigurationDTO = mediator.getEngineManger().createCurrentCodeConfigurationTableViewDTO();
        ObservableList<LimitedCodeConfigurationDTO> currentCodeConfigurationList = getCodeConfigurationTableViewDTOList(currentLimitedCodeConfigurationDTO);
        codeConfigurationTableView.setItems(currentCodeConfigurationList);
        codeConfigurationTableView.getColumns().clear();
        codeConfigurationTableView.getColumns().addAll(rotorsColumn, chosenReflectorColumn, positionsAndNotchColumn, plugBoardColumn);
        currentCodeConfigurationLabel.setText(mediator.getEngineManger().getCurrentCodeDescription());
    }
    public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabController) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabController;
    }
    private ObservableList<LimitedCodeConfigurationDTO> getCodeConfigurationTableViewDTOList(LimitedCodeConfigurationDTO limitedCodeConfigurationDTO) {

        ObservableList<LimitedCodeConfigurationDTO> CodeConfigurationList;

        CodeConfigurationList = FXCollections.observableArrayList(limitedCodeConfigurationDTO);
        rotorsColumn.setCellValueFactory(
                new PropertyValueFactory<>("rotors")
        );
        chosenReflectorColumn.setCellValueFactory(
                new PropertyValueFactory<>("reflector")
        );
        positionsAndNotchColumn.setCellValueFactory(
                new PropertyValueFactory<>("positionsAndNotch")
        );
        plugBoardColumn.setCellValueFactory(
                new PropertyValueFactory<>("plugBoardPairs")
        );
        if(limitedCodeConfigurationDTO.getPlugBoardPairs().length() * 8>=92) {
            plugBoardColumn.setPrefWidth(limitedCodeConfigurationDTO.getPlugBoardPairs().length() * 8);
            plugBoardColumn.setMaxWidth(limitedCodeConfigurationDTO.getPlugBoardPairs().length() * 8);
            plugBoardColumn.setMinWidth(limitedCodeConfigurationDTO.getPlugBoardPairs().length() * 8);
        }
        else{
            plugBoardColumn.setPrefWidth(92);
            plugBoardColumn.setMaxWidth(92);
            plugBoardColumn.setMinWidth(92);
        }
        return CodeConfigurationList;
    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
        if (mediator.getEngineManger().getIsCodeConfigurationSet()) {
            setCurrentCodeConfiguration();
        } else {
            codeConfigurationTableView.getItems().clear();
            currentCodeConfigurationLabel.setText("");
        }
    }
}