package clientServer.uBoatEngine;

import engineManager.EngineManager;

import java.util.*;

public class UBoatManger {
    private final Map<String, EngineManager> uBoatMangerMap;
    public UBoatManger() {
        uBoatMangerMap = new HashMap<>();
    }
   /* public synchronized void addGraph(String graphName, Mediator newMed) {
        mediatorsMap.put(graphName,newMed);
    }
    public synchronized Map<String,Mediator> getMediatorsMap() {
        return Collections.unmodifiableMap(mediatorsMap);
    }
    public boolean isGraphExists(String graphName) {return mediatorsMap.containsKey(graphName);}
*/
}
