package managers.uBoatEngine;

import bruteForce.AgentInfoDTO;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.AlliesAgent;

import java.util.*;

public class AlliesManager {
    private final Map<String, Allies> alliesManagerMap;
    public AlliesManager() {
        this.alliesManagerMap = new HashMap<>();

    }

    public synchronized void addAllies(Allies allies, String alliesTeamName) {
        this.alliesManagerMap.put(alliesTeamName,allies);
    }
    public Allies getAlliesByAlliesTeamName(String alliesTeamName){
        return alliesManagerMap.get(alliesTeamName);

    }
    public synchronized void addAgentToAllies(AlliesAgent agent,String alliesTeamName){
        Allies allies=getAlliesByAlliesTeamName(alliesTeamName);
        allies.addAgent(agent);
    }
    public synchronized boolean updateAgentMissionStatus(AgentInfoDTO agentInfoDTO){
       Allies allies= getAlliesByAlliesTeamName(agentInfoDTO.getAlliesTeamName());
       if(allies!=null) {
           allies.setAgentMissionsStatus(agentInfoDTO.getAgentName(), agentInfoDTO.getAmountOfCandidatesStrings(), agentInfoDTO.getAmountOfReceivedMissions(), agentInfoDTO.getAmountOfMissionsToExecute());
           return true;
       }
       return false;
    }
    /*public synchronized EngineManager getEngineMangerByAlliesTeamName(String alliesTeamName){
        for (EngineManager engineManager : uBoatAvailableContestsMap.values()){
            if(engineManager.isAlliesExists(alliesTeamName)){
                return engineManager;
            }
        }
        return null;
    }*/
    public synchronized  Map<String, Allies> getAlliesManagerMap() {
        return  alliesManagerMap;
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