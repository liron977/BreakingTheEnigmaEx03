package machineDTO;

public class LimitedCodeConfigurationDTO {
    String rotors;
    String positionsAndNotch;
    String reflector;
    String plugBoardPairs;

    String formatCodeConfiguration;

    public LimitedCodeConfigurationDTO(String rotors, String positionsAndNotch, String reflector, String plugBoardPairs,String formatCodeConfiguration){
        this.plugBoardPairs=plugBoardPairs;
        this.positionsAndNotch=positionsAndNotch;
        this.rotors=rotors;
        this.reflector=reflector;
        this.formatCodeConfiguration=formatCodeConfiguration;
    }

    public String getFormatCodeConfiguration() {
        return formatCodeConfiguration;
    }

    public String getPlugBoardPairs() {
        return plugBoardPairs;
    }
    public String getPositionsAndNotch() {
        return positionsAndNotch;
    }
    public String getReflector() {
        return reflector;
    }
    public String getRotors() {
        return rotors;
    }
    public void setRotors(String rotors) {
        this.rotors = rotors;
    }
    public void setPlugBoardPairs(String plugBoardPairs) {
        this.plugBoardPairs = plugBoardPairs;
    }
    public void setPositionsAndNotch(String positionsAndNotch) {
        this.positionsAndNotch = positionsAndNotch;
    }
    public void setReflector(String reflector) {
        this.reflector = reflector;
    }
}