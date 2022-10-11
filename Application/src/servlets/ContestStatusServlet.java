package servlets;

import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
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
            if(request.getParameter(ParametersConstants.ROLE).equals("uboat")){
                battleName=request.getParameter(ParametersConstants.BATTLE_FIELD);
            }
            else {
                String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
                 battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
            }
            if (battleName != null) {
                EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
                ContestStatusInfoDTO contestStatusInfoDTO = new ContestStatusInfoDTO(
                        engineManager.getContestStatus(),
                        engineManager.getIsContestEnded(),
                        engineManager.getAlliesWinnwerTeamName(),
                        engineManager.getIsAlliesConfirmedGameOver());
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
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
       AlliesBruteForceResultsMapManager alliesGetBruteForceResultServlet=ServletUtils.getAlliesBruteForceResultsMapManager(getServletContext());
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        String battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
        if (battleName != null) {
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
            engineManager.setAlliesConfirmedGameOver(true);
            alliesManager.clearAlliesValues(theAlliesTeamName);
            alliesGetBruteForceResultServlet.clearBruteForceResults(theAlliesTeamName);
            alliesMissionsManager.clearMissionFromBlockingQueue(theAlliesTeamName);
            engineManager.clearAlliesActiveTeams();
        }
    }
}