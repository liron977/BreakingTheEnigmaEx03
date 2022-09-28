package bruteForce;

public class UBoatContestInfoDTO {
    private String battleFieldName;
    private String uBoatUserName;
    private String contestStatus;
    private String contestLevel;
    private int amountOfNeededDecryptionTeams;
    private int amountOfActiveDecryptionTeams;

    public UBoatContestInfoDTO(String battleFieldName, String uBoatUserName,
                               String contestStatus, String contestLevel,
                               int amountOfNeededDecryptionTeams, int amountOfActiveDecryptionTeams){
        this.battleFieldName=battleFieldName;
        this.uBoatUserName=uBoatUserName;
        this.contestStatus=contestStatus;
        this.contestLevel=contestLevel;
        this.amountOfNeededDecryptionTeams=amountOfNeededDecryptionTeams;
        this.amountOfActiveDecryptionTeams=amountOfActiveDecryptionTeams;
    }

    public int getAmountOfActiveDecryptionTeams() {
        return amountOfActiveDecryptionTeams;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getContestLevel() {
        return contestLevel;
    }

    public String getUBoatUserName() {
        return uBoatUserName;
    }

    public String getContestStatus() {
        return contestStatus;
    }
    public int getAmountOfNeededDecryptionTeams() {
        return amountOfNeededDecryptionTeams;
    }
}
