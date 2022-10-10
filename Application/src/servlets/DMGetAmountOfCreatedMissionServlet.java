package servlets;

import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.AlliesManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class DMGetAmountOfCreatedMissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            AlliesManager alliesManager= ServletUtils.getAlliesManager(getServletContext());
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            int amountOfCreatedMissions=alliesManager.getAmountOfCreadedMission(theAlliesTeamName);
            out.println(amountOfCreatedMissions);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
