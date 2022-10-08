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
        Thread.currentThread().setName("DMCreateMissionsServlet");
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        String stringToConvert = request.getParameter(ParametersConstants.STRING_TO_CONVERT_BRUTE_FORCE);
        Allies alies = alliesManager.getAlliesByAlliesTeamName(theAlliesTeamName);
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
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
        int sizeOfMission = battleField.getAlliesSizeOfMission();
      engineManager.setMaxAmountOfMissions(level, sizeOfMission);

        Long amountOfSubListsToCreate = calculateAmountOfMissionsToCreate(engineManager, sizeOfMission);
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
       // BlockingQueue<bruteForce.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue=alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName);
       // int missionsCounter = 0;
        boolean isAddedToBlockingQueue=false;
       String initialStartingPosition= engineManager.getInitialStartingPosition();
        int missionIndex = 0;

        for (int i = 0; i < amountOfSubListsToCreate; i++) {
            missionsCounter++;
            missionIndex = i;
            TheMissionInfoDTO theMissionInfo =new TheMissionInfoDTO(initialStartingPosition, sizeOfMission, /*engineManagerCopy,*/stringToConvert,engineManager.getMachineUsedRotorsIdArray(),engineManager.getMachineReflectorId());

            isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
            while (!isAddedToBlockingQueue) {
                if(alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size()<1000) {
                    isAddedToBlockingQueue = alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName, theMissionInfo);
                }
            }

            missionsCounter=missionsCounter.intValue()+1;
            System.out.println(missionsCounter.intValue());
            initialStartingPosition = engineManager.getNextStartingPositionByString(sizeOfMission);
        }


        int x=0;
    }

    public void createMediumLevelMission(Integer missionsCounter,EngineManager engineManager,
                                         Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        for (String reflectorId : engineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
            engineManager.chooseManuallyReflect(reflectorId);
            createLowLevelMission(missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
        }

    }

    public void createHighLevelMission(Integer missionsCounter,EngineManager engineManager,
                                       Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
        String[] concatRotorsPosition = engineManager.getTheMachineEngine().getUsedRotorsId();
        List<String[]> optionalRotorsPositionList = new ArrayList<>();
        getAllPermutationsOfRotorsPosition(concatRotorsPosition.length, concatRotorsPosition, optionalRotorsPositionList);
        for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
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
            List<String[]> optionalRotorsPositionList = new ArrayList<>();
            getAllPermutationsOfRotorsPosition(optionalRotors.length, optionalRotors, optionalRotorsPositionList);
            for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
                engineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                createMediumLevelMission(missionsCounter,engineManager,amountOfSubListsToCreate,sizeOfMission,theAlliesTeamName,stringToConvert);
            }
            missionsCounterInImpossible=missionsCounterInImpossible++;
            System.out.println("missionsCounterInImpossible"+missionsCounterInImpossible.intValue());
        }
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());

        System.out.println("missions in bq:"+alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName).size());


    }

    public Long calculateAmountOfMissionsToCreate(EngineManager engineManager, int sizeOfMission) {
        Long amountOfSubListsToCreate = (engineManager.getAmountOfPossibleStartingPositionList()) / sizeOfMission;
        if (((engineManager.getAmountOfPossibleStartingPositionList()) % sizeOfMission) != 0) {
            amountOfSubListsToCreate++;
        }
        return amountOfSubListsToCreate;
    }
    private void updateEngineManager(EngineManager engineManagerCopy, EngineManager engineManager) {
        engineManager.setStartingPositionValues(engineManagerCopy.getTheLastStartingPos(), engineManagerCopy.getLastIndex(), engineManagerCopy.getFirstList(), engineManagerCopy.getCountPossibleStartingPosition());
/*    }

    public List<String> getFinalPositionByMissionSize(int sizeOfMission) {
        StringBuilder stringToAdd = new StringBuilder();
        String initialStartingPosition = theLastStartingPos;
        String keyboard = getTheMachineEngine().getKeyboard();
        int nextSignalIndex = 0;
        String lastSignal = "";
        int amountOfStartingPos = 0;
        boolean happened = true;


        stringToAdd.append(initialStartingPosition, 0, initialStartingPosition.length());
        for (int i = 0; i < initialStartingPosition.length(); i++) {
            lastSignal = lastSignal + String.valueOf(keyboard.charAt(keyboard.length() - 1));
        }
        while (amountOfStartingPos < sizeOfMission && !possibleStartingPositionList.contains(lastSignal)) {

            if (sizeOfMission > keyboard.length()) {
                for (int j = 0; j < keyboard.length() & amountOfStartingPos < (sizeOfMission) && lastIndex < keyboard.length(); j++) {
                    stringToAdd.setCharAt(initialStartingPosition.length() - 1, keyboard.charAt(lastIndex));
                    lastIndex++;
                    if (!stringToAdd.toString().equals(theLastStartingPos) || firstList) {
                        firstList = false;
                        possibleStartingPositionList.add(stringToAdd.toString());
                        amountOfStartingPos++;
                    } else {
                    }


                }

            } else {

                for (int j = 0; (j < (sizeOfMission) && (lastIndex < keyboard.length()) && (amountOfStartingPos < sizeOfMission)); j++) {
                    stringToAdd.setCharAt(initialStartingPosition.length() - 1, keyboard.charAt(lastIndex));
                    lastIndex++;
                    if (!stringToAdd.toString().equals(theLastStartingPos) || firstList) {
                        firstList = false;
                        possibleStartingPositionList.add(stringToAdd.toString());
                        amountOfStartingPos++;
                    } else {
                        happened = false;
                        if (!happened && !firstList) {
                            sizeOfMission++;
                            happened = true;
                        }
                    }
                }
                if (!firstList) {
                    sizeOfMission--;
                }
            }

            for (int i = stringToAdd.length() - 1; amountOfStartingPos <= sizeOfMission && i > 0; i--) {
                lastIndex = 0;
                if (stringToAdd.charAt(i) != keyboard.charAt(keyboard.length() - 1) || amountOfStartingPos >= sizeOfMission) {
                    break;
                } else {
                    if (stringToAdd.charAt(i - 1) != keyboard.charAt(keyboard.length() - 1)) {
                        char signalToReplace = stringToAdd.charAt(i - 1);
                        nextSignalIndex = keyboard.indexOf(signalToReplace) + 1;
                        for (int k = i; k <= stringToAdd.length() - 1; k++) {
                            stringToAdd.setCharAt(k, keyboard.charAt(0));
                        }
                        stringToAdd.setCharAt(i - 1, keyboard.charAt(nextSignalIndex));
                        i = 0;
                    }
                }
            }
        }
        this.amountOfPossibleStartingPositionList = Long.valueOf(possibleStartingPositionList.size());
        if(possibleStartingPositionList.contains("'''")){
            int x=0;
        }
        theLastStartingPos = possibleStartingPositionList.get(possibleStartingPositionList.size() - 1);
        lastIndex = keyboard.indexOf(theLastStartingPos.charAt(theLastStartingPos.length() - 1));
        //return possibleStartingPositionList;
         *//*  for (String s : possibleStartingPositionList) {
               System.out.println(s);
           }
         *//*  countPossibleStartingPosition=countPossibleStartingPosition+possibleStartingPositionList.size();


         *//* System.out.println("*************");*//*

        return possibleStartingPositionList;
    }*/
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