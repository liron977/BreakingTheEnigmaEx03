package managers.bruteForce;

import bruteForce.BruteForceResultDTO;

import java.util.*;

public class AlliesBruteForceResultsMapManager {

    private final Map<String, List<BruteForceResultDTO>> bruteForceResultsManagerMap;

        public AlliesBruteForceResultsMapManager() {
            bruteForceResultsManagerMap = new HashMap<>();
        }
        public synchronized List<BruteForceResultDTO> getBruteForceListByAlliesTeamName(String alliesTeamName) {
            return bruteForceResultsManagerMap.get(alliesTeamName);
        }

        public synchronized Map<String,  List<BruteForceResultDTO>> bruteForceResultsManagerMap() {
            return Collections.unmodifiableMap(bruteForceResultsManagerMap);
        }
        public synchronized void addBruteForceResultIntoList(String alliesTeamName, BruteForceResultDTO bruteForceResultsDTO) throws InterruptedException {
            List<BruteForceResultDTO>  bruteForceResultsList = getBruteForceListByAlliesTeamName(alliesTeamName);
            boolean isAddedToBlockingQueue=false;
            if (bruteForceResultsList == null) {
                bruteForceResultsList = new ArrayList<>();
            }
            bruteForceResultsList.add(bruteForceResultsDTO);
            bruteForceResultsManagerMap.put(alliesTeamName, bruteForceResultsList);

        }
        public synchronized void clearBruteForceResults(String alliesTeamName){
            List<BruteForceResultDTO>  bruteForceResultsList = getBruteForceListByAlliesTeamName(alliesTeamName);
            bruteForceResultsList=new ArrayList<>();
            bruteForceResultsManagerMap.put(alliesTeamName, bruteForceResultsList);

        }
    public synchronized void addBruteForceResultsIntoList(String alliesTeamName, List<BruteForceResultDTO> bruteForceResultsDTOList) throws InterruptedException {
        List<BruteForceResultDTO>  bruteForceResultsList = getBruteForceListByAlliesTeamName(alliesTeamName);
        if (bruteForceResultsList == null) {
            bruteForceResultsList = new ArrayList<>();
        }
        for (BruteForceResultDTO bruteForceResult:bruteForceResultsDTOList) {
            bruteForceResultsList.add(bruteForceResult);
        }
        bruteForceResultsManagerMap.put(alliesTeamName, bruteForceResultsList);
    }
/*        public synchronized BruteForceResultDTO getBruteForceResultFromList(String alliesTeamName) throws InterruptedException {
            try {
                List<BruteForceResultDTO> missionsInfoList=bruteForceResultsManagerMap.get(alliesTeamName);
                if(missionsInfoList!=null) {
                    BruteForceResultDTO theBruteForceResult = missionsInfoBlockingQueue.poll();
                    return theBruteForceResult;
                }
                return null;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            return null;

        }*/

    public synchronized void addResultToBruteForceResultsManagerMap(String alliesTeamName) throws InterruptedException {
        List<BruteForceResultDTO> bruteForceResultDTOList=new ArrayList<>();
        bruteForceResultsManagerMap.put(alliesTeamName,bruteForceResultDTOList);
    }
    public synchronized void removeAliesTeam(String alliesTeamName) {
        bruteForceResultsManagerMap.remove(alliesTeamName);
    }
    public synchronized List<BruteForceResultDTO> getbruteForceResultDTOEntries(int fromIndex,String alliesTeamName){
        List<BruteForceResultDTO>  bruteForceResultsList = getBruteForceListByAlliesTeamName(alliesTeamName);
        if(bruteForceResultsList!=null) {
            if (fromIndex < 0 || fromIndex > bruteForceResultsList.size()) {
                fromIndex = 0;
            }
            return bruteForceResultsList.subList(fromIndex, bruteForceResultsList.size());
        }
        return null;
    }

    public synchronized int getVersion(String alliesTeamName) {
        List<BruteForceResultDTO>  bruteForceResultsList = getBruteForceListByAlliesTeamName(alliesTeamName);
       if(bruteForceResultsList!=null) {
           return bruteForceResultsList.size();
       }
       return 0;
    }


}