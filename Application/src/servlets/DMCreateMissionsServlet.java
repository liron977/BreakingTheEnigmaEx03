package servlets;


import bruteForceLogic.TheMissionInfo;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.UBoatBattleField;
import engineManager.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class DMCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
  /*      amountOfMissionsProceeded.setValue(0);
        maxAmountOfMissions = 0L;
        start = System.currentTimeMillis();*/
        UBoatBattleField battleField = engineManager.getBattleField();
        String level = battleField.getLevel();
        int sizeOfMission = battleField.getAlliesSizeOfMission();
        Long maxAmountOfMissions = engineManager.setMaxAmountOfMissions(level, sizeOfMission);
        Long amountOfSubListsToCreate = calculateAmountOfMissionsToCreate(engineManager, sizeOfMission);
        //amountOfMissionsCounter.setValue(maxAmountOfMissions);
        //uiAdapterInterface.updateAmountOfMissionsPerLevel(engineManager.displayMaxAmountOfMissionsWithCommas(maxAmountOfMissions));
        if (level.equals("Easy")) {
            createLowLevelMission(engineManager, amountOfSubListsToCreate, sizeOfMission,theAlliesTeamName,stringToConvert);
        } /*else if (level.equals("Medium")) {
            createMediumLevelMission();
        } else if (level.equals("Hard")) {
            createHighLevelMission();
        } else {
            createImpossibleLevelMission();
        }*/
    }

    public void createLowLevelMission(EngineManager engineManager,
                                      Long amountOfSubListsToCreate,int sizeOfMission
            ,String theAlliesTeamName,String stringToConvert) throws Exception {
       // BlockingQueue<bruteForceLogic.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue = new LinkedBlockingQueue<bruteForceLogic.TheMissionInfo>(1000);
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
       // BlockingQueue<bruteForceLogic.TheMissionInfo> theMissionInfoBlockingQueuelockingQueue=alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName);
        int missionsCounter = 0;
       String initialStartingPosition= engineManager.getInitialStartingPosition();
        int missionIndex = 0;
        for (int i = 0; i < amountOfSubListsToCreate; i++) {
            missionsCounter++;
            missionIndex = i;
            EngineManager engineManagerCopy = engineManager.cloneEngineManager();
            TheMissionInfo theMissionInfo =new TheMissionInfo(initialStartingPosition, sizeOfMission, engineManagerCopy,stringToConvert);
            updateEngineManager(engineManagerCopy, engineManager);
            alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName,theMissionInfo);
            initialStartingPosition = engineManager.getNextStartingPositionByString(sizeOfMission);
        }
        int x=0;
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

}