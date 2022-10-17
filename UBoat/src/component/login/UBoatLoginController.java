package component.login;

import com.sun.istack.internal.NotNull;
import component.mainWindowUBoat.LoadFileController;
import component.mainWindowUBoat.MainWindowUBoatController;
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

public class UBoatLoginController {

    @FXML
    private TextField uBoatNameTextField;

    @FXML
    private Button loginButton;
    @FXML
    private Label loginErrorLabel;

    String userName;

    private final StringProperty errorMessageProperty;
    private MainWindowUBoatController mainWindowUBoatController;
    private Stage primaryStage;
    private Scene sControllerScene;
    private SimpleDoubleProperty amountOfThreadsProperty;
    private Mediator mediator;
    private LoadFileController loadFileController;

    public UBoatLoginController() {
        errorMessageProperty = new SimpleStringProperty("");
        amountOfThreadsProperty=new SimpleDoubleProperty();
    }

    @FXML public void initialize() {
        loginErrorLabel.textProperty().bind(errorMessageProperty);
        uBoatNameTextField.clear();
        //loadFileController=new LoadFileController();
    }

    @FXML private void loginButtonClicked(ActionEvent event) {
        userName = uBoatNameTextField.getText();

        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        loginButton.setDisable(true);
        uBoatNameTextField.setDisable(true);
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("role", "uboat")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() { //todo i guess it should be sync no?

            @Override public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->{
                            loginButton.setDisable(false);
                            uBoatNameTextField.setDisable(false);
                            errorMessageProperty.set("Something went wrong: " + e.getMessage());
                        }
                );
            }

            @Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->{
                            loginButton.setDisable(false);
                    uBoatNameTextField.setDisable(false);
                            errorMessageProperty.set("Something went wrong: " + responseBody);
                    } );
                }
                else {
                    mainWindowUBoatController.setUserName(userName);
                    Platform.runLater(() -> {
                     /*   mainWindowUBoatController.updateAlliesTableView();*/
                        primaryStage.setScene(sControllerScene);
                        primaryStage.centerOnScreen();
                        primaryStage.setTitle("Enigma-UBoat: "+userName);
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
        URL superScreenUrl = getClass().getResource("/component/mainWindowUBoat/MainWindowUBoat.fxml");
        fxmlLoader.setLocation(superScreenUrl);
        try {
            Parent root1 = fxmlLoader.load(superScreenUrl.openStream());
           mainWindowUBoatController =fxmlLoader.getController();
            mainWindowUBoatController.setPrimaryStage(primaryStage);
          //  primaryStage.setTitle("Enigma-UBoat: "+userName);
            sControllerScene = new Scene(root1);
            primaryStage.setMinHeight(300f);
            primaryStage.setMinWidth(400f);
            sControllerScene.getStylesheets().add(getClass().getResource("/utils/CSS//BlueStyle.css").toExternalForm());
            EngineManagerInterface engineManager=new EngineManager();
            Mediator mediator=new Mediator(engineManager);
            mainWindowUBoatController.setMediator(mediator);

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