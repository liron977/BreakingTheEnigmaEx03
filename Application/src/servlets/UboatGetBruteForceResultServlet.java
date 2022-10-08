package servlets;

import bruteForce.BruteForceResultDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import utils.BruteForceResultAndVersion;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UboatGetBruteForceResultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String roleParameter = request.getParameter(ParametersConstants.ROLE);
        String teamName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        if (teamName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        UboatBruteForceResultsMapManager  uboatBruteForceResultsManager = ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
        if(uboatBruteForceResultsManager==null) {
            return;
        }

   /*     List<BruteForceResultDTO> list = uboatBruteForceResultsManager.getBruteForceListByUboatName(teamName);
        System.out.println("*************************");
        for (BruteForceResultDTO brute : list) {
            System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
        }*/



        int bruteForceResultVersion = ServletUtils.getIntParameter(request, ParametersConstants.UBOAT_BRUTE_FORCE_RESULT_VERSION_PARAMETER);
        if (bruteForceResultVersion == ParametersConstants.INT_PARAMETER_ERROR) {
            return;
        }

        int bruteForceResultManagerCounter = 0;
        List<BruteForceResultDTO> bruteForceResultDTOEntries;
        synchronized (getServletContext()) {
            bruteForceResultManagerCounter = uboatBruteForceResultsManager.getVersion(teamName);
            bruteForceResultDTOEntries = uboatBruteForceResultsManager.getbruteForceResultDTOEntries(bruteForceResultVersion,teamName);
        }
    /*    for (BruteForceResultDTO brute : bruteForceResultDTOEntries) {
            System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
        }
*/

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

    private void logServerMessage(String message){
        System.out.println(message);
    }
}
