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
    public Allies( String alliesName){
        this.alliesName=alliesName;
        this.missionSize=0;
        agentsAmount=0;
        alliesAgents=new ArrayList<>();
        this.battlefieldName="";
        this.amountOfCreatedMissions=0;
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

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName = battlefieldName;
    }

    public void addAgent(AlliesAgent agent){
    alliesAgents.add(agent);
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


}