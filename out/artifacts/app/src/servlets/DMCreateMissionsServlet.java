package servlets;

import bruteForce.TheMissionInfoDTO;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.UBoatBattleField;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DMCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //  Thread.currentThread().setName("DMCreateMissionsServlet");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        System.out.println(theAlliesTeamName+"DMCreateMissionsServlet ");
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
        String stringToConvert = engineManager.getConvertedString();
        Allies alies = alliesManager.getAlliesByAlliesTeamName(theAlliesTeamName);
         if(engineManager!=null) {
            try {
                createMission(engineManager, theAlliesTeamName, stringToConvert);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createMission(EngineManager engineManager,String theAlliesTeamName,String stringToConvert) throws Exception {
        UBoatBattleField battleField = engineManager.getBattleField();
        String level = battleField.getLevel();
        int sizeOfMission = battleField.getAlliesSizeOfMission(theAlliesTeamName);
     // engineManager.maxAmountOfMissionscalculation(level, sizeOfMission);

        Long amountOfSubListsToCreate = calculateAmountOfMissionsToCreate(engineManager, sizeOfMission);
        updateTotalAmountOfMissions(engineManager,sizeOfMission,theAlliesTeamName);
           if (level.equals("Easy")) {
            createLowLevelMission(0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else if (level.equals("Medium")) {
            createMediumLevelMission(0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else if (level.equals("Hard")) {
            createHighLevelMission(0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else {
            createImpossibleLevelMission(engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        }
    }

    public void createLowLevelMission(Integer missionsCounter,EngineManager engineManager,
                                      Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
       // BlockingQueue<bruteForce.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue = new LinkedBlockingQueue<bruteForce.TheMissionInfo>(1000);
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
       // BlockingQueue<bruteForce.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue=alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName);
       // int missionsCounter = 0;
        boolean isAddedToBlockingQueue=false;
       String initialStartingPosition= engineManager.getInitialStartingPosition();
        int missionIndex = 0;
        System.out.println(amountOfSubListsToCreate+"amountOfSubListsToCreate");
        for (int i = 0; i < amountOfSubListsToCreate&&!engineManager.getIsContestEnded(); i++) {
            missionsCounter++;
            missionIndex = i;
            TheMissionInfoDTO theMissionInfo =new TheMissionInfoDTO(initialStartingPosition, sizeOfMission, /*engineManagerCopy,*/stringToConvert,engineManager.getMachineUsedRotorsIdArray(),engineManager.getMachineReflectorId());

            isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
            while (!isAddedToBlockingQueue) {
                if(alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()<=1000) {
                    isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
                }
            }
            if(theAlliesTeamName!=null&&alliesManager!=null) {
                alliesManager.increaseAmountOfCreatedMission(theAlliesTeamName);
                //missionsCounter=missionsCounter.intValue()+1;
            }
      /*      System.out.println(missionsCounter.intValue());*/
            initialStartingPosition = engineManager.getNextStartingPositionByString(sizeOfMission);
        }
        System.out.println(missionsCounter+"missionsCounter");
    }

    public void createMediumLevelMission(Integer missionsCounter,EngineManager engineManager,
                                         Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        int countr=0;
        for (String reflectorId : engineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
            countr++;
            if(engineManager.getIsContestEnded()){
                break;
            }
            engineManager.chooseManuallyReflect(reflectorId);
            createLowLevelMission(missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
        }
        System.out.println(countr+"countr");

    }

    public void createHighLevelMission(Integer missionsCounter,EngineManager engineManager,
                                       Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        String[] concatRotorsPosition = engineManager.getTheMachineEngine().getUsedRotorsId();
        List<String[]> optionalRotorsPositionList = new ArrayList<>();
        getAllPermutationsOfRotorsPosition(concatRotorsPosition.length, concatRotorsPosition, optionalRotorsPositionList);
        for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
            if(engineManager.getIsContestEnded()){
                break;
            }
            engineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
            createMediumLevelMission(missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
        }
    }


    public void createImpossibleLevelMission(EngineManager engineManager,
                                             Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        Integer missionsCounter = 0;
        Integer missionsCounterInImpossible = 0;
        List<String[]> optionalRotorsList = new ArrayList<>();
        optionalRotorsList = getOptionalRotors(engineManager);
        for (String[] optionalRotors : optionalRotorsList) {
            if(engineManager.getIsContestEnded()){
                break;
            }
            List<String[]> optionalRotorsPositionList = new ArrayList<>();
            getAllPermutationsOfRotorsPosition(optionalRotors.length, optionalRotors, optionalRotorsPositionList);
            for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
                if(engineManager.getIsContestEnded()){
                    break;
                }
                engineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                createMediumLevelMission(missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
            }
            missionsCounterInImpossible=missionsCounterInImpossible++;
           //System.out.println("missionsCounterInImpossible"+missionsCounterInImpossible.intValue());
        }
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());

      //  System.out.println("missions in bq:"+alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size());


    }

    public Long calculateAmountOfMissionsToCreate(EngineManager engineManager, int sizeOfMission) {
        Long amountOfSubListsToCreate = (engineManager.getAmountOfPossibleStartingPositionList()) / sizeOfMission;
        if (((engineManager.getAmountOfPossibleStartingPositionList()) % sizeOfMission) != 0) {
            amountOfSubListsToCreate++;
        }
        return amountOfSubListsToCreate;
    }
    public void updateTotalAmountOfMissions(EngineManager engineManager, int sizeOfMission,String theAlliesTeamName){
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        long maxAmountOfMissions =(engineManager.maxAmountOfMissionscalculation(engineManager.getLevel(), sizeOfMission));
        System.out.println(maxAmountOfMissions+"maxAmountOfMissions");
        System.out.println(engineManager.getLevel()+"engineManager.getLevel()");
        System.out.println(sizeOfMission+"sizeOfMission");
        alliesManager.setTotalAmountOfCreadedMission(theAlliesTeamName,maxAmountOfMissions);

    }
    private void updateEngineManager(EngineManager engineManagerCopy, EngineManager engineManager) {
        engineManager.setStartingPositionValues(engineManagerCopy.getTheLastStartingPos(), engineManagerCopy.getLastIndex(), engineManagerCopy.getFirstList(), engineManagerCopy.getCountPossibleStartingPosition());
    }
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
    public List<String[]> getOptionalRotors(EngineManager engineManager) {
        List<String[]> listOfOptionalRotors = new ArrayList<>();
        List<int[]> listOfOptionalRotorsByInt = generate(engineManager.getTheMachineEngine().getMaxAmountOfRotors(), engineManager.getAmountOfUsedRotors());
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