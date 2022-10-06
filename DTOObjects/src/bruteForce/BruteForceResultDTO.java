package bruteForce;

public class BruteForceResultDTO {
    private String convertedString;
    private String alliesTeamName;
    private String codeDescription;
    private int theMissionNumber;
    public BruteForceResultDTO(int theMissionNumber,String convertedString,String alliesTeamName,String codeDescription){
        this.codeDescription=codeDescription;
        this.alliesTeamName=alliesTeamName;
        this.convertedString=convertedString;
        this.theMissionNumber=theMissionNumber;
    }

    public void setTheMissionNumber(int theMissionNumber) {
        this.theMissionNumber = theMissionNumber;
    }

    public int getTheMissionNumber() {
        return theMissionNumber;
    }

    public String getConvertedString() {
        return convertedString;
    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public String getCodeDescription() {
        return codeDescription;
    }
}