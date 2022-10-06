
package bruteForce;
import java.io.Serializable;
import java.util.List;

public class TheMissionInfoDTO implements Serializable {
    private String initialStartingPosition;
    private int sizeOfMission;
    private String[] usedRotors;
    private String reflector;
    private String stringToConvert;
    public TheMissionInfoDTO(String initialStartingPosition, int sizeOfMission
                          , String stringToConvert,String[] usedRotors,String reflector){
        this.initialStartingPosition=initialStartingPosition;
        this.sizeOfMission=sizeOfMission;
       /* this.engineManager=engineManager;*/
        this.stringToConvert=stringToConvert;
        this.usedRotors=usedRotors;
        this.reflector=reflector;
    }

    public void setReflector(String reflector) {
        this.reflector = reflector;
    }

    public void setUsedRotors(String[] usedRotors) {
        this.usedRotors = usedRotors;
    }

    public String getReflector() {
        return reflector;
    }

    public String[] getUsedRotors() {
        return usedRotors;
    }

    public String getStringToConvert() {
        return stringToConvert;
    }
    public int getSizeOfMission() {
        return sizeOfMission;
    }
   /* public String getFinalPosition() {
        return finalPosition;
    }*/
    public String getInitialStartingPosition() {
        return initialStartingPosition;
    }

   /* public EngineManager getEngineManager() {
        return engineManager;
    }*/
}