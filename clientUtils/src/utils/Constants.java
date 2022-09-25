package utils;

public class Constants {
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";// http://localhost:8080
    public final static String CONTEXT_PATH = "/app";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;// http://localhost:8080/Application_Web_exploded
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/LoginServlet";// http://localhost:8080/Application_Web_exploded/LoginServlet

}
