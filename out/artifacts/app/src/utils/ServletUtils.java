package utils;

import managers.agent.AgentsManager;
import managers.bruteForce.AlliesMissionsManager;
import managers.bruteForce.UboatBruteForceResultsMapManager;
import managers.bruteForce.AlliesBruteForceResultsMapManager;
import managers.uBoatEngine.AlliesManager;
import managers.uBoatEngine.UBoatAvailableContestsManager;
import managers.users.UserManager;
import managers.uBoatEngine.MediatorForEngineManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static constants.ParametersConstants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String MEDIATORS_MANAGER_ATTRIBUTE_NAME = "MediatorForEngineManager";
    //private static final String BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME = "bruteForceResultsInfoManager";
    private static final String AGENTS_INFO_MANAGER_ATTRIBUTE_NAME = "agentsInfoManager";
    private static final String ALLIES_MANAGER_ATTRIBUTE_NAME = "alliesManager";
    private static final String UBOAT_AVAILABLE_CONTESTS_ATTRIBUTE_NAME = "uBoatAvailableContests";
    private static final String ALLIES_MISSIONS_MANAGER_ATTRIBUTE_NAME = "alliesMissionsManager";
    private static final String BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME = "bruteForceResultsManager";
    private static final String ALLIES_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME = "bruteForceResultsListManager";
    private static final String UBOAT_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME = "bruteForceResultsListManagerForUboat";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object chatManagerLock = new Object();
    private static final Object bruteForceResultsInfoLock = new Object();
    private static final Object mediatorsManagerLock = new Object();
    private static final Object agentManagerLock = new Object();
    private static final Object uBoatAvailableContestsLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static final Object alliesMissionsManagerLock = new Object();
    private static final Object uboatBruteForceResultManagerLock = new Object();
    private static final Object alliesBruteForceResultManagerLock = new Object();
    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static AlliesManager getAlliesManager(ServletContext servletContext) {

        synchronized (alliesManagerLock) {
            if (servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME, new AlliesManager());
            }
        }
        return (AlliesManager) servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME);
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

    public static AlliesBruteForceResultsMapManager getBruteForceResultsManager(ServletContext servletContext) {
        synchronized (bruteForceResultsInfoLock) {
            if (servletContext.getAttribute(BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME, new AlliesBruteForceResultsMapManager());
            }
        }
        return (AlliesBruteForceResultsMapManager) servletContext.getAttribute(BRUTE_FORCE_RESULTS_MANAGER_ATTRIBUTE_NAME);
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
    public static UBoatAvailableContestsManager getUBoatAvailableContestsManager(ServletContext servletContext) {
        synchronized (uBoatAvailableContestsLock) {
            if (servletContext.getAttribute(UBOAT_AVAILABLE_CONTESTS_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(UBOAT_AVAILABLE_CONTESTS_ATTRIBUTE_NAME, new UBoatAvailableContestsManager());
            }
        }
        return (UBoatAvailableContestsManager) servletContext.getAttribute(UBOAT_AVAILABLE_CONTESTS_ATTRIBUTE_NAME);
    }
    public static AlliesMissionsManager getAlliesMissionsManager(ServletContext servletContext) {
        synchronized (uBoatAvailableContestsLock) {
            if (servletContext.getAttribute(ALLIES_MISSIONS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_MISSIONS_MANAGER_ATTRIBUTE_NAME, new AlliesMissionsManager());
            }
        }
        return (AlliesMissionsManager) servletContext.getAttribute(ALLIES_MISSIONS_MANAGER_ATTRIBUTE_NAME);
    }
    public static AlliesBruteForceResultsMapManager getAlliesBruteForceResultsListManager(ServletContext servletContext) {
        synchronized (alliesBruteForceResultManagerLock) {
            if (servletContext.getAttribute(ALLIES_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME, new AlliesBruteForceResultsMapManager());
            }
        }
        return (AlliesBruteForceResultsMapManager) servletContext.getAttribute(ALLIES_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME);
    }
    public static UboatBruteForceResultsMapManager getUboatBruteForceResultsMapManager(ServletContext servletContext) {
        synchronized (uboatBruteForceResultManagerLock) {
            if (servletContext.getAttribute(UBOAT_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(UBOAT_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME, new UboatBruteForceResultsMapManager());
            }
        }
        return (UboatBruteForceResultsMapManager) servletContext.getAttribute(UBOAT_BRUTE_FORCE_RESULTS_LIST_MANAGER_ATTRIBUTE_NAME);
    }
}