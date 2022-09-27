package utils;

import com.google.gson.Gson;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";// http://localhost:8080
    public final static String CONTEXT_PATH = "/app";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;// http://localhost:8080/Application_Web_exploded
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/LoginServlet";// http://localhost:8080/Application_Web_exploded/LoginServlet
    public static final String UPLOAD_XML_FILE = "http://localhost:8080/app/UploadXmlFile";
    public static final String MACHINE_DETAILS = "http://localhost:8080/app/MachineDetailsServlet";
    public static final String SET_CODE_CONFIGURATION = "http://localhost:8080/app/setConfigurationsServlet";
    public static final String DISPLAY_CURRENT_CODE_CONFIGURATION = "http://localhost:8080/app/CurrentCodeServlet";
    public static final Gson GSON_INSTANCE = new Gson();

}