package decryptionManager;

import bruteForceLogic.AgentMission;
import engine.theEnigmaEngine.Allies;
import engine.theEnigmaEngine.AlliesAgent;
import machineEngine.EngineManager;

import java.util.concurrent.BlockingQueue;

public class AlliesDecryptionManager {

    private EngineManager engineManager;
    private Allies allies;
    private String level;
    private BlockingQueue<Runnable> blockingQueue;
    String stringToConvert;

    public AlliesDecryptionManager(EngineManager engineManager, String stringToConvert){
        this.stringToConvert=stringToConvert;
        this.engineManager=engineManager;
        level=engineManager.getLevel();

    }

    public void createMission(String agentName) throws Exception {
        AlliesAgent agent=allies.getAgentByName(agentName);

        if (level.equals("Easy")) {
            createLowLevelMission(agent);
        } /*else if (level.equals("Medium")) {
            createMediumLevelMission();
        } else if (level.equals("High")) {
            createHighLevelMission();
        } else {
            createImpossibleLevelMission();
        }*/
    }
    public BlockingQueue<Runnable> createLowLevelMission(AlliesAgent agent) throws Exception {
        int missionsCounter = agent.getMissionsAmount();
        engineManager.getInitialStartingPosition();
        for (int i = missionsCounter; i > 0 ; i--) {
            missionsCounter++;
            EngineManager engineManagerCopy = engineManager.cloneEngineManager();
            AgentMission agentMission = new AgentMission(allies.getAlliesName(),stringToConvert,engineManagerCopy,engineManagerCopy.createPossibleStartingPositionList(allies.getMissionSize()));
            updateEngineManager(engineManagerCopy);
            blockingQueue.put(agentMission);

        }
        return blockingQueue;
    }

    private void updateEngineManager(EngineManager engineManagerCopy) {
        engineManager.setStartingPositionValues(engineManagerCopy.getTheLastStartingPos(), engineManagerCopy.getLastIndex(), engineManagerCopy.getFirstList(), engineManagerCopy.getCountPossibleStartingPosition());
    }


}