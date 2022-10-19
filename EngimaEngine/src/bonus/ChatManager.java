package bonus;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private final List<SingleChatEntry> chatDataList = new ArrayList();

    public ChatManager() {
    }

    public synchronized void addChatString(String message, String username) {
        this.chatDataList.add(new SingleChatEntry(message, username));
    }

    public synchronized List<SingleChatEntry> getChatEntries(int fromIndex) {
        if (fromIndex < 0 || fromIndex > this.chatDataList.size()) {
            fromIndex = 0;
        }

        return this.chatDataList.subList(fromIndex, this.chatDataList.size());
    }

    public synchronized int getVersion() {
        return this.chatDataList.size();
    }
}