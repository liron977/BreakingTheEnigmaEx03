package component.uBoatContestTab;

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

public class UboatBruteForceResultTableViewRefresher extends TimerTask {

        private final Consumer<BruteForceResultAndVersion> bruteForceTableViewConsumer;
        private final IntegerProperty chatVersion;
        private final BooleanProperty shouldUpdate;
        private int requestNumber;
        private String battleFieldName;

    public UboatBruteForceResultTableViewRefresher(String battleFieldName, IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<BruteForceResultAndVersion> bruteForceTableViewConsumer) {
            this.bruteForceTableViewConsumer = bruteForceTableViewConsumer;
            this.chatVersion = chatVersion;
            this.shouldUpdate = shouldUpdate;
            this.battleFieldName=battleFieldName;
            requestNumber = 0;
        }

        @Override
        public void run() {

            if (!shouldUpdate.get()) {
                return;
            }

            final int finalRequestNumber = ++requestNumber;

            //noinspection ConstantConditions
            String finalUrl = HttpUrl
                    .parse(Constants.UBOAT_BRUTE_FORCE_RESULTS)
                    .newBuilder()
                    .addQueryParameter("battlefield",battleFieldName)
                    .addQueryParameter("UboatBruteForceResultVersion", String.valueOf(chatVersion.get()))
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
                            //BruteForceResultAndVersion  chatLinesWithVersionCloned=chatLinesWithVersion.cloneBruteForceResultAndVersion();
                            bruteForceTableViewConsumer.accept(chatLinesWithVersion);
                        }
                    } else {
                    }
                }
            });

        }

    }