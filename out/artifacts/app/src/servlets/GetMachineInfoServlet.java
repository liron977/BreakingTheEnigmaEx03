package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineDTO.TheMachineEngineDTO;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetMachineInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            Gson gson=new Gson();
            response.setContentType("application/json");
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
            TheMachineEngineDTO theMachineEngineDTO=new TheMachineEngineDTO(engineManager.getMachineUsedRotorsId(),
                  /*  engineManager.getMachineRotorsSetId(),*/engineManager.getMachineReflectorId()
                  /*  ,engineManager.getMachineReflectorsSetId()*/);
            try {
                String json = gson.toJson(theMachineEngineDTO);
                out.println(json);
                out.flush();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}