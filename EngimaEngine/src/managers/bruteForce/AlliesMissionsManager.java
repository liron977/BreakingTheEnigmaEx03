package managers.bruteForce;



import bruteForce.TheMissionInfoDTO;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AlliesMissionsManager {

    private final Map<String, BlockingQueue<TheMissionInfoDTO>> alliesMissionsManagerMap;
    public AlliesMissionsManager() {
        alliesMissionsManagerMap = new HashMap<>();
    }
    public synchronized  BlockingQueue<TheMissionInfoDTO> getMissionsBlockingQueueByAlliesTeamName(String alliesTeamName) {
       return alliesMissionsManagerMap.get(alliesTeamName);
    }

    public synchronized Map<String,  BlockingQueue<TheMissionInfoDTO>> alliesMissionsManagerMap() {
        return Collections.unmodifiableMap(alliesMissionsManagerMap);
    }
    public synchronized void addMissionInfoIntoMissionBlockingQueue (String alliesTeamName, TheMissionInfoDTO theMissionInfo) throws InterruptedException {
        BlockingQueue<TheMissionInfoDTO>  missionInfoBlockingQueue = getMissionsBlockingQueueByAlliesTeamName(alliesTeamName);
        if (missionInfoBlockingQueue == null) {
            missionInfoBlockingQueue = new LinkedBlockingQueue<TheMissionInfoDTO>(1000);
        }
        missionInfoBlockingQueue.put(theMissionInfo);
        alliesMissionsManagerMap.put(alliesTeamName, missionInfoBlockingQueue);
        }
    public synchronized TheMissionInfoDTO getMissionFromBlockingQueue(String alliesTeamName) throws InterruptedException {
      try {
          BlockingQueue<TheMissionInfoDTO> missionsInfoBlockingQueue=alliesMissionsManagerMap.get(alliesTeamName);
          if(missionsInfoBlockingQueue!=null) {
              TheMissionInfoDTO theMissionInfo = missionsInfoBlockingQueue.poll();
              return theMissionInfo;
          }
          return null;
      }
      catch (Exception e){
          System.out.println(e.getMessage());
      }
      return null;

    }
    public synchronized void addAlliesToAlliesMissionsManagerMap(String alliesTeamName) throws InterruptedException {
        alliesMissionsManagerMap.put(alliesTeamName,new LinkedBlockingQueue<TheMissionInfoDTO>(1000) );

    }


    public synchronized void removeAliesTeam(String alliesTeamName) {
        alliesMissionsManagerMap.remove(alliesTeamName);
    }
}