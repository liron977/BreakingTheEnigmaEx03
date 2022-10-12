package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import machineEngine.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
    public class UBoatContestSettingsServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            Gson gson= new Gson();
            String convertedStringFromGson=gson.fromJson(request.getReader(),String.class);
            UBoatAvailableContestsManager uBoatAvailableContestsManger = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            MediatorForEngineManager mediatorForEngineManager=ServletUtils.getMediatorForEngineManager(getServletContext());
            String battleName= request.getParameter(ParametersConstants.BATTLE_FIELD);
            EngineManager engineManager=mediatorForEngineManager.getEngineMangerByBattleFiLedName(battleName);
            engineManager.setConvertedStringInBattleField(convertedStringFromGson);
            engineManager.setIsActiveContest();
            engineManager.getBattleField().setIsConvertedStringSet();
            //  uBoatAvailableContestsManger.addUBoatAvailableContest(engineManager,battleName);
            response.setStatus(HttpServletResponse.SC_OK);
        }
}