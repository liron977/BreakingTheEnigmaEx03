package managers.uBoatEngine;

import machineEngine.EngineManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MediatorForEngineManager {
    private final Map<String, EngineManager> EngineManagersMap = new HashMap<>();


    public MediatorForEngineManager() {
    }

    public synchronized void addEngineManger(String battleName, EngineManager newEngineManager) {
        EngineManagersMap.put(battleName, newEngineManager);
    }
    public synchronized void replaceEngineManager(EngineManager engineManager,String battleName){
        EngineManager engineManagerClone=engineManager.cloneEngineManager();
        EngineManagersMap.put(battleName,engineManagerClone);

    }

    public synchronized Map<String, EngineManager> getEngineManagersMap() {
        return Collections.unmodifiableMap(EngineManagersMap);
    }
    public synchronized EngineManager getEngineMangerByBattleFiLedName(String battleFiLedName){
      return EngineManagersMap.get(battleFiLedName);
    }
    public synchronized EngineManager getEngineMangerByAlliesTeamName(String alliesTeamName){
        for (EngineManager engineManager : EngineManagersMap.values()){
            if(engineManager.isAlliesExists(alliesTeamName)){
                return engineManager;
            }
        }
        return null;
    }
    public synchronized void removeUboat(String battleFieldName){
        if(getEngineMangerByBattleFiLedName(battleFieldName)!=null){
            EngineManagersMap.remove(battleFieldName);
        }
    }
    public boolean isBattleExists(String battleName) {
        return EngineManagersMap.containsKey(battleName);
    }
}