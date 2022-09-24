package BruteForceLogic;

import bruteForce.DecryptionInfoDTO;
import engine.theEnigmaEngine.Agent;
import engineManager.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import machineDTO.ConvertedStringDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public class TheMission implements Runnable {
    List<String> startingPositions;
    EngineManager engineManager;
    int missionId;
    String stringToConvert;
    List<ConvertedStringDTO> convertedStringDTOList;
    private Function<Long, Agent> getAgent;
    private BlockingQueue<DecryptionInfoDTO> resultsBlockingQueue;
    private List<DecryptionInfoDTO> decryptionInfoDTOList;
    private Agent agent;

    UiAdapterInterface uiAdapterInterface;
    private SimpleBooleanProperty isDecryptionInfoDTOAddedToBlockingQueue;
    private SimpleLongProperty missionProcessTime;
    private SimpleBooleanProperty onFinishProperty;
    private SimpleBooleanProperty missionFinishedProperty;
    private SimpleLongProperty amountOfMissionsLeft;
    private Long maxAmountOfMissions;
    private SimpleIntegerProperty amountOfMissionsProceeded;
    private SimpleBooleanProperty stopProperty;
    private SimpleBooleanProperty pauseProperty;
    private final Object pauseLock = new Object();
    public TheMission(SimpleBooleanProperty pausePropertyIn,SimpleBooleanProperty stopProperty, Long maxAmountOfMissions,SimpleIntegerProperty amountOfMissionsProceeded,SimpleBooleanProperty missionFinishedProperty,Double currentProgress,SimpleBooleanProperty onFinishProperty, SimpleLongProperty missionProcessTime, SimpleLongProperty amountOfMissionsLeft, UiAdapterInterface uiAdapterInterface, int missionsCounter, EngineManager engineManager, List<String> possibleStartingPositions, String stringToConvert, Function<Long, Agent> getAgent, BlockingQueue<DecryptionInfoDTO> resultsBlockingQueue, SimpleBooleanProperty isDecryptionInfoDTOAddedToBlockingQueue) throws Exception {
        startingPositions = possibleStartingPositions;
        this.engineManager = engineManager;
        this.stringToConvert = stringToConvert;
        convertedStringDTOList=new ArrayList<>();
        this.missionId=missionsCounter;
        this.getAgent=getAgent;
        this.stopProperty=stopProperty;
        this.maxAmountOfMissions=maxAmountOfMissions;
        this.amountOfMissionsProceeded=amountOfMissionsProceeded;
        this.uiAdapterInterface=uiAdapterInterface;
        this.amountOfMissionsLeft=amountOfMissionsLeft;
        this.resultsBlockingQueue=resultsBlockingQueue;
        this.decryptionInfoDTOList=new ArrayList<>();
        this.missionFinishedProperty=missionFinishedProperty;
        pauseProperty=new SimpleBooleanProperty(pausePropertyIn.getValue());
        pausePropertyIn.addListener((obser)->{
        if (pausePropertyIn.getValue()) {
            pauseProperty.setValue(true);

        } else {
            pauseProperty.setValue(false);
            synchronized (pauseLock) {
                pauseLock.notifyAll();

            }
        }});
        this.isDecryptionInfoDTOAddedToBlockingQueue=isDecryptionInfoDTOAddedToBlockingQueue;
        this.missionProcessTime=missionProcessTime;
        this.onFinishProperty=onFinishProperty;
    }
    public void getConvertedStringsFounded() throws InterruptedException {
        missionFinishedProperty.setValue(false);
            for (String optionalStartingPosition : startingPositions) {
                if (stopProperty.getValue()) {
                    break;
                }
                while (pauseProperty.get()) {
                    synchronized (pauseLock) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException ignore) {
                        }
                    }
                }
                engineManager.chooseManuallyStartingPosition(optionalStartingPosition);
                engineManager.createCurrentCodeDescriptionDTO();
                String convertedStringCode=engineManager.getCurrentCodeDescription();
                ConvertedStringDTO convertedStringDTOTemp = engineManager.getConvertedString(stringToConvert);
                if (engineManager.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                    DecryptionInfoDTO decryptionInfoDTO = new DecryptionInfoDTO(convertedStringDTOTemp.getConvertedString(), agent.getId(),convertedStringCode);
                    resultsBlockingQueue.put(decryptionInfoDTO);
                }
            }
        }
    @Override
    public void run() {
        if (!stopProperty.getValue()) {
            long start = System.currentTimeMillis();
            agent = getAgent.apply(Thread.currentThread().getId());
            try {
                getConvertedStringsFounded();
                synchronized (this) {
                    long end = System.currentTimeMillis();
                    long res = end - start;
                    missionProcessTime.set(res);
                    onFinishProperty.setValue(!onFinishProperty.getValue());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
        }
    }
}