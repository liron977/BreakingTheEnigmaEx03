package bruteForce;

import java.util.ArrayList;
import java.util.List;

public class ContestStatusInfoDTO {
    private String alliesWinnerTeamName;
    private String contestStatus;
    private List<String> alliesTeamNames;
    private boolean isContestEnded;
    private boolean isUboatSettingsCompleted;
    private boolean isAlliesConfirmedGameOver;
    public ContestStatusInfoDTO(boolean isUboatSettingsCompleted,String contestStatus,boolean isContestEnded,String alliesWinnerTeamName,boolean isAlliesConfirmedGameOver){
        this.isContestEnded=isContestEnded;
        this.alliesWinnerTeamName=alliesWinnerTeamName;
        this.contestStatus=contestStatus;
        //this.isAlliesConfirmedGameOver=false;
        alliesTeamNames=new ArrayList<>();
        this.isUboatSettingsCompleted=isUboatSettingsCompleted;
        this.isAlliesConfirmedGameOver = isAlliesConfirmedGameOver;
    }
//    public ContestStatusInfoDTO() {
//    }
        public boolean getIsAlliesConfirmedGameOver(){
        return isAlliesConfirmedGameOver;
    }

    public void setAlliesTeamNames(List<String> alliesTeamNames) {
        this.alliesTeamNames = alliesTeamNames;
    }
    public void addAllies(String alliesName){
        alliesTeamNames.add(alliesName);
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
    public boolean isAlliesExist(String alliesTeamName){
        for (String str:alliesTeamNames) {
            if(str.equals(alliesTeamName)){
                return true;
            }
        }
        return false;
    }

    public void setUboatSettingsCompleted(boolean uboatSettingsCompleted) {
        isUboatSettingsCompleted = uboatSettingsCompleted;
    }

    public String getAlliesWinnerTeamName() {
        return alliesWinnerTeamName;
    }
    public boolean isContestEnded() {
        return isContestEnded;
    }
}