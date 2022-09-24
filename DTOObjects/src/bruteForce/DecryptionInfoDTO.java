package bruteForce;

public class DecryptionInfoDTO {
    private String convertedString;
    private int agentId;
    private String codeDescription;
    public DecryptionInfoDTO(String convertedString,int agentId,String codeDescription){
        this.codeDescription=codeDescription;
        this.agentId=agentId;
        this.convertedString=convertedString;
    }
    public String getConvertedString() {
        return convertedString;
    }
    public int getAgentId(){
        return agentId;
    }
    public String getCodeDescription() {
        return codeDescription;
    }
}
