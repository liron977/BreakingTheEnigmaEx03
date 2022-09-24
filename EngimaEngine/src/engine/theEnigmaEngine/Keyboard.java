package engine.theEnigmaEngine;

import java.io.Serializable;
import java.util.Locale;

public class Keyboard  implements Serializable {
    private String keyboard;
   private int keyboardAmount;
    public Keyboard (String keyboard){
        this.keyboard=keyboard.toUpperCase();
        this.keyboardAmount=keyboard.length();
    }
    public int getIndexFromKeyboard(String ch){
        return keyboard.indexOf(ch.charAt(0));
    }
    public String getCharacterFromKeyboardByIndex(int index){
        return Character.toString(keyboard.charAt(index));
    }
    public int getKeyboardAmount(){
        return keyboardAmount;
    }
    public String getKeyboard(){
        return keyboard ;
    }

}