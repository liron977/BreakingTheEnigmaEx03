package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Pair;
import engine.theEnigmaEngine.PlugsBoard;
import machineEngine.EngineManager;
import managers.uBoatEngine.MediatorForEngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import machineDTO.ConvertedStringProcessDTO;
import utils.ServletUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class EncryptServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (PrintWriter out = response.getWriter()) {
            BufferedReader streamReader;
            StringBuilder responseStrBuilder;
            String jsonStringToConvert = null;
            ConvertedStringProcessDTO convertedStringProcessDTO=null;
            List<String> exceptionList=new ArrayList<>();
            response.setContentType("application/json");
            MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            Map<String, EngineManager> mediatorsForEngineManagerMap = mediatorsManager.getEngineManagersMap();
            String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            Collection<Part> parts = request.getParts();
            String convertedString="";
            EngineManager engineManager = mediatorsForEngineManagerMap.get(theBattleFieldName);
            if (engineManager != null) {
                for (Part part : parts) {
                    InputStream in = part.getInputStream();
                    streamReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    responseStrBuilder = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }
                    if (part.getName().equals("stringToConvert")) {
                        jsonStringToConvert = responseStrBuilder.toString();
                        if (jsonStringToConvert == null) {
                            exceptionList.add("Please insert a string to convert");
                        } else {
                            String stringToConvertWithoutExcludedSignals = engineManager.getTheMachineEngine().getDictionary().removeExcludeCharsFromString(jsonStringToConvert).toUpperCase();
                            String stringWithoutLegalSignals = engineManager.isStringIncludeIllegalSignal(stringToConvertWithoutExcludedSignals).toUpperCase();
                            if (stringWithoutLegalSignals.length() != 0) {
                                String msg = "There are illegal chars in the string: ".concat(stringToConvertWithoutExcludedSignals);
                                exceptionList.add((msg));
                                convertedStringProcessDTO = new ConvertedStringProcessDTO(stringToConvertWithoutExcludedSignals, "",exceptionList);
                            } else {
                                if (isStringToConvertLegal(engineManager, stringToConvertWithoutExcludedSignals,exceptionList)) {
                                    PlugsBoard plugsBoard = engineManager.getTheMachineEngine().getPlugsBoard();
                                    List<Pair> listOfPairsOfSwappingCharacter = new ArrayList<>();
                                    if (plugsBoard != null) {
                                        listOfPairsOfSwappingCharacter = engineManager.getTheMachineEngine().getPlugsBoard().getPairsOfSwappingCharacter();
                                        engineManager.getTheMachineEngine().initEmptyPlugBoard();
                                    }
                                    convertedString = engineManager.getConvertedString(stringToConvertWithoutExcludedSignals).getConvertedString().toUpperCase();
                                    convertedStringProcessDTO = new ConvertedStringProcessDTO(stringToConvertWithoutExcludedSignals, convertedString,exceptionList);
                                    if (plugsBoard != null) {
                                        engineManager.getTheMachineEngine().getPlugsBoard().setPairsOfSwappingCharacter(listOfPairsOfSwappingCharacter);
                                    }
                                }
                                else {
                                    convertedStringProcessDTO = new ConvertedStringProcessDTO(stringToConvertWithoutExcludedSignals, "",exceptionList);
                                    engineManager.getBattleField().setConvertedString(convertedString);
                                }
                            }

                        }
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(convertedStringProcessDTO);
                    out.println(json);
                    out.flush();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
        private Boolean isStringToConvertLegal(EngineManager engineManager,String stringToConvert,List<String> exceptionList) throws Exception {
        String[] stringToConvertArray = stringToConvert.split(" ");
        String IllegalWords = "";
        Boolean isStringToConvertIsLegal=true;
        for (String str : stringToConvertArray) {
            if (!engineManager.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(str)) {
                if (isStringToConvertIsLegal) {
                    IllegalWords = str;
                } else {
                    IllegalWords = (IllegalWords.concat(", ")).concat(str);
                }
                isStringToConvertIsLegal = false;
            }
        }
        if(!isStringToConvertIsLegal) {
            exceptionList.add(("Please enter legal word from the dictionary \nThe illegal words: "+IllegalWords));
        }
        return isStringToConvertIsLegal;
    }
}