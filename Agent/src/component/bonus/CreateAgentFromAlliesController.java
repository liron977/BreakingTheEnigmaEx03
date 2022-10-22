package component.bonus;

import bruteForce.AgentInfoDTO;
import bruteForce.ContestStatusInfoDTO;
import com.sun.istack.internal.NotNull;
import component.AgentDashboard.AgentDashboardController;
import constants.Constants;
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
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class CreateAgentFromAlliesController {

    @FXML
    private TextField agentNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private TextField missionAmountTextFiled;

    @FXML
    private Slider threadsAmountSlider;

    @FXML
    private TextField alliesNameTextField;
    private Timer timer;

    private String selectedAlliesTeamName;
    ContestStatusInfoDTO contestStatusInfoDTOFromGson;
    private Stage primaryStage;
    AgentDashboardController agentDashboardController;
    private Timer timerForGetMissions;
    private TimerTask getMissionsRefresher;

    private String agentName;
    private int missionAmount;
    private int threadsAmount;
    private final StringProperty errorMessageProperty;

    private Scene mainWindowAlliesControllerScene;
    AgentInfoDTO agentInfoDTO;

    public CreateAgentFromAlliesController() {
        this.errorMessageProperty = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        loginErrorLabel.textProperty().bind(errorMessageProperty);
    }

    public void setAlliesName(String alliesName){
        alliesNameTextField.setText(alliesName);
    }
    public void setContestStatusInfoDTOFromGson(ContestStatusInfoDTO contestStatusInfoDTOFromGson) {
        this.contestStatusInfoDTOFromGson = contestStatusInfoDTOFromGson;
    }

    private void collectDataFromLoginForm(){
        selectedAlliesTeamName=alliesNameTextField.getText();
        agentName=agentNameTextField.getText();
        // missionAmount=Integer.parseInt(missionAmountTextFiled.getText());
        threadsAmount=(int) threadsAmountSlider.getValue();

    }
    @FXML private void loginButtonClicked(ActionEvent event) {
        collectDataFromLoginForm();
        boolean dataIsValid=isDataValid();
        boolean dataIsEmpty=isDataEmpty();
        if(dataIsValid && !dataIsEmpty){
            agentInfoDTO=new AgentInfoDTO(agentName,threadsAmount,missionAmount,selectedAlliesTeamName);

            executeLogin();
        }
    }
    private boolean isDataEmpty() {
        boolean dataIsEmpty=false;
        if(agentName.isEmpty()){
            displayErrors("Please enter user name.");
            dataIsEmpty=true;
        }
        return dataIsEmpty;

    }
    private boolean isDataValid(){
        boolean isMissionsAmountValid=true;

        try {
            missionAmount = Integer.parseInt(missionAmountTextFiled.getText());
        }
        catch (Exception e){
            displayErrors("The mission amount should be a number \n between 1 - 2,147,483,647");
            isMissionsAmountValid=false;
        }
        if(missionAmount<=0&&isMissionsAmountValid){
            displayErrors("The mission amount should be positive.\n Please insert positive number.");
            isMissionsAmountValid=false;
        }
        return isMissionsAmountValid;
    }

    private boolean addAgentToAlliesTeam(){
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
            if (response.code() == 200) {
                return true;
            }
            else if(response.code() == 409){
                return false;
            }
            else if (response.code() != 200) {
             /*   Platform.runLater(() -> {
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
               /* Platform.runLater(() -> {
                    {
                        AgentThreadTask agentThreadTask=new AgentThreadTask(agentDashboardController);
                        new Thread(agentThreadTask).start();
                        //startGetMissionsRefresher();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    }
                });*/
            }
        } catch (IOException e) {
        }
        return false;
    }

    public void executeLogin(){
        agentNameTextField.setDisable(true);
        loginButton.setDisable(true);
        String finalUrl =HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", agentName)
                .addQueryParameter("role", "agent")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->{
                            agentNameTextField.setDisable(false);
                            loginButton.setDisable(false);
                            errorMessageProperty.set("Something went wrong: " + e.getMessage());
                        }
                );
            }

            @Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    agentNameTextField.setDisable(false);
                    loginButton.setDisable(false);
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set(responseBody)
                    );
                }
                else {
                    boolean isAddedSuccessfully=addAgentToAlliesTeam();
                    while(!isAddedSuccessfully){
                        Platform.runLater(() -> {
                            errorMessageProperty.set("The Allies Team is in the middle of a contest, please wait.");
                        });
                        isAddedSuccessfully=addAgentToAlliesTeam();
                    }
                    if(isAddedSuccessfully) {
                        //mainWindowAlliesController.setAlliesTeamName(alliesTeamName);
                        Platform.runLater(() -> {
                            loadSuperScreen();
                         Stage primaryStage =new Stage();
                            primaryStage.setScene(mainWindowAlliesControllerScene);
                            primaryStage.centerOnScreen();
                            primaryStage.show();
                        });
                    }
                }
                Objects.requireNonNull(response.body()).close();
            }
        });
    }
    public void setPrimaryStageAndLoadInTheBackgroundSuperScreen(Stage primaryStageIn){
        primaryStage=primaryStageIn;

    }
    private void loadSuperScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage primaryStage=new Stage();
        URL superScreenUrl = getClass().getResource("/component/AgentDashboard/AgentDashboard.fxml");
        fxmlLoader.setLocation(superScreenUrl);
        try {
            Parent root1 = fxmlLoader.load(superScreenUrl.openStream());
            agentDashboardController=fxmlLoader.getController();
            agentDashboardController.setPrimaryStage(primaryStage);
            agentDashboardController.setAgentInfoDTO(agentInfoDTO);
            agentDashboardController.setAmountOfMissionsPerAgent(String.valueOf(missionAmount));
            agentDashboardController.setSelectedAlliesTeamName(selectedAlliesTeamName);
            agentDashboardController.setAmountOfThreads(threadsAmount);
            agentDashboardController.startContestTableViewRefresher();
            agentDashboardController.startCheckIfNewContestRefresher();
           // agentDashboardController.setAgentLoginController(this);
            //  agentDashboardController.startContestStatusRefresher();
            primaryStage.setTitle("Agent: "+agentName);
            mainWindowAlliesControllerScene = new Scene(root1);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            mainWindowAlliesControllerScene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
      /*      primaryStage.centerOnScreen();
            primaryStage.show();*/
        }
        catch (IOException ignore) {
            ignore.printStackTrace();} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void displayErrors(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
   /* public void startGetMissionsRefresher() {
        getMissionsRefresher = new GetMissionsRefresher(agentDashboardController);
        timerForGetMissions = new Timer();
        timerForGetMissions.schedule(getMissionsRefresher, REFRESH_RATE, REFRESH_RATE);
    }*/

}
