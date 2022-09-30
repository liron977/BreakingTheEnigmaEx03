package bruteForce;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class UBoatContestInfoWithCheckBoxDTO {
    private StringProperty battleFieldName;
    private StringProperty uBoatUserName;
    private final StringProperty contestStatus;
    private StringProperty contestLevel;
    private IntegerProperty amountOfNeededDecryptionTeams;
    private IntegerProperty amountOfActiveDecryptionTeams;
    private CheckBox selectionContestColumn;
    private Boolean isSelected;

    public UBoatContestInfoWithCheckBoxDTO(UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoWithoutCheckBoxDTO){
        this.battleFieldName=new SimpleStringProperty(uBoatContestInfoWithoutCheckBoxDTO.getBattleFieldName());
        this.uBoatUserName=new SimpleStringProperty(uBoatContestInfoWithoutCheckBoxDTO.getUBoatUserName());
        this.contestStatus=new SimpleStringProperty(uBoatContestInfoWithoutCheckBoxDTO.getContestStatus());
        this.contestLevel=new SimpleStringProperty(uBoatContestInfoWithoutCheckBoxDTO.getContestLevel());
        this.amountOfNeededDecryptionTeams=new SimpleIntegerProperty(uBoatContestInfoWithoutCheckBoxDTO.getAmountOfNeededDecryptionTeams());
        this.amountOfActiveDecryptionTeams=new SimpleIntegerProperty(uBoatContestInfoWithoutCheckBoxDTO.getAmountOfActiveDecryptionTeams());
        this.selectionContestColumn = new CheckBox();
        this.isSelected=false;
    }

    public int getAmountOfActiveDecryptionTeams() {
        return amountOfActiveDecryptionTeams.getValue();
    }

    public String getBattleFieldName() {
        return battleFieldName.getValue();
    }

    public String getContestLevel() {
        return contestLevel.getValue();
    }

    public String getUBoatUserName() {
        return uBoatUserName.getValue();
    }

    public final StringProperty getContestStatus() {
        return contestStatus;
    }


    public final StringProperty contestStatusProperty() {
        return contestStatus;
    }
    public final IntegerProperty amountOfActiveDecryptionTeamsProperty() {
        return amountOfActiveDecryptionTeams;
    }
    public int getAmountOfNeededDecryptionTeams() {
        return amountOfNeededDecryptionTeams.getValue();
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