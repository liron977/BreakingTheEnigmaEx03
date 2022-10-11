package utils.http;

import okhttp3.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpClientUtil {

    private static final SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private static final OkHttpClient HTTP_CLIENT;

    public HttpClientUtil() {
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static OkHttpClient getOkHttpClient() {
        return HTTP_CLIENT;
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = (new Request.Builder()).url(finalUrl).build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, RequestBody body, Callback callback) {
        Request request = (new Request.Builder()).url(finalUrl).method("POST", body).build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    static {
        HTTP_CLIENT = (new OkHttpClient.Builder()).cookieJar(simpleCookieManager).followRedirects(false).build();
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

    }
}