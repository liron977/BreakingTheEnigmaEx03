package Controllers.SingleDecryptionInfo;

public interface DecryptionInfoInterface {
    String getCodeConfiguration();
    String getConvertedString();
    int getAgentId();
    void setCodeConfiguration(String codeConfiguration);
    void setConvertedString(String convertedString);
    void setAgentId(int agentId);

}
