package servlets;

import com.google.gson.Gson;
import constants.ParametersConstants;
import engine.theEnigmaEngine.Allies;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.uBoatEngine.AlliesManager;
import managers.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

public class AlliesLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);//check if the user exists- returns null if not
        String roleFromSession = request.getParameter(ParametersConstants.ROLE);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());// return the user manager (one instance for all)

        if (usernameFromSession == null) { //user is not logged in yet
            String usernameFromParameter = request.getParameter(ParametersConstants.USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    } else {
                        //add the new user to the users list
                        response.setContentType("text/plain;charset=UTF-8");
                        Gson gson = new Gson();
                        Allies allies = new Allies(usernameFromParameter);
                        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
                        if (alliesManager != null) {
                            alliesManager.addAllies(allies, usernameFromParameter);
                            userManager.addUser(usernameFromParameter, roleFromSession);
                            request.getSession(true).setAttribute(ParametersConstants.USERNAME, usernameFromParameter);
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    }
                }
            }
        } else {
            //user is already logged in



                response.setStatus(HttpServletResponse.SC_OK);

            }
        }
    }