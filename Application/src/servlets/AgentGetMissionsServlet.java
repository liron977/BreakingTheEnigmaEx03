package servlets;

import bruteForceLogic.TheMissionInfo;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesMissionsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AgentGetMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            AlliesMissionsManager alliesMissionsManager = ServletUtils.getAlliesMissionsManager(getServletContext());
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            String amountOfMissionsString = request.getParameter(ParametersConstants.AMOUNT_OF_MISSIONS_PER_AGENT);
            int amountOfMissions = Integer.parseInt(amountOfMissionsString);//todo
            List<TheMissionInfo> theMissionInfoList = new ArrayList<>();
            int counter = 0;
            while (counter < amountOfMissions) {
                try {
                    TheMissionInfo theMissionInfo = alliesMissionsManager.getMissionFromBlockingQueue(theAlliesTeamName);
                    if (theMissionInfo == null) {
                        break;
                    } else {
                        counter++;
                        theMissionInfoList.add(theMissionInfo);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(theMissionInfoList);
            out.println(json);
            out.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}