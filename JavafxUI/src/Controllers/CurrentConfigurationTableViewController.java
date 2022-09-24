package Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.CodeConfigurationTableViewDTO;
import mediator.Mediator;

import java.util.EventObject;

public class CurrentConfigurationTableViewController implements EventsHandler {

    Mediator mediator;
    @FXML
    private TableView<CodeConfigurationTableViewDTO> codeConfigurationTableView;


    @FXML
    private TableColumn<CodeConfigurationTableViewDTO, String> plugBoardColumn;
    @FXML
    private Label currentCodeConfigurationLabel;
    UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    @FXML
    private TableColumn<CodeConfigurationTableViewDTO, String> positionsAndNotchColumn;
    @FXML
    private TableColumn<CodeConfigurationTableViewDTO, String> chosenReflectorColumn;

    @FXML
    private TableColumn<CodeConfigurationTableViewDTO, String> rotorsColumn;
    SimpleIntegerProperty plugBoardColumnWidthProperty;
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void setCurrentCodeConfiguration() {
        CodeConfigurationTableViewDTO currentCodeConfigurationTableViewDTO = mediator.getEngineManger().createCurrentCodeConfigurationTableViewDTO();
        ObservableList<CodeConfigurationTableViewDTO> currentCodeConfigurationList = getCodeConfigurationTableViewDTOList(currentCodeConfigurationTableViewDTO);
        codeConfigurationTableView.setItems(currentCodeConfigurationList);
        codeConfigurationTableView.getColumns().clear();
        codeConfigurationTableView.getColumns().addAll(rotorsColumn, chosenReflectorColumn, positionsAndNotchColumn, plugBoardColumn);
        currentCodeConfigurationLabel.setText(mediator.getEngineManger().getCurrentCodeDescription());
    }
    public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabController) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabController;
    }
    private ObservableList<CodeConfigurationTableViewDTO> getCodeConfigurationTableViewDTOList(CodeConfigurationTableViewDTO codeConfigurationTableViewDTO) {

        ObservableList<CodeConfigurationTableViewDTO> CodeConfigurationList;

        CodeConfigurationList = FXCollections.observableArrayList(codeConfigurationTableViewDTO);
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
        if(codeConfigurationTableViewDTO.getPlugBoardPairs().length() * 8>=92) {
            plugBoardColumn.setPrefWidth(codeConfigurationTableViewDTO.getPlugBoardPairs().length() * 8);
            plugBoardColumn.setMaxWidth(codeConfigurationTableViewDTO.getPlugBoardPairs().length() * 8);
            plugBoardColumn.setMinWidth(codeConfigurationTableViewDTO.getPlugBoardPairs().length() * 8);
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