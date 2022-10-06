package BruteForce;

import MachineEngine.MachineEngine;
import bruteForce.BruteForceResultDTO;
import javafx.beans.property.SimpleBooleanProperty;
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

    UiAdapterInterface uiAdapterInterface;
    int missionNumber=0;
    String lastStartingPos;
    public AgentMissionRunnable(String lastStartingPos,int missionNumber,UiAdapterInterface uiAdapterInterface,MachineEngine machineEngineCopy,
                                String stringToConvert, String alliesTeamName
            , String initialStartingPosition, int sizeOfMission) {

        this.machineEngineCopy = machineEngineCopy;
        this.stringToConvert = stringToConvert;
        this.initialStartingPosition = initialStartingPosition;
        this.alliesTeamName = alliesTeamName;
        this.sizeOfMission=sizeOfMission;
        this.resultsBlockingQueue= new LinkedBlockingQueue<>();
        this.missionNumber=missionNumber;
        this.lastStartingPos=lastStartingPos;

        this.uiAdapterInterface=uiAdapterInterface;
    }

    public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
        this.resultsBlockingQueue = resultsBlockingQueue;
    }

    public BlockingQueue<BruteForceResultDTO> getResultsBlockingQueue() {
        return resultsBlockingQueue;
    }

    public synchronized void getConvertedStringsFounded() throws InterruptedException {
        int index = 0;
Thread.currentThread().setName("AgentMissionRunnable");
        while (index<sizeOfMission){
            machineEngineCopy.chooseManuallyStartingPosition(initialStartingPosition);
            machineEngineCopy.createCurrentCodeDescriptionDTO();
            String convertedStringCode = machineEngineCopy.getCurrentCodeDescription();
            ConvertedStringDTO convertedStringDTOTemp = machineEngineCopy.getConvertedString(stringToConvert);
            if (machineEngineCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(missionNumber,convertedStringDTOTemp.getConvertedString(), alliesTeamName, convertedStringCode);
                bruteForceResultDTO.setTheMissionNumber(missionNumber);
                resultsBlockingQueue.put(bruteForceResultDTO);
                System.out.println(bruteForceResultDTO.getConvertedString());
                System.out.println(bruteForceResultDTO.getCodeDescription());
            }
            if(index==999999998){
                int x=0;
            }
            if(initialStartingPosition.equals(lastStartingPos)){
                break;
            }
            initialStartingPosition= machineEngineCopy.createPossiblePosition(initialStartingPosition);
            index++;
        }
        publishResults();

    }
    private void publishResults() throws InterruptedException {
        synchronized (this){
            if(resultsBlockingQueue.size()>0) {
                uiAdapterInterface.updateResults(resultsBlockingQueue);
                uiAdapterInterface.updateResultsOnAgent(resultsBlockingQueue);
            }
        }
       // System.out.println(index+"index");
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