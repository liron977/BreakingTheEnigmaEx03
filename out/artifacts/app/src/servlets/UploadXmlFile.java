package servlets;

import com.google.gson.Gson;
import machineDTO.ListOfErrorsDTO;
import machineEngine.EngineManager;
import managers.uBoatEngine.MediatorForEngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import machineDTO.ListOfExceptionsDTO;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadXmlFile extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part xmlFile = request.getParts().stream().findFirst().get();
       PrintWriter out = response.getWriter();
       boolean isFileValid=false;
        try {
            EngineManager engineManager = new EngineManager();
            ListOfExceptionsDTO listOfExceptionsDTO=engineManager.loadFileByInputStream(xmlFile.getInputStream(),xmlFile.getName());
            if (listOfExceptionsDTO.getListOfException().size()==0) {
                MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
                String battleName = engineManager.getBattleName().trim();
                if (mediatorsManager.isBattleExists(battleName)) {
                    listOfExceptionsDTO.addException(new Exception("The battle name already exists"));
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                else {
                    isFileValid=true;
                    mediatorsManager.addEngineManger(battleName, engineManager);
                    UBoatAvailableContestsManager uBoatAvailableContestsManger = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
                    uBoatAvailableContestsManger.addUBoatAvailableContest(engineManager, engineManager.getBattleName());
                    out.println(battleName.trim());
                    out.flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
           if(!isFileValid){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                List<String> errorsList=new ArrayList<>();
                for (Exception exception : listOfExceptionsDTO.getListOfException()) {
                    errorsList.add(exception.getMessage());
                }
                ListOfErrorsDTO listOfErrorsDTO=new ListOfErrorsDTO(errorsList);
                Gson gson = new Gson();
                String json = gson.toJson(listOfErrorsDTO);
                out.println(json);
                out.flush();
            }
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }

    private Boolean checkIfXmlFileIsValidAndIfValidAddMachine(PrintWriter out, InputStream inputStream, String userName) throws Exception {
        EngineManager engineManager = new EngineManager();
        //engineManager.setUploadedBy(userName);
        ListOfExceptionsDTO listOfExceptionsDTO=engineManager.loadFileByInputStream(inputStream,userName);
        if (listOfExceptionsDTO.getListOfException().size()==0) {
            MediatorForEngineManager mediatorsManager = ServletUtils.getMediatorForEngineManager(getServletContext());
            String battleName = engineManager.getBattleName().trim();
            if (mediatorsManager.isBattleExists(battleName)) {
                throw new Exception("The battle name already exists");
            }
            // med.updateGraphForTaskOnlyToNullAfterLoadXml(); //todo see how to change the way we use it, now we have map of mission its diffrent
            mediatorsManager.addEngineManger(battleName, engineManager);
            out.println(battleName.trim());
            out.flush();
        }
        else {
            List<String> errorsList=new ArrayList<>();
          /*  for (Exception exception : listOfExceptionsDTO.getListOfException()) {
                errorsList.add(exception.getMessage());
            }

            out.println(json);
            out.flush();*/
            return false;
        }
        UBoatAvailableContestsManager uBoatAvailableContestsManger = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
        uBoatAvailableContestsManger.addUBoatAvailableContest(engineManager,engineManager.getBattleName());
        return true;
    }
}