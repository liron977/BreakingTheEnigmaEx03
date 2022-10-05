package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import machineEngine.EngineManager;
import managers.uBoatEngine.MediatorForEngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import machineDTO.LimitedCodeConfigurationDTO;
import machineDTO.FullCodeDescriptionDTO;
import utils.ServletUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class setConfigurationsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
        Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
        String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
        String configurationSelectionType= request.getParameter(ParametersConstants.CONFIGURATION_SELECTION_TYPE);
        EngineManager engineManager = mediatorsForEngineManagerMap.get(theBattleFieldName);
     /*   Collection<Part> parts = request.getParts();*/
        BufferedReader streamReader;
        StringBuilder responseStrBuilder;
        String jsonTargetStatusDuringTaskDto = null;
        LimitedCodeConfigurationDTO dto = null;
        Gson gson= new Gson();
        LimitedCodeConfigurationDTO dtoFromGson=gson.fromJson(request.getReader(),LimitedCodeConfigurationDTO.class);
        dto=dtoFromGson;
       /* for (Part part : parts) {
            InputStream in = part.getInputStream();
            streamReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            if (part.getName().equals("gsonLimitedCodeConfigurationDTO")) {
                jsonTargetStatusDuringTaskDto = responseStrBuilder.toString();
                gson = new Gson();
                LimitedCodeConfigurationDTO dtoFromGson = gson.fromJson(jsonTargetStatusDuringTaskDto, LimitedCodeConfigurationDTO.class);
                dto = dtoFromGson;
            }
        }*/
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
   private String setManuallyCodeConfiguration(PrintWriter out, EngineManager engineManager, LimitedCodeConfigurationDTO limitedCodeConfigurationDTO) {
       Boolean isMachineDefined = engineManager.getMachineDefined();
        List<Exception> listOfExceptions = new ArrayList<>();
        String error = "";
        if(!isMachineDefined){
            listOfExceptions.add(new Exception("Please insert a xml file"));
        }
        else {
            String rotorsId = limitedCodeConfigurationDTO.getRotors();
            String startingPosition = limitedCodeConfigurationDTO.getPositionsAndNotch();
            String reflector = limitedCodeConfigurationDTO.getReflector();
            String plugBoardPairs = limitedCodeConfigurationDTO.getPlugBoardPairs();
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
   private String setRandomCodeButtonActionListener (PrintWriter out, EngineManager engineManager, LimitedCodeConfigurationDTO limitedCodeConfigurationDTO) {
        Boolean isMachineDefined = engineManager.getMachineDefined();
       List<Exception> listOfExceptions=new ArrayList<>();
       String error="";
        if(isMachineDefined==false){
            listOfExceptions.add(new Exception("Please insert a xml file"));
        }
        else {
            FullCodeDescriptionDTO fullCodeDescriptionDTO = engineManager.initCodeAutomatically();
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