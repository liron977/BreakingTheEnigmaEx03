package engine.validator;

import engine.theEnigmaEngine.Rotor;
import engine.theEnigmaEngine.TheMachineEngine;
import java.util.ArrayList;
import java.util.List;

public class UserInputRotorsValidator implements Validator {
    private String userInput;
    private List<Exception> listOfException;
    private TheMachineEngine theMachineEngine;
    private String[] rotorsId;

    public UserInputRotorsValidator(String userInput, TheMachineEngine theMachineEngine) {
        this.listOfException = new ArrayList<>();
        this.userInput = userInput.toUpperCase();
        this.theMachineEngine = theMachineEngine;
    }

    private void isRotorsIdFromUserInputIsValid() {
        int numberOfRotorsFromTheFile = theMachineEngine.getAmountOfUsedRotors();
        if (rotorsId.length > numberOfRotorsFromTheFile) {
            listOfException.add(new Exception("The number of rotors you entered is more than it should be,please enter [" + numberOfRotorsFromTheFile + "] Rotors ID's"));
        } else if (rotorsId.length < numberOfRotorsFromTheFile) {
            listOfException.add(new Exception("The number of rotors you entered is less than it should be,please enter [" + numberOfRotorsFromTheFile + "] Rotors ID's"));

        } else if (rotorsId.length == 0) {
            listOfException.add(new Exception("You didn't entered rotors id,Please enter [" + numberOfRotorsFromTheFile + "] Rotors ID's"));
        } else {
            for (String rotorsId : rotorsId) {
                if (!theMachineEngine.getRotorsSet().isRotorsIdExists(rotorsId)) {
                    listOfException.add(new Exception("The rotor id [" + rotorsId + "] does not exists in the machine,please choose "+numberOfRotorsFromTheFile+" of the following ID's: {"+getMachineRotorsId(theMachineEngine.getRotorsSet().getListOfRotors())+"}"));
                }
            }
        }
    }
    private String getMachineRotorsId(List<Rotor> machineRotors){
        String machineRotorsId="";
        for (Rotor rotor:machineRotors) {
            if(machineRotorsId!=""){
                machineRotorsId=machineRotorsId+",";
            }
            machineRotorsId=machineRotorsId+ rotor.getRotorId();
        }
        return machineRotorsId;
    }
    private void isRotorsIdIsANumber(){
        boolean isNumeric;
        for (String id:rotorsId) {
            isNumeric = id.chars().allMatch( Character::isDigit );
            if(!isNumeric){
                listOfException.add(new Exception("The rotors id can be numbers only,the rotor id: [" +id +"] is not valid"));
            }
        }
    }

    private void isRotorIDIsUniq(){
        for(int i=0;i<rotorsId.length;i++) {
            String str=rotorsId[i];
            for(int j=i+1;j<rotorsId.length;j++){
                if(str.equals(rotorsId[j])){
                    listOfException.add(new Exception("You entered the same rotors id ["+str.toString()+"]"));
                    break;
                }
            }
        }
    }
    private boolean isRotorIdStructureIsValid() {
        for (int i = 0; i < userInput.length(); i++) {
            if ((!Character.isDigit((userInput.charAt(i)))) && (userInput.charAt(i) != ',')) {
                listOfException.add(new Exception("The input is not valid you need to enter numbers separated by a comma"));
                return false;
            }
        }
        return true;
    }
    public  String[] getFilteredUserInput(){
        return rotorsId;
    }
    private void splitTheUserInputToGetTheRotorsId(){
        this.rotorsId=userInput.split(",");
    }

    @Override
    public void validate() {
        if(isRotorIdStructureIsValid()) {
           splitTheUserInputToGetTheRotorsId();
            isRotorsIdFromUserInputIsValid();
            isRotorIDIsUniq();
            isRotorsIdIsANumber();


        }
    }
    @Override
    public List<Exception> getListOfException() {
        return listOfException;
    }
}