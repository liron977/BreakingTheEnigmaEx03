package component.uBoatMachineTab.machineTab;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.LimitedCodeConfigurationDTO;

import uiMediator.Mediator;
import utils.EventsHandler;

import java.util.EventObject;

public class OriginalCodeConfigurationController implements EventsHandler {


        Mediator mediator;

        @FXML
        private TableView<LimitedCodeConfigurationDTO> originalCodeConfigurationTableView;

        @FXML
        private TableColumn<LimitedCodeConfigurationDTO, String> plugBoardColumn;

    @FXML
    private Label originalCodeConfigurationLabel;
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

        public void setOriginalCodeConfiguration() throws Exception {
            LimitedCodeConfigurationDTO originalLimitedCodeConfigurationDTO =mediator.getEngineManger().createOriginalCodeConfigurationTableViewDTO();
            ObservableList<LimitedCodeConfigurationDTO> originalCodeConfigurationList=getCodeConfigurationTableViewDTOList(originalLimitedCodeConfigurationDTO);
            originalCodeConfigurationTableView.setItems(originalCodeConfigurationList);
            originalCodeConfigurationTableView.getColumns().clear();
            originalCodeConfigurationTableView.getColumns().addAll(rotorsColumn,chosenReflectorColumn,positionsAndNotchColumn,plugBoardColumn);
            originalCodeConfigurationLabel.setText(mediator.getEngineManger().getOriginalCodeDescription());
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
      /*  plugBoardColumnWidthProperty= new SimpleIntegerProperty(codeConfigurationTableViewDTO.getPlugBoardPairs().length()*100);
        plugBoardColumn.prefWidthProperty().bind(plugBoardColumnWidthProperty);*/
           // if(plugBoardColumn.widthProperty().get()<codeConfigurationTableViewDTO.getPlugBoardPairs().length()*8) {
          /*  originalCodeConfigurationTableView.fixedCellSizeProperty().set();*/
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
                setOriginalCodeConfiguration();
            }
            else{
                originalCodeConfigurationTableView.getItems().clear();
                originalCodeConfigurationLabel.setText("");
            }
        }
    }