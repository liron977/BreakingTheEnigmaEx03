package managers.uBoatEngine;

import engineManager.EngineManager;

import java.util.HashMap;
import java.util.Map;

public class UBoatAvailableContestsManager {
    private Map<String , EngineManager> uBoatAvailableContestsMap;
    public UBoatAvailableContestsManager() {
       this.uBoatAvailableContestsMap =new HashMap<>();
    }
    public synchronized void addUBoatAvailableContest(EngineManager engineForNewUBoatAvailableContest,String battleFieldName) {
       this.uBoatAvailableContestsMap.put(battleFieldName,engineForNewUBoatAvailableContest);
    }
    public synchronized Map<String,EngineManager> getUBoatAvailableContestsMap() {
        return uBoatAvailableContestsMap;
    }
/*    public boolean isUBoatContestsAvailable(MediatorForEngineManager newUBoatAvailableContest) {
        return uBoatAvailableContestsMap.contains(newUBoatAvailableContest) ;
    }*/
}
