package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineEngine.EngineManager;
import managers.uBoatEngine.MediatorForEngineManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ResetCodeConfigurationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
            String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            EngineManager engineManager = mediatorsForEngineManagerMap.get(theBattleFieldName);
            engineManager.resetCurrentCode();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
