package bruteForce;

public class AgentInfoDTO {

    private String agentName;
    private int threadsAmount;
    private int missionsAmount;
    private String alliesTeamName;

    public AgentInfoDTO( String agentName, int threadsAmount,int missionsAmount, String alliesTeamName){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;
        this.alliesTeamName=alliesTeamName;
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