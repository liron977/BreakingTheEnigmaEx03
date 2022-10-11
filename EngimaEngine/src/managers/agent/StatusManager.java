package managers.agent;

import bruteForce.ContestStatusInfoDTO;
import machineEngine.EngineManager;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private final Map<String, ContestStatusInfoDTO> statusManagersMap = new HashMap<>();
    public synchronized ContestStatusInfoDTO getContestStatusInfoDTOByAlliesTeamName(String battlefield) {
        return statusManagersMap.get(battlefield);
    }
    public synchronized void addContestStatusInfoDTO(String battlefield, ContestStatusInfoDTO contestStatusInfoDTO) throws InterruptedException {

        statusManagersMap.put(battlefield, contestStatusInfoDTO);

    }
    public synchronized ContestStatusInfoDTO getContestStatusInfoDTOByAlliesName(String alliesTeamName){

            for (ContestStatusInfoDTO contestStatusInfoDTO : statusManagersMap.values()){
                if(contestStatusInfoDTO.isAlliesExist(alliesTeamName)){
                    return contestStatusInfoDTO;
                }
            }
            return null;

    }
}