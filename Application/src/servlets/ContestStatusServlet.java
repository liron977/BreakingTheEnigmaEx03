package servlets;

import bruteForce.AgentContestInfoDTO;
import bruteForce.AlliesConfirmedDTO;
import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.AlliesAgent;
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
import java.lang.reflect.Type;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String battleName;
            String theAlliesTeamName ="";
            String agentName ="";
            boolean isUboat=false;
            boolean isAgent=false;
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            if(request.getParameter(ParametersConstants.ROLE).equals("UBoat")){
                battleName=request.getParameter(ParametersConstants.BATTLE_FIELD);
                isUboat=true;
            }
            else {
                theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
                 battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
                 if(request.getParameter(ParametersConstants.ROLE).equals("Agent")){
                     agentName=request.getParameter(ParametersConstants.AGENT_NAME);
                     isAgent=true;
                 }
            }
            if (battleName != null&&!battleName.isEmpty()) {
                EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
                if (engineManager != null) {

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

                    if(!isUboat){
                        contestStatusInfoDTO.setAlliesConfirmedGameOver(theAlliesTeamName,engineManager.getIsAlliesConfirmedGameOver(theAlliesTeamName));
                        if(isAgent) {
                            contestStatusInfoDTO.setIsDataShouldDeleteByAgentName(agentName, engineManager.getIsDataShouldDeleteByAgentName(agentName,theAlliesTeamName));
                        }
                    }
                    if(engineManager.getIsContestEnded()){
                      engineManager.updateCurrentAlliesStatusListAtTheEndOfContest();
                    }
                    if (!engineManager.getIsContestEnded()){
                        int bb=0;
                        // System.out.println("!engineManager.getIsContestEnded()");
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(contestStatusInfoDTO);
                    out.println(json);
                    out.flush();
                }
            }
            else{
               // String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
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
        Gson gson=new Gson();
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        Type agentsNamesList = new TypeToken<ArrayList<String>>() {}.getType();
         List<String> agentsNamesListFromGson =gson.fromJson(request.getReader(),agentsNamesList);

        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        AlliesMissionsManager alliesMissionsManager=ServletUtils.getAlliesMissionsManager(getServletContext());
       AlliesBruteForceResultsMapManager alliesGetBruteForceResultServlet=ServletUtils.getAlliesBruteForceResultsMapManager(getServletContext());
        UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
       // String battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
        StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
        ContestStatusInfoDTO contestStatusInfoDTO=statusManager.getContestStatusInfoDTOByBattlefield(theBattleFieldName);
        if(contestStatusInfoDTO==null){
            contestStatusInfoDTO=new ContestStatusInfoDTO(false,"Wait",true,"");
        }
            try {
                contestStatusInfoDTO.addAllies(theAlliesTeamName,true);
                contestStatusInfoDTO.addAgents(theAlliesTeamName,agentsNamesListFromGson,true);
                statusManager.addContestStatusInfoDTO(theAlliesTeamName,contestStatusInfoDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        contestStatusInfoDTO.setAlliesConfirmedGameOver(theAlliesTeamName,true);
        contestStatusInfoDTO.setIsDataShouldDeleteByAlliesName(theAlliesTeamName,true);
        theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        AgentsManager agentsManager=ServletUtils.getAgentManager(getServletContext());
        agentsManager.initValues(theAlliesTeamName);
        alliesManager.clearAlliesValues(theAlliesTeamName);
        contestStatusInfoDTO.setContestStatus("Wait");
        contestStatusInfoDTO.setUboatSettingsCompleted(false);
        alliesGetBruteForceResultServlet.clearBruteForceResults(theAlliesTeamName);
        alliesMissionsManager.clearMissionFromBlockingQueue(theAlliesTeamName);
        alliesMissionsManager.removeAliesTeam(theAlliesTeamName);
        if (theBattleFieldName != null&&!theBattleFieldName.isEmpty()) {
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(theBattleFieldName);
            if(engineManager!=null) {
                engineManager.setAlliesConfirmedGameOver(theAlliesTeamName,true);
                engineManager.setIsDataForAgentShouldBeDeletedByAlliesName(theAlliesTeamName,true);
                engineManager.initMaxAmountOfMissions();
                engineManager.clearBattleFieldValues("allies", theAlliesTeamName);
                // engineManager.clearAlliesRegisteredToContestList();
                engineManager.clearAlliesRegisteredToContest(theAlliesTeamName);
            }
        }
    }
}