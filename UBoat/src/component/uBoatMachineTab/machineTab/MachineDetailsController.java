package component.uBoatMachineTab.machineTab;

import com.sun.istack.internal.NotNull;
import component.uBoatMachineTab.UBoatMachineTabController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import machineDTO.TheMachineSettingsDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import uiMediator.Mediator;
import constants.Constants;
import utils.EventsHandler;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

public class MachineDetailsController implements EventsHandler {
    Mediator mediator;
    @FXML
    private GridPane grid;
    @FXML
    private VBox vbox1;
    @FXML
    private AnchorPane reflectorInfo;
    @FXML
    private Label ReflectorsAmountLable;
    @FXML
    private ImageView wheelsImage;
    @FXML
    private AnchorPane messagesAmount;
    @FXML
    private Label messagesAmountLabel;
    @FXML
    private AnchorPane anchor1;

    @FXML
    private Label rotorsAmountLable;
    String battleName="";
    @FXML
    private UBoatMachineTabController uBoatMachineTabController;
    int column=0;
    int  row=0;
    private TheMachineSettingsDTO theMachineSettingsDTO;
    List<String> descriptions=new ArrayList<>();
    @FXML
    public void initialize() throws Exception{

    }
    public void initMachineDetails(){
        ReflectorsAmountLable.setText("");
        rotorsAmountLable.setText("");
        messagesAmountLabel.setText("");
    }
    public void setMachineDetailsValues(){

        if (battleName.trim().isEmpty()) {
            showAlertMessage("The battle field is empty.");
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_DETAILS)
                .newBuilder()
                .addQueryParameter("battlefield", battleName.trim())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() { //todo i guess it should be sync no?
            @Override public void onFailure(@NotNull Call call, @NotNull IOException e) {
/*
                Platform.runLater(() -> {
                    showAlertMessage("Something went wrong: "+ e.getMessage());
            });*/
            }
            @Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                 /*   String responseBody = response.body().string();
                    Platform.runLater(() -> {
                       showAlertMessage("Something went wrong: " +responseBody);
                    });*/
                }
                else {
                    try {
                        TheMachineSettingsDTO theMachineSettingsDTOFromGson =Constants.GSON_INSTANCE.fromJson(response.body().string(),TheMachineSettingsDTO.class);
                        theMachineSettingsDTO=theMachineSettingsDTOFromGson;

                    }
                    catch (IOException ignore){}
                    Platform.runLater(() -> {
                        try {
                            setValues();
                            setTheMachineSettingsDTO(theMachineSettingsDTO);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                Objects.requireNonNull(response.body()).close();
            }
        });

    }
    public void setUBoatMachineTabController(UBoatMachineTabController uBoatMachineTabController){
        this.uBoatMachineTabController=uBoatMachineTabController;
    }
    public void setTheMachineSettingsDTO(TheMachineSettingsDTO theMachineSettingsDTO){
        uBoatMachineTabController.setTheMachineSettingsDTO(theMachineSettingsDTO);
    }
    private void showAlertMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setContentText(message);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();

    }
    public void setValues() throws Exception {
       // descriptions = mediator.getMachineConfiguration();
       // theMachineSettingsDTO = mediator.getTheMachineSettingsDTO();
        rotorsAmountLable.setText(theMachineSettingsDTO.getAmountOfUsedRotors() + " Rotors out of " + theMachineSettingsDTO.getMaxAmountOfRotors());
        ReflectorsAmountLable.setText(String.valueOf(theMachineSettingsDTO.getAmountOfReflectors()) + " Reflectors");
        messagesAmountLabel.setText(String.valueOf(theMachineSettingsDTO.getAmountOfProcessedMessages()) + " proceeded messages");
    }
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void setBattleName(String battleName) {
        this.battleName=battleName.trim();
    }
    public void setuBoatMachineTabController(UBoatMachineTabController uBoatMachineTabController) {
        this.uBoatMachineTabController = uBoatMachineTabController;
    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
       setValues();
    }
}