package servlets;

import bruteForce.AlliesDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class RegisterAlliesToContestServlet  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        AlliesDTO alliesToRegister = gson.fromJson(request.getReader(), AlliesDTO.class);
        UBoatAvailableContestsManager uBoatAvailableContestsManger = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        //MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());
        AlliesManager alliesManager=ServletUtils.getAlliesManager(getServletContext());
        Allies allies=alliesManager.getAlliesByAlliesTeamName(alliesToRegister.getAlliesName());
        allies.setMissionSize(alliesToRegister.getMissionSize());

        String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        EngineManager engineManager = uBoatAvailableContestsManger.getEngineManagerByBattleFieldName(battleName);
        if (engineManager != null) {
            boolean isAlliesAddedSuccessfully = engineManager.addAlliesToContest(allies);
            if (isAlliesAddedSuccessfully) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }
    }