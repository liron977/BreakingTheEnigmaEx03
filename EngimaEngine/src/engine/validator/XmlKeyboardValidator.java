package engine.validator;

import schemaGenerated.CTEEnigma;

import java.util.ArrayList;
import java.util.List;

public class XmlKeyboardValidator implements Validator{

    private CTEEnigma enigmaDescriptor;
    private List<Exception> errors;

    public XmlKeyboardValidator(CTEEnigma enigmaDescriptor) {
        this.enigmaDescriptor = enigmaDescriptor;
        errors=new ArrayList<>();
    }
    @Override
    public void validate() {
        isKeyboardSizeIsEven();
        isKeyboardIsNotEmpty();
    }

    @Override
    public List<Exception> getListOfException() {
        return errors;
    }
    private void isKeyboardSizeIsEven(){
        int inputSize=enigmaDescriptor.getCTEMachine().getABC().trim().length();
        if((inputSize%2)!=0){
            errors.add(new Exception("The ABC size need to be even,the ABC size that you entered is ["+inputSize +"]"));
        }
    }
    private void isKeyboardIsNotEmpty(){
        if(enigmaDescriptor.getCTEMachine().getABC().trim().length()==0){
            errors.add(new Exception("You must enter ABC"));
        }
    }
}