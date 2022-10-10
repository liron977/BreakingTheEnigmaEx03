package bruteForce;

public class ContestStatusInfoDTO {
    private String alliesWinnerTeamName;
    private boolean isContestEnded;
    public ContestStatusInfoDTO(boolean isContestEnded,String alliesWinnerTeamName){
        this.isContestEnded=isContestEnded;
        this.alliesWinnerTeamName=alliesWinnerTeamName;
    }

    public void setAlliesWinnerTeamName(String alliesWinnerTeamName) {
        this.alliesWinnerTeamName = alliesWinnerTeamName;
    }

    public String getAlliesWinnerTeamName() {
        return alliesWinnerTeamName;
    }
    public boolean isContestEnded() {
        return isContestEnded;
    }
}
