package bruteForce;

public class AgentContestInfoDTO {

    private String alliesTeamName;
    private String agentName;
    private boolean dataShouldDelete;

    public AgentContestInfoDTO(String alliesTeamName, String agentName, boolean dataShouldDelete){
        this.alliesTeamName=alliesTeamName;
        this.agentName=agentName;
        this.dataShouldDelete=dataShouldDelete;
    }
     public boolean getIsDataShouldBeDeleted(){
        return this.dataShouldDelete;
   }
    public String getAgentName() {
        return agentName;
    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    public void setDataShouldDelete(boolean dataShouldDelete) {
        this.dataShouldDelete = dataShouldDelete;
    }

}
