package engine.validator;

import engine.theEnigmaEngine.Reflector;
import engine.theEnigmaEngine.TheMachineEngine;
import java.util.ArrayList;
import java.util.List;

public class UserInputReflectorValidator implements Validator{
    private List<Exception> listOfException;
    private String reflectorId;
    private TheMachineEngine theMachineEngine;
    private String userChosenInput;

    public UserInputReflectorValidator(String userChosenInput, TheMachineEngine theMachineEngine) {
        this.listOfException = new ArrayList<>();
        this.theMachineEngine = theMachineEngine;
        this.userChosenInput=userChosenInput;
        this.reflectorId="";
    }
    private boolean isUserChosenInputIsValid(){
        for (char signal : userChosenInput.toCharArray()) {
            if (!Character.isDigit(signal)){
                listOfException.add(new Exception("Insert only numbers between 1-5,the character ["+signal+"] is not valid!"));
                return false;
            }
        }
        if((userChosenInput.length()>1)||(userChosenInput.length()<1)){
            listOfException.add(new Exception("Insert only numbers between 1-5"));
            return false;
        }
       else {
            char signal = userChosenInput.charAt(0);
            if ((signal > '5') || (signal < '1')) {
                listOfException.add(new Exception("Insert only numbers between 1-5"));
                return false;
            }
        }
       return true;
    }
    private void isReflectorIdValid(){
        if(!theMachineEngine.getReflectorsSet().searchReflectorById(reflectorId)){
            listOfException.add(new Exception("The reflector ID is not exist in the machine, please enter one of the following: {"+getMachineReflectorsID(theMachineEngine.getReflectorsSet().getListOfReflectors())+"}"));
        }
    }
    private String getMachineReflectorsID(List<Reflector> MachineReflectors){
String machineReflectorsID="";
        for (Reflector reflector:MachineReflectors) {
            if(machineReflectorsID!=""){
                machineReflectorsID=machineReflectorsID+",";
            }
            machineReflectorsID=machineReflectorsID+ reflector.getReflectorId();
        }
        return machineReflectorsID;

    }
  private void convertUserChooseToId() {
      switch (userChosenInput) {
          case "1":
              this.reflectorId = "I";
              break;
          case "2":
              this.reflectorId = "II";
              break;
          case "3":
              this.reflectorId = "III";
              break;
          case "4":
              this.reflectorId = "IV";
              break;
          case "5":
              this.reflectorId = "V";
              break;
      }
  }
    @Override
    public void validate() {
      //  if(isUserChosenInputIsValid()) {
            //convertUserChooseToId();
            isReflectorIdValid();

        //}
    }
    @Override
    public List<Exception> getListOfException() {
        return listOfException;
    }
    public String getReflectorId(){
        return reflectorId;
    }
}