package component.alliesContest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.BruteForceResultAndVersion;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class AlliesBruteForceResultTableViewRefresher extends TimerTask {

    private final Consumer<BruteForceResultAndVersion> bruteForceTableViewConsumer;
    private final IntegerProperty bruteForceResultTableViewVersion;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;
    private String alliesTeamName;

    public AlliesBruteForceResultTableViewRefresher(String alliesTeamName, IntegerProperty bruteForceResultTableViewVersion, BooleanProperty shouldUpdate, Consumer<BruteForceResultAndVersion> bruteForceTableViewConsumer) {
        this.bruteForceTableViewConsumer = bruteForceTableViewConsumer;
        this.bruteForceResultTableViewVersion = bruteForceResultTableViewVersion;
        this.shouldUpdate = shouldUpdate;
        this.alliesTeamName = alliesTeamName;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

       // final int finalRequestNumber = ++requestNumber;

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ALLIES_BRUTE_FORCE_RESULTS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .addQueryParameter("AlliesBruteForceResultVersion", String.valueOf(bruteForceResultTableViewVersion.get()))
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    BruteForceResultAndVersion chatLinesWithVersion = GSON_INSTANCE.fromJson(rawBody, BruteForceResultAndVersion.class);
                    if(chatLinesWithVersion!=null) {
                       // BruteForceResultAndVersion chatLinesWithVersionCloned=chatLinesWithVersion.cloneBruteForceResultAndVersion();
                        bruteForceTableViewConsumer.accept(chatLinesWithVersion);
                    }
                } else {
                }
            }
        });

    }

}