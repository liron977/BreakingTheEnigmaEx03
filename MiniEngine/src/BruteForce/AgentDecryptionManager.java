package BruteForce;

import MachineEngine.MachineEngine;
import bruteForce.BruteForceResultDTO;
import bruteForce.TheMissionInfoDTO;
import engine.theEnigmaEngine.TheMachineEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AgentDecryptionManager {
    private MachineEngine machineEngine;
    private int amountOfSubListsToCreate;
    private String stringToConvert;
    private String alliesTeamName;
    private List<TheMissionInfoDTO> theMissionInfoDTOList;
    private  BlockingQueue<Runnable> missionsInfoBlockingQueue;
    private  BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
    ThreadPoolExecutor threadPoolExecutor;
    SimpleBooleanProperty isMissionEndedProperty;
    private boolean isMissionsFinished = false;
    UiAdapterInterface uiAdapterInterface;
    SimpleIntegerProperty amountOfDoneMissions;

    SimpleIntegerProperty amountOfMissionsInTheQueue;
    SimpleIntegerProperty amountOfAskedMissionsProperty;
    private SimpleBooleanProperty  isContestEnded;
    private String agentName;

public AgentDecryptionManager(SimpleIntegerProperty amountOfMissionsInTheQueue,SimpleIntegerProperty amountOfAskedMissionsProperty,SimpleIntegerProperty amountOfDoneMissions,UiAdapterInterface uiAdapterInterface,
                              SimpleBooleanProperty isMissionEndedProperty,ThreadPoolExecutor threadPoolExecutor, TheMachineEngine theMachineEngine
        , String alliesTeamName, List<TheMissionInfoDTO> theMissionInfoDTOList,
                              BlockingQueue<Runnable> missionsInfoBlockingQueue
               , SimpleBooleanProperty  isContestEnded,String agentName){
   this.machineEngine=new MachineEngine(theMachineEngine);
   this.theMissionInfoDTOList=theMissionInfoDTOList;
   this.alliesTeamName=alliesTeamName;
   this.missionsInfoBlockingQueue=missionsInfoBlockingQueue;
   this.threadPoolExecutor=threadPoolExecutor;
   this.isMissionEndedProperty=isMissionEndedProperty;
   this.uiAdapterInterface=uiAdapterInterface;
   this.amountOfDoneMissions=amountOfDoneMissions;
   this.amountOfAskedMissionsProperty=amountOfAskedMissionsProperty;
   this.amountOfMissionsInTheQueue=amountOfMissionsInTheQueue;
   if(amountOfMissionsInTheQueue.getValue()<0){
       int r=0;
   }
    this.isContestEnded=isContestEnded;
    amountOfMissionsInTheQueue.setValue(0);
    this.agentName=agentName;
}
    public void createMission() throws Exception {
    int missionsCounter=0;
    String lastStartingPos=machineEngine.getLastStartingPos();
        threadPoolExecutor.prestartAllCoreThreads();
        for (TheMissionInfoDTO theMissionInfoDTO : theMissionInfoDTOList) {
            if (isContestEnded.getValue()) {
                break;
            }
                machineEngine.getTheMachineEngine().updateUsedRotors(theMissionInfoDTO.getUsedRotors());
                if (theMissionInfoDTO.getReflector().equals("II")) {
                    int x = 0;
                }
                machineEngine.chooseManuallyReflect(theMissionInfoDTO.getReflector());
                MachineEngine machineEngineCopy = machineEngine.cloneMachineEngine();
                AgentMissionRunnable agentMissionRunnable = new AgentMissionRunnable(amountOfMissionsInTheQueue, amountOfAskedMissionsProperty, lastStartingPos, missionsCounter, uiAdapterInterface, machineEngineCopy,
                        theMissionInfoDTO.getStringToConvert(), alliesTeamName
                        , theMissionInfoDTO.getInitialStartingPosition(),
                        theMissionInfoDTO.getSizeOfMission(), amountOfDoneMissions
                ,isContestEnded,agentName);
                missionsCounter++;
                missionsInfoBlockingQueue.put(agentMissionRunnable);
            }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
    }



    /*public void createLowLevelMission() throws Exception {
        int missionsCounter = 0;
        for (TheMissionInfoDTO theMissionInfoDTO : theMissionInfoDTOList) {
            missionsCounter++;
            MachineEngine machineEngineCopy = machineEngine.cloneMachineEngine();
            AgentMissionRunnable agentMissionRunnable = new AgentMissionRunnable(machineEngineCopy,
                    theMissionInfoDTO.getStringToConvert(), alliesTeamName
                    , theMissionInfoDTO.getInitialStartingPosition(),
                    theMissionInfoDTO.getSizeOfMission());
            missionsInfoBlockingQueue.put(agentMissionRunnable);
        }
    }

    public void createMediumLevelMission() throws Exception {
        for (String reflectorId : machineEngine.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
            machineEngine.chooseManuallyReflect(reflectorId);
            createLowLevelMission();
        }
    }

    public void createHighLevelMission() throws Exception {
        String[] concatRotorsPosition = machineEngine.getTheMachineEngine().getUsedRotorsId();
        List<String[]> optionalRotorsPositionList = new ArrayList<>();
        getAllPermutationsOfRotorsPosition(concatRotorsPosition.length, concatRotorsPosition, optionalRotorsPositionList);
        for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
            machineEngine.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
            createMediumLevelMission();
        }
    }


    public void createImpossibleLevelMission() throws Exception {
        int missionsCounter = 0;
        List<String[]> optionalRotorsList = new ArrayList<>();
        optionalRotorsList = getOptionalRotors();
        for (String[] optionalRotors : optionalRotorsList) {
            List<String[]> optionalRotorsPositionList = new ArrayList<>();
            getAllPermutationsOfRotorsPosition(optionalRotors.length, optionalRotors, optionalRotorsPositionList);
            for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
                machineEngine.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                createMediumLevelMission();
            }
        }
    }*/

    public static void getAllPermutationsOfRotorsPosition(int length, String[] rotorsId, List<String[]> optionalRotorsPosition) {
        String[] tmpArray;
        if (length == 1) {
            tmpArray = Arrays.copyOf(rotorsId, rotorsId.length);
            optionalRotorsPosition.add(tmpArray);
        } else {
            for (int i = 0; i < length - 1; i++) {
                getAllPermutationsOfRotorsPosition(length - 1, rotorsId, optionalRotorsPosition);
                if (length % 2 == 0) {
                    swap(rotorsId, i, length - 1);
                } else {
                    swap(rotorsId, 0, length - 1);
                }
            }
            getAllPermutationsOfRotorsPosition(length - 1, rotorsId, optionalRotorsPosition);
        }
    }

    private static void swap(String[] input, int a, int b) {
        String tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
    public List<String[]> getOptionalRotors() {
        List<String[]> listOfOptionalRotors = new ArrayList<>();
        List<int[]> listOfOptionalRotorsByInt = generate(machineEngine.getTheMachineEngine().getMaxAmountOfRotors(), machineEngine.getAmountOfUsedRotors());
        for (int i = 0; i < listOfOptionalRotorsByInt.size(); i++) {
            {
                String[] rotors = new String[listOfOptionalRotorsByInt.get(i).length];
                for (int j = 0; j < listOfOptionalRotorsByInt.get(i).length; j++) {

                    rotors[j] = String.valueOf(listOfOptionalRotorsByInt.get(i)[j]);

                }
                listOfOptionalRotors.add(i, rotors);
            }
        }
        return listOfOptionalRotors;
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 1, n, 0);
        return combinations;
    }

    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }



}