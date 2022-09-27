package utils;

import bruteForce.AgentInfoDTO;
import managers.AgentsManager;
import managers.BruteForceResultsInfoManager;
import managers.users.UserManager;
import managers.MediatorForEngineManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import servlets.BruteForceResultsServlet;

import static constants.ParametersConstants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String MEDIATORS_MANAGER_ATTRIBUTE_NAME = "MediatorForEngineManager";
    private static final String BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME = "bruteForceResultsInfoManager";
    private static final String AGENTS_INFO_MANAGER_ATTRIBUTE_NAME = "agentsInfoManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object bruteForceResultsInfoLock = new Object();
    private static final Object mediatorsManagerLock = new Object();
    private static final Object agentManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static AgentsManager getAgentManager(ServletContext servletContext) {

        synchronized (agentManagerLock) {
            if (servletContext.getAttribute(AGENTS_INFO_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(AGENTS_INFO_MANAGER_ATTRIBUTE_NAME, new AgentsManager());
            }
        }
        return (AgentsManager) servletContext.getAttribute(AGENTS_INFO_MANAGER_ATTRIBUTE_NAME);
    }

    public static MediatorForEngineManager getMediatorForEngineManager(ServletContext servletContext) {
        synchronized (mediatorsManagerLock) {
            if (servletContext.getAttribute(MEDIATORS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(MEDIATORS_MANAGER_ATTRIBUTE_NAME, new MediatorForEngineManager());
            }
        }
        return (MediatorForEngineManager) servletContext.getAttribute(MEDIATORS_MANAGER_ATTRIBUTE_NAME);
    }

    public static BruteForceResultsInfoManager getBruteForceResultsInfoManager(ServletContext servletContext) {
        synchronized (bruteForceResultsInfoLock) {
            if (servletContext.getAttribute(BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME, new MediatorForEngineManager());
            }
        }
        return (BruteForceResultsInfoManager) servletContext.getAttribute(MEDIATORS_MANAGER_ATTRIBUTE_NAME);
    }

/*    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }*/

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}