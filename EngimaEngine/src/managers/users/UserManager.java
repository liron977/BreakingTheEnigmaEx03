package managers.users;

import java.util.*;

public class UserManager {
    private final Map<String,String> usersMap;
    public UserManager() {
        usersMap = new HashMap<>();
    }
    public synchronized void addUser(String username, String roleFromSession) {
        usersMap.put(username,roleFromSession);
    }
    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }
    public synchronized Map<String,String> getUsers() {
        return Collections.unmodifiableMap(usersMap);
    }
    public boolean isUserExists(String username) {return usersMap.containsKey(username);}
    public synchronized List<String> getUserNamesByValue(Map<String, String> map,String role) {
        List<String> alliesTeamsNames=new ArrayList<>();
        for (String key : getKeys(map, role)) {
            alliesTeamsNames.add(key);
        }
        return  alliesTeamsNames;
    }

    private synchronized Set<String> getKeys(Map<String, String> map, String value) {

        Set<String> result = new HashSet<>();
        if (map.containsValue(value)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (Objects.equals(entry.getValue(), value)) {
                    result.add(entry.getKey());
                }
            }
        }
        return result;

    }

}