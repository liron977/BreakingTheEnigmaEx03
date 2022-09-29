package component.alliesContest;

import bruteForce.AlliesDTO;
import component.mainWindowAllies.MainWindowAlliesController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;

public class AlliesContestController {
    MainWindowAlliesController mainWindowAlliesController;

    @FXML
    private Button submitButton;

    @FXML
    private TextField missionSizeTextField;

    private String alliesTeamName;
    private String selectedBattleField="";


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
}