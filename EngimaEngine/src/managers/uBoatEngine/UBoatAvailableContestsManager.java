package managers.uBoatEngine;

import bruteForce.BruteForceResultDTO;
import machineEngine.EngineManager;

import java.util.HashMap;
import java.util.List;
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
    public synchronized String getUboatNameByAlliesTeamName(String alliesTeamName){
        for (EngineManager engineManager : uBoatAvailableContestsMap.values()){
            if(engineManager.isAlliesExists(alliesTeamName)){
                return engineManager.getBattleField().getBattleName();
            }
        }
        return null;
    }
    public synchronized boolean isStringToConvertFound(String battleFieldName, List<BruteForceResultDTO> bruteForceResultDTOListFromGson){
        EngineManager engineManager=getEngineManagerByBattleFieldName(battleFieldName);
        String stringToConvertFromEngine=engineManager.getStringToConvert();
        for (BruteForceResultDTO bruteForceResult:bruteForceResultDTOListFromGson) {
            if(bruteForceResult.getConvertedString().equals(stringToConvertFromEngine)){
                return true;
            }
        }
        return false;
    }
    public synchronized Map<String,EngineManager> getUBoatAvailableContestsMap() {
        return uBoatAvailableContestsMap;
    }
    public synchronized void updateContestStatus(String battleFieldName,String alliesWinnerTeamName){
        EngineManager engineManager=getEngineManagerByBattleFieldName(battleFieldName);
        engineManager.setIsContestEnded(true);
        engineManager.setAlliesWinnwerTeamName(alliesWinnerTeamName);
    }
    public synchronized void clearValues(String battleFieldName){
    EngineManager engineManager=getEngineManagerByBattleFieldName(battleFieldName);
    if(engineManager!=null) {
        engineManager.clearBattleFieldValues("uboat");
    }
  }
  public synchronized void removeAvailableContests(String battleFieldName){
        if(getEngineManagerByBattleFieldName(battleFieldName)!=null){
            uBoatAvailableContestsMap.remove(battleFieldName);
        }

  }
/*    public boolean isUBoatContestsAvailable(MediatorForEngineManager newUBoatAvailableContest) {
        return uBoatAvailableContestsMap.contains(newUBoatAvailableContest) ;
    }*/
}