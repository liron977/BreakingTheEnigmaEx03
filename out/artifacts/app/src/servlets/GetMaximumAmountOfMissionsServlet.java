package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.UBoatBattleField;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetMaximumAmountOfMissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            MediatorForEngineManager mediatorForEngineManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            EngineManager engineManager = mediatorForEngineManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            UBoatBattleField battleField = engineManager.getBattleField();
            int sizeOfMission = battleField.getAlliesSizeOfMission(theAlliesTeamName);
            long maxAmountOfMissions = (engineManager.maxAmountOfMissionscalculation(engineManager.getLevel(), sizeOfMission));
            //  System.out.println(maxAmountOfMissions + "maxAmountOfMissions");
            // System.out.println(engineManager.getLevel() + "engineManager.getLevel()");
            //System.out.println(sizeOfMission + "sizeOfMission");
            response.setStatus(HttpServletResponse.SC_OK);
            alliesManager.setTotalAmountOfCreadedMission(theAlliesTeamName, maxAmountOfMissions);
            out.println(maxAmountOfMissions);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
