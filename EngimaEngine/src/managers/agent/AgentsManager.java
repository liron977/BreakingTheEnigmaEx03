package managers.agent;

import bruteForce.AgentInfoDTO;

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
    public List<AgentInfoDTO> getAgentsByAlliesTeamName(String alliesTeamName){
        return agentManagerMap.get(alliesTeamName);
    }

    public synchronized Map<String, List<AgentInfoDTO>> getAgentManagerMap() {
        return Collections.unmodifiableMap(agentManagerMap);
    }
    public synchronized boolean updateAgentMissionStatus(AgentInfoDTO agentInfoDTO){
        List<AgentInfoDTO> agents= getAgentsByAlliesTeamName(agentInfoDTO.getAlliesTeamName());
        for (AgentInfoDTO agent:agents) {
            if(agent.getAgentName().equals(agentInfoDTO.getAgentName())){
                agent.setAmountOfMissionsToExecute(agentInfoDTO.getAmountOfMissionsToExecute());
                agent.setAmountOfCandidatesStrings(agentInfoDTO.getAmountOfCandidatesStrings());
                agent.setAmountOfReceivedMissions(agentInfoDTO.getAmountOfReceivedMissions());
                System.out.println("***************update******************");
                System.out.println("amountOfMissionsToExecute "+agent.getAmountOfMissionsToExecute());
                System.out.println("amountOfCandidatesStrings "+agent.getAmountOfCandidatesStrings());
                System.out.println("amountOfReceivedMissions "+agent.getAmountOfReceivedMissions());

                return true;
            }

        }
        return false;
    }
    public synchronized void initValues(String alliesTeamName){
        List<AgentInfoDTO> agents= getAgentsByAlliesTeamName(alliesTeamName);
        if(agents!=null) {
            for (AgentInfoDTO agent : agents) {
                if(agent!=null) {
                    agent.initValues();
                }
            }
        }
    }
}