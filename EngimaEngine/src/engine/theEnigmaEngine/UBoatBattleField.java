package engine.theEnigmaEngine;

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
    private boolean isUboatReady;
    private String stringToConvert;
    private List<Allies> alliesRegisteredToContest;
    private boolean isContestEnded;
    private String alliesWinnwerTeamName;
    private boolean isAlliesConfirmedGameOver;
    private boolean isConvertedStringSet;

   public UBoatBattleField(String battleName, int alliesNeededTeamsAmount, String level){
       this.battleName=battleName;
       this.alliesNeededTeamsAmount=alliesNeededTeamsAmount;
       this.level=level;
       this.alliesActiveTeamsAmount=0;
       this.contestStatus="Wait..";
       this.convertedString="";
       this.stringToConvert="";
       this.isUboatReady=false;
       alliesRegisteredToContest=new ArrayList<>();
       this.isContestEnded=false;
       this.isConvertedStringSet=false;
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
public void setIsAlliesConfirmedGameOver(){
    isAlliesConfirmedGameOver=false;
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
           if(alliesActiveTeamsAmount==alliesNeededTeamsAmount&&isConvertedStringSet){
               contestStatus="Active";
               isUboatReady=true;
           }
           return true;
       }
       return false;
   }
public boolean getIsConvertedStringSet(){
       return isConvertedStringSet;
}

    public List<Allies> getAlliesRegisteredToContest() {
        return alliesRegisteredToContest;
    }
public List<String> getAlliesRegisteredNames(){
       List<String> alliesTeamNames=new ArrayList<>();
    for (Allies allies:alliesRegisteredToContest) {
        alliesTeamNames.add(allies.getAlliesName());

    }
    return alliesTeamNames;
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
        isConvertedStringSet=true;
        /*if(alliesActiveTeamsAmount==alliesNeededTeamsAmount){
            contestStatus="Active";
            isUboatReady=true;
        }*/
    }
    public void setIsActiveContest(){

        if(alliesActiveTeamsAmount==alliesNeededTeamsAmount&&isConvertedStringSet){
            contestStatus="Active";
            isUboatReady=true;
        }
    }


    public String getConvertedString() {
        return convertedString;
    }
    public int getAlliesSizeOfMission(String alliesTeamName){
        for (Allies allies:alliesRegisteredToContest) {
            if(allies.getAlliesName().equals(alliesTeamName)){
                return allies.getMissionSize();
            }
        }
        return 1;
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
    public void clearValues(){
       alliesActiveTeamsAmount=0;
        this.contestStatus="Wait..";
        this.convertedString="";
        this.stringToConvert="";
        this.isUboatReady=false;
        alliesRegisteredToContest=new ArrayList<>();
        this.isContestEnded=false;
        this.alliesWinnwerTeamName="";
        this.isConvertedStringSet=false;
       // this.isAlliesConfirmedGameOver=false;
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