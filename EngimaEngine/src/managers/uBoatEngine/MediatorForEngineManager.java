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

    public synchronized Map<String, EngineManager> getEngineManagersMap() {
        return Collections.unmodifiableMap(EngineManagersMap);
    }
    public synchronized EngineManager getEngineMangerByBattleFiLedName(String battleFiLedName){
      return EngineManagersMap.get(battleFiLedName);
    }
    public boolean isBattleExists(String battleName) {
        return EngineManagersMap.containsKey(battleName);
    }
}