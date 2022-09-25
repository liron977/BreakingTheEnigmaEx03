package engine.validator;

import schemaGenerated.CTEDecipher;
import schemaGenerated.CTEEnigma;

import java.util.ArrayList;
import java.util.List;

public class XmlAgentValidator implements Validator{

    private List<Exception> errors;
    private CTEDecipher cteDecipher;

    @Override
    public void validate() {
       // isNumberOfAgentsValid();

    }

    public XmlAgentValidator(CTEEnigma cteEnigma){
       this.cteDecipher=cteEnigma.getCTEDecipher();
       errors=new ArrayList<>();
    }
 /*   public void isNumberOfAgentsValid(){
        int numberOfAgents=cteDecipher.getAgents();
        if(numberOfAgents<2){
            errors.add(new Exception("The number of agents should 2 and more"));

        }
        else if(numberOfAgents>50){
            errors.add(new Exception("The number of agents cant be more than 50"));
        }
    }*/



    @Override
    public List<Exception> getListOfException() {
        return errors;
    }
}