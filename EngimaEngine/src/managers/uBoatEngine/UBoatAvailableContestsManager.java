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
    public EngineManager getEngineManagerByBattleFieldName(String battleFieldName){
        return uBoatAvailableContestsMap.get(battleFieldName);

    }
    public synchronized EngineManager getEngineMangerByAlliesTeamName(String alliesTeamName){
        for (EngineManager engineManager : uBoatAvailableContestsMap.values()){
            if(engineManager.isAlliesExists(alliesTeamName)){
                return engineManager;
            }
    }
        return null;
    }
    public synchronized Map<String,EngineManager> getUBoatAvailableContestsMap() {
        return uBoatAvailableContestsMap;
    }

/*    public boolean isUBoatContestsAvailable(MediatorForEngineManager newUBoatAvailableContest) {
        return uBoatAvailableContestsMap.contains(newUBoatAvailableContest) ;
    }*/
}