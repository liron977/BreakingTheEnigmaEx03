package servlets;

import bruteForceLogic.TheMissionInfo;
import com.google.gson.Gson;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DMCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        String stringToConvert = request.getParameter(ParametersConstants.STRING_TO_CONVERT_BRUTE_FORCE);
        Allies alies = alliesManager.getAlliesByAlliesTeamName(theAlliesTeamName);
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
        try {
            createMission(engineManager,theAlliesTeamName,stringToConvert);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
       // BlockingQueue<TheMissionInfo> theMissionInfoBlockingQueuelockingQueue = new LinkedBlockingQueue<TheMissionInfo>(1000);
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
       // BlockingQueue<TheMissionInfo> theMissionInfoBlockingQueuelockingQueue=alliesMissionsManager.getMissionsBlockingQueueByAlliesTeamName(theAlliesTeamName);
        int missionsCounter = 0;
        engineManager.getInitialStartingPosition();
        int missionIndex = 0;
        for (int i = 0; i < amountOfSubListsToCreate; i++) {
            missionsCounter++;
            missionIndex = i;
            EngineManager engineManagerCopy = engineManager.cloneEngineManager();
            String initialStartingPosition = "";//todo
            String finalPosition = "";//todo
            TheMissionInfo theMissionInfo = new TheMissionInfo(initialStartingPosition, sizeOfMission, finalPosition, engineManagerCopy,stringToConvert);
            updateEngineManager(engineManagerCopy, engineManager);
            alliesMissionsManager.addMissionInfoIntoMissionBlockingQueue(theAlliesTeamName,theMissionInfo);
        }
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
    }
}

