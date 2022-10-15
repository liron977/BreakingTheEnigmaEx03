package servlets;

import bruteForce.DMAmountOfMissionsInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.MediatorForEngineManager;
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
            String battlefield = request.getParameter(ParametersConstants.BATTLE_FIELD);
            MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());

            Long amountOfCreatedMissions=alliesManager.getAmountOfCreadedMission(theAlliesTeamName);
            Long totalAmountOfCreatedMissions= alliesManager.getTotalAmountOfCreadedMission(theAlliesTeamName);
            DMAmountOfMissionsInfoDTO dmAmountOfMissionsInfoDTO=new
                    DMAmountOfMissionsInfoDTO(mediatorForEngineManager.isBattleExists(battlefield),totalAmountOfCreatedMissions,amountOfCreatedMissions);
            Gson gson = new Gson();
            String json = gson.toJson(dmAmountOfMissionsInfoDTO);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}