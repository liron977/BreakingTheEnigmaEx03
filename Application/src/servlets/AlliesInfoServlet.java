package servlets;

import bruteForce.AlliesDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import engineManager.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.MediatorForEngineManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class AlliesInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String battleName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            MediatorForEngineManager mediatorForEngineManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            EngineManager engineManager = mediatorForEngineManager.getEngineMangerByBattleFiLedName(battleName);
            List<Allies> registeredAlliesList = engineManager.getRegisteredAlliesList();
            List<AlliesDTO> alliesDTOList = createAlliesDTOList(registeredAlliesList);

            if (alliesDTOList != null) {
                Gson gson = new Gson();
                String json = gson.toJson(alliesDTOList);
                out.println(json);
                out.flush();
            }


    } catch(Exception e)
    {
        throw new RuntimeException(e);
    }

}

        public List<AlliesDTO> createAlliesDTOList(List<Allies> registeredAlliesList){
        List<AlliesDTO> alliesDTOList=new ArrayList<>();
            for (Allies allies:registeredAlliesList) {
                AlliesDTO alliesDTO=new AlliesDTO(allies.getMissionSize(),allies.getAlliesName());
                alliesDTO.setAgentsAmount(allies.getAgentsAmount());
                alliesDTOList.add(alliesDTO);

            }
            return alliesDTOList;

        }



    }