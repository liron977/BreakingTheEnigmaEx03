package utils;

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
    public static final String ALLIES_TEAM_NAMES= "http://localhost:8080/app/AlliesTeamNamesServlet";
    public static final String UBOATS_CONTESTS_INFO= "http://localhost:8080/app/UBoatContestsServlet";
    public static final String UBOATS_CONTESTS_SETTINGS= "http://localhost:8080/app/UBoatContestSettingsServlet";
    public static final String REGISTER_ALLIES_TO_CONTEST= "http://localhost:8080/app/RegisterAlliesToContestServlet";
    public static final String ALLIES_LOGIN= "http://localhost:8080/app/AlliesLoginServlet";
    public static final String DISPLAY_ALLIES_TEAMS_INFO= "http://localhost:8080/app/AlliesInfoServlet";
    public static final String ALLIES_CREATE_MISSIONS= "http://localhost:8080/app/DMCreateMissionsServlet";
    public static final String CONTEST_INFO_FOR_AGENT= "http://localhost:8080/app/ContestInfoForAgentServlet";

    public static final String AGENT_GET_MISSIONS= "http://localhost:8080/app/AgentGetMissionsServlet";
    public static final String GET_ENGINE_MANAGER= "http://localhost:8080/app/GetEngineManagerServlet";
    public static final Gson GSON_INSTANCE = new Gson();
    public final static int REFRESH_RATE = 500;

}