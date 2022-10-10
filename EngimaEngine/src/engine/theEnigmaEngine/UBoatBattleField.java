package engine.theEnigmaEngine;

import bruteForce.AlliesDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UBoatBattleField implements Serializable {
   private String battleName;
   private int alliesNeededTeamsAmount;
   private String level;
   private int alliesActiveTeamsAmount;
   private String contestStatus;
    private String uploadedBy;
    private String convertedString;
    private String stringToConvert;
    private List<Allies> alliesRegisteredToContest;
    private boolean isContestEnded;
    private String alliesWinnwerTeamName;
    private boolean isAlliesConfirmedGameOver;

   public UBoatBattleField(String battleName, int alliesNeededTeamsAmount, String level){
       this.battleName=battleName;
       this.alliesNeededTeamsAmount=alliesNeededTeamsAmount;
       this.level=level;
       this.alliesActiveTeamsAmount=0;
       this.contestStatus="Wait..";
       this.convertedString="";
       this.stringToConvert="";
       alliesRegisteredToContest=new ArrayList<>();
       this.isContestEnded=false;
       this.alliesWinnwerTeamName="";
       this.isAlliesConfirmedGameOver=false;
   }
public boolean isAlliesExists(String alliesTeamName){
    for (Allies allies:alliesRegisteredToContest) {
        if(allies.getAlliesName().equals(alliesTeamName)){
            return true;
        }
    }
     return false;
}
    public void addAgentToAllies(AlliesAgent agent,String alliesTeamName){
        for (Allies a:alliesRegisteredToContest) {
            if(a.getAlliesName().equals(alliesTeamName)){
                a.addAgent(agent);
            }
        }
    }
   public boolean addAllies(Allies allies){
      // Allies newAllies=new Allies(alliesDTO.getAlliesName());
       //newAllies.setMissionSize(alliesDTO.getMissionSize());
       if(alliesActiveTeamsAmount<alliesNeededTeamsAmount){
           alliesRegisteredToContest.add(allies);
           alliesActiveTeamsAmount++;
           return true;
       }
       return false;
   }


    public List<Allies> getAlliesRegisteredToContest() {
        return alliesRegisteredToContest;
    }

    public void setUploadedBy(String uploadedBy){
       this.uploadedBy=uploadedBy;
   }
    public String getLevel() {
        return level;
    }
    public String getBattleName() {
        return battleName;
    }
    public int getAlliesNeededTeamsAmount() {
        return alliesNeededTeamsAmount;
    }
    public String getUploadedByName(){
       return uploadedBy;
    }

    public int getAlliesActiveTeamsAmount() {
        return alliesActiveTeamsAmount;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
    }

    public void setConvertedString(String convertedString) {
        this.convertedString = convertedString;
    }

    public String getConvertedString() {
        return convertedString;
    }
    public int getAlliesSizeOfMission(){
      return alliesRegisteredToContest.get(0).getMissionSize();
    }

    public String getStringToConvert() {
        return stringToConvert;
    }

    public void setStringToConvert(String stringToConvert) {
        this.stringToConvert = stringToConvert;
    }

    public void setIsContestEnded(boolean contestEnded) {
        isContestEnded = contestEnded;
    }
    public boolean getIsContestEnded() {
       return isContestEnded;
    }
    public String getAlliesWinnwerTeamName() {
        return alliesWinnwerTeamName;
    }
    public void setAlliesWinnwerTeamName(String alliesWinnwerTeamName) {
        this.alliesWinnwerTeamName = alliesWinnwerTeamName;
    }
    public void setAlliesConfirmedGameOver(boolean alliesConfirmedGameOver) {
        isAlliesConfirmedGameOver = alliesConfirmedGameOver;
    }
    public boolean getIsAlliesConfirmedGameOver(){
        return isAlliesConfirmedGameOver;
    }
}