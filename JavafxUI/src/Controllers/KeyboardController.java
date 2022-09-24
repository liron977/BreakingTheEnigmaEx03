package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import mediator.Mediator;

import java.util.EventObject;


public class KeyboardController implements EventsHandler{
    Mediator mediator;

    @FXML
    private FlowPane inputKeyBoardFlowPane;

    @FXML
    private FlowPane outputKeyBoardFlowPane;
    Button[] keyboardButtonsInput;
    Button[] keyboardButtonsOutput;

    Button lastButtonColorChanged;
    @FXML
    private EncryptDecryptTabController encryptDecryptTabController;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;

    }
    public void addToKeyBoard(){
        clearKeyboard();
        String keyboard=mediator.getEngineManger().getTheMachineEngine().getKeyboard();
        keyboardButtonsInput=new Button[keyboard.length()];
        keyboardButtonsOutput=new Button[keyboard.length()];
        for (int i=0;i<keyboard.length();i++){
            Button buttonToInput=new Button(String.valueOf(keyboard.charAt(i)));
            buttonToInput.disableProperty().set(true);
            Button buttonToOutput=new Button(String.valueOf(keyboard.charAt(i)));
            buttonToOutput.disableProperty().set(true);
            buttonToInput.setOnAction((ev)->{
                if(lastButtonColorChanged!=null){
                    removeButtonColor(lastButtonColorChanged);
                }
                   String res= encryptDecryptTabController.getEncryptDecryptController().addToStringToConvertTextFiled(buttonToInput.getText());
                   changeButtonColor(getButtonByString(res));
            });
            keyboardButtonsInput[i]=buttonToInput;
            keyboardButtonsOutput[i]=buttonToOutput;
            inputKeyBoardFlowPane.getChildren().add(buttonToInput);
            outputKeyBoardFlowPane.getChildren().add(buttonToOutput);
        }

    }
   private Button getButtonByString(String str){
        for (int i=0;i<keyboardButtonsOutput.length;i++){
            if(keyboardButtonsOutput[i].getText().equals(str)){
                lastButtonColorChanged=keyboardButtonsOutput[i];
                return keyboardButtonsOutput[i];
            }
        }
            return  null;
    }
    public void changeButtonColor(Button button){
        button.setStyle("-fx-background-color: #ffd500; " +
                "-fx-text-fill: black;");

    }
    public void removeButtonColor(Button button){
        button.setStyle(null);

    }
    public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;

    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
        addToKeyBoard();
    }
    public void clearKeyboard(){
        outputKeyBoardFlowPane.getChildren().clear();
        inputKeyBoardFlowPane.getChildren().clear();

    }
    public void disableKeyboard(){
        if(lastButtonColorChanged!=null){
            removeButtonColor(lastButtonColorChanged);
        }
        for (Button b:keyboardButtonsInput) {
            b.disableProperty().setValue(true);
        }
        for (Button b:keyboardButtonsOutput) {
            b.disableProperty().setValue(true);
        }
    }
    public void unDisableKeyboard(){
        for (Button b:keyboardButtonsInput) {
            b.disableProperty().setValue(false);
        }


    }

  void changeButtonColorByString(String str){
      if(lastButtonColorChanged!=null){
          removeButtonColor(lastButtonColorChanged);
      }
        for(int i=0;i<keyboardButtonsOutput.length;i++){
            if(keyboardButtonsOutput[i].getText().equals(str)){
                lastButtonColorChanged=keyboardButtonsOutput[i];
                changeButtonColor(keyboardButtonsOutput[i]);
                break;
            }
        }


  }
}