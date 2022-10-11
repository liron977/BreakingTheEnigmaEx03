package bruteForce;

public class ContestStatusInfoDTO {
    private String alliesWinnerTeamName;
    private String contestStatus;
    private boolean isContestEnded;
    private boolean isUboatSettingsCompleted;
    private boolean isAlliesConfirmedGameOver;
    public ContestStatusInfoDTO(boolean isUboatSettingsCompleted,String contestStatus,boolean isContestEnded,String alliesWinnerTeamName,boolean isAlliesConfirmedGameOver){
        this.isContestEnded=isContestEnded;
        this.alliesWinnerTeamName=alliesWinnerTeamName;
        this.contestStatus=contestStatus;
        this.isAlliesConfirmedGameOver=false;
        this.isUboatSettingsCompleted=isUboatSettingsCompleted;

        this.isAlliesConfirmedGameOver = isAlliesConfirmedGameOver;
    }
    public boolean getIsAlliesConfirmedGameOver(){
        return isAlliesConfirmedGameOver;
    }

    public String getContestStatus() {
        return contestStatus;
    }
    public boolean isContestActive(){
        if(contestStatus.equals("Active")){
            return true;
        }
        return false;
    }

    public boolean isUboatSettingsCompleted() {
        return isUboatSettingsCompleted;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
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