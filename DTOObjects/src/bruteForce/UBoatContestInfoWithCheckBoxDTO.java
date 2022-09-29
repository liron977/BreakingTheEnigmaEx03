package bruteForce;

import javafx.scene.control.CheckBox;

public class UBoatContestInfoWithCheckBoxDTO {
    private String battleFieldName;
    private String uBoatUserName;
    private String contestStatus;
    private String contestLevel;
    private int amountOfNeededDecryptionTeams;
    private int amountOfActiveDecryptionTeams;
    private CheckBox selectionContestColumn;
    private Boolean isSelected;

    public UBoatContestInfoWithCheckBoxDTO(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoWithoutCheckBoxDTO){
        this.battleFieldName=uBoatContestInfoWithoutCheckBoxDTO.getBattleFieldName();
        this.uBoatUserName=uBoatContestInfoWithoutCheckBoxDTO.getUBoatUserName();
        this.contestStatus=uBoatContestInfoWithoutCheckBoxDTO.getContestStatus();
        this.contestLevel=uBoatContestInfoWithoutCheckBoxDTO.getContestLevel();
        this.amountOfNeededDecryptionTeams=uBoatContestInfoWithoutCheckBoxDTO.getAmountOfNeededDecryptionTeams();
        this.amountOfActiveDecryptionTeams=uBoatContestInfoWithoutCheckBoxDTO.getAmountOfActiveDecryptionTeams();
        this.selectionContestColumn = new CheckBox();
        this.isSelected=false;
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
   public CheckBox getSelectionContestColumn() {
        return this.selectionContestColumn;
    }
      public Boolean getIsSelected(){
          return isSelected;
      }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
 public void setSelectionContestColumn(CheckBox selectionContestColumn) {
        this.selectionContestColumn = selectionContestColumn;
    }
}