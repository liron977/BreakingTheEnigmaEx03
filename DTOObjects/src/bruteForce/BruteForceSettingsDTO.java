package bruteForce;

public class BruteForceSettingsDTO {
    String convertedString;
    int agentsAmount;
    int missionSize;
    String missionLevel;
   public BruteForceSettingsDTO( String convertedString,int agentsAmount, int missionSize, String missionLevel){
       this.agentsAmount =agentsAmount;
       this.convertedString=convertedString;
       this.missionLevel=missionLevel;
       this.missionSize=missionSize;
    }
    public String getConvertedString() {
        return convertedString;
    }
    public int getAgentsAmount() {
        return agentsAmount;
    }
    public int getMissionSize() {
        return missionSize;
    }
    public String getMissionLevel() {
        return missionLevel;
    }
}