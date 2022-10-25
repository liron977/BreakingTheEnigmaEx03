package servlets;

import bruteForce.ContestStatusInfoDTO;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.agent.StatusManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class UpdateContestStatusAgentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String alliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        String agentName = request.getParameter(ParametersConstants.AGENT_NAME);
        UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
       EngineManager engineManager= uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(alliesTeamName);
       if(engineManager!=null) {
           engineManager.setAlliesConfirmedGameOver(alliesTeamName,false);
           engineManager.setIsDataForAgentShouldBeDeletedByAgentName(alliesTeamName,agentName,false);

       }
       StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
       ContestStatusInfoDTO contestStatusInfoDTO=statusManager.getContestStatusInfoDTOByAlliesName(alliesTeamName);
       if(contestStatusInfoDTO!=null) {
           contestStatusInfoDTO.setAlliesConfirmedGameOver(alliesTeamName, false);
           contestStatusInfoDTO.setIsDataShouldDeleteByAgentName(agentName, false);
       }
        response.setStatus(HttpServletResponse.SC_OK);
    }

}