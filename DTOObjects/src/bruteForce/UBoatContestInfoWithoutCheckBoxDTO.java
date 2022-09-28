package bruteForce;

public class UBoatContestInfoWithoutCheckBoxDTO {
    private String battleFieldName;
    private String uBoatUserName;
    private String contestStatus;
    private String contestLevel;
    private int amountOfNeededDecryptionTeams;
    private int amountOfActiveDecryptionTeams;
//   private CheckBox select;
  /*  private Boolean isSelected;*/

    public UBoatContestInfoWithoutCheckBoxDTO(String battleFieldName, String uBoatUserName,
                                              String contestStatus, String contestLevel,
                                              int amountOfNeededDecryptionTeams, int amountOfActiveDecryptionTeams){
        this.battleFieldName=battleFieldName;
        this.uBoatUserName=uBoatUserName;
        this.contestStatus=contestStatus;
        this.contestLevel=contestLevel;
        this.amountOfNeededDecryptionTeams=amountOfNeededDecryptionTeams;
        this.amountOfActiveDecryptionTeams=amountOfActiveDecryptionTeams;
        //this.select = new CheckBox();
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
/*   public CheckBox getSelect() {
        return this.select;
    }*/
/*      public Boolean getIsSelected(){
          return isSelected;
      }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }*/
/* public void setSelect(CheckBox select) {
        this.select = select;
    }*/
}
