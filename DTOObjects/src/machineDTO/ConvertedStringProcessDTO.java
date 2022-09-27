package machineDTO;

import java.util.List;

public class ConvertedStringProcessDTO {
    private String convertedString;
    private String stringToConvertWithoutExcludedSignals;
    private List<String> exceptionList;
    public ConvertedStringProcessDTO(String stringToConvertWithoutExcludedSignals, String convertedString, List<String> exceptionList){
        this.stringToConvertWithoutExcludedSignals=stringToConvertWithoutExcludedSignals;
        this.convertedString=convertedString;
        this.exceptionList=exceptionList;
    }
    public String getConvertedString() {
        return convertedString;
    }
    public String getStringToConvertWithoutExcludedSignals() {
        return stringToConvertWithoutExcludedSignals;
    }

    public List<String> getExceptionList() {
        return exceptionList;
    }
}
