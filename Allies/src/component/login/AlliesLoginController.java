package component.login;

import com.sun.istack.internal.NotNull;


import component.mainWindowAllies.MainWindowAlliesController;
import machineEngine.EngineManager;
import machineEngine.EngineManagerInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import uiMediator.Mediator;
import constants.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AlliesLoginController {

    @FXML
    private TextField uBoatNameTextField;

    @FXML
    private Button loginButton;
    @FXML
    private Label loginErrorLabel;

    String alliesTeamName;

    private final StringProperty errorMessageProperty;
    private MainWindowAlliesController mainWindowAlliesController;
    private Stage primaryStage;
    private Scene mainWindowAlliesControllerScene;
    private SimpleDoubleProperty amountOfThreadsProperty;
    private Mediator mediator;



    public AlliesLoginController() {
        errorMessageProperty = new SimpleStringProperty("");
        amountOfThreadsProperty=new SimpleDoubleProperty();
    }

    @FXML public void initialize() {
        loginErrorLabel.textProperty().bind(errorMessageProperty);
        uBoatNameTextField.clear();
        //loadFileController=new LoadFileController();
    }

    @FXML private void loginButtonClicked(ActionEvent event) {

        alliesTeamName = uBoatNameTextField.getText();

        if (alliesTeamName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        uBoatNameTextField.setDisable(true);
        loginButton.setDisable(true);
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_LOGIN)
                .newBuilder()
                .addQueryParameter("username", alliesTeamName)
                .addQueryParameter("role", "allies")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {


            @Override public void onFailure(@NotNull Call call, @NotNull IOException e) {
              /*  Platform.runLater(() ->{
                            uBoatNameTextField.setDisable(false);
                            loginButton.setDisable(false);
                            errorMessageProperty.set("Something went wrong: " + e.getMessage());
                        }
                );*/
            }

            @Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    uBoatNameTextField.setDisable(false);
                    loginButton.setDisable(false);
                    String responseBody = response.body().string();
                    Platform.runLater(() ->{
                        uBoatNameTextField.setDisable(false);
                        loginButton.setDisable(false);
                        errorMessageProperty.set("Something went wrong: " + responseBody);

                            }
                    );
                }
                else {
                    String threadsAmount=null;
                    try {
                        threadsAmount = (response.body()).string();
                    }
                    catch (IOException ignore){}
                    mainWindowAlliesController.setAlliesTeamName(alliesTeamName);
                    Platform.runLater(() -> {
                        mainWindowAlliesController.updateAgentsTableView();
                        mainWindowAlliesController.updateUBoatContestTableView();
                        primaryStage.setScene(mainWindowAlliesControllerScene);
                        primaryStage.centerOnScreen();
                        primaryStage.setTitle("Enigma-Allies App: "+alliesTeamName);
                        primaryStage.show();
                       // loadFileController.setMediator(mediator);
                    });
                }
                Objects.requireNonNull(response.body()).close();
            }
        });

    }


    private void loadSuperScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL superScreenUrl = getClass().getResource("/component/mainWindowAllies/MainWindowAllies.fxml");
        fxmlLoader.setLocation(superScreenUrl);
        try {
            Parent root1 = fxmlLoader.load(superScreenUrl.openStream());
            mainWindowAlliesController=fxmlLoader.getController();
            mainWindowAlliesController.setPrimaryStage(primaryStage);
            //primaryStage.setTitle("Enigma-Allies- "+alliesTeamName);
            mainWindowAlliesControllerScene = new Scene(root1);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            mainWindowAlliesControllerScene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
            EngineManagerInterface engineManager=new EngineManager();
            Mediator mediator=new Mediator(engineManager);
            //sController.setMediator(mediator);

        }
        catch (IOException ignore) {
            ignore.printStackTrace();} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML  private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }
    @FXML private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    public void setPrimaryStageAndLoadInTheBackgroundSuperScreen(Stage primaryStageIn){
        primaryStage=primaryStageIn;
       loadSuperScreen();
    }
    public void setMediator(Mediator mediator) throws Exception {
        this.mediator = mediator;
       // loadFileComponentController.setMediator(mediator);
    }
}