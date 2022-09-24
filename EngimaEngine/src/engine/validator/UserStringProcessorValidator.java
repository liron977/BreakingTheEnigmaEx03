package engine.validator;

import engine.theEnigmaEngine.TheMachineEngine;

import java.util.ArrayList;
import java.util.List;

public class UserStringProcessorValidator implements Validator {
    private List<Exception> listOfException;
    private String userInput;
    private String keyboard;

    public UserStringProcessorValidator(String userInput, TheMachineEngine enigmaMachine){
        this.userInput=userInput;
        listOfException=new ArrayList<>();
        keyboard=enigmaMachine.getKeyboard();

    }
    public void validate(){
        isValidByKeyboard();

    }
    private void isValidByKeyboard(){

        for (int i=0;i<userInput.length();i++) {
           if(!keyboard.contains(String.valueOf(userInput.charAt(i)))){
               listOfException.add(new Exception("The signal ["+userInput.charAt(i)+"] is not valid you should insert input that exist in the following keyboard:"+keyboard));
           }
        }
    }
    public List<Exception> getListOfException(){
        return listOfException;
    }
}