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
    private CodeDescriptionDTO codeDescriptionDTO;

     public TheMachineSettingsDTO(int amountOfUsedRotors, int maxAmountOfRotors, int amountOfReflectors, int amountOfProcessedMessages, CodeDescriptionDTO codeDescriptionDTO,List<String> reflectorsId, String[] rotorsId,String keyboard) {
         this.amountOfProcessedMessages = amountOfProcessedMessages;
         this.amountOfReflectors = amountOfReflectors;
         this.maxAmountOfRotors = maxAmountOfRotors;
         this.codeDescriptionDTO = codeDescriptionDTO;
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
    public CodeDescriptionDTO getCurrentCodeDescriptionDTO() {
        return codeDescriptionDTO;
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