package bruteForce;

public class AlliesDTO {
   private String alliesName;
    private int missionSize;
    private int agentsAmount;

    public AlliesDTO(int missionSize, String alliesName){
        this.alliesName=alliesName;
        this.missionSize=missionSize;
        agentsAmount=0;
    }

    public void setAgentsAmount(int agentsAmount) {
        this.agentsAmount = agentsAmount;
    }

    public int getAgentsAmount() {
        return agentsAmount;
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