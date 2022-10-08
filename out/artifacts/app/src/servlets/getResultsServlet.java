package servlets;

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
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class getResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            BruteForceResultsManager bruteForceResultsManager = ServletUtils.getBruteForceResultsManager(getServletContext());
            Gson gson = new Gson();
            Type bruteForceResultsListType = new TypeToken<ArrayList<BruteForceResultDTO>>() {
            }.getType();
            List<BruteForceResultDTO> bruteForceResultDTOListFromGson = gson.fromJson(request.getReader(), bruteForceResultsListType);
            try {
                List<BruteForceResultDTO> bruteForceResultDTOList = bruteForceResultsManager.getBruteForceResultsIntoList(theAlliesTeamName);

                String json = gson.toJson(bruteForceResultDTOList);
                out.println(json);
                out.flush();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}