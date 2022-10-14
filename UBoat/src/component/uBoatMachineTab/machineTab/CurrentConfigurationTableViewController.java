package component.uBoatMachineTab.machineTab;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import machineDTO.LimitedCodeConfigurationDTO;

import okhttp3.*;
import uiMediator.Mediator;
import constants.Constants;
import utils.EventsHandler;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.EventObject;

public class CurrentConfigurationTableViewController implements EventsHandler {

    Mediator mediator;
    @FXML
    private TableView<LimitedCodeConfigurationDTO> codeConfigurationTableView;


    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> plugBoardColumn;
    @FXML
    private Label currentCodeConfigurationLabel;
    //  UserInputBruteForceLogicTabController userInputBruteForceLogicTabController;
    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> positionsAndNotchColumn;
    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> chosenReflectorColumn;

    @FXML
    private TableColumn<LimitedCodeConfigurationDTO, String> rotorsColumn;
    SimpleIntegerProperty plugBoardColumnWidthProperty;
    String battleName;
   private LimitedCodeConfigurationDTO limitedCodeConfigurationDTO;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    /* public void setCurrentCodeConfiguration() {
         CodeConfigurationTableViewDTO currentCodeConfigurationTableViewDTO = mediator.getEngineManger().createCurrentCodeConfigurationTableViewDTO();
         ObservableList<CodeConfigurationTableViewDTO> currentCodeConfigurationList = getCodeConfigurationTableViewDTOList(currentCodeConfigurationTableViewDTO);
         codeConfigurationTableView.setItems(currentCodeConfigurationList);
         codeConfigurationTableView.getColumns().clear();
         codeConfigurationTableView.getColumns().addAll(rotorsColumn, chosenReflectorColumn, positionsAndNotchColumn, plugBoardColumn);
         currentCodeConfigurationLabel.setText(mediator.getEngineManger().getCurrentCodeDescription());
     }*/
    public void setCurrentCodeConfiguration() {
       Gson gson = new Gson();
        String finalUrl = HttpUrl
                .parse(Constants.DISPLAY_CURRENT_CODE_CONFIGURATION)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
                /*Platform.runLater(() -> {
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
                });*/
            } else {
                try {
                    LimitedCodeConfigurationDTO limitedCodeConfigurationDTOFromGson =Constants.GSON_INSTANCE.fromJson(response.body().string(),LimitedCodeConfigurationDTO.class);
                    limitedCodeConfigurationDTO=limitedCodeConfigurationDTOFromGson;
                }
                catch (IOException ignore){}
                Platform.runLater(() -> {
                    {
                        ObservableList<LimitedCodeConfigurationDTO> limitedCodeConfigurationDTOList = getCodeConfigurationTableViewDTOList(limitedCodeConfigurationDTO);
                        codeConfigurationTableView.setItems(limitedCodeConfigurationDTOList);
                        codeConfigurationTableView.getColumns().clear();
                        codeConfigurationTableView.getColumns().addAll(rotorsColumn, chosenReflectorColumn, positionsAndNotchColumn, plugBoardColumn);
                        currentCodeConfigurationLabel.setText(limitedCodeConfigurationDTO.getFormatCodeConfiguration());
                    }
                });
            }
        } catch (IOException e) {
        }
    }
   /* public void setUserInputBruteForceLogicTabController(UserInputBruteForceLogicTabController userInputBruteForceLogicTabController) {
        this.userInputBruteForceLogicTabController = userInputBruteForceLogicTabController;
    }
 */   private ObservableList<LimitedCodeConfigurationDTO> getCodeConfigurationTableViewDTOList(LimitedCodeConfigurationDTO limitedCodeConfigurationDTO) {

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
    public void setBattleName(String battleName){
        this.battleName=battleName;
    }

}