
package bruteForce;
import java.io.Serializable;

public class TheMissionInfoDTO implements Serializable {
    private String initialStartingPosition;
    private int sizeOfMission;
    private String stringToConvert;
    public TheMissionInfoDTO(String initialStartingPosition, int sizeOfMission
                          , String stringToConvert){
        this.initialStartingPosition=initialStartingPosition;
        this.sizeOfMission=sizeOfMission;
       /* this.engineManager=engineManager;*/
        this.stringToConvert=stringToConvert;
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