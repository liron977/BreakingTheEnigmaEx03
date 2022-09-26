package servlets;

import engineManager.EngineManager;
import engineManager.MediatorForEngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import machineDTO.ListOfExceptionsDTO;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import static java.lang.System.out;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadXmlFile extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part xmlFile = request.getParts().stream().findFirst().get();
       PrintWriter out = response.getWriter();
        try {
            checkIfXmlFileIsValidAndIfValidAddMachine(out,xmlFile.getInputStream(),xmlFile.getName());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }

    private void checkIfXmlFileIsValidAndIfValidAddMachine(PrintWriter out,InputStream inputStream, String userName) throws Exception {

        EngineManager engineManager = new EngineManager();
        engineManager.setUploadedBy(userName);
        ListOfExceptionsDTO listOfExceptionsDTO=engineManager.loadFileByInputStream(inputStream);
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
            for (Exception exception : listOfExceptionsDTO.getListOfException()) {
                out.println(exception.getMessage());
            }
        }
    }
}