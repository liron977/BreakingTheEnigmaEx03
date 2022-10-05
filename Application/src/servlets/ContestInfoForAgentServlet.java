package servlets;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.UBoatBattleField;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ContestInfoForAgentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            Map<String, EngineManager> uBoatAvailableContestsManagerMap = uBoatAvailableContestsManager.getUBoatAvailableContestsMap();
            String alliesTeamFieldName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            if (uBoatAvailableContestsManagerMap != null) {
                for (String battleFieldName : uBoatAvailableContestsManagerMap.keySet()) {
                    EngineManager engineManager=uBoatAvailableContestsManagerMap.get(battleFieldName);
                    if(engineManager.isAlliesExists(alliesTeamFieldName)){
                        createUBoatContestInfoWithoutCheckBoxDTO(engineManager,battleFieldName,out);
                        response.setStatus(HttpServletResponse.SC_OK);
                        break;
                    }
                }
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void createUBoatContestInfoWithoutCheckBoxDTO(EngineManager engineManager,String battleFieldName,PrintWriter out){
        UBoatBattleField uBoatBattleField=engineManager.getBattleField();
        UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTO=new UBoatContestInfoWithoutCheckBoxDTO(battleFieldName,
                uBoatBattleField.getUploadedByName(),uBoatBattleField.getContestStatus(),
                uBoatBattleField.getLevel(),uBoatBattleField.getAlliesNeededTeamsAmount()
                ,uBoatBattleField.getAlliesActiveTeamsAmount());
        Gson gson = new Gson();
        String json = gson.toJson(uBoatContestInfoDTO);
        out.println(json);
        out.flush();
    }

}