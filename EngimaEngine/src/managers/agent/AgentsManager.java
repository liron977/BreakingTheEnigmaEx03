package managers.agent;

import bruteForce.AgentInfoDTO;
import bruteForce.DecryptionInfoDTO;

import java.util.*;

public class AgentsManager {

    private final Map<String, List<AgentInfoDTO>> agentManagerMap = new HashMap<>();
    public AgentsManager() {
    }
    public synchronized void addAgentInfoDTOList(String teamName, AgentInfoDTO newAgentInfoDTO) {
        List<AgentInfoDTO> bruteForceResultsList = agentManagerMap.get(teamName);
        if (bruteForceResultsList == null) {
            bruteForceResultsList = new ArrayList<>();
        }
        if (!bruteForceResultsList.contains(newAgentInfoDTO.getAgentName())) {
            bruteForceResultsList.add(newAgentInfoDTO);
            agentManagerMap.put(teamName, bruteForceResultsList);
        }
    }

    public synchronized Map<String, List<AgentInfoDTO>> getAgentManagerMap() {
        return Collections.unmodifiableMap(agentManagerMap);
    }
}