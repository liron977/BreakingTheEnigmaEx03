package bruteForce;

import java.util.ArrayList;
import java.util.List;

public class ContestStatusInfoDTO {
    private String alliesWinnerTeamName;
    private String contestStatus;
    //private List<String> alliesTeamNames;
    private List<AlliesConfirmedDTO> alliesConfirmedDTOList;
    private List<AgentContestInfoDTO> agentContestInfoList;
    private boolean isContestEnded;
    private boolean isUboatSettingsCompleted;
   // private boolean isAlliesConfirmedGameOver;
    public ContestStatusInfoDTO(boolean isUboatSettingsCompleted,String contestStatus,
                                boolean isContestEnded,
                                String alliesWinnerTeamName){
        this.isContestEnded=isContestEnded;
        this.alliesWinnerTeamName=alliesWinnerTeamName;
        this.contestStatus=contestStatus;
        //this.isAlliesConfirmedGameOver=false;
        alliesConfirmedDTOList=new ArrayList<>();
        this.isUboatSettingsCompleted=isUboatSettingsCompleted;
        agentContestInfoList=new ArrayList<>();
       // this.isAlliesConfirmedGameOver = isAlliesConfirmedGameOver;
    }
//    public ContestStatusInfoDTO() {
//    }
        public boolean getIsAlliesConfirmedGameOverByAlliesTeamName(String alliesTeamName){
            for (AlliesConfirmedDTO alliesConfirmDTO: alliesConfirmedDTOList) {
                if(alliesConfirmDTO.getAlliesName().equals(alliesTeamName)){
                    return alliesConfirmDTO.getIsAlliesConfirmedGameOver();
                }

            }
            return false;
    }

    public void setAlliesConfirmedDTOList(List<AlliesConfirmedDTO> alliesConfirmedDTOList) {
        this.alliesConfirmedDTOList = alliesConfirmedDTOList;
    }


    /* public void setAlliesTeamNames(List<String> alliesTeamNames) {
            this.alliesTeamNames = alliesTeamNames;
        }*/
    public void addAllies(String alliesName,boolean isAlliesConfirmedGameOver){
        AlliesConfirmedDTO alliesConfirmedDTO=new AlliesConfirmedDTO(alliesName,isAlliesConfirmedGameOver);
        alliesConfirmedDTOList.add(alliesConfirmedDTO);
    }
    public void addAgents(String alliesTeamName,List<String> agentNameList,boolean isDataShouldBeDeleted){
        for (String agentName:agentNameList) {
            AgentContestInfoDTO agentContestInfoDTO=new AgentContestInfoDTO(
                    alliesTeamName,agentName,isDataShouldBeDeleted);
            agentContestInfoList.add(agentContestInfoDTO);
        }

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

    public void setAlliesConfirmedGameOver(String alliesTeamName ,boolean alliesConfirmedGameOver) {
        for (AlliesConfirmedDTO alliesConfirmDTO:alliesConfirmedDTOList) {
            if(alliesConfirmDTO.getAlliesName().equals(alliesTeamName)){
                alliesConfirmDTO.setAlliesConfirmedGameOver(alliesConfirmedGameOver);
            }
        }
    }

    public void setAlliesWinnerTeamName(String alliesWinnerTeamName) {
        this.alliesWinnerTeamName = alliesWinnerTeamName;
    }
    public boolean isAlliesExist(String alliesTeamName){
        for (AlliesConfirmedDTO alliesConfirmedDTO:alliesConfirmedDTOList) {
            if(alliesConfirmedDTO.getAlliesName().equals(alliesTeamName)){
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


    public void addAgent(String alliesTeamName,String agentName,boolean isDatsShouldDelete){
    AgentContestInfoDTO agentContestInfo=new AgentContestInfoDTO(alliesTeamName,agentName,isDatsShouldDelete);
        agentContestInfoList.add(agentContestInfo);
    }
    public void setAgentContestInfoList(List<AgentContestInfoDTO> agentContestInfoList) {
        this.agentContestInfoList = agentContestInfoList;
    }
    public boolean getIsDataShouldDeleteByAgentName(String agentName){
        for (AgentContestInfoDTO agentContestInfoDTO: agentContestInfoList) {
            if(agentContestInfoDTO.getAgentName().equals(agentName)){
                return agentContestInfoDTO.getIsDataShouldBeDeleted();
            }
        }
        return false;
    }
    public void setIsDataShouldDeleteByAgentName(String agentName ,boolean isDatsShouldDelete) {
        for (AgentContestInfoDTO agentContestInfoDTO:agentContestInfoList) {
            if(agentContestInfoDTO.getAgentName().equals(agentName)){
                agentContestInfoDTO.setDataShouldDelete(isDatsShouldDelete);
            }
        }
    }
    public void setIsDataShouldDeleteByAlliesName(String alliesName ,boolean isDatsShouldDelete) {
        for (AgentContestInfoDTO agentContestInfoDTO:agentContestInfoList) {
            if(agentContestInfoDTO.getAlliesTeamName().equals(alliesName)){
                agentContestInfoDTO.setDataShouldDelete(isDatsShouldDelete);
            }
        }
    }
public boolean getIsUboatSettingsCompleted(){
        return isUboatSettingsCompleted;
}
}