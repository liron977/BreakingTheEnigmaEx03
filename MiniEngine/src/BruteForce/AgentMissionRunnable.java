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
   // SimpleIntegerProperty amountOfDecipheringStringsProperty;
   SimpleIntegerProperty amountOfDoneMissions;
    public AgentMissionRunnable(String lastStartingPos,int missionNumber,UiAdapterInterface uiAdapterInterface,MachineEngine machineEngineCopy,
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
               /* System.out.println(bruteForceResultDTO.getConvertedString());
                System.out.println(bruteForceResultDTO.getCodeDescription());*/
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
       // amountOfDoneMissions.setValue(amountOfDoneMissions.getValue()+1);
        synchronized (this){
            if(resultsList.size()>0) {
                uiAdapterInterface.saveResultsOnServer(resultsList);

              /*  for (BruteForceResultDTO brute:resultsBlockingQueue) {
                    System.out.println("in runnable"+Thread.currentThread().getName());
                    System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber());
                }*/

                //uiAdapterInterface.updateResultsOnAgent(resultsBlockingQueue);
            }
        }
       // uiAdapterInterface.updateAmountDoneMissionsPerAgent(amountOfDoneMissions.getValue());
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