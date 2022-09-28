package servlets;

import bruteForce.AgentInfoDTO;
import com.google.gson.Gson;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AgentsManager;
import managers.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AlliesTeamServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            UserManager userInfoManager = ServletUtils.getUserManager(getServletContext());
            Map<String, String> userInfoManagerMap = userInfoManager.getUsers();

            List<String> alliesTeamsNamesList=userInfoManager.getUserNamesByValue(userInfoManagerMap,"allies");
            if (alliesTeamsNamesList != null) {

                Gson gson = new Gson();
                String json = gson.toJson(alliesTeamsNamesList);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}