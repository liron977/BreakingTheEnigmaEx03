package engine.theEnigmaEngine;

import machineDTO.ConvertedStringDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TheMachineEngine implements Serializable {
    private RotorsSet rotorsSet;
    private RotorsSet usedRotors;
    private Reflector reflector;
    private ReflectorsSet reflectorsSet;
    private Keyboard keyboard;
    private PlugsBoard plugsBoard;
    private int amountOfUsedRotors;
    private Dictionary dictionary;
     // private Agents agents;



    public Reflector getReflector() {
        return reflector;
    }

    public TheMachineEngine(RotorsSet rotorsSet, ReflectorsSet reflectorsSet, Keyboard keyboard, int amountOfUsedRotors,Dictionary dictionary){
        this.rotorsSet=rotorsSet;
        this.keyboard=keyboard;
        this.reflectorsSet=reflectorsSet;
        this.amountOfUsedRotors=amountOfUsedRotors;
        this.dictionary=dictionary;
        //this.agents=agents;
    }

    public List<String> getReflectorsId(){
       return reflectorsSet.getReflectorsId();
    }
    public void reverseUsedRotors(){
        usedRotors.reverseRotors();
    }
 public List<String> getStringPairsOfSwappingCharacter(){
        if(plugsBoard!=null){
            return plugsBoard.getStringPairsOfSwappingCharacter();
        }
       return null;
 }
    public List<String> getListOfNotch(){
       return usedRotors.getNotchList();
    }
    public List<String> getOriginalNotchPositionList(){
        return usedRotors.getOriginalNotchPositionList();
    }
    public String[] getArrayOfRotorsId(){
        return usedRotors.getAllRotorsId();
    }
    public void addPlugsBoardTOTheMachine(PlugsBoard plugsBoard){
        this.plugsBoard=plugsBoard;
    }

    public void  addSelectedReflector(String reflectorId){

        this.reflector=reflectorsSet.getReflectorById(reflectorId.toUpperCase());
    }
    public ReflectorsSet getReflectorsSet(){
        return reflectorsSet;
    }
    public int getReflectorsAmount(){
        return reflectorsSet.getReflectorsAmount();
    }

    public int getMaxAmountOfRotors() {
        return rotorsSet.getMaxAmountOfRotors();
    }
    public void initEmptyPlugBoard() {
        if(plugsBoard!=null){
            plugsBoard.initEmptyPlugBoard();
        }

    }



    public String[] getRotorsId(){
        String[] rotorsId=new String[rotorsSet.getListOfRotors().size()];
        int i=0;
        for (Rotor rotor: rotorsSet.getListOfRotors()) {
               rotorsId[i]=rotor.getRotorId();
               i++;
        }
        return rotorsId;
    }
    public String[] getReflectorId(){
        String[] reflectorsId=new String[]{};
        int i=0;
        for (Reflector reflector: reflectorsSet.getListOfReflectors()) {
            reflectorsId[i]=reflector.getReflectorId();
            i++;
        }
        return reflectorsId;
    }

    public PlugsBoard getPlugsBoard() {
        return plugsBoard;
    }

    public String getKeyboard(){
        return keyboard.getKeyboard();
    }
    public RotorsSet getRotorsSet(){
        return rotorsSet;
    }
    public void setUsedRotors(Rotor usedRotor){
        this.usedRotors=usedRotors;
    }
    public int getAmountOfUsedRotors(){
        return amountOfUsedRotors;
    }
    public void createUsedRotorsSet(List<Rotor> listOfRotors) {
         this.usedRotors=new RotorsSet(listOfRotors);
    }
    public RotorsSet getUsedRotors(){
        return usedRotors;
    }

public void resetCurrentRotorSetCode(){
    usedRotors.resetCurrentRotorsCode();
}
public String manageDecode(String signal){
    int indexOfSignal;
    String result;

    usedRotors.manageSpins();
    indexOfSignal= theProcessFromTheRotorsToReflector(signal);
    result=theProcessFromTheReflectorToRotors(indexOfSignal);

    return result;
}
    private int theProcessFromTheRotorsToReflector(String signal){
        String entryValue=signal;
    if(plugsBoard!=null) {
        entryValue = plugsBoard.getSwappedCharacter(signal);
    }
        int indexOfSignal= keyboard.getIndexFromKeyboard(entryValue);
        for (Rotor rotor: usedRotors.getListOfRotors()) {
            entryValue=rotor.getEntryValueFromRotorByIndex(indexOfSignal);
            indexOfSignal=rotor.getIndexFromRotorByEntryValue(entryValue);
        }

        return indexOfSignal;
    }
    private String theProcessFromTheReflectorToRotors(int indexOfSignal){
        Rotor tmpRotor=null;
        String entryValue="";

        indexOfSignal=reflector.getExitIndexFromTheReflector(indexOfSignal);
        Collections.reverse(usedRotors.getListOfRotors());
        for (Rotor rotor:usedRotors.getListOfRotors()) {
            entryValue=rotor.getExitValueFromRotorByIndex(indexOfSignal);
            indexOfSignal=rotor.getEntryIndexFromRotorByValue(entryValue);
            tmpRotor=rotor;
        }
        Collections.reverse(usedRotors.getListOfRotors());
        if(tmpRotor!=null){
            indexOfSignal=tmpRotor.getEntryIndexFromRotorByValue(entryValue);
            entryValue=keyboard.getCharacterFromKeyboardByIndex(indexOfSignal);
            if(plugsBoard!=null) {
                entryValue = plugsBoard.getSwappedCharacter(entryValue);
            }
        }
        return  entryValue;
    }
    public Dictionary getDictionary(){
        return dictionary;
    }
   /* public Agents getAgents(){
        return agents;
    }*/
    public void updateUsedRotors(String[] rotorsIdArray){
       List<Rotor> listOfRotors = new ArrayList<>();
        for (String rotorsId:rotorsIdArray) {
            listOfRotors.add(getRotorsSet().getRotorById(rotorsId));
        }
        createUsedRotorsSet(listOfRotors);
    }
    public String[] getUsedRotorsId(){
        String[] usedRotorsId=new String[usedRotors.getListOfRotors().size()];
        int i=0;
        for (Rotor rotor: usedRotors.getListOfRotors()) {
            usedRotorsId[i]=rotor.getRotorId();
            i++;
        }
        return usedRotorsId;
    }
/*    public Agents getAgents(){
        return agents;
    }*/

    public ConvertedStringDTO getConvertedStringFromMachine(String userInputString) {
        long begin = System.nanoTime();

        String convertedString = "";
        userInputString = userInputString.toUpperCase();
        for (int i = 0; i < userInputString.length(); i++) {
            String userInputByString = String.valueOf(userInputString.charAt(i));
            String convertedCharByString = manageDecode(userInputByString);
            convertedString = convertedString.concat(convertedCharByString);
        }
        long end = System.nanoTime();
       // ProcessTimeToConvert = ProcessTimeToConvert + (end - begin);
        ConvertedStringDTO convertedStringDTO = new ConvertedStringDTO(convertedString);

        return convertedStringDTO;
    }
}