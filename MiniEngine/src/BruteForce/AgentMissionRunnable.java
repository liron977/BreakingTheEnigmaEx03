package BruteForce;

import MachineEngine.MachineEngine;
import bruteForce.BruteForceResultDTO;
import javafx.beans.property.SimpleIntegerProperty;
import machineDTO.ConvertedStringDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentMissionRunnable implements Runnable {
    private List<BruteForceResultDTO> resultsList;
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
    SimpleIntegerProperty amountOfMissionsInTheQueue;
   // SimpleIntegerProperty amountOfDecipheringStringsProperty;
   SimpleIntegerProperty amountOfDoneMissions;
    SimpleIntegerProperty amountOfAskedMissionsProperty;
    public AgentMissionRunnable(SimpleIntegerProperty amountOfMissionsInTheQueue,SimpleIntegerProperty amountOfAskedMissionsProperty,String lastStartingPos,int missionNumber,UiAdapterInterface uiAdapterInterface,MachineEngine machineEngineCopy,
                                String stringToConvert, String alliesTeamName
            , String initialStartingPosition, int sizeOfMission
    ,SimpleIntegerProperty amountOfDoneMissions) {

        this.machineEngineCopy = machineEngineCopy;
        this.stringToConvert = stringToConvert;
        this.initialStartingPosition = initialStartingPosition;
        this.alliesTeamName = alliesTeamName;
        this.sizeOfMission=sizeOfMission;
        this.resultsList = new ArrayList<>();
        this.missionNumber=missionNumber;
        this.lastStartingPos=lastStartingPos;
        //this.amountOfDecipheringStringsProperty=new SimpleIntegerProperty(0);
        this.uiAdapterInterface=uiAdapterInterface;
       this.amountOfDoneMissions=amountOfDoneMissions;
       this.amountOfAskedMissionsProperty=amountOfAskedMissionsProperty;
       this.amountOfMissionsInTheQueue=amountOfMissionsInTheQueue;
    }

    public void setResultsList(List<BruteForceResultDTO> resultsList) {
        this.resultsList = resultsList;
    }

    public List<BruteForceResultDTO> getResultsList() {
        return resultsList;
    }

    public synchronized void getConvertedStringsFounded() throws InterruptedException {
        int index = 0;
        Thread.currentThread().setName("AgentMissionRunnable "+missionNumber);
        while (index<sizeOfMission){
            machineEngineCopy.chooseManuallyStartingPosition(initialStartingPosition);
            machineEngineCopy.createCurrentCodeDescriptionDTO();
            String convertedStringCode = machineEngineCopy.getCurrentCodeDescription();
            ConvertedStringDTO convertedStringDTOTemp = machineEngineCopy.getConvertedString(stringToConvert);
            if (machineEngineCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(missionNumber,convertedStringDTOTemp.getConvertedString(), alliesTeamName, convertedStringCode);
                bruteForceResultDTO.setTheMissionNumber(missionNumber);
                resultsList.add(bruteForceResultDTO);
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

            if(resultsList.size()>0) {
                uiAdapterInterface.saveResultsOnServer(resultsList);

            }
            amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
            uiAdapterInterface.updateAmountDoneMissionsPerAgent(amountOfDoneMissions.getValue());
            amountOfMissionsInTheQueue.setValue(amountOfAskedMissionsProperty.getValue()-amountOfDoneMissions.getValue());
            uiAdapterInterface.updateAmountMissionsInTheQueuePerAgent(amountOfMissionsInTheQueue.getValue());


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