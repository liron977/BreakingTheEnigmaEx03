package servlets;

import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class UpdateContestStatusAgentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String alliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
       EngineManager engineManager= uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(alliesTeamName);
       if(engineManager!=null) {
           engineManager.setIsAlliesConfirmedGameOver();
       }
    }

}