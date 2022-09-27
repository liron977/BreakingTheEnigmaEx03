package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import engineManager.EngineManager;
import engineManager.MediatorForEngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineDTO.TheMachineSettingsDTO;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CurrentCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try(PrintWriter out = response.getWriter()){
        response.setContentType("application/json");
        MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
        Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
        String theBattleFieldName=request.getParameter(ParametersConstants.BATTLE_FIELD);
        EngineManager engineManager= mediatorsForEngineManagerMap.get(theBattleFieldName);
        if(engineManager!=null) {
            TheMachineSettingsDTO dto = engineManager.getTheMachineSettingsDTO();

            //todo we have map inside
            Gson gson = new Gson();
            String json = gson.toJson(dto);
            out.println(json);
            out.flush();
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
}