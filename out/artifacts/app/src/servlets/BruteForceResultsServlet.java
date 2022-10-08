package servlets;

import bruteForce.BruteForceResultDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesBruteForceResultsMapManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BruteForceResultsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        AlliesBruteForceResultsMapManager bruteForceResultsManager=ServletUtils.getBruteForceResultsManager(getServletContext());
        UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        String battleName=uBoatAvailableContestsManager.getUboatNameByAlliesTeamName(theAlliesTeamName);
        UboatBruteForceResultsMapManager uboatBruteForceResultsMapManager=ServletUtils.getUboatBruteForceResultsMapManager(getServletContext());
        Gson gson=new Gson();
        Type bruteForceResultsListType = new TypeToken<ArrayList<BruteForceResultDTO>>() {}.getType();
        List<BruteForceResultDTO> bruteForceResultDTOListFromGson = gson.fromJson(request.getReader(), bruteForceResultsListType);
        if(bruteForceResultDTOListFromGson!=null) {
            try {
                uboatBruteForceResultsMapManager.addBruteForceResultsIntoList(battleName,bruteForceResultDTOListFromGson);
                bruteForceResultsManager.addBruteForceResultsIntoList(theAlliesTeamName, bruteForceResultDTOListFromGson);
                List<BruteForceResultDTO> list = uboatBruteForceResultsMapManager.getBruteForceListByUboatName(battleName);
                System.out.println("*************************");
                for (BruteForceResultDTO brute : list) {
                    System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
/*

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String roleParameter = request.getParameter(ParametersConstants.ROLE);
        String teamName;
        Object manager="";
        if(roleParameter.equals("allies")) {
            teamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            manager = (AlliesBruteForceResultsMapManager) ServletUtils.getAlliesBruteForceResultsListManager(getServletContext());
        }
        else {
            teamName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            manager = (UboatBruteForceResultsMapManager) ServletUtils.getUboatBruteForceResultsListManager(getServletContext());
        }
        if (teamName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        */
/*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         *//*

        int bruteForceResultVersion = ServletUtils.getIntParameter(request, ParametersConstants.UBOAT_BRUTE_FORCE_RESULT_VERSION_PARAMETER);
        if (bruteForceResultVersion == ParametersConstants.INT_PARAMETER_ERROR) {
            return;
        }

        int bruteForceResultManagerCounter = 0;
        List<BruteForceResultDTO> bruteForceResultDTOEntries;
        synchronized (getServletContext()) {
            bruteForceResultManagerCounter = bruteForceResultsManager.getVersion(teamName);
            bruteForceResultDTOEntries = bruteForceResultsManager.getbruteForceResultDTOEntries(bruteForceResultVersion,teamName);
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

    private void logServerMessage(String message){
        System.out.println(message);
    }
*/

/*    private static class BruteForceResultAndVersion {

        final private List<BruteForceResultDTO> entries;
        final private int version;

        public BruteForceResultAndVersion(List<BruteForceResultDTO> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
    }*/
}