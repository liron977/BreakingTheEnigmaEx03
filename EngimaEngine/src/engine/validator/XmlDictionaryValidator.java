package engine.validator;

import engine.theEnigmaEngine.Keyboard;
import engine.theEnigmaEngine.TheMachineEngine;
import schemaGenerated.CTEDictionary;
import schemaGenerated.CTEEnigma;

import java.util.ArrayList;
import java.util.List;

public class XmlDictionaryValidator implements Validator{
   private CTEDictionary cteDictionary;
   private String[] dictionary;
    private List<Exception> errors;
    private String keyboard;

    @Override
    public void validate() {
        excludeAndSplitDictionary();

    }
    public XmlDictionaryValidator(CTEEnigma cteEnigma){
        this.cteDictionary=cteEnigma.getCTEDecipher().getCTEDictionary();
        this.keyboard=cteEnigma.getCTEMachine().getABC().toUpperCase().trim();
        errors=new ArrayList<>();

    }
    public void isDictionaryValid(){

        for(int i=0;i<dictionary.length;i++){
            for(int j=0;j<dictionary[i].length();j++){
                if(!keyboard.contains(dictionary[i])){

                }
            }
        }

    }
    public void excludeAndSplitDictionary(){
        if(cteDictionary!=null) {
            String dic = cteDictionary.getWords().trim().toUpperCase();
            String cteExcludeChars = cteDictionary.getExcludeChars().toUpperCase().trim();
            for (int i = 0; i < cteExcludeChars.length(); i++) {
                dic.replace(String.valueOf(cteExcludeChars.charAt(i)), "");

            }
            dictionary = dic.split(" ");

        }



    }

    @Override
    public List<Exception> getListOfException() {
        return errors;
    }
}