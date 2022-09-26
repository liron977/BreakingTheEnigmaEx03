package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engineManager.EngineManager;
import engineManager.MediatorForEngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import javafx.event.ActionEvent;
import machineDTO.CodeConfigurationTableViewDTO;
import utils.ServletUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class setConfigurationsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
        Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
        String theBattleFieldName = request.getParameter(Constants.BATTLE_FIELD);
        EngineManager engineManager = mediatorsForEngineManagerMap.get(theBattleFieldName);
        Collection<Part> parts = request.getParts();
        BufferedReader streamReader;
        StringBuilder responseStrBuilder;
        String jsonTargetStatusDuringTaskDto=null;
        CodeConfigurationTableViewDTO dto=null;
        Gson gson;
        for (Part part : parts) {
            InputStream in= part.getInputStream();
            streamReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            if(part.getName().equals("gsonCodeConfigurationTableViewDTO")){
                jsonTargetStatusDuringTaskDto= responseStrBuilder.toString();
                gson=new Gson();
                dto = gson.fromJson(jsonTargetStatusDuringTaskDto, CodeConfigurationTableViewDTO.class);
            }
        }
/*
           */    // CodeConfigurationTableViewDTO codeConfigurationTableViewDTO = gson.fromJson(request.getM, CodeConfigurationTableViewDTO.class);
            String error = setManuallyCodeConfiguration(response.getWriter(), engineManager, dto);

if (error.isEmpty()){
    response.setStatus(HttpServletResponse.SC_OK);
}
else {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.getWriter().println(error);
    response.getWriter().flush();
}

        }




    String setManuallyCodeConfiguration(PrintWriter out, EngineManager engineManager, CodeConfigurationTableViewDTO codeConfigurationTableViewDTO) {
        List<Exception> listOfExceptions = new ArrayList<>();
        String error = "";
        String rotorsId = codeConfigurationTableViewDTO.getRotors();
        String startingPosition = codeConfigurationTableViewDTO.getPositionsAndNotch();
        String reflector = codeConfigurationTableViewDTO.getReflector();
        String plugBoardPairs = codeConfigurationTableViewDTO.getPlugBoardPairs();
        listOfExceptions = engineManager.getAllErrorsRelatedToChosenManuallyStartingPosition(startingPosition).getListOfException();
        listOfExceptions.addAll(engineManager.getAllErrorsRelatedToChosenManuallyRotors(rotorsId).getListOfException());
        boolean isReflectoIDValid = reflector != "";
        listOfExceptions.addAll(engineManager.getAllErrorsRelatedToChosenManuallyPlugBoard(plugBoardPairs).getListOfException());
        if (listOfExceptions != null) {
            if (listOfExceptions.size() == 0 && isReflectoIDValid) {
                engineManager.chooseManuallyRotors();
                engineManager.chooseManuallyStartingPosition(startingPosition);
                engineManager.chooseManuallyReflect(reflector);
                if (plugBoardPairs.equals("")) {
                    engineManager.getTheMachineEngine().initEmptyPlugBoard();
                } else {
                    engineManager.chooseManuallyPlugBoard(plugBoardPairs);
                }
                engineManager.DefineIsCodeConfigurationSetValueToTrue();

            } else {
                for (Exception e : listOfExceptions) {
                    error = error +" "+ e.getMessage();
                }


            }

        }
        return error;


    }
}