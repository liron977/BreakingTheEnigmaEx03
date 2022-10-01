package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UserManager;
import managers.agent.AgentsManager;
import managers.uBoatEngine.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;

public class DMCreateMissionsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        Allies alies = alliesManager.getAlliesByAlliesTeamName(theAlliesTeamName);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());


        Gson gson = new Gson();
    }
}
