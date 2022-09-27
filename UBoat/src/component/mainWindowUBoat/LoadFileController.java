package component.mainWindowUBoat;
import com.sun.istack.internal.NotNull;
import engineManager.EngineManager;
import engineManager.EngineManagerInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import machineDTO.TheMachineSettingsDTO;
import okhttp3.*;
import uiMediator.Mediator;
import utils.Constants;
import utils.EventsHandler;
import utils.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

public class LoadFileController {
    Mediator mediator;
    private boolean isFileLoadSuccessfully;
    private boolean isFileNameValid;
    private List<EventsHandler> handlers=new ArrayList<>();
    @FXML
    public Button loadFileButton;
    @FXML
    public Label loadFileLabel;

    @FXML
    private RadioButton blueStyleRadioButton;
    @FXML
    private RadioButton darkStyleRadioButton;
    @FXML
    private RadioButton pinkStyleRadioButton;
    @FXML
    private MainWindowUBoatController mainWindowUBoatController;
   /* @FXML
    private EncryptDecryptTabController encryptDecryptTabController;
  */  @FXML
    private  CheckBox enableAnimationsCheckBox;
  String battleName;

    private Stage primaryStage;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private SimpleBooleanProperty isXmlLoaded;
    ToggleGroup styleRadioButtonTg;
    private SimpleBooleanProperty isEnableAnimationsProperty;
    private String uBoatUserName;
    private SimpleBooleanProperty isMachineDefined;

    public LoadFileController(){
        isXmlLoaded= new SimpleBooleanProperty(false);
        alert = new Alert(Alert.AlertType.ERROR);
        handlers = new ArrayList<>();
        isMachineDefined=new SimpleBooleanProperty(false);
    }
    public SimpleBooleanProperty isXmlLoadedProperty(){return isXmlLoaded;}
    @FXML
    public void initialize() {
        handlers = new ArrayList<>();
        isEnableAnimationsProperty =new SimpleBooleanProperty(false);
        EngineManagerInterface engineManager=new EngineManager();
         this.mediator=new Mediator(engineManager);
        //enableAnimationsCheckBox.selectedProperty().setValue(false);
        //styleRadioButtonTg = new ToggleGroup();
        //blueStyleRadioButton.setToggleGroup(styleRadioButtonTg);
       // blueStyleRadioButton.selectedProperty().set(true);
        //pinkStyleRadioButton.setToggleGroup(styleRadioButtonTg);
        //darkStyleRadioButton.setToggleGroup(styleRadioButtonTg);
    }
    @FXML
    void enableAnimationsCheckBoxActionListener(ActionEvent event) {
      if(enableAnimationsCheckBox.isSelected()){
          isEnableAnimationsProperty.setValue(true);
      }
      else {
          isEnableAnimationsProperty.setValue(false);
      }
    }
/*    @FXML
    private void setStyle(ActionEvent event) {
        Scene scene= mainWindowUBoatController.mainBorder.getScene();;

        if(blueStyleRadioButton.isSelected()){
            scene.getStylesheets().clear();

            scene.getStylesheets().add(getClass().getResource("/CSS/BlueStyle.css").toExternalForm());
          }
        else if(pinkStyleRadioButton.isSelected()){
            scene.getStylesheets().clear();

            scene.getStylesheets().add(getClass().getResource("/CSS/pinkStyle.css").toExternalForm());
        }
        else if(darkStyleRadioButton.isSelected()){
            scene.getStylesheets().clear();

            scene.getStylesheets().add(getClass().getResource("/CSS/darkStyle.css").toExternalForm());
        }


    }*/
    private void printListOfExceptions(List<Exception> exceptionList, String errorToAdd) {
        alert = new Alert(Alert.AlertType.ERROR);
        String errorMessage = "";
        for (Exception message : exceptionList) {
            errorMessage = errorMessage + message.getMessage() + "\n";
        }
        alert.setContentText(errorMessage + "\n" + errorToAdd);
        alert.setTitle("Error!");
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void addHandler(EventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    /*public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;
    }*/
    public void setAnimationsBinding(){
       // SimpleBooleanProperty isEnableAnimationsPropertyFromEncrypt=encryptDecryptTabController.getIsEnableAnimationsPropertyFromEncryptController();
       // SimpleBooleanProperty isEnableAnimationsPropertyFromBruteForce= mainWindowUBoatController.getBruteForceResultsTabController().getIsEnableAnimationsProperty();
       // isEnableAnimationsPropertyFromEncrypt.bind(isEnableAnimationsProperty);
        //isEnableAnimationsPropertyFromBruteForce.bind(isEnableAnimationsProperty);
    }

    public void setMainWindowUBoatController(MainWindowUBoatController mainWindowUBoatController) {
        this.mainWindowUBoatController = mainWindowUBoatController;
        bindIsMachineDefinedProperty();
    }

    private void fireEvent() throws Exception {
        EventObject myEvent = new EventObject(this);
        List<EventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (EventsHandler handler : handlers) {
            handler.eventHappened(myEvent);
        }
    }

/*    @FXML
    public void loadNewFileButtonActionListener(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String message = "";
*//*       FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();*//*
        String absolutePath="C:\\Users\\97254\\IdeaProjects\\BreakingTheEnigmaEx03\\EngimaEngine\\src\\resources\\ex3-basic.xml";
        List<Exception> exceptionList = new ArrayList<>();
        if (mediator.fileNameValidation(absolutePath)) {

            try {
                exceptionList = mediator.isFileLoadSuccessfully(absolutePath).getListOfException();
                if (exceptionList.size() == 0) {
                    loadFileLabel.setText(absolutePath);
                    isXmlLoaded.set(true);
                   *//* this.encryptDecryptTabController.getStatisticsController().resetStatisticsController();
                    this.encryptDecryptTabController.getEncryptDecryptController().reset();
                  *//*  isFileLoadSuccessfully = true;
                    message = "The xml was uploaded successfully";
                    alert.setContentText(message);
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();
                    fireEvent();
                } else {
                    printListOfExceptions(exceptionList, message);
                }
            } catch (Exception e) {
                printListOfExceptions(exceptionList,"The file is not valid");
            }
        }

    }*/
@FXML private void loadNewFileButtonActionListener(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select the xml file");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
    File selectedFile = fileChooser.showOpenDialog(primaryStage);
    if (selectedFile == null) {
        return;
    }
    try {
        loadXmlFileAndSendFileToServer(selectedFile);
    } catch (IOException ignore) {}
}

    public void setUserName(String uBoatUserName){
        this.uBoatUserName=uBoatUserName;
    }
    public Boolean loadXmlFileAndSendFileToServer(File selectedFile) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
       // String message="";
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart(uBoatUserName,selectedFile.getName(), RequestBody.create(selectedFile,
                                MediaType.parse("text/plain"))).build();

        Request request = new Request.Builder()
                .url(Constants.UPLOAD_XML_FILE)
                .post(body)
                .build();

        Call call= HttpClientUtil.getOkHttpClient().newCall(request);
        Response response=call.execute();
        if(response.code()==200){
            isMachineDefined.set(true);
            battleName=response.body().string();
            mainWindowUBoatController.setBattleName(battleName);
            Platform.runLater(() -> {
                loadFileLabel.setText(/*"Load status: Successfully"*/selectedFile.getPath());
               String message = "The xml was uploaded successfully";
               setMachineDetails();
               mainWindowUBoatController.getIsCodeDefined().setValue(false);
                alert.setContentText(message);
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();


            });}
        else{
            isMachineDefined.set(false);
            Platform.runLater(() -> {
                try {
                     String message = "Load status: error loading " +response.body().string();
                    alert.setContentText(message);
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();

                } catch (IOException e) {}
            });}
        return (response.code()==200);
    }

    public void setMachineDetails(){
        mainWindowUBoatController.setMachineDetails();
    }

    public void bindIsMachineDefinedProperty(){
      mainWindowUBoatController.getIsMachineDefined().bind(isMachineDefined);
    }


/*    @FXML private void loadNewFileButtonActionListener(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String message = "";
       FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("role", "uboat")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() { //todo i guess it should be sync no?

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
                    String threadsAmount=null;
                    try {
                        threadsAmount = (response.body()).string();
                    }
                    catch (IOException ignore){}
                    String finalThreadsAmount = threadsAmount;
                    Platform.runLater(() -> {
                        if(finalThreadsAmount !=null&&!finalThreadsAmount.equals("null")&&!finalThreadsAmount.equals("")) {
                            int threadsAmountInteger= Integer.parseInt(finalThreadsAmount);
                            //sController.updateUserNameAndRoleAndThreads(userName, threadsAmountInteger);
                        }
                        else{
                            // sController.updateUserNameAndRoleAndThreads(userName,amountOfThreadsProperty.getValue());
                        }
                        primaryStage.setScene(sControllerScene);
                        primaryStage.show();
                        // loadFileController.setMediator(mediator);
                    });
                }
                Objects.requireNonNull(response.body()).close();
            }
        });

    }*/
}