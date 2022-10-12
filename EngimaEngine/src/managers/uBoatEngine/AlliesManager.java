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
    public synchronized void increaseAmountOfCreatedMission(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        if(allies!=null) {
           allies.increaseAmountOfCreatedMissions();
        }
    }
    public synchronized Long getAmountOfCreadedMission(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        return Long.valueOf(allies.getAmountOfCreatedMissions());
        }
    public synchronized Long getTotalAmountOfCreadedMission(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        return Long.valueOf(allies.getTotalAmountOfMissionToCreate());
    }
    public synchronized void setTotalAmountOfCreadedMission(String alliesTeamName,Long totalAmountOfCreadedMission){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
         allies.setTotalAmountOfMissionToCreate(totalAmountOfCreadedMission);
    }
    public synchronized void clearAlliesValues(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        if(allies!=null){
            allies.clearAlliesValues();
        }

    }
    public synchronized  Map<String, Allies> getAlliesManagerMap() {
        return  alliesManagerMap;
    }
    public synchronized void decreaseMaxAmountOfMissions(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        if(allies!=null){
            allies.decreaseMaxAmountOfMissions();
        }
    }
    public synchronized Long getMaxAmountOfMissions(String alliesTeamName){
        Allies allies= getAlliesByAlliesTeamName(alliesTeamName);
        if(allies!=null){
           return allies.getMaxAmountOfMissions();
        }
        return Long.valueOf(-1);
    }
}