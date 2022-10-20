package servlets;

import bruteForce.AgentContestInfoDTO;
import bruteForce.AlliesConfirmedDTO;
import bruteForce.ContestStatusInfoDTO;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.AlliesAgent;
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
import java.util.ArrayList;
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
                engineManager.getAlliesWinnwerTeamName());
        List<Allies> registeredAlliesList=engineManager.getRegisteredAlliesList();
        List<AlliesConfirmedDTO> alliesConfirmedDTOList=new ArrayList<>();
        for (Allies allies:registeredAlliesList) {
            alliesConfirmedDTOList.add(new AlliesConfirmedDTO(allies.getAlliesName(),allies.getAlliesConfirmedGameOver()));
        }
        contestStatusInfoDTO.setAlliesConfirmedDTOList(alliesConfirmedDTOList);
        List<AgentContestInfoDTO> agentContestInfoDTOList=new ArrayList<>();
        for (Allies allies:registeredAlliesList) {
            for (AlliesAgent alliesAgent:allies.getAlliesAgents()) {
                agentContestInfoDTOList.add(new AgentContestInfoDTO(allies.getAlliesName(),alliesAgent.getAgentName(),alliesAgent.getIsDataShouldBeDelete()));
            }
        }
        contestStatusInfoDTO.setAgentContestInfoList(agentContestInfoDTOList);

        try {
            statusManager.addContestStatusInfoDTO(battleName,contestStatusInfoDTO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
        uboatBruteForceResultsMapManager.clearBruteForceResults(battleName);
        uBoatAvailableContestsManager.clearValues(battleName);
        //uBoatAvailableContestsManager.removeAvailableContests(battleName);
    }



}