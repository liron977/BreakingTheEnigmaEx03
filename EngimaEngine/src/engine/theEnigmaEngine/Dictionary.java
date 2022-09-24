package engine.theEnigmaEngine;

import java.io.Serializable;

public class Dictionary implements Serializable {
    private String [] dictionary;
    private String excludeChars;

    public Dictionary(String [] dictionary,String excludeChars){
        this.dictionary=dictionary;
        this.excludeChars=excludeChars;
    }
    public String removeExcludeCharsFromString(String str){
        for(int i=0;i<excludeChars.length();i++){
            str =str.replace(String.valueOf(excludeChars.charAt(i)),"");
        }
        return str;
    }
    private boolean isWordExistInTheDictionary(String wordInput){
        for (String word:dictionary) {
            if(word.equals(wordInput)){
                return true;
            }
        }
        return false;
    }

    public String[] getDictionary() {
        return dictionary;
    }

    public boolean isStringExistsInTheDictionary(String str){
        String[] words=str.split(" ");
        if(words.length==0) {
        return false;
        }
        for (String word:words) {
            if(!isWordExistInTheDictionary(word)) {
                return false;
            }
        }
        return true;
    }
    public String getExcludedSignalsInString(String str){
        String excludedSignalsInString="";
        String excludedChar="";
        for(int i=0;i<excludeChars.length();i++){
            excludedChar=String.valueOf(excludeChars.charAt(i));
            if(str.contains(excludedChar)){
                excludedSignalsInString=excludedSignalsInString.concat(excludedChar);
                }
            }
        return excludedSignalsInString;
        }
}