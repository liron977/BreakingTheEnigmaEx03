package constants;

import com.google.gson.Gson;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    public final static String CONTEXT_PATH = "/app";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/LoginServlet";
    public static final String UPLOAD_XML_FILE = "http://localhost:8080/app/UploadXmlFile";
    public static final String MACHINE_DETAILS = "http://localhost:8080/app/MachineDetailsServlet";
    public static final String SET_CODE_CONFIGURATION = "http://localhost:8080/app/setConfigurationsServlet";
    public static final String DISPLAY_CURRENT_CODE_CONFIGURATION = "http://localhost:8080/app/CurrentCodeServlet";
    public static final String GET_DICTIONARY= "http://localhost:8080/app/GetDictionaryServlet";
    public static final String ENCRYPT= "http://localhost:8080/app/EncryptServlet";
    public static final String AGENTS_INFO_TABLE_VIEW= "http://localhost:8080/app/AgentsInfoServlet";
    public static final String AGENTS_MISSIONS_STATUS= "http://localhost:8080/app/AgentMissionsStatusServlet";
    public static final String ALLIES_TEAM_NAMES= "http://localhost:8080/app/AlliesTeamNamesServlet";
    public static final String UBOATS_CONTESTS_INFO= "http://localhost:8080/app/UBoatContestsServlet";
    public static final String UBOATS_CONTESTS_SETTINGS= "http://localhost:8080/app/UBoatContestSettingsServlet";
    public static final String REGISTER_ALLIES_TO_CONTEST= "http://localhost:8080/app/RegisterAlliesToContestServlet";
    public static final String ALLIES_LOGIN= "http://localhost:8080/app/AlliesLoginServlet";
    public static final String DISPLAY_ALLIES_TEAMS_INFO= "http://localhost:8080/app/AlliesInfoServlet";
    public static final String ALLIES_CREATE_MISSIONS= "http://localhost:8080/app/DMCreateMissionsServlet";
    public static final String CONTEST_INFO = "http://localhost:8080/app/ContestInfoServlet";

    public static final String AGENT_GET_MISSIONS= "http://localhost:8080/app/AgentGetMissionsServlet";
    public static final String GET_ENGINE_INPUTSTREAM= "http://localhost:8080/app/GetXmlInputStreamServlet";
    public static final String GET_MACHINE_INFO="http://localhost:8080/app/GetMachineInfoServlet";
    public static final String BRUTE_FORCE_RESULTS="http://localhost:8080/app/BruteForceResultsServlet";
    public static final String ALLIES_BRUTE_FORCE_RESULTS="http://localhost:8080/app/AlliesGetBruteForceResultServlet";
    public static final String UBOAT_BRUTE_FORCE_RESULTS="http://localhost:8080/app/UboatGetBruteForceResultServlet";
    public static final String CONTEST_STATUS ="http://localhost:8080/app/ContestStatusServlet";
    public static final String DM_GET_AMOUNT_OF_CREATED_MISSIONS="http://localhost:8080/app/DMGetAmountOfCreatedMissionServlet";
    public static final String CLEAR_CONTEST_UBOAT="http://localhost:8080/app/ClearContestUboatServlet";
    public static final String UPDATE_CONTEST_STATUS_AGENT="http://localhost:8080/app/UpdateContestStatusAgentServlet";
    public static final String GET_MAXIMUM_AMOUNT_OF_MISSIONS="http://localhost:8080/app/GetMaximumAmountOfMissionsServlet";
    public static final String START_NEW_CONTEST_STATUS="http://localhost:8080/app/StartNewContestStatusServlet";
    public static final String UBOAT_LOGOUT="http://localhost:8080/app/UboatLogoutServlet";
    public static final String RESET_CODE_CONFIGURATION="http://localhost:8080/app/ResetCodeConfigurationServlet";
    public static final String CHAT_LINES_LIST="http://localhost:8080/app/ChatServlet";
    public static final String SEND_CHAT_LINE="http://localhost:8080/app/SendChatServlet";
    public static final String GET_NOT_AVAILABLE_AGENTS="http://localhost:8080/app/GetNotAvailableAgentsServlet";
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";


    public static final Gson GSON_INSTANCE = new Gson();
    public final static int REFRESH_RATE = 500;

}