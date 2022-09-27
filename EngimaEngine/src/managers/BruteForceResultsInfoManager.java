package managers;

import bruteForce.DecryptionInfoDTO;
import engineManager.EngineManager;

import java.util.*;

public class BruteForceResultsInfoManager {
    private final Map<String, List<DecryptionInfoDTO>> bruteForceResultsMap = new HashMap<>();

    public BruteForceResultsInfoManager() {
    }

    public synchronized void addBruteForceResult(String battleName, List<DecryptionInfoDTO> newDecryptionInfoDTO) {
       List<DecryptionInfoDTO> bruteForceResultsList= bruteForceResultsMap.get(battleName);
       if(bruteForceResultsList==null){
           bruteForceResultsList=new ArrayList<>();
       }
       bruteForceResultsList.addAll(newDecryptionInfoDTO);

        bruteForceResultsMap.put(battleName,bruteForceResultsList);
    }

    public synchronized Map<String, List<DecryptionInfoDTO>> getBruteForceResultsMap() {
        return Collections.unmodifiableMap(bruteForceResultsMap);
    }



   /* public boolean isBattleExists(String battleName) {
        return bruteForceResultsMap.containsKey(battleName);
    }*/

}