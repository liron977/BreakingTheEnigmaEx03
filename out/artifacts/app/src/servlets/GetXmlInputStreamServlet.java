package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import machineEngine.EngineManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import utils.ServletUtils;

import java.io.*;

public class GetXmlInputStreamServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            String theAlliesTeamName = request.getParameter(ParametersConstants.ALLIES_TEAM_NAME);
            UBoatAvailableContestsManager uBoatAvailableContestsManager = ServletUtils.getUBoatAvailableContestsManager(getServletContext());
            EngineManager engineManager = uBoatAvailableContestsManager.getEngineMangerByAlliesTeamName(theAlliesTeamName);
            InputStream inputStream=engineManager.getInputStream();
            Gson gson = new Gson();

               /*  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos);
                 oos.writeObject(engineManager);
                 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bais);
                 out.println(ois.read());
                 out.flush();*/
                 try {
                     String json = gson.toJson(inputStream);
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