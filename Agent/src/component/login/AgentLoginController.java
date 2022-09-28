package component.login;

import bruteForce.AgentInfoDTO;
import com.sun.istack.internal.NotNull;
import component.mainWindowAgent.AgentMainWindowController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import okhttp3.*;
import uiMediator.Mediator;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import static utils.Constants.REFRESH_RATE;

public class AgentLoginController {

    private Timer timer;
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
    private Stage primaryStage;
    AlliesTeamsRefresher alliesTeamsRefresher;
    AgentMainWindowController agentMainWindowController;

    private String agentName;
    private int missionAmount;
    private int threadsAmount;
    private final StringProperty errorMessageProperty;

    private Scene mainWindowAlliesControllerScene;
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
    public void startListRefresher() {
        alliesTeamsRefresher = new AlliesTeamsRefresher(
                this::updateAlliesTeamNamesComboBox);
        timer = new Timer();
        timer.schedule(alliesTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void updateAlliesTeamNamesComboBox(List<String> agentInfoDTOList) {
        Platform.runLater(() -> {
            for (String s: alliesTeamComboBox.getItems()) {
                if(!agentInfoDTOList.contains(s))
                {
                    alliesTeamComboBox.getItems().remove(s);
                }
            }
            for (String str:agentInfoDTOList) {
               if(!alliesTeamComboBox.getItems().contains(str)) {
                   alliesTeamComboBox.getItems().add(str);
               }

            }

        });
    }

    @FXML private void loginButtonClicked(ActionEvent event) {
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
                        addAgentToAlliesTeam();
                        primaryStage.setScene(mainWindowAlliesControllerScene);
                        primaryStage.show();
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
    public void setPrimaryStageAndLoadInTheBackgroundSuperScreen(Stage primaryStageIn){
        primaryStage=primaryStageIn;
        loadSuperScreen();
    }
    private void loadSuperScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL superScreenUrl = getClass().getResource("/component/mainWindowAgent/AgentMainWindow.fxml");
        fxmlLoader.setLocation(superScreenUrl);
        try {
            Parent root1 = fxmlLoader.load(superScreenUrl.openStream());
            agentMainWindowController=fxmlLoader.getController();
            agentMainWindowController.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Enigma-Allies");
            mainWindowAlliesControllerScene = new Scene(root1);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            mainWindowAlliesControllerScene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
             //sController.setMediator(mediator);

        }
        catch (IOException ignore) {
            ignore.printStackTrace();} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}