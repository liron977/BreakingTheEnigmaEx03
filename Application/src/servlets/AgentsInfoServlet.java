package servlets;

import bruteForce.AgentInfoDTO;
import bruteForce.DecryptionInfoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import engine.theEnigmaEngine.AlliesAgent;
import engineManager.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AgentsManager;
import managers.BruteForceResultsInfoManager;
import managers.MediatorForEngineManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import managers.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgentsInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            AgentsManager agentsInfoManager = ServletUtils.getAgentManager(getServletContext());
            Map<String, List<AgentInfoDTO>> agentsInfoManagerMap = agentsInfoManager.getAgentManagerMap();
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            List<AgentInfoDTO> agentInfoDTOList=getAgentIndoDTOListByTheAlliesTeamName(agentsInfoManagerMap,theAlliesTeamName);

            if (agentInfoDTOList != null) {

                Gson gson = new Gson();
                String json = gson.toJson(agentInfoDTOList);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AgentsManager agentManager = ServletUtils.getAgentManager(getServletContext());
        UserManager userManager=ServletUtils.getUserManager(getServletContext());
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
       String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        Gson gson= new Gson();
        AgentInfoDTO dtoFromGson=gson.fromJson(request.getReader(),AgentInfoDTO.class);
        AlliesAgent alliesAgent=new AlliesAgent(dtoFromGson.getAgentName(),dtoFromGson.getThreadsAmount(), dtoFromGson.getMissionsAmount(), dtoFromGson.getAlliesTeamName());
        alliesManager.addAgentToAllies(alliesAgent,theAlliesTeamName);

        if(userManager.isUserExists(dtoFromGson.getAgentName())){
            agentManager.addAgentInfoDTOList(theAlliesTeamName,dtoFromGson);
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
    private List<AgentInfoDTO> getAgentIndoDTOListByTheAlliesTeamName( Map<String, List<AgentInfoDTO>> agentsInfoManagerMap, String theAlliesTeamName){
        List<AgentInfoDTO> agentInfoDTOList=new ArrayList<>();
        agentInfoDTOList= agentsInfoManagerMap.get(theAlliesTeamName);
        return agentInfoDTOList;
    }

}