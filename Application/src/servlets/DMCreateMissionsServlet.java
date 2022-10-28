package servlets;

import bruteForce.TheMissionInfoDTO;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.TheMachineEngine;
import engine.theEnigmaEngine.UBoatBattleField;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.MiniEngineManager;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DMCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //  Thread.currentThread().setName("DMCreateMissionsServlet");
      //  System.out.println("in DMCreateMissionsServlet");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
       // System.out.println(theAlliesTeamName+"DMCreateMissionsServlet ");
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
        String stringToConvert = engineManager.getConvertedString();
        MiniEngineManager miniEngineManager;
        synchronized (getServletContext()) {
            TheMachineEngine theMachineEngineCopy = engineManager.getTheMachineEngine().cloneTheMachineEngine();
             miniEngineManager = new MiniEngineManager();
            miniEngineManager.setTheMachineEngine(theMachineEngineCopy);
        }
        Allies alies = alliesManager.getAlliesByAlliesTeamName(theAlliesTeamName);
         if(miniEngineManager!=null) {
            try {
                createMission(miniEngineManager,engineManager, theAlliesTeamName, stringToConvert);
            } catch (Exception e) {
                //System.out.println("Exeption");
                throw new RuntimeException(e);
            }
        }
    }

    public void createMission(MiniEngineManager miniEngineManager,EngineManager engineManager,String theAlliesTeamName,String stringToConvert) throws Exception {
        UBoatBattleField battleField = engineManager.getBattleField();
        String level = battleField.getLevel();
        int sizeOfMission = battleField.getAlliesSizeOfMission(theAlliesTeamName);
     // engineManager.maxAmountOfMissionscalculation(level, sizeOfMission);
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        alliesManager.clearAmountOfCreatedMissions(theAlliesTeamName);
        Long amountOfSubListsToCreate = calculateAmountOfMissionsToCreate(miniEngineManager, sizeOfMission);
        updateTotalAmountOfMissions(engineManager,sizeOfMission,theAlliesTeamName);
           if (level.equals("Easy")) {
            createLowLevelMission(miniEngineManager,0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else if (level.equals("Medium")) {
            createMediumLevelMission(miniEngineManager,0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else if (level.equals("Hard")) {
            createHighLevelMission(miniEngineManager,0,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } else {
            createImpossibleLevelMission(miniEngineManager,engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        }
    }

    public void createLowLevelMission(MiniEngineManager miniEngineManager,Integer missionsCounter,EngineManager engineManager,
                                      Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
       // BlockingQueue<bruteForce.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue = new LinkedBlockingQueue<bruteForce.TheMissionInfo>(1000);
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
       // BlockingQueue<bruteForce.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue=alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName);
       // int missionsCounter = 0;
        boolean isAddedToBlockingQueue=false;
       String initialStartingPosition= miniEngineManager.getInitialStartingPosition();
        int missionIndex = 0;

        /*System.out.println(amountOfSubListsToCreate+" amountOfSubListsToCreate");
        System.out.println(Thread.currentThread().getId()+" Thread.currentThread().getId()");
        System.out.println(Thread.currentThread().getName()+" Thread.currentThread().getName()");*/
        //System.out.println(alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()+"alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()");
        for (int i = 0; i < amountOfSubListsToCreate&&!engineManager.getIsContestEnded(); i++) {
            missionsCounter++;
            missionIndex = i;
            TheMissionInfoDTO theMissionInfo =new TheMissionInfoDTO(initialStartingPosition, sizeOfMission, /*engineManagerCopy,*/stringToConvert,miniEngineManager.getMachineUsedRotorsIdArray(),miniEngineManager.getMachineReflectorId());
            //System.out.println(initialStartingPosition+" initialStartingPosition");
            isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
            /*System.out.println("***********************************");
            System.out.println("missions in blocking queue: "+alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size());
            System.out.println("***********************************");
         */   while (!isAddedToBlockingQueue&&!engineManager.getIsContestEnded()) {
                if(alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()<1000) {
                    isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
                }
            }
            if(engineManager.getIsContestEnded()){
                break;
            }
            if(theAlliesTeamName!=null&&alliesManager!=null) {
                alliesManager.increaseAmountOfCreatedMission(theAlliesTeamName);

            }
      /*      System.out.println(missionsCounter.intValue());*/
            initialStartingPosition = miniEngineManager.getNextStartingPositionByString(sizeOfMission);
            System.out.println("initialStartingPosition "+initialStartingPosition);
        }
        //System.out.println(missionsCounter+"missionsCounter");
    }

    public void createMediumLevelMission(MiniEngineManager miniEngineManager,Integer missionsCounter,EngineManager engineManager,
                                         Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        int countr=0;
        for (String reflectorId : miniEngineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
            countr++;
            if(engineManager.getIsContestEnded()){
                break;
            }
            miniEngineManager.chooseManuallyReflect(reflectorId);
            createLowLevelMission(miniEngineManager,missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
        }
        System.out.println(countr+"countr");

    }

    public void createHighLevelMission(MiniEngineManager miniEngineManager,Integer missionsCounter,EngineManager engineManager,
                                       Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        String[] concatRotorsPosition = miniEngineManager.getTheMachineEngine().getUsedRotorsId();
        List<String[]> optionalRotorsPositionList = new ArrayList<>();
        int counter=0;
        getAllPermutationsOfRotorsPosition(concatRotorsPosition.length, concatRotorsPosition, optionalRotorsPositionList);
        for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
            if(engineManager.getIsContestEnded()){
                break;
            }
            counter++;
            miniEngineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
            createMediumLevelMission(miniEngineManager,missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
        }
    }

    public void createImpossibleLevelMission(MiniEngineManager miniEngineManager,EngineManager engineManager,
                                             Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
       // threadPoolExecutor.prestartAllCoreThreads();
        boolean isAddedToBlockingQueue=false;
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());

        int missionsCounter = 0;
        List<String[]> optionalRotorsList = new ArrayList<>();
        optionalRotorsList = getOptionalRotors(miniEngineManager);
        for (String[] optionalRotors : optionalRotorsList) {
            List<String[]> optionalRotorsPositionList = new ArrayList<>();
            getAllPermutationsOfRotorsPosition(optionalRotors.length, optionalRotors, optionalRotorsPositionList);
            for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
                miniEngineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                for (String reflectorId : miniEngineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
                    miniEngineManager.chooseManuallyReflect(reflectorId);
                    String initialStartingPosition= miniEngineManager.getInitialStartingPosition();
                    int missionIndex = 0;
                  //  System.out.println(amountOfSubListsToCreate+"amountOfSubListsToCreate");
                    for (int i = 0; i < amountOfSubListsToCreate&&!engineManager.getIsContestEnded(); i++) {
                       // missionsCounter++;
                        missionIndex = i;
                        TheMissionInfoDTO theMissionInfo =new TheMissionInfoDTO(initialStartingPosition, sizeOfMission, /*engineManagerCopy,*/stringToConvert,miniEngineManager.getMachineUsedRotorsIdArray(),miniEngineManager.getMachineReflectorId());

                        isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
                        if(isAddedToBlockingQueue){
                            missionsCounter++;
                           // System.out.println("missionsCounter"+missionsCounter);

                        }
                           while (!isAddedToBlockingQueue&&!engineManager.getIsContestEnded()) {
                            if(alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()<1000) {
                                isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
                                if(isAddedToBlockingQueue){
                                    missionsCounter++;
                                    //System.out.println("missionsCounter"+missionsCounter);
                                }
                            }
                        }
                        if(engineManager.getIsContestEnded()){
                            break;
                        }
                       // System.out.println("**************************************");
                        if(theAlliesTeamName!=null&&alliesManager!=null) {
                            alliesManager.increaseAmountOfCreatedMission(theAlliesTeamName);
                            //missionsCounter=missionsCounter.intValue()+1;
                        }
                        /*      System.out.println(missionsCounter.intValue());*/
                        initialStartingPosition = miniEngineManager.getNextStartingPositionByString(sizeOfMission);
                    }
                    }
                }
            }
        }




   /* public void createImpossibleLevelMission(EngineManager engineManager,
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
*/
    public Long calculateAmountOfMissionsToCreate(MiniEngineManager miniEngineManager, int sizeOfMission) {
        Long amountOfSubListsToCreate = (miniEngineManager.getAmountOfPossibleStartingPositionList()) / sizeOfMission;
        if (((miniEngineManager.getAmountOfPossibleStartingPositionList()) % sizeOfMission) != 0) {
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
    public List<String[]> getOptionalRotors(MiniEngineManager miniEngineManager) {
        List<String[]> listOfOptionalRotors = new ArrayList<>();
        List<int[]> listOfOptionalRotorsByInt = generate(miniEngineManager.getTheMachineEngine().getMaxAmountOfRotors(), miniEngineManager.getAmountOfUsedRotors());
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