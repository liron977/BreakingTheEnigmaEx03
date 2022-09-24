package Controllers.SingleDecryptionInfo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DecryptionInfo implements DecryptionInfoInterface{
   protected SimpleStringProperty codeConfigurationProperty;
    protected SimpleStringProperty convertedStringProperty;
    protected SimpleIntegerProperty agentIdProperty;
    public DecryptionInfo(String codeConfiguration,String convertedString,int agentId) {
        this.codeConfigurationProperty = new SimpleStringProperty(codeConfiguration);
        this.convertedStringProperty = new SimpleStringProperty(convertedString);
        this.agentIdProperty = new SimpleIntegerProperty(agentId);
    }
    @Override
    public String getCodeConfiguration() {
        return codeConfigurationProperty.get();
    }
    @Override
    public String getConvertedString() {
        return convertedStringProperty.get();
    }
    @Override
    public int getAgentId() {
        return agentIdProperty.get();
    }
    @Override
    public void setCodeConfiguration(String codeConfiguration) {codeConfigurationProperty.set(codeConfiguration);}
    @Override
    public void setConvertedString(String convertedString) {
        convertedStringProperty.set(convertedString);
    }
    @Override
    public void setAgentId(int agentId) {
        agentIdProperty.set(agentId+1);
    }
    @Override
    public String toString() {
        return codeConfigurationProperty + " : " + convertedStringProperty + " : " +agentIdProperty;
    }
}
