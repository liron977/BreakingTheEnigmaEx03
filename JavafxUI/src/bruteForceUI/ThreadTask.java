package bruteForceUI;

import bruteForceLogic.UiAdapterInterface;
import decryptionManager.DecryptionManager;
import machineEngine.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

public class ThreadTask extends Task<Boolean> {
    private UiAdapterInterface UiAdapter;
    private DecryptionManager decryptionManager;
    private String stringToConvert;
    private int sizeOfMission;
    private String taskType;
    private SimpleBooleanProperty pauseProperty;
    private EngineManager engineManager;
    private SimpleBooleanProperty startProperty;
    private SimpleBooleanProperty allMissionsDoneProperty;
    SimpleBooleanProperty stopProperty;
    SimpleBooleanProperty waitProperty;
    SimpleBooleanProperty clearProperty;
   // private SimpleIntegerProperty newAmountOfThreadsProperty;
    public ThreadTask(SimpleBooleanProperty clearProperty,SimpleBooleanProperty waitProperty,SimpleBooleanProperty allMissionsDoneProperty,String stringToConvert,UiAdapterInterface uiAdapt, SimpleBooleanProperty pauseProperty, EngineManager engineManager, int sizeOfMission, String taskType) {
        this.UiAdapter = uiAdapt;
        this.engineManager=engineManager;
        this.sizeOfMission = sizeOfMission;
        this.taskType = taskType;
        this.pauseProperty = pauseProperty;
        this.stringToConvert=stringToConvert;
        this.waitProperty=waitProperty;
        this.clearProperty=clearProperty;
        this.allMissionsDoneProperty=allMissionsDoneProperty;
    }
    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public Boolean call() throws Exception {
        EngineManager engineManagerClone=engineManager.cloneEngineManager();
        engineManagerClone.getTheMachineEngine().initEmptyPlugBoard();
        decryptionManager=new DecryptionManager(clearProperty,waitProperty,pauseProperty,stringToConvert,engineManagerClone,sizeOfMission,taskType,UiAdapter);
        this.decryptionManager.createMission();
        allMissionsDoneProperty.setValue(true);
        return Boolean.TRUE;
    }
}