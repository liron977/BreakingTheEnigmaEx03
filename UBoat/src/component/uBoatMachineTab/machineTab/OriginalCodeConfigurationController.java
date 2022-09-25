package component.uBoatMachineTab.machineTab;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.CodeConfigurationTableViewDTO;

import uiMediator.Mediator;
import utils.EventsHandler;

import java.util.EventObject;

public class OriginalCodeConfigurationController implements EventsHandler {


        Mediator mediator;

        @FXML
        private TableView<CodeConfigurationTableViewDTO> originalCodeConfigurationTableView;

        @FXML
        private TableColumn<CodeConfigurationTableViewDTO, String> plugBoardColumn;

    @FXML
    private Label originalCodeConfigurationLabel;
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

        public void setOriginalCodeConfiguration() throws Exception {
            CodeConfigurationTableViewDTO originalCodeConfigurationTableViewDTO =mediator.getEngineManger().createOriginalCodeConfigurationTableViewDTO();
            ObservableList<CodeConfigurationTableViewDTO> originalCodeConfigurationList=getCodeConfigurationTableViewDTOList(originalCodeConfigurationTableViewDTO);
            originalCodeConfigurationTableView.setItems(originalCodeConfigurationList);
            originalCodeConfigurationTableView.getColumns().clear();
            originalCodeConfigurationTableView.getColumns().addAll(rotorsColumn,chosenReflectorColumn,positionsAndNotchColumn,plugBoardColumn);
            originalCodeConfigurationLabel.setText(mediator.getEngineManger().getOriginalCodeDescription());
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
      /*  plugBoardColumnWidthProperty= new SimpleIntegerProperty(codeConfigurationTableViewDTO.getPlugBoardPairs().length()*100);
        plugBoardColumn.prefWidthProperty().bind(plugBoardColumnWidthProperty);*/
           // if(plugBoardColumn.widthProperty().get()<codeConfigurationTableViewDTO.getPlugBoardPairs().length()*8) {
          /*  originalCodeConfigurationTableView.fixedCellSizeProperty().set();*/
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
                setOriginalCodeConfiguration();
            }
            else{
                originalCodeConfigurationTableView.getItems().clear();
                originalCodeConfigurationLabel.setText("");
            }
        }
    }