package managers.agent;

import bruteForce.ContestStatusInfoDTO;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private final Map<String, ContestStatusInfoDTO> statusManagersMap = new HashMap<>();
    public synchronized ContestStatusInfoDTO getContestStatusInfoDTOByBattlefield(String battlefield) {
        return statusManagersMap.get(battlefield);
    }
    public synchronized void addContestStatusInfoDTO(String battlefield, ContestStatusInfoDTO contestStatusInfoDTO) throws InterruptedException {

        statusManagersMap.put(battlefield, contestStatusInfoDTO);

    }
    public synchronized ContestStatusInfoDTO getContestStatusInfoDTOByAlliesName(String alliesTeamName) {
        if (statusManagersMap.size() != 0) {


            for (ContestStatusInfoDTO contestStatusInfoDTO : statusManagersMap.values()) {
                if (contestStatusInfoDTO.isAlliesExist(alliesTeamName)) {
                    return contestStatusInfoDTO;
                }
            }
        }
        return null;
    }
    public synchronized void removeUboat(String uboatName) {
        statusManagersMap.remove(uboatName);
    }

    public synchronized void deleteHistory(){
        Map<String, ContestStatusInfoDTO> statusManagersMap = new HashMap<>();
    }
}