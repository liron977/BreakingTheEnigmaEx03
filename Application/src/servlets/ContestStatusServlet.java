package servlets;

import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.agent.AgentsManager;
import managers.agent.StatusManager;
import managers.bruteForce.AlliesBruteForceResultsMapManager;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class ContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String battleName;
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            if(request.getParameter(ParametersConstants.ROLE).equals("UBoat")){
                battleName=request.getParameter(ParametersConstants.BATTLE_FIELD);
            }
            else {
                String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
                 battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
            }
            if (battleName != null&&!battleName.isEmpty()) {
                EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
                ContestStatusInfoDTO contestStatusInfoDTO = new ContestStatusInfoDTO(
                        engineManager.getIsConvertedStringSet(),
                        engineManager.getContestStatus(),
                        engineManager.getIsContestEnded(),
                        engineManager.getAlliesWinnwerTeamName(),
                        engineManager.getIsAlliesConfirmedGameOver());
                Gson gson = new Gson();
                String json = gson.toJson(contestStatusInfoDTO);
                out.println(json);
                out.flush();
            }
            else{
                String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
                StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
                ContestStatusInfoDTO contestStatusInfoDTO=statusManager.getContestStatusInfoDTOByAlliesName(theAlliesTeamName);
                Gson gson = new Gson();
                String json = gson.toJson(contestStatusInfoDTO);
                out.println(json);
                out.flush();

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
       AlliesBruteForceResultsMapManager alliesGetBruteForceResultServlet=ServletUtils.getAlliesBruteForceResultsMapManager(getServletContext());
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
       // String battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
        StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
        ContestStatusInfoDTO contestStatusInfoDTO=statusManager.getContestStatusInfoDTOByBattlefield(theBattleFieldName);
        if(contestStatusInfoDTO==null){
            contestStatusInfoDTO=new ContestStatusInfoDTO(false,"Wait",true,"",true);
        }
            try {
                contestStatusInfoDTO.addAllies(theAlliesTeamName);
                statusManager.addContestStatusInfoDTO(theAlliesTeamName,contestStatusInfoDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        contestStatusInfoDTO.setAlliesConfirmedGameOver(true);
        if (theBattleFieldName != null&&!theBattleFieldName.isEmpty()) {
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(theBattleFieldName);
            engineManager.setAlliesConfirmedGameOver(true);
            theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            AgentsManager agentsManager=ServletUtils.getAgentManager(getServletContext());
            agentsManager.initValues(theAlliesTeamName);
            alliesManager.clearAlliesValues(theAlliesTeamName);
            contestStatusInfoDTO.setContestStatus("Wait");
            contestStatusInfoDTO.setUboatSettingsCompleted(false);
            alliesGetBruteForceResultServlet.clearBruteForceResults(theAlliesTeamName);
            alliesMissionsManager.clearMissionFromBlockingQueue(theAlliesTeamName);
            alliesMissionsManager.removeAliesTeam(theAlliesTeamName);
            engineManager.initMaxAmountOfMissions();
            engineManager.clearBattleFieldValues("allies");
            engineManager.clearAlliesRegisteredToContestList();
        }
    }
}