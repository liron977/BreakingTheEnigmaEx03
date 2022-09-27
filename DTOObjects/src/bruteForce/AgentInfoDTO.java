package bruteForce;

public class AgentInfoDTO {

    private String agentName;
    private String threadsAmount;
    private String missionsAmount;

    public AgentInfoDTO( String agentName, String threadsAmount,String missionsAmount){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;

    }

    public String getAgentName() {
        return agentName;
    }

    public String getMissionsAmount() {
        return missionsAmount;
    }

    public String getThreadsAmount() {
        return threadsAmount;
    }
}