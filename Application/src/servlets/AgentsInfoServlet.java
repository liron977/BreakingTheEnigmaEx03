package servlets;

import bruteForce.AgentInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.AlliesAgent;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.agent.AgentsManager;
import managers.uBoatEngine.AlliesManager;
import managers.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
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
           // List<AgentInfoDTO> agentInfoDTOList=agentsInfoManager.getAgentsByAlliesTeamName(theAlliesTeamName);

            if (agentInfoDTOList != null) {

                Gson gson = new Gson();
                String json = gson.toJson(agentInfoDTOList);
                out.println(json);
                out.flush();
            }
            response.setStatus(HttpServletResponse.SC_OK);
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
        if(agentInfoDTOList==null){
            agentInfoDTOList=new ArrayList<>();
        }
        return agentInfoDTOList;
    }

}