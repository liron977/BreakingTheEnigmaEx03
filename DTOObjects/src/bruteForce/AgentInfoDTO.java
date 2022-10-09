package bruteForce;

public class AgentInfoDTO {

    private String agentName;
    private int threadsAmount;
    private int missionsAmount;
    private String alliesTeamName;
    private int amountOfCandidatesStrings;
    private int amountOfReceivedMissions;
    private int amountOfMissionsToExecute;

    public AgentInfoDTO(String agentName, int threadsAmount,int missionsAmount, String alliesTeamName){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;
        this.alliesTeamName=alliesTeamName;
        this.amountOfCandidatesStrings=0;
        this.amountOfMissionsToExecute=0;
        this.amountOfReceivedMissions=0;
    }

    public void setAmountOfCandidatesStrings(int amountOfCandidatesStrings) {
        this.amountOfCandidatesStrings = amountOfCandidatesStrings;
    }

    public void setAmountOfMissionsToExecute(int amountOfMissionsToExecute) {
        this.amountOfMissionsToExecute = amountOfMissionsToExecute;
    }

    public void setAmountOfReceivedMissions(int amountOfReceivedMissions) {
        this.amountOfReceivedMissions = amountOfReceivedMissions;
    }

    public int getAmountOfCandidatesStrings() {
        return amountOfCandidatesStrings;
    }

    public int getAmountOfMissionsToExecute() {
        return amountOfMissionsToExecute;
    }

    public int getAmountOfReceivedMissions() {
        return amountOfReceivedMissions;
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