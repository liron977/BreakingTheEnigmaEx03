package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
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
import machineDTO.CodeDescriptionDTO;
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
        String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        String configurationSelectionType= request.getParameter(ParametersConstants.CONFIGURATION_SELECTION_TYPE);
        EngineManager engineManager = mediatorsForEngineManagerMap.get(theBattleFieldName);
        Collection<Part> parts = request.getParts();
        BufferedReader streamReader;
        StringBuilder responseStrBuilder;
        String jsonTargetStatusDuringTaskDto = null;
        CodeConfigurationTableViewDTO dto = null;
        Gson gson;
        for (Part part : parts) {
            InputStream in = part.getInputStream();
            streamReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            if (part.getName().equals("gsonCodeConfigurationTableViewDTO")) {
                jsonTargetStatusDuringTaskDto = responseStrBuilder.toString();
                gson = new Gson();
                CodeConfigurationTableViewDTO dtoFromGson = gson.fromJson(jsonTargetStatusDuringTaskDto, CodeConfigurationTableViewDTO.class);
                dto = dtoFromGson;
            }
        }
        String error="";
        if(configurationSelectionType.equals("Manually")) {
            error = setManuallyCodeConfiguration(response.getWriter(), engineManager, dto);
        }
        else {
            error=setRandomCodeButtonActionListener(response.getWriter(), engineManager, dto);
        }
        if (error.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(error);
            response.getWriter().flush();
        }
    }
   private String setManuallyCodeConfiguration(PrintWriter out, EngineManager engineManager, CodeConfigurationTableViewDTO codeConfigurationTableViewDTO) {
       Boolean isMachineDefined = engineManager.getMachineDefined();
        List<Exception> listOfExceptions = new ArrayList<>();
        String error = "";
        if(!isMachineDefined){
            listOfExceptions.add(new Exception("Please insert a xml file"));
        }
        else {
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

                }
            }
        }
       if(listOfExceptions.size()!=0){
           error =getErrorsRelatedToConfiguration(listOfExceptions);
       }
        return error;
    }
   private String setRandomCodeButtonActionListener (PrintWriter out, EngineManager engineManager, CodeConfigurationTableViewDTO codeConfigurationTableViewDTO) {
        Boolean isMachineDefined = engineManager.getMachineDefined();
       List<Exception> listOfExceptions=new ArrayList<>();
       String error="";
        if(isMachineDefined==false){
            listOfExceptions.add(new Exception("Please insert a xml file"));
        }
        else {
            CodeDescriptionDTO codeDescriptionDTO = engineManager.initCodeAutomatically();
            engineManager.DefineIsCodeConfigurationSetValueToTrue();
        }
        if(listOfExceptions.size()!=0){
            error =getErrorsRelatedToConfiguration(listOfExceptions);
            }
        return error;
        }
        private String getErrorsRelatedToConfiguration( List<Exception> listOfExceptions) {
            String error = "";
            for (Exception e : listOfExceptions) {
                error = error + " " + e.getMessage() + "\n";
            }
            return error;
        }
    }
