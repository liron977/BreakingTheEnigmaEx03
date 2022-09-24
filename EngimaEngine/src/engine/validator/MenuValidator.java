package engine.validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuValidator implements Validator, Serializable {

    private List<Exception> exceptionList;
    private Boolean isMachineDefined;
    private Boolean isCodeDefined;

    public MenuValidator(){
        exceptionList=new ArrayList<>();
        isCodeDefined=false;
        isMachineDefined=false;

    }
    public void isXmlLoaded(){
        exceptionList=new ArrayList<>();
        if(!isMachineDefined){
            exceptionList.add(new Exception("The machine is not defined yet,please load file.\n"));
        }
    }
    public void isCodeConfigurationWasDefined(){
        if(!isCodeDefined){
            exceptionList.add(new Exception("The code configuration is not defined yet,please set code configuration.\n"));
        }
    }

    public Boolean getMachineDefined() {
        return isMachineDefined;
    }

    public void setTrueValueToIsMachineDefined(){
        isMachineDefined=true;
    }
    public void setTrueValueToUpdateIsCodeDefined(){
        isCodeDefined=true;
    }
    public void updateExceptionList(){
        exceptionList=new ArrayList<>();
    }
    public void reset(){
        updateExceptionList();
        isCodeDefined=false;
        isMachineDefined=false;
    }
    @Override
    public void validate() {

    }
    @Override
    public List<Exception> getListOfException() {
        return exceptionList;
    }
}