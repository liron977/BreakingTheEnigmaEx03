package bruteForce;

import java.io.Serializable;

public class BruteForceResultDTO implements Serializable {
    private String convertedString;
    private String alliesTeamName;
    private String codeDescription;
    private String agentName;
    private int theMissionNumber;

    public BruteForceResultDTO(int theMissionNumber,String convertedString
            ,String alliesTeamName,String codeDescription,String  agentName){
        this.codeDescription=codeDescription;
        this.alliesTeamName=alliesTeamName;
        this.convertedString=convertedString;
        this.theMissionNumber=theMissionNumber;
        this.agentName=agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
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