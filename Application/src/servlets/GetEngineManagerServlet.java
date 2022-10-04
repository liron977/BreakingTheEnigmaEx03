package servlets;

import bruteForceLogic.TheMissionInfo;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.TheMachineEngine;
import engineManager.EngineManager;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.bruteForce.AlliesMissionsManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetEngineManagerServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (ServletOutputStream out = response.getOutputStream()) {
            response.setContentType("application/json");
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);

            Gson gson = new Gson();

                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos);
                 oos.writeObject(engineManager);
                 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bais);
                 out.println(ois.read());
                 out.flush();
           /* String json = gson.toJson(theMachineEngine);
            out.println(json);
            out.flush();*/


        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}