package engine.validator;
import engine.theEnigmaEngine.Pair;
import engine.theEnigmaEngine.TheMachineEngine;
import java.util.ArrayList;
import java.util.List;

public class UserInputStartingPositionValidator implements Validator{

    private List<Exception> listOfException;
    private TheMachineEngine theMachineEngine;
    private String rotorsPosition;
    private List<Pair> pairsOfSwappingCharacter = new ArrayList<>();
    public UserInputStartingPositionValidator(String rotorsPosition,TheMachineEngine theMachineEngine) {
        this.listOfException = new ArrayList<>();
        this.theMachineEngine = theMachineEngine;
        this.rotorsPosition=rotorsPosition.toUpperCase();

    }
    private void isStartingPositionStructureIsValid() {
        char signal;
        for (int i = 0; i < rotorsPosition.length(); i++) {
            signal=rotorsPosition.charAt(i);
            if(!theMachineEngine.getKeyboard().contains(String.valueOf(signal))){
                listOfException.add(new Exception("The rotors position must be signal from the machine keyboard,the signal ["+signal+"] is not valid"));
            }
        }
    }
    private void isRotorsPositionAmountIsValid(){
        int amountOfUsedRotors= theMachineEngine.getAmountOfUsedRotors();
        if(rotorsPosition.length()>amountOfUsedRotors){
            listOfException.add(new Exception("You entered too much signals for stating position,please enter ["+amountOfUsedRotors+"] signals"));
        }
        else if(rotorsPosition.length()<amountOfUsedRotors){
            listOfException.add(new Exception("The amount of the starting position signals should be equal to the amount of the rotors,please enter ["+amountOfUsedRotors+"] signals"));
        }
    }
    @Override
    public void validate() {
        isStartingPositionStructureIsValid();
        isRotorsPositionAmountIsValid();
    }
    @Override
    public List<Exception> getListOfException() {
        return listOfException;
    }
}