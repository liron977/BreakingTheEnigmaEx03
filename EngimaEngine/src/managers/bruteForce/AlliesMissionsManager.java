package managers.bruteForce;



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
      try {
          BlockingQueue<TheMissionInfo> missionsInfoBlockingQueue=alliesMissionsManagerMap.get(alliesTeamName);
          if(missionsInfoBlockingQueue!=null) {
              TheMissionInfo theMissionInfo = missionsInfoBlockingQueue.poll();
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
        alliesMissionsManagerMap.put(alliesTeamName,new LinkedBlockingQueue<TheMissionInfo>(1000) );

    }


    public synchronized void removeAliesTeam(String alliesTeamName) {
        alliesMissionsManagerMap.remove(alliesTeamName);
    }
}