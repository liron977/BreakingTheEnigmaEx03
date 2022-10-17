package engine.theEnigmaEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Allies implements Serializable {

   private String alliesName;
   private int missionSize;
   private List<AlliesAgent> alliesAgents;
   private String battlefieldName;
   private int agentsAmount;
   private int amountOfCreatedMissions;
   private Long totalAmountOfMissionToCreate;
   private Long maxAmountOfMissions;

    public Allies( String alliesName){
        this.alliesName=alliesName;
        this.missionSize=0;
        agentsAmount=0;
        alliesAgents=new ArrayList<>();
        this.battlefieldName="";
        this.amountOfCreatedMissions=0;
        this.totalAmountOfMissionToCreate=0l;
        maxAmountOfMissions=Long.valueOf(-1);
    }
    public void setAgentMissionsStatus(String agentName,int amountOfCandidatesStrings,int amountOfReceivedMissions, int amountOfMissionsToExecute){
        for (AlliesAgent agent:alliesAgents) {
            if(agent.getAgentName().equals(agentName)){
                agent.setAmountOfMissionsToExecute(amountOfMissionsToExecute);
                agent.setAmountOfCandidatesStrings(amountOfCandidatesStrings);
                agent.setAmountOfReceivedMissions(amountOfReceivedMissions);
            }

        }
    }

    public int getAmountOfCreatedMissions() {
        return amountOfCreatedMissions;
    }

    public void setAmountOfCreatedMissions(int amountOfCreatedMissions) {
        this.amountOfCreatedMissions = amountOfCreatedMissions;
    }
    public void increaseAmountOfCreatedMissions(){
        this.amountOfCreatedMissions=this.amountOfCreatedMissions+1;
    }

    public void setTotalAmountOfMissionToCreate(Long totalAmountOfMissionToCreate) {
        this.totalAmountOfMissionToCreate = totalAmountOfMissionToCreate;
        this.maxAmountOfMissions=totalAmountOfMissionToCreate;
    }

    public Long getTotalAmountOfMissionToCreate() {
        return totalAmountOfMissionToCreate;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName = battlefieldName;
    }

    public void addAgent(AlliesAgent agent){

        alliesAgents.add(agent);
        agentsAmount++;
    }
    public void setAgentsAmount(int agentsAmount) {
        this.agentsAmount = agentsAmount;
    }

    public int getAgentsAmount() {
        return alliesAgents.size();
    }

    public int getMissionSize() {
        return missionSize;
    }

    public String getAlliesName() {
        return alliesName;
    }
    public void clearAlliesValues(){
        for (AlliesAgent agent:alliesAgents ) {
            agentsAmount--;
        }
    totalAmountOfMissionToCreate=0L;
    amountOfCreatedMissions=0;
    battlefieldName="";
    missionSize=0;
    maxAmountOfMissions=Long.valueOf(-1);
     }
    public void setAlliesName(String alliesName) {
        this.alliesName = alliesName;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }
      public AlliesAgent getAgentByName(String agentName) {
          for (AlliesAgent agent : alliesAgents) {
              if (agent.getAgentName().equals(agentName)) {
                  return agent;
              }
          }
          return null;
      }

    public Long getMaxAmountOfMissions() {
        return maxAmountOfMissions;
    }

    public List<AlliesAgent> getAlliesAgents() {
        return alliesAgents;
    }

    public void setAlliesAgents(List<AlliesAgent> alliesAgents) {
        this.alliesAgents = alliesAgents;
    }

    public void setMaxAmountOfMissions(Long maxAmountOfMissions) {
        this.maxAmountOfMissions = maxAmountOfMissions;
    }
    public void decreaseMaxAmountOfMissions() {
        this.maxAmountOfMissions = maxAmountOfMissions-1;
        if (maxAmountOfMissions<0){
            int x=0;
        }
    }
}