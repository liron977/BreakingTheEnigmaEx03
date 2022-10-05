package machineDTO;

import java.util.List;

public class TheMachineEngineDTO {
    List<String> usedRotorsId;
    //List<String> rotorsSetId;
    String reflectorId;
   /* List<String> reflectorSetId;*/
    int amountOfUsedRotors;
    public TheMachineEngineDTO(List<String> usedRotorsId, /*List<String> rotorsSetId,*/ String reflectorId/*,List<String> reflectorSetId*/){
     this.usedRotorsId=usedRotorsId;
//     this.rotorsSetId=rotorsSetId;
     this.reflectorId=reflectorId;
     this.amountOfUsedRotors=usedRotorsId.size();
//     this.reflectorSetId=reflectorSetId;
     }

    public int getAmountOfUsedRotors() {
        return amountOfUsedRotors;
    }

 /*   public List<String> getReflectorSetId() {
        return reflectorSetId;
    }*/

 /*   public List<String> getRotorsSetId() {
        return rotorsSetId;
    }*/

    public String getReflectorId() {
        return reflectorId;
    }

    public List<String> getUsedRotorsId() {
        return usedRotorsId;
    }
}