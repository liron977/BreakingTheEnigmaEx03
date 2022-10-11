package servlets;

import bruteForce.ContestStatusInfoDTO;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.agent.StatusManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

public class ClearContestUboatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        MediatorForEngineManager mediatorForEngineManager= ServletUtils.getMediatorForEngineManager(getServletContext());
        StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
        EngineManager engineManager=uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
        ContestStatusInfoDTO contestStatusInfoDTO = new ContestStatusInfoDTO(
                engineManager.getIsConvertedStringSet(),
                engineManager.getContestStatus(),
                engineManager.getIsContestEnded(),
                engineManager.getAlliesWinnwerTeamName(),
                engineManager.getIsAlliesConfirmedGameOver());
        List<String> alliesTeamNames=engineManager.getAlliesRegisteredTeamNames();
        contestStatusInfoDTO.setAlliesTeamNames(alliesTeamNames);
        try {
            statusManager.addContestStatusInfoDTO(battleName,contestStatusInfoDTO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
        uboatBruteForceResultsMapManager.clearBruteForceResults(battleName);
        uBoatAvailableContestsManager.clearValues(battleName);
    }



}