package managers.bruteForce;

import bruteForce.BruteForceResultDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BruteForceResultsManager{

    private final Map<String, BlockingQueue<BruteForceResultDTO>> bruteForceResultsManagerMap;

        public BruteForceResultsManager() {
            bruteForceResultsManagerMap = new HashMap<>();
        }
        public synchronized  BlockingQueue<BruteForceResultDTO> getResultsBlockingQueueByAlliesTeamName(String alliesTeamName) {
            return bruteForceResultsManagerMap.get(alliesTeamName);
        }

        public synchronized Map<String,  BlockingQueue<BruteForceResultDTO>> bruteForceResultsManagerMap() {
            return Collections.unmodifiableMap(bruteForceResultsManagerMap);
        }
        public synchronized void addBruteForceResultIntoBlockingQueue (String alliesTeamName, BruteForceResultDTO bruteForceResultsDTO) throws InterruptedException {
            BlockingQueue<BruteForceResultDTO>  bruteForceResultsBlockingQueue = getResultsBlockingQueueByAlliesTeamName(alliesTeamName);
            boolean isAddedToBlockingQueue=false;
            if (bruteForceResultsBlockingQueue == null) {
                bruteForceResultsBlockingQueue = new LinkedBlockingQueue<BruteForceResultDTO>();
            }
           bruteForceResultsBlockingQueue.put(bruteForceResultsDTO);
            bruteForceResultsManagerMap.put(alliesTeamName, bruteForceResultsBlockingQueue);

        }
    public synchronized void addBruteForceResultsIntoBlockingQueue (String alliesTeamName, List<BruteForceResultDTO> bruteForceResultsDTOList) throws InterruptedException {
        BlockingQueue<BruteForceResultDTO>  bruteForceResultsBlockingQueue = getResultsBlockingQueueByAlliesTeamName(alliesTeamName);
        if (bruteForceResultsBlockingQueue == null) {
            bruteForceResultsBlockingQueue = new LinkedBlockingQueue<BruteForceResultDTO>();
        }
        for (BruteForceResultDTO bruteForceResult:bruteForceResultsDTOList) {
            bruteForceResultsBlockingQueue.put(bruteForceResult);
        }
        bruteForceResultsManagerMap.put(alliesTeamName, bruteForceResultsBlockingQueue);
    }
        public synchronized BruteForceResultDTO getBruteForceResultFromBlockingQueue(String alliesTeamName) throws InterruptedException {
            try {
                BlockingQueue<BruteForceResultDTO> missionsInfoBlockingQueue=bruteForceResultsManagerMap.get(alliesTeamName);
                if(missionsInfoBlockingQueue!=null) {
                    BruteForceResultDTO theBruteForceResult = missionsInfoBlockingQueue.poll();
                    return theBruteForceResult;
                }
                return null;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            return null;

        }

    public synchronized void addResultToBruteForceResultsManagerMap(String alliesTeamName) throws InterruptedException {
        bruteForceResultsManagerMap.put(alliesTeamName,new LinkedBlockingQueue<BruteForceResultDTO>() );

    }


        public synchronized void removeAliesTeam(String alliesTeamName) {
            bruteForceResultsManagerMap.remove(alliesTeamName);
        }
}