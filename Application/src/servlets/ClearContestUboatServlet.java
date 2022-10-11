package servlets;

import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class ClearContestUboatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
        uboatBruteForceResultsMapManager.clearBruteForceResults(battleName);
        UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        uBoatAvailableContestsManager.clearValues(battleName);
    }

}