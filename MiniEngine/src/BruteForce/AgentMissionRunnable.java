package BruteForce;

import MachineEngine.MachineEngine;
import bruteForce.BruteForceResultDTO;
import machineEngine.EngineManager;
import machineDTO.ConvertedStringDTO;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentMissionRunnable implements Runnable {
    private BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
    private MachineEngine machineEngineCopy;
    private String initialStartingPosition;
    private String stringToConvert;
    private String alliesTeamName;
    private String finalPosition;
    private int sizeOfMission;
    private List<String> listOfPossiblePosition;

    public AgentMissionRunnable(MachineEngine machineEngineCopy,
                                String stringToConvert, String alliesTeamName
            , String initialStartingPosition,int sizeOfMission) {

        this.machineEngineCopy = machineEngineCopy;
        this.stringToConvert = stringToConvert;
        this.initialStartingPosition = initialStartingPosition;
        this.alliesTeamName = alliesTeamName;
        this.sizeOfMission=sizeOfMission;
        this.resultsBlockingQueue= new LinkedBlockingQueue<>();
    }

    public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
        this.resultsBlockingQueue = resultsBlockingQueue;
    }

    public BlockingQueue<BruteForceResultDTO> getResultsBlockingQueue() {
        return resultsBlockingQueue;
    }

    public synchronized void getConvertedStringsFounded() throws InterruptedException {
        int index = 0;

        while (index<sizeOfMission){
if(initialStartingPosition.equals(" 'T")&&machineEngineCopy.getTheMachineEngine().getReflector().getReflectorId().equals("II")){
    int x=0;
}
if(machineEngineCopy.getTheMachineEngine().getReflector().getReflectorId().equals("II")){
    int x=0;
}
            machineEngineCopy.chooseManuallyStartingPosition(initialStartingPosition);
            machineEngineCopy.createCurrentCodeDescriptionDTO();
            String convertedStringCode = machineEngineCopy.getCurrentCodeDescription();
            ConvertedStringDTO convertedStringDTOTemp = machineEngineCopy.getConvertedString(stringToConvert);
            if (machineEngineCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(convertedStringDTOTemp.getConvertedString(), alliesTeamName, convertedStringCode);
                resultsBlockingQueue.put(bruteForceResultDTO);
                System.out.println(bruteForceResultDTO.getConvertedString());
                System.out.println(bruteForceResultDTO.getCodeDescription());
            }
            initialStartingPosition= machineEngineCopy.createPossiblePosition(initialStartingPosition);

        }
        System.out.println(index+"index");
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