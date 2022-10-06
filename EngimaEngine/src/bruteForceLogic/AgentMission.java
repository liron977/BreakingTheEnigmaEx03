package bruteForceLogic;

import bruteForce.BruteForceResultDTO;
import machineEngine.EngineManager;
import machineDTO.ConvertedStringDTO;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentMission implements Runnable {
   // private BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
    private EngineManager engineManagerCopy;
    private List<String> startingPositions;
    private String stringToConvert;
   private String alliesTeamName;
   public AgentMission(String alliesTeamName,String stringToConvert,EngineManager engineManagerCopy, List<String> startingPositions) {
       this.alliesTeamName = alliesTeamName;
       this.engineManagerCopy = engineManagerCopy;
       this.stringToConvert = stringToConvert;
       this.startingPositions = startingPositions;
   }

    public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
        //this.resultsBlockingQueue = resultsBlockingQueue;
    }

    public void getConvertedStringsFounded() throws InterruptedException {

        for (String optionalStartingPosition : startingPositions) {

            engineManagerCopy.chooseManuallyStartingPosition(optionalStartingPosition);
            engineManagerCopy.createCurrentCodeDescriptionDTO();
            String convertedStringCode=engineManagerCopy.getCurrentCodeDescription();
            ConvertedStringDTO convertedStringDTOTemp = engineManagerCopy.getConvertedString(stringToConvert);
            if (engineManagerCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(0,convertedStringDTOTemp.getConvertedString(),alliesTeamName ,convertedStringCode);
               // resultsBlockingQueue.put(bruteForceResultDTO);
            }
        }
    }
    @Override
    public void run() {
        try {
            getConvertedStringsFounded();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}