package engine.theEnigmaEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class Reflector  implements Serializable{
    private String reflectorId;
    private List<Pair> reflectorPairStructure=new ArrayList<>();
    private List<String> reflectorStructure=new ArrayList<>();
    public Reflector(String reflectorId,List<Pair> reflectorPairStructure ){
        this.reflectorId=reflectorId.toUpperCase();
        this.reflectorPairStructure=reflectorPairStructure;
        initReflector();
        updateReflector();
    }
    public String getReflectorId() {
        return reflectorId;
    }

    public List<String> getReflectorStructure() {
       return reflectorStructure;
    }
    public void updateReflector(){
        String originalInputValue;
        String originalOutputValue;
        int newInputIndex;
        int newExitIndex;
        for(int i=0;i<reflectorPairStructure.size();i++){
            originalInputValue=this.reflectorPairStructure.get(i).getEntry();
            originalOutputValue=this.reflectorPairStructure.get(i).getExit();
            newInputIndex=Integer.parseInt(originalOutputValue)-1;
            newExitIndex=Integer.parseInt(originalInputValue)-1 ;
            reflectorStructure.set(newInputIndex,originalInputValue);
            reflectorStructure.set(newExitIndex,originalInputValue);
        }
    }
    private void initReflector(){
        for (int i=0;i<reflectorPairStructure.size()*2;i++) {
          String str="1";
            reflectorStructure.add(str);

        }
    }
    public int getExitIndexFromTheReflector(int entryIndex) {
        String valueOfEntryIndex=reflectorStructure.get(entryIndex);
        for(int i=0;i<reflectorStructure.size();i++) {
            if((i!=(entryIndex))&&(reflectorStructure.get(i)==valueOfEntryIndex)){
                return i;
            }
        }
        return -1;
    }
}
