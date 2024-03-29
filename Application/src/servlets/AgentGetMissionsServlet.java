package servlets;
import bruteForce.TheMissionInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AgentGetMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {

            //Thread.currentThread().setName("AgentGetMissionsServlet");
            AlliesMissionsManager alliesMissionsManager = ServletUtils.getAlliesMissionsManager(getServletContext());
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());

            String amountOfMissionsString = request.getParameter(ParametersConstants.AMOUNT_OF_MISSIONS_PER_AGENT);
            int amountOfMissions = Integer.parseInt(amountOfMissionsString);//todo

            List<TheMissionInfoDTO> theMissionInfoList = new ArrayList<>();
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);

            if (engineManager != null) {
                if (!engineManager.isAlliesExists(theAlliesTeamName)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    if (alliesManager.getMaxAmountOfMissions(theAlliesTeamName) == -1) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        if (alliesManager.getMaxAmountOfMissions(theAlliesTeamName) <= 0) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            System.out.println(alliesManager.getMaxAmountOfMissions(theAlliesTeamName)+"0");
                        } else if (engineManager.getIsContestEnded()) {
                            response.setStatus(HttpServletResponse.SC_GONE);
                        } else {
                                  int counter = 0;
                                while (counter < amountOfMissions) {
                                    try {
                                        TheMissionInfoDTO theMissionInfo = alliesMissionsManager.getMissionFromBlockingQueue(theAlliesTeamName);

                                        if (theMissionInfo == null) {

                                            break;
                                        } else {
                                            counter++;
                                            alliesManager.decreaseMaxAmountOfMissions(theAlliesTeamName);
                                            theMissionInfoList.add(theMissionInfo);
                                        }
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                   }

                            response.setStatus(HttpServletResponse.SC_OK);
                            Gson gson = new Gson();
                            String json = gson.toJson(theMissionInfoList);
                            out.println(json);
                            out.flush();

                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}