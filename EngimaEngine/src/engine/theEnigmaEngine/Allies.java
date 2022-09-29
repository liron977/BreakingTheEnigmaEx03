package engine.theEnigmaEngine;

import java.util.ArrayList;
import java.util.List;

public class Allies {

   private String alliesName;
   private int missionSize;
   private List<AlliesAgent> alliesAgents;
private int agentsAmount;
    public Allies( String alliesName){
        this.alliesName=alliesName;
        this.missionSize=0;
        agentsAmount=0;
        alliesAgents=new ArrayList<>();

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
}