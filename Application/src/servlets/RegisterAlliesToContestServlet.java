package servlets;

import bruteForce.AlliesDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;

public class RegisterAlliesToContestServlet  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        AlliesDTO alliesToRegister = gson.fromJson(request.getReader(), AlliesDTO.class);
        UBoatAvailableContestsManager uBoatAvailableContestsManger = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        //MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());

        String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        EngineManager engineManager = uBoatAvailableContestsManger.getEngineManagerByBattleFieldName(battleName);
        if (engineManager != null) {
/*
        EngineManager engineManager=mediatorForEngineManager.getEngineMangerByBattleFiLedName(battleName);
*/
            boolean isAlliesAddedSuccessfully = engineManager.addAlliesToContest(alliesToRegister);
            if (isAlliesAddedSuccessfully) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }
    }