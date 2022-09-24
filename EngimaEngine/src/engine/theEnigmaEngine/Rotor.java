package engine.theEnigmaEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Rotor  implements Serializable{
    private List<Pair> rotorStructure = new ArrayList<>();
    private String rotorId;
    private String startingPosition;
    private Pair notchPair;
    private int originalNotchPosition;
    private int entriesAmount;

    public Rotor(String rotorId, int entriesAmount, List<Pair> rotorStructure,int notchPosition) {
        this.rotorId = rotorId;
        this.entriesAmount = entriesAmount;
        this.rotorStructure = rotorStructure;
        initRotorStructure();
        this.notchPair = this.rotorStructure.get((notchPosition) -1);
        //this.originalNotchPosition=notchPosition;

    }

    public String getStartingPosition() {
        return startingPosition;
    }
    public String getSignalInWindow(){

       return rotorStructure.get(0).getEntry();

    }
    public int getOriginalNotchPosition(){
        return originalNotchPosition;
    }

    public Pair getNotchPair() {
        return notchPair;
    }
    public int getNotchPosition(){
        String entry=notchPair.getEntry();
        int index=getEntryIndexFromRotorByValue(entry)+1;
        return index;
    }

    public void setRotorStartingPosition(String startingPosition){
        this.startingPosition =startingPosition;
        initRotorStructure();
        this.originalNotchPosition=getEntryIndexFromRotorByValue(notchPair.getEntry())+1;
}
    public void initRotorStructure() {
        List<Pair> tmpRotorStructure = new ArrayList<>();
        tmpRotorStructure.addAll(rotorStructure);
        int newPosition;
        int numberToMove=getEntryIndexFromRotorByValue(startingPosition)+1;
        if(numberToMove!=0) {
            for (int i = 0; i < entriesAmount; i++) {
                newPosition = i - numberToMove + 1;
                if (newPosition < 0) {
                    newPosition = newPosition + entriesAmount;
                }
                tmpRotorStructure.set(newPosition, rotorStructure.get(i));
            }
            this.rotorStructure = tmpRotorStructure;
        }
    }

    public List<Pair> getRotorStructure() {
        return rotorStructure;
    }
     public String getRotorId (){
        return rotorId;
     }
    public void spinRotor() {
        int newPosition;
        List<Pair> tmpRotorStructure = new ArrayList<>();
        tmpRotorStructure.addAll(rotorStructure);
        for (int i = 0; i < entriesAmount; i++) {
            newPosition = i - 1;
            if (newPosition < 0) {
                newPosition = newPosition + entriesAmount;
            }
            tmpRotorStructure.set(newPosition, rotorStructure.get(i));
        }
        rotorStructure = tmpRotorStructure;
    }
    public boolean isNotchLocatedInWindow() {
        return rotorStructure.get(0).equals(notchPair);
    }
    public String getEntryValueFromRotorByIndex(int index){
      return rotorStructure.get(index).getEntry();
    }
    public String getExitValueFromRotorByIndex(int index){
        return rotorStructure.get(index).getExit();
    }
    public int getIndexFromRotorByEntryValue(String str){
        for(int i=0;i<rotorStructure.size();i++){
            if(rotorStructure.get(i).getExit().equals(str)){
                return i;
            }
        }
        return -1;
    }

    public int getEntryIndexFromRotorByValue(String str){
        for(int i=0;i<rotorStructure.size();i++){
            if(rotorStructure.get(i).getEntry().equals(str)){
                return i;
            }
        }
        return -1;
    }

}