package servlets;

import bruteForce.BruteForceResultDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesBruteForceResultsMapManager;
import utils.BruteForceResultAndVersion;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AlliesGetBruteForceResultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String teamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        if (teamName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        AlliesBruteForceResultsMapManager alliesBruteForceResultsManager = ServletUtils.getAlliesBruteForceResultsListManager(getServletContext());
        if(alliesBruteForceResultsManager==null){
            return;
        }
        int bruteForceResultVersion = ServletUtils.getIntParameter(request, ParametersConstants.ALLIES_BRUTE_FORCE_RESULT_VERSION_PARAMETER);
        if (bruteForceResultVersion == ParametersConstants.INT_PARAMETER_ERROR) {
            return;
        }

        int bruteForceResultManagerCounter = 0;
        List<BruteForceResultDTO> bruteForceResultDTOEntries;
        synchronized (getServletContext()) {
            bruteForceResultManagerCounter = alliesBruteForceResultsManager.getVersion(teamName);
            bruteForceResultDTOEntries = alliesBruteForceResultsManager.getbruteForceResultDTOEntries(bruteForceResultVersion, teamName);
         /*   if(bruteForceResultDTOEntries!=null) {
                for (BruteForceResultDTO brute : bruteForceResultDTOEntries) {
                    System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
                }
            }*/
        }


        // log and create the response json string
        BruteForceResultAndVersion bruteForceResultAndVersion = new BruteForceResultAndVersion(bruteForceResultDTOEntries, bruteForceResultManagerCounter);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(bruteForceResultAndVersion);
        logServerMessage("Server Chat version: " + bruteForceResultManagerCounter + ", User '" + teamName + "' Chat version: " + bruteForceResultVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}