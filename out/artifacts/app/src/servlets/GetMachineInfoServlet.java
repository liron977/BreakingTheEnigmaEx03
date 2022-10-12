package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineDTO.TheMachineEngineDTO;
import managers.uBoatEngine.MediatorForEngineManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetMachineInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            response.setContentType("application/json");
            String battleFieldTeamName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            EngineManager engineManager;
            if (battleFieldTeamName != null) {
                MediatorForEngineManager mediatorForEngineManager= ServletUtils.getMediatorForEngineManager(getServletContext());
                engineManager = mediatorForEngineManager.getEngineMangerByBattleFiLedName(battleFieldTeamName);
            } else {
                UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
                String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
                engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
            }
            if (engineManager != null) {
                TheMachineEngineDTO theMachineEngineDTO = new TheMachineEngineDTO(engineManager.getMachineUsedRotorsId()
                        , engineManager.getKeyboardAsArray().length,
                        engineManager.getMachineReflectorId()
                );
                try {
                    String json = gson.toJson(theMachineEngineDTO);
                    out.println(json);
                    out.flush();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}