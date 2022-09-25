package component.mainWindowUBoat;
import engineManager.EngineManager;
import engineManager.EngineManagerInterface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import uiMediator.Mediator;
import utils.EventsHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class LoadFileController {
    Mediator mediator;
    private boolean isFileLoadSuccessfully;
    private boolean isFileNameValid;
    private List<EventsHandler> handlers = new ArrayList<>();
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

    private Stage primaryStage;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private SimpleBooleanProperty isXmlLoaded;
    ToggleGroup styleRadioButtonTg;
    private SimpleBooleanProperty isEnableAnimationsProperty;

    public LoadFileController(){
        isXmlLoaded= new SimpleBooleanProperty(false);
        alert = new Alert(Alert.AlertType.ERROR);
        handlers = new ArrayList<>();

    }
    public SimpleBooleanProperty isXmlLoadedProperty(){return isXmlLoaded;}
    @FXML
    public void initialize() {
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
    }

    private void fireEvent() throws Exception {
        EventObject myEvent = new EventObject(this);
        List<EventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (EventsHandler handler : handlers) {
            handler.eventHappened(myEvent);
        }
    }

    @FXML
    public void loadNewFileButtonActionListener(ActionEvent event) throws Exception {
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
        List<Exception> exceptionList = new ArrayList<>();
        if (mediator.fileNameValidation(absolutePath)) {

            try {
                exceptionList = mediator.isFileLoadSuccessfully(absolutePath).getListOfException();
                if (exceptionList.size() == 0) {
                    loadFileLabel.setText(absolutePath);
                    isXmlLoaded.set(true);
                   /* this.encryptDecryptTabController.getStatisticsController().resetStatisticsController();
                    this.encryptDecryptTabController.getEncryptDecryptController().reset();
                  */  isFileLoadSuccessfully = true;
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
    }
}