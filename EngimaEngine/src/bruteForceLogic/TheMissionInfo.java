package bruteForceLogic;

import engineManager.EngineManager;

public class TheMissionInfo {
    private String initialStartingPosition;
    private int sizeOfMission;
    private String finalPosition;
    private EngineManager engineManager;
    public TheMissionInfo(String initialStartingPosition, int sizeOfMission, String finalPosition,EngineManager engineManager){
        this.initialStartingPosition=initialStartingPosition;
        this.sizeOfMission=sizeOfMission;
        this.finalPosition=finalPosition;
        this.engineManager=engineManager;
    }
    public int getSizeOfMission() {
        return sizeOfMission;
    }
    public String getFinalPosition() {
        return finalPosition;
    }
    public String getInitialStartingPosition() {
        return initialStartingPosition;
    }
}
