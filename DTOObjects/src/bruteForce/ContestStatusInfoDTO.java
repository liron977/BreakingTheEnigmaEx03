package bruteForce;

public class ContestStatusInfoDTO {
    private String alliesWinnerTeamName;
    private boolean isContestEnded;
    private boolean isAlliesConfirmedGameOver;
    public ContestStatusInfoDTO(boolean isContestEnded,String alliesWinnerTeamName,boolean isAlliesConfirmedGameOver){
        this.isContestEnded=isContestEnded;
        this.alliesWinnerTeamName=alliesWinnerTeamName;
        this.isAlliesConfirmedGameOver=false;
        this.isAlliesConfirmedGameOver = isAlliesConfirmedGameOver;
    }
    public boolean getIsAlliesConfirmedGameOver(){
        return isAlliesConfirmedGameOver;
    }

    public void setAlliesConfirmedGameOver(boolean alliesConfirmedGameOver) {
        isAlliesConfirmedGameOver = alliesConfirmedGameOver;
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
