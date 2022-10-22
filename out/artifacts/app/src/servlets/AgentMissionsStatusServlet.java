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

public class AgentMissionsStatusServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
       AgentsManager agentsManager=ServletUtils.getAgentManager(getServletContext());
        Gson gson= new Gson();
        AgentInfoDTO dtoFromGson=gson.fromJson(request.getReader(),AgentInfoDTO.class);
      boolean isUpdateSucceededInAlliesManager= alliesManager.updateAgentMissionStatus(dtoFromGson);
      boolean isUpdateSucceededInAgentManager=agentsManager.updateAgentMissionStatus(dtoFromGson);
    if(isUpdateSucceededInAlliesManager&&isUpdateSucceededInAgentManager) {
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("AgentMissionsStatusServlet-ok");
    }
    else{
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        System.out.println("AgentMissionsStatusServlet-failed");


    }
    }
}