package machineDTO;

import java.util.List;

public class TheMachineSettingsDTO {
    private int amountOfUsedRotors;
    private int maxAmountOfRotors;
    private int amountOfReflectors;
    private int amountOfProcessedMessages;
    private String keyboard;
    private String[] rotorsId;
     List<String> reflectorsId;
    private FullCodeDescriptionDTO fullCodeDescriptionDTO;

     public TheMachineSettingsDTO(int amountOfUsedRotors, int maxAmountOfRotors, int amountOfReflectors, int amountOfProcessedMessages, FullCodeDescriptionDTO fullCodeDescriptionDTO, List<String> reflectorsId, String[] rotorsId, String keyboard) {
         this.amountOfProcessedMessages = amountOfProcessedMessages;
         this.amountOfReflectors = amountOfReflectors;
         this.maxAmountOfRotors = maxAmountOfRotors;
         this.fullCodeDescriptionDTO = fullCodeDescriptionDTO;
         this.amountOfUsedRotors = amountOfUsedRotors;
         this.reflectorsId = reflectorsId;
         this.rotorsId = rotorsId;
         this.keyboard = keyboard;
     }
    public String[] getRotorsId() {
        return rotorsId;
    }
    public String getKeyboard() {
        return keyboard;
    }
    public List<String> getReflectorsId() {
        return reflectorsId;
    }
    public FullCodeDescriptionDTO getCurrentCodeDescriptionDTO() {
        return fullCodeDescriptionDTO;
    }
    public int getAmountOfProcessedMessages() {
        return amountOfProcessedMessages;
    }
    public int getAmountOfReflectors() {
        return amountOfReflectors;
    }
    public int getAmountOfUsedRotors() {
        return amountOfUsedRotors;
    }
    public int getMaxAmountOfRotors() {
        return maxAmountOfRotors;
    }
}