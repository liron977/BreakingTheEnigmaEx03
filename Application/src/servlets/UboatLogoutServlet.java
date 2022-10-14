package servlets;

import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class UboatLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            uBoatAvailableContestsManager.removeAvailableContests(battleName);
            UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
            uboatBruteForceResultsMapManager.removeUboat(battleName);
            MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());
            mediatorForEngineManager.removeUboat(battleName);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
