package engine.validator;

import schemaGenerated.CTEEnigma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XmlBattlefieldValidator implements Validator, Serializable {

    private List<Exception> exceptionList;
    private CTEEnigma enigmaDescriptor;
    @Override
    public void validate() {
        if(isBattleFieldDetailsExist()){
            isBattleFieldNameIsValid();
            isBattleFieldAlliesIsValid();
            isBattleFieldIsValid();
        }
    }
    public XmlBattlefieldValidator(CTEEnigma cteEnigma){
        this.enigmaDescriptor=cteEnigma;
        exceptionList=new ArrayList<>();
    }
    private void isBattleFieldNameIsValid(){
        if(enigmaDescriptor.getCTEBattlefield().getBattleName().equals("")){
            exceptionList.add(new Exception("The file is not valid,please enter a battlefield name"));
        }
    }
    private void isBattleFieldAlliesIsValid(){
        if(enigmaDescriptor.getCTEBattlefield().getAllies()<=0){
            exceptionList.add(new Exception("The file is not valid,please enter a positive allies amount "));
        }
    }
    private void isBattleFieldIsValid(){
        if(((!enigmaDescriptor.getCTEBattlefield().getLevel().equals("Easy"))&&
        (!enigmaDescriptor.getCTEBattlefield().getLevel().equals("Medium"))&&
        (!enigmaDescriptor.getCTEBattlefield().getLevel().equals("Hard"))&&
        (!enigmaDescriptor.getCTEBattlefield().getLevel().equals("Insane"))))
        {
            exceptionList.add(new Exception("The file is not valid,please legal level:[Easy,Medium,Hard,Impossible]"));
        }

    }
    private boolean isBattleFieldDetailsExist(){
        if(enigmaDescriptor.getCTEBattlefield()==null){
            exceptionList.add(new Exception("The file is not valid"));
            return false;
        }
        return true;
    }
    @Override
    public List<Exception> getListOfException() {
        return exceptionList;
    }
}
