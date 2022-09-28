package component;

import bruteForce.AgentInfoDTO;
import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import machineDTO.LimitedCodeConfigurationDTO;
import okhttp3.*;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Objects;

public class AgentLoginController {
    @FXML
    private TextField agentNameTextField;
    @FXML
    private ComboBox<String> alliesTeamComboBox;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private TextField missionAmountTextFiled;
    @FXML
    private Slider threadsAmountSlider;
    private String selectedAlliesTeamName;
    private String agentName;
    private int missionAmount;
    private int threadsAmount;
    private final StringProperty errorMessageProperty;
    AgentInfoDTO agentInfoDTO;

    public AgentLoginController() {
        this.errorMessageProperty = new SimpleStringProperty("");
    }

    @FXML public void initialize() {
        loginErrorLabel.textProperty().bind(errorMessageProperty);
    }


    private void collectDataFromLoginForm(){
        selectedAlliesTeamName=alliesTeamComboBox.getValue();
        agentName=agentNameTextField.getText();
        missionAmount=Integer.parseInt(missionAmountTextFiled.getText());
        threadsAmount=(int) threadsAmountSlider.getValue();
         agentInfoDTO=new AgentInfoDTO(agentName,threadsAmount,missionAmount,selectedAlliesTeamName);
    }
    @FXML
    void loginButtonClicked(ActionEvent event) {
     collectDataFromLoginForm();
        String finalUrl =HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", agentName)
                .addQueryParameter("role", "agent")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {


            @Override public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                }
                else {


                    //mainWindowAlliesController.setAlliesTeamName(alliesTeamName);
                    Platform.runLater(() -> {
                  /*      mainWindowAlliesController.updateAgentsTableView();
                        primaryStage.setScene(mainWindowAlliesControllerScene);
                        primaryStage.show();*/
                        // loadFileController.setMediator(mediator);
                    });
                }
                Objects.requireNonNull(response.body()).close();
            }
        });
    }
    private void addAgentToAlliesTeam(){
        String agentInfoDTOGson = Constants.GSON_INSTANCE.toJson(agentInfoDTO);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), agentInfoDTOGson);
        String finalUrl = HttpUrl
                .parse(Constants.AGENTS_INFO_TABLE_VIEW)
                .newBuilder()
                .addQueryParameter("alliesTeamName", selectedAlliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
                Platform.runLater(() -> {
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
                });
            } else {
                Platform.runLater(() -> {
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    }
                });
            }
        } catch (IOException e) {
        }
    }

}
