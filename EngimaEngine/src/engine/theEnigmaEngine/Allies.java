package engine.theEnigmaEngine;

public class Allies {

   private String alliesName;
   private int missionSize;

    public Allies(int missionSize, String alliesName){
        this.alliesName=alliesName;
        this.missionSize=missionSize;

    }

    public int getMissionSize() {
        return missionSize;
    }

    public String getAlliesName() {
        return alliesName;
    }

    public void setAlliesName(String alliesName) {
        this.alliesName = alliesName;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }
}