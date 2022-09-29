package engine.theEnigmaEngine;

public class AlliesAgent {
    private String agentName;
    private int threadsAmount;
    private int missionsAmount;
    private String alliesTeamName;

    public AlliesAgent( String agentName, int threadsAmount,int missionsAmount, String alliesTeamName){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;
        this.alliesTeamName=alliesTeamName;
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setMissionsAmount(int missionsAmount) {
        this.missionsAmount = missionsAmount;
    }

    public void setThreadsAmount(int threadsAmount) {
        this.threadsAmount = threadsAmount;
    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getMissionsAmount() {
        return missionsAmount;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }
}