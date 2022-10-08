package servlets;

import bruteForce.BruteForceResultDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.BruteForceResultsListManager;
import managers.bruteForce.BruteForceResultsMapManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BruteForceResultsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        BruteForceResultsMapManager bruteForceResultsManager=ServletUtils.getBruteForceResultsManager(getServletContext());
        Gson gson=new Gson();
        Type bruteForceResultsListType = new TypeToken<ArrayList<BruteForceResultDTO>>() {}.getType();
        List<BruteForceResultDTO> bruteForceResultDTOListFromGson = gson.fromJson(request.getReader(), bruteForceResultsListType);
        if(bruteForceResultDTOListFromGson!=null) {
            try {
                bruteForceResultsManager.addBruteForceResultsIntoBlockingQueue(theAlliesTeamName, bruteForceResultDTOListFromGson);
                List<BruteForceResultDTO> list = bruteForceResultsManager.getBruteForceListByAlliesTeamName(theAlliesTeamName);
                System.out.println("*************************");
                for (BruteForceResultDTO brute : list) {
                    System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        BruteForceResultsMapManager bruteForceResultsManager=ServletUtils.getBruteForceResultsManager(getServletContext());
       // List<BruteForceResultDTO> bruteForceResultDTOList=bruteForceResultsManager.getBruteForceListByAlliesTeamName(theAlliesTeamName);
        if (theAlliesTeamName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        /*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         */
        int bruteForceResultVersion = ServletUtils.getIntParameter(request, ParametersConstants.BRUTE_FORCE_RESULT_VERSION_PARAMETER);
        if (bruteForceResultVersion == ParametersConstants.INT_PARAMETER_ERROR) {
            return;
        }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int bruteForceResultManagerCounter = 0;
        List<BruteForceResultDTO> bruteForceResultDTOEntries;
        synchronized (getServletContext()) {
            bruteForceResultManagerCounter = bruteForceResultsManager.getVersion(theAlliesTeamName);
            bruteForceResultDTOEntries = bruteForceResultsManager.getbruteForceResultDTOEntries(bruteForceResultVersion,theAlliesTeamName);
        }

        // log and create the response json string
        BruteForceResultAndVersion bruteForceResultAndVersion = new BruteForceResultAndVersion(bruteForceResultDTOEntries, bruteForceResultManagerCounter);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(bruteForceResultAndVersion);
        logServerMessage("Server Chat version: " + bruteForceResultManagerCounter + ", User '" + theAlliesTeamName + "' Chat version: " + bruteForceResultVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private static class BruteForceResultAndVersion {

        final private List<BruteForceResultDTO> entries;
        final private int version;

        public BruteForceResultAndVersion(List<BruteForceResultDTO> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
    }
}