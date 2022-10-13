package servlets;

import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineDTO.TheMachineSettingsDTO;
import machineEngine.EngineManager;
import managers.agent.StatusManager;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class StartNewContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            Boolean shouldStartNewContest=false;
            String alliesTeamName=request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            EngineManager engineManager=uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(alliesTeamName);

            if(engineManager!=null) {
                shouldStartNewContest=true;

            }
            Gson gson = new Gson();
            String json = gson.toJson(shouldStartNewContest);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}