package component.uBoatContestTab;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.GSON_INSTANCE;

public class BruteForceResultTableViewRefresher extends TimerTask {

        private final Consumer<BruteForceResultAndVersion> chatlinesConsumer;
        private final IntegerProperty chatVersion;
        private final BooleanProperty shouldUpdate;
        private int requestNumber;

    public BruteForceResultTableViewRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<BruteForceResultAndVersion> chatlinesConsumer) {
            this.chatlinesConsumer = chatlinesConsumer;
            this.chatVersion = chatVersion;
            this.shouldUpdate = shouldUpdate;
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
                    .parse(Constants.BRUTE_FORCE_RESULTS)
                    .newBuilder()
                    .addQueryParameter("bruteForceResultVersion", String.valueOf(chatVersion.get()))
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
                        chatlinesConsumer.accept(chatLinesWithVersion);
                    } else {
                    }
                }
            });

        }

    }