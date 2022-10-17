package servlets;

import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.agent.StatusManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import managers.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class UboatLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            StatusManager statusManager=ServletUtils.getStatusManager(getServletContext());
            statusManager.removeUboat(battleName);
            System.out.println(statusManager.getContestStatusInfoDTOByBattlefield(battleName));
            uBoatAvailableContestsManager.removeAvailableContests(battleName);
            UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
            uboatBruteForceResultsMapManager.removeUboat(battleName);
            MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());
            String uboatUserName=mediatorForEngineManager.getEngineMangerByBattleFiLedName(battleName).getBattleField().getUploadedByName();
            userManager.removeUser(uboatUserName);
            try {
                mediatorForEngineManager.removeUboat(battleName);
            }
            catch (Exception e){
                System.out.println(" e- removeUboat");
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}