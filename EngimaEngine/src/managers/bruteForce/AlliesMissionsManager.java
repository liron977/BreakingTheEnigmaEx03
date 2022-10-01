package managers.bruteForce;

import bruteForce.AgentInfoDTO;
import bruteForceLogic.TheMissionInfo;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AlliesMissionsManager {

    private final Map<String, BlockingQueue<TheMissionInfo>> alliesMissionsManagerMap;
    public AlliesMissionsManager() {
        alliesMissionsManagerMap = new HashMap<>();
    }
    public synchronized  BlockingQueue<TheMissionInfo> getMissionsBlockingQueueByAlliesTeamName(String alliesTeamName) {
       return alliesMissionsManagerMap.get(alliesTeamName);
    }

    public synchronized Map<String,  BlockingQueue<TheMissionInfo>> alliesMissionsManagerMap() {
        return Collections.unmodifiableMap(alliesMissionsManagerMap);
    }
    public synchronized void addMissionInfoIntoMissionBlockingQueue (String alliesTeamName, TheMissionInfo theMissionInfo) throws InterruptedException {
        BlockingQueue<TheMissionInfo>  missionInfoBlockingQueue = getMissionsBlockingQueueByAlliesTeamName(alliesTeamName);
        if (missionInfoBlockingQueue == null) {
            missionInfoBlockingQueue = new LinkedBlockingQueue<TheMissionInfo>(1000);
        }
        missionInfoBlockingQueue.put(theMissionInfo);
        alliesMissionsManagerMap.put(alliesTeamName, missionInfoBlockingQueue);
        }
    public synchronized TheMissionInfo getMissionFromBlockingQueue(String alliesTeamName) throws InterruptedException {
        BlockingQueue<TheMissionInfo> missionsInfoBlockingQueue=alliesMissionsManagerMap.get(alliesTeamName);
        TheMissionInfo theMissionInfo=missionsInfoBlockingQueue.poll();
        return theMissionInfo;
    }

    public synchronized void removeAliesTeam(String alliesTeamName) {
        alliesMissionsManagerMap.remove(alliesTeamName);
    }
}

