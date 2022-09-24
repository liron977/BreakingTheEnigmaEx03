package Controllers;

import animatefx.animation.ZoomIn;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import machineDTO.ListOfExceptionsDTO;
import mediator.Mediator;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class EncryptDecryptController {

    Mediator mediator;

   private String convertedString="";
    private int stringToConvertIndex=0;
    private List<EventsHandler> handlers=new ArrayList<>();
    private String stringToConvert="";

    ToggleGroup modeToggleGroup=new ToggleGroup();


    @FXML
    private Pane encryptDecrypt;

    @FXML
    private TextArea stringToConvertTextFiled;

    @FXML
    private Button resetButton;

    @FXML
    private Button processOrDoneButton;

    @FXML
    private Label convertedStringLabel;
    @FXML
    private RadioButton automaticModeRadioButton;

    @FXML
    private RadioButton manuallyModeRadioButton;

    @FXML
    private Button clearButton;
    @FXML
    private EncryptDecryptTabController encryptDecryptTabController;

    private SimpleBooleanProperty isEnableAnimationsProperty;

    public SimpleBooleanProperty getIsEnableAnimationsProperty(){
        return isEnableAnimationsProperty;
    }


    @FXML
    void resetButtonActionListener(ActionEvent event) throws Exception {
        mediator.resetCurrentCode();
        convertedStringLabel.setText("");
        stringToConvertTextFiled.setText("");
        stringToConvertIndex=0;
        convertedString="";
        stringToConvert="";
        fireEvent();
    }


    public RadioButton getManuallyModeRadioButton() {
        return manuallyModeRadioButton;
    }
public ToggleGroup getModeToggleGroup() {
    return modeToggleGroup;
}

    public String getProcessOrDoneButton() {
        return processOrDoneButton.getText();
    }
    public String addToStringToConvertTextFiled(String stringToAdd){
        stringToConvertTextFiled.setText(stringToConvertTextFiled.getText()+stringToAdd);
        return  String.valueOf(convertedStringLabel.getText().charAt(convertedStringLabel.getText().length()-1));

    }
    public void initialize(){
        automaticModeRadioButton.setToggleGroup(modeToggleGroup);
        manuallyModeRadioButton.setToggleGroup(modeToggleGroup);
        isEnableAnimationsProperty=new SimpleBooleanProperty(true);
        modeToggleGroup.getToggles().get(0).setSelected(true);
        stringToConvertTextFiled.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                stringToConvertTextFiledAction();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
 private void stringToConvertTextFiledAction() throws Exception {
        if(processOrDoneButton.getText().equals("Done")){
            convertStringManually();
            fireEvent();
        }
 }
    @FXML
    void clearButtonActionListener(ActionEvent event) {
        convertedStringLabel.setText("");
        stringToConvertTextFiled.setText("");
        stringToConvert="";
        convertedString="";
    }
    @FXML
    void automaticModeRadioButtonOnListener(MouseEvent event) {
        stringToConvertTextFiled.setText("");
        convertedStringLabel.setText("");
        convertedString="";
        stringToConvert="";
        stringToConvertIndex=0;
        processOrDoneButton.setText("Process");

    }
    @FXML
    void manuallyModeRadioButtonOnListener(MouseEvent event) {
        stringToConvertTextFiled.setText("");
        convertedStringLabel.setText("");
        convertedString="";
        stringToConvert="";
        stringToConvertIndex=0;
        processOrDoneButton.setText("Done");
    }

    private void fireEvent () throws Exception {
        EventObject myEvent = new EventObject(this);
        List<EventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (EventsHandler handler : handlers) {
            handler.eventHappened(myEvent);
        }
    }
    public void addHandler (EventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }
    public void convertStringAutomatically() throws Exception {
        ListOfExceptionsDTO listOfExceptionsDTO = mediator.getAllErrorsRelatedToUserStringToConvert(stringToConvertTextFiled.getText());
        if (listOfExceptionsDTO.getListOfException().size() != 0) {
            printListOfExceptions(listOfExceptionsDTO);
        }
        else {
            convertedString = mediator.getConvertedString(stringToConvertTextFiled.getText());
            convertedStringLabel.setText(convertedString);
            if(isEnableAnimationsProperty.getValue()) {
                new ZoomIn(convertedStringLabel).play();
            }
            mediator.addToHistoryAndStatistics(stringToConvertTextFiled.getText(), convertedString);
            fireEvent();
        }
    }
  private void convertStringManually() throws Exception {
        String signal;
        int amountOfSignalToProcess=stringToConvertTextFiled.getText().length()-stringToConvertIndex;
      String currentString=stringToConvertTextFiled.getText();
      int indexToDelete=-1;
      boolean flag=false;
        String tmpStrFirstPart,tmpstrSecondPart;
        if(amountOfSignalToProcess<0) {
            amountOfSignalToProcess = Math.abs(amountOfSignalToProcess);
            if (currentString.equals("")) {
                stringToConvert = "";
                convertedString = "";
                convertedStringLabel.setText(convertedString);
            }
          else {  for (int j = 0; j < amountOfSignalToProcess; j++) {
                for (int i = 0; i < stringToConvert.length() - 1; i++) {
                    if (currentString.charAt(i) != stringToConvert.charAt(i)) {
                        indexToDelete = i;
                        flag = true;
                        tmpStrFirstPart = stringToConvert.substring(0, i);
                        tmpstrSecondPart = stringToConvert.substring(i + 1, stringToConvert.length());
                        stringToConvert = tmpStrFirstPart + tmpstrSecondPart;
                        tmpStrFirstPart = convertedString.substring(0, i);
                        tmpstrSecondPart = convertedString.substring(i + 1, convertedString.length());
                        convertedString = tmpStrFirstPart + tmpstrSecondPart;
                        convertedStringLabel.setText(convertedString);
                        break;
                    }
                }
                if (!flag) {
                    stringToConvert = stringToConvert.substring(0, stringToConvert.length() - 1);
                    convertedStringLabel.setText(convertedString.substring(0, convertedString.length() - 1));
                    convertedString = convertedString.substring(0, convertedString.length() - 1);
                }
            }
        }
            stringToConvertIndex=stringToConvertTextFiled.getText().length();
        }
        else {
            for (int i = 0; i < (amountOfSignalToProcess); i++) {
                signal = stringToConvertTextFiled.getText(stringToConvertIndex, stringToConvertIndex + 1);
                ListOfExceptionsDTO listOfExceptionsDTO = mediator.getAllErrorsRelatedToUserStringToConvert(signal);
                if (listOfExceptionsDTO.getListOfException().size() != 0) {
                    printListOfExceptions(listOfExceptionsDTO);
                    stringToConvertTextFiled.setText(stringToConvertTextFiled.getText().substring(0, stringToConvertIndex));

                } else {
                    String str=mediator.getConvertedString(signal);
                    encryptDecryptTabController.changeButtonColorByString(str);
                    convertedString += str;
                    stringToConvert=stringToConvertTextFiled.getText();
                    convertedStringLabel.setText(convertedString);
                    stringToConvertIndex++;
                }
            }
        }
        if ((stringToConvertTextFiled.getText().equals(""))||(stringToConvertTextFiled.getText().equals(null))){
            stringToConvertIndex = 0;
            convertedString="";
        }
        if(isEnableAnimationsProperty.getValue()) {
            new ZoomIn(convertedStringLabel).play();
        }
    }

    private void printListOfExceptions(ListOfExceptionsDTO listOfExceptionsDTO) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String errorMessage="";
        for (Exception message : listOfExceptionsDTO.getListOfException()) {
            errorMessage=errorMessage+message.getMessage()+"\n";
        }
        alert.setContentText(errorMessage);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;

    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
   private void doneButtonAction() throws Exception {
        mediator.addToHistoryAndStatistics(stringToConvertTextFiled.getText(),convertedStringLabel.getText());
        stringToConvert="";
        stringToConvertIndex=0;
        convertedString="";
        stringToConvertTextFiled.setText("");
        convertedStringLabel.setText("");
        fireEvent();
    }
    @FXML
    void processOrDoneButtonActionListener (ActionEvent event) throws Exception{
        if(!stringToConvertTextFiled.getText().equals("")) {
            if (processOrDoneButton.getText().equals("Done")) {
                doneButtonAction();
            } else {
                convertStringAutomatically();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage="Please insert a string to convert";
            alert.setContentText(errorMessage);
            alert.getDialogPane().setExpanded(true);
            alert.showAndWait();
        }
    }
    public void reset(){
        stringToConvertIndex=0;
        stringToConvert="";
        convertedString="";
        stringToConvertTextFiled.setText("");
        convertedStringLabel.setText("");
        modeToggleGroup.getToggles().get(0).setSelected(true);
        processOrDoneButton.setText("Process");
    }

}