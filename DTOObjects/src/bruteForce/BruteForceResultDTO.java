package bruteForce;

public class BruteForceResultDTO {
    private String convertedString;
    private String alliesTeamName;
    private String codeDescription;
    public BruteForceResultDTO(String convertedString,String alliesTeamName,String codeDescription){
        this.codeDescription=codeDescription;
        this.alliesTeamName=alliesTeamName;
        this.convertedString=convertedString;
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