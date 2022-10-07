package servlets;

import bruteForce.AlliesDTO;
import bruteForce.BruteForceResultDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.BruteForceResultsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BruteForceResultsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
        BruteForceResultsManager bruteForceResultsManager=ServletUtils.getBruteForceResultsManager(getServletContext());
        Gson gson=new Gson();
        Type bruteForceResultsListType = new TypeToken<ArrayList<BruteForceResultDTO>>() {}.getType();
        List<BruteForceResultDTO> bruteForceResultDTOListFromGson = gson.fromJson(request.getReader(), bruteForceResultsListType);
        if(bruteForceResultDTOListFromGson!=null) {
            try {
                bruteForceResultsManager.addBruteForceResultsIntoBlockingQueue(theAlliesTeamName, bruteForceResultDTOListFromGson);
                BlockingQueue<BruteForceResultDTO> queue = bruteForceResultsManager.getResultsBlockingQueueByAlliesTeamName(theAlliesTeamName);
                System.out.println("*************************");
                for (BruteForceResultDTO brute : queue) {
                    System.out.println(brute.getConvertedString() + " " + brute.getCodeDescription() + " " + brute.getTheMissionNumber() + "IN SERVLET");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}