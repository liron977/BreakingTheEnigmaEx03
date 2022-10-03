package bruteForceLogic;

import engineManager.EngineManager;

import java.io.Serializable;

public class TheMissionInfo implements Serializable {
    private String initialStartingPosition;
    private int sizeOfMission;
   /* private String finalPosition;*/
    private EngineManager engineManager;
    private String stringToConvert;
    public TheMissionInfo(String initialStartingPosition, int sizeOfMission,EngineManager engineManager
                          ,String stringToConvert){
        this.initialStartingPosition=initialStartingPosition;
        this.sizeOfMission=sizeOfMission;
        this.engineManager=engineManager;
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

    public EngineManager getEngineManager() {
        return engineManager;
    }
}