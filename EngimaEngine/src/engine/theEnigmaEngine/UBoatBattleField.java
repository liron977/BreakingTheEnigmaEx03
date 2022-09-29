package engine.theEnigmaEngine;

import bruteForce.AlliesDTO;

import java.util.ArrayList;
import java.util.List;

public class UBoatBattleField {
   private String battleName;
   private int alliesNeededTeamsAmount;
   private String level;
   private int alliesActiveTeamsAmount;
   private String contestStatus;
    private String uploadedBy;
    private String convertedString;
    private List<Allies> alliesRegisteredToContest;

   public UBoatBattleField(String battleName, int alliesNeededTeamsAmount, String level){
       this.battleName=battleName;
       this.alliesNeededTeamsAmount=alliesNeededTeamsAmount;
       this.level=level;
       this.alliesActiveTeamsAmount=0;
       this.contestStatus="Wait..";
       this.convertedString="";
       alliesRegisteredToContest=new ArrayList<>();
   }
   public boolean addAllies(AlliesDTO alliesDTO){
       Allies newAllies=new Allies(alliesDTO.getMissionSize(),alliesDTO.getAlliesName());
       if(alliesActiveTeamsAmount<alliesNeededTeamsAmount){
           alliesRegisteredToContest.add(newAllies);
           alliesActiveTeamsAmount++;
           return true;
       }
       return false;
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
}