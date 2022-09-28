package servlets;

import bruteForce.UBoatContestInfoDTO;
import com.google.gson.Gson;
import engine.theEnigmaEngine.UBoatBattleField;
import engineManager.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.MediatorForEngineManager;
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
            MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
            List<UBoatContestInfoDTO> uBoatContestInfoDTOList=new ArrayList<>();
            if (mediatorsForEngineManagerMap != null) {
                for (String battleFieldName : mediatorsForEngineManagerMap.keySet()) {
                    EngineManager engineManager=mediatorsForEngineManagerMap.get(battleFieldName);
                    UBoatBattleField uBoatBattleField=engineManager.getBattleField();
                    UBoatContestInfoDTO uBoatContestInfoDTO=new UBoatContestInfoDTO(battleFieldName,
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
