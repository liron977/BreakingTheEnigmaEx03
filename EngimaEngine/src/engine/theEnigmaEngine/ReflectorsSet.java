package engine.theEnigmaEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class ReflectorsSet implements Serializable {
    private List<Reflector> reflectors =new ArrayList<>();

    public ReflectorsSet(List<Reflector> reflectors){
        this.reflectors=reflectors;;
    }

    public List<String> getReflectorsId(){
        List<String> reflectorsId=new ArrayList<>();
        for (Reflector reflector:reflectors) {
            reflectorsId.add(reflector.getReflectorId());
        }
        return reflectorsId;
    }
    public List<Reflector> getListOfReflectors() {
        return reflectors;
    }
    public int getReflectorsAmount() {
        return reflectors.size();
    }
    public Reflector getReflectorById(String reflectorId){
        for (Reflector reflector:reflectors) {
            if(reflector.getReflectorId().equals(reflectorId)){
                return reflector;
            }

        }
        return null;
    }
    public boolean searchReflectorById(String reflectorId){
        for (Reflector reflector:reflectors) {
            if(reflector.getReflectorId().equals(reflectorId)){
                return true;
            }
        }
        return false;
    }
}