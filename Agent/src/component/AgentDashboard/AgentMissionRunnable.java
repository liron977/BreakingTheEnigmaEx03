package component.AgentDashboard;

import bruteForce.BruteForceResultDTO;
import engineManager.EngineManager;
import machineDTO.ConvertedStringDTO;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentMissionRunnable implements Runnable{
        private BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
        private EngineManager engineManagerCopy;
        private List<String> startingPositions;
        private String stringToConvert;
       private String alliesTeamName;
        private String finalPosition;
        private String initialStartingPosition;
        private int sizeOfMission;

        public AgentMissionRunnable(String initialStartingPosition, int sizeOfMission,
                                    String finalPosition,EngineManager engineManager,
                                    String stringToConvert, String alliesTeamName){
            this.initialStartingPosition=initialStartingPosition;
            this.engineManagerCopy=engineManager;
            this.stringToConvert=stringToConvert;
            this.sizeOfMission=sizeOfMission;
            this.finalPosition=finalPosition;
        }

        public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
            this.resultsBlockingQueue = resultsBlockingQueue;
        }

    public BlockingQueue<BruteForceResultDTO> getResultsBlockingQueue() {
        return resultsBlockingQueue;
    }

    public void getConvertedStringsFounded() throws InterruptedException {

            for (String optionalStartingPosition : startingPositions) {

                engineManagerCopy.chooseManuallyStartingPosition(optionalStartingPosition);
                engineManagerCopy.createCurrentCodeDescriptionDTO();
                String convertedStringCode=engineManagerCopy.getCurrentCodeDescription();
                ConvertedStringDTO convertedStringDTOTemp = engineManagerCopy.getConvertedString(stringToConvert);
                if (engineManagerCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                    BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(convertedStringDTOTemp.getConvertedString(),alliesTeamName ,convertedStringCode);
                    resultsBlockingQueue.put(bruteForceResultDTO);
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