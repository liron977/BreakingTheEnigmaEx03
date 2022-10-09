package managers.bruteForce;

import bruteForce.BruteForceResultDTO;

import java.util.*;

public class UboatBruteForceResultsMapManager {

    private final Map<String, List<BruteForceResultDTO>> bruteForceResultsManagerMap;

    public UboatBruteForceResultsMapManager() {
        bruteForceResultsManagerMap = new HashMap<>();
    }

    public synchronized List<BruteForceResultDTO> getBruteForceListByUboatName(String uboatName) {
        return bruteForceResultsManagerMap.get(uboatName);
    }

    public synchronized Map<String, List<BruteForceResultDTO>> bruteForceResultsManagerMap() {
        return Collections.unmodifiableMap(bruteForceResultsManagerMap);
    }

    public synchronized void addBruteForceResultIntoList(String uboatName, BruteForceResultDTO bruteForceResultsDTO) throws InterruptedException {
        List<BruteForceResultDTO> bruteForceResultsList = getBruteForceListByUboatName(uboatName);
        boolean isAddedToBlockingQueue = false;
        if (bruteForceResultsList == null) {
            bruteForceResultsList = new ArrayList<>();
        }
        bruteForceResultsList.add(bruteForceResultsDTO);
        bruteForceResultsManagerMap.put(uboatName, bruteForceResultsList);

    }

    public synchronized void addBruteForceResultsIntoList(String uboatName, List<BruteForceResultDTO> bruteForceResultsDTOList) throws InterruptedException {
        List<BruteForceResultDTO> bruteForceResultsList = getBruteForceListByUboatName(uboatName);
        if (bruteForceResultsList == null) {
            bruteForceResultsList = new ArrayList<>();
        }
        for (BruteForceResultDTO bruteForceResult : bruteForceResultsDTOList) {
            bruteForceResultsList.add(bruteForceResult);
        }
        bruteForceResultsManagerMap.put(uboatName, bruteForceResultsList);
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

    public synchronized void addResultToBruteForceResultsManagerMap(String uboatName) throws InterruptedException {
        List<BruteForceResultDTO> bruteForceResultDTOList = new ArrayList<>();
        bruteForceResultsManagerMap.put(uboatName, bruteForceResultDTOList);
    }

    public synchronized void removeUboat(String uboatName) {
        bruteForceResultsManagerMap.remove(uboatName);
    }

    public synchronized List<BruteForceResultDTO> getbruteForceResultDTOEntries(int fromIndex, String uboatName) {
        List<BruteForceResultDTO> bruteForceResultsList = getBruteForceListByUboatName(uboatName);
        if(bruteForceResultsList!=null) {
            if (fromIndex < 0 || fromIndex > bruteForceResultsList.size()) {
                fromIndex = 0;
            }
            return bruteForceResultsList.subList(fromIndex, bruteForceResultsList.size());
        }
        return null;
    }

    public synchronized int  getVersion(String uboatName) {
        List<BruteForceResultDTO> bruteForceResultsList = getBruteForceListByUboatName(uboatName);
        if (bruteForceResultsList != null) {
            return bruteForceResultsList.size();
        }
        return 0;
    }

}