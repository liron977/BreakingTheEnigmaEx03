package servlets;

import bruteForce.UBoatContestInfoWithoutCheckBoxDTO;
import com.google.gson.Gson;
import engine.theEnigmaEngine.UBoatBattleField;
import engineManager.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UBoatContestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            UBoatAvailableContestsManager uBoatAvailableContestsManager=ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            Map<String, EngineManager> uBoatAvailableContestsManagerMap = uBoatAvailableContestsManager.getUBoatAvailableContestsMap();
           /* MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();*/
            List<UBoatContestInfoWithoutCheckBoxDTO> uBoatContestInfoDTOList=new ArrayList<>();
            if (uBoatAvailableContestsManagerMap != null) {
                for (String battleFieldName : uBoatAvailableContestsManagerMap.keySet()) {
                    EngineManager engineManager=uBoatAvailableContestsManagerMap.get(battleFieldName);
                    UBoatBattleField uBoatBattleField=engineManager.getBattleField();
                    UBoatContestInfoWithoutCheckBoxDTO uBoatContestInfoDTO=new UBoatContestInfoWithoutCheckBoxDTO(battleFieldName,
                            uBoatBattleField.getUploadedByName(),uBoatBattleField.getContestStatus(),
                            uBoatBattleField.getLevel(),uBoatBattleField.getAlliesNeededTeamsAmount()
                            ,uBoatBattleField.getAlliesActiveTeamsAmount());
                    uBoatContestInfoDTOList.add(uBoatContestInfoDTO);
                }
                Gson gson = new Gson();
                String json = gson.toJson(uBoatContestInfoDTOList);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
