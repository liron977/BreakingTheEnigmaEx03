package servlets;

import bruteForce.ContestStatusInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetContestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            String battleName = uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
            if (battleName != null) {
                EngineManager engineManager = uBoatAvailableContestsManager.getEngineManagerByBattleFieldName(battleName);
                ContestStatusInfoDTO contestStatusInfoDTO = new ContestStatusInfoDTO(
                        engineManager.getIsContestEnded(),
                        engineManager.getAlliesWinnwerTeamName());
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
}
