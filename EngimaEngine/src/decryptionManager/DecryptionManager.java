package decryptionManager;

import bruteForceLogic.TheMission;
import bruteForceLogic.UiAdapterInterface;
import bruteForce.DecryptionInfoDTO;
import engine.theEnigmaEngine.Agent;
import engine.theEnigmaEngine.Agents;
import machineEngine.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import machineDTO.ConvertedStringDTO;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class DecryptionManager {
    private int amountOfAgents;
    private Long amountOfSubListsToCreate;
    private int sizeOfMission;
    private Long maxAmountOfMissions = 0L;
    private SimpleLongProperty amountOfMissionsCounter;
    int countOfDecryption = 0;
    private EngineManager engineManager;
    private int missionIndex;
    private SimpleIntegerProperty amountOfMissionsProceeded;
    private List<String> possibleStartingPositionList;
    private ThreadPoolExecutor threadPoolExecutor;
    private Map<Integer, Agent> agentsMap;
    private Agents agents;
    private String taskType;
    private String stringToConvert;
    private BlockingQueue<DecryptionInfoDTO> resultsBlockingQueue;
    private BlockingQueue<Runnable> blockingQueue;
    private UiAdapterInterface uiAdapterInterface;
    private SimpleBooleanProperty isDecryptionInfoDTOAddedToBlockingQueue;
    private SimpleBooleanProperty pauseProperty;
    private SimpleBooleanProperty stopProperty;
    private SimpleBooleanProperty missionFinishedProperty;
    private SimpleBooleanProperty clearProperty;
    private boolean isMissionsFinished = false;
    private final Object pauseLock = new Object();
    private long start;
    private SimpleLongProperty missionProcessTime;
    private SimpleLongProperty missionProcessTimeIn;
    Double currentProgress;
    private SimpleBooleanProperty onFinishProperty;
    private Long missionProcessTimeTotal;

    public DecryptionManager(SimpleBooleanProperty inClearProperty, SimpleBooleanProperty inStopProperty, SimpleBooleanProperty pausePropertyIn, String stringToConvert, EngineManager engineManager, int sizeOfMission, String taskType, UiAdapterInterface uiAdapterInterface) {
        this.engineManager = engineManager;
        this.agents = engineManager.getUsedAgents();
        this.amountOfAgents = agents.getAmountOfAgents();
        this.sizeOfMission = sizeOfMission;
        this.missionIndex = 0;
        this.taskType = taskType;
        this.stringToConvert = stringToConvert;
        this.amountOfMissionsCounter = new SimpleLongProperty(0L);
        resultsBlockingQueue = new LinkedBlockingQueue<>();
        //this.possibleStartingPositionList = engineManager.getPossibleStartingPositionList();
        blockingQueue = new LinkedBlockingQueue<Runnable>(1000);
        this.threadPoolExecutor = new ThreadPoolExecutor(this.amountOfAgents, this.amountOfAgents, 0L, TimeUnit.MILLISECONDS, blockingQueue);
        this.uiAdapterInterface = uiAdapterInterface;
        pauseProperty = new SimpleBooleanProperty(pausePropertyIn.getValue());
        stopProperty = new SimpleBooleanProperty(inStopProperty.getValue());
        clearProperty = new SimpleBooleanProperty(inClearProperty.getValue());
        missionProcessTime = new SimpleLongProperty(0L);
        onFinishProperty = new SimpleBooleanProperty(false);
        missionFinishedProperty = new SimpleBooleanProperty(false);
        amountOfMissionsProceeded = new SimpleIntegerProperty(0);
        missionProcessTimeTotal = 0l;
        clearProperty.bind(inClearProperty);
        stopProperty.bind(inStopProperty);
        inStopProperty.addListener((observ) -> {
            if (inStopProperty.getValue()) {
                try {
                    shutDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pausePropertyIn.addListener((obsev) -> {
            if (pausePropertyIn.getValue()) {
                pauseProperty.setValue(true);

            } else {
                pauseProperty.setValue(false);
                synchronized (pauseLock) {
                    pauseLock.notifyAll();

                }
            }
        });
        onFinishProperty.addListener((obsev) -> {
            missionProcessTimeTotal += missionProcessTime.getValue();
            missionProcessTime.setValue(0l);
            synchronized (this) {
                amountOfMissionsCounter.setValue(amountOfMissionsCounter.getValue() - 1);
                amountOfMissionsProceeded.setValue(amountOfMissionsProceeded.getValue() + 1);
                if(amountOfMissionsCounter.getValue()==0){
                    int c=9;
                }
                uiAdapterInterface.updateAmountOfMissionsPerLevel(engineManager.displayMaxAmountOfMissionsWithCommas(amountOfMissionsCounter.getValue()));
                Double currentProgress = ((double) amountOfMissionsProceeded.getValue()) / maxAmountOfMissions;
                uiAdapterInterface.updateProgressBarDuringTask(currentProgress);
            }
        });
        new Thread(() -> {
            try {
                getResultsFromBlockingQueue();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        calculateAmountOfMissionsToCreate();
        createAgents();
    }

    public int countOfDecryption() {
        return countOfDecryption;
    }

    Function<Long, Agent> getAgent = id -> agentsMap.get(id.intValue() % amountOfAgents);

    private void createAgents() {
        agentsMap = new HashMap<Integer, Agent>();
        for (int i = 0; i < amountOfAgents; i++) {
            agentsMap.put(i, new Agent(i));
        }
    }

    public void calculateAmountOfMissionsToCreate() {
        amountOfSubListsToCreate = (engineManager.getAmountOfPossibleStartingPositionList()) / sizeOfMission;
        if (((engineManager.getAmountOfPossibleStartingPositionList()) % sizeOfMission) != 0) {
            amountOfSubListsToCreate++;
        }
    }

    private List<String> createPossibleStartingPositionForMission() {
        List<String> subPossibleStartingPosition = possibleStartingPositionList.subList(missionIndex * sizeOfMission, sizeOfMission + missionIndex * sizeOfMission);
        return subPossibleStartingPosition;
    }

    public void createMission() throws Exception {
        amountOfMissionsProceeded.setValue(0);
        maxAmountOfMissions = 0L;
        start = System.currentTimeMillis();
        maxAmountOfMissions = engineManager.maxAmountOfMissionscalculation(taskType, sizeOfMission);
        amountOfMissionsCounter.setValue(maxAmountOfMissions);
        uiAdapterInterface.updateAmountOfMissionsPerLevel(engineManager.displayMaxAmountOfMissionsWithCommas(maxAmountOfMissions));

        if (taskType.equals("Low")) {
            createLowLevelMission();
        } else if (taskType.equals("Medium")) {
            createMediumLevelMission();
        } else if (taskType.equals("High")) {
            createHighLevelMission();
        } else {
            createImpossibleLevelMission();
        }
    }

    private void shutDown() throws InterruptedException {
        synchronized (pauseLock) {
            threadPoolExecutor.shutdown();
        }
    }

    public void updateProgress() {
        currentProgress = ((double) amountOfMissionsProceeded.get()) / maxAmountOfMissions;
    }

    public static void getAllPermutationsOfRotorsPosition(int length, String[] rotorsId, List<String[]> optionalRotorsPosition) {
        String[] tmpArray;
        if (length == 1) {
            tmpArray = Arrays.copyOf(rotorsId, rotorsId.length);
            optionalRotorsPosition.add(tmpArray);
        } else {
            for (int i = 0; i < length - 1; i++) {
                getAllPermutationsOfRotorsPosition(length - 1, rotorsId, optionalRotorsPosition);
                if (length % 2 == 0) {
                    swap(rotorsId, i, length - 1);
                } else {
                    swap(rotorsId, 0, length - 1);
                }
            }
            getAllPermutationsOfRotorsPosition(length - 1, rotorsId, optionalRotorsPosition);
        }
    }

    private static void swap(String[] input, int a, int b) {
        String tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    public void getResultsFromBlockingQueue() throws InterruptedException {
        while (!isMissionsFinished) {
            uiAdapterInterface.updateTileOnDisplay(resultsBlockingQueue.take());
            countOfDecryption++;
            uiAdapterInterface.updateCounterOnDisplay(countOfDecryption);
        }
        Thread.currentThread().interrupt();
    }

    public List<String[]> getOptionalRotors() {
        List<String[]> listOfOptionalRotors = new ArrayList<>();
        List<int[]> listOfOptionalRotorsByInt = generate(engineManager.getTheMachineEngine().getMaxAmountOfRotors(), engineManager.getAmountOfUsedRotors());
        for (int i = 0; i < listOfOptionalRotorsByInt.size(); i++) {
            {
                String[] rotors = new String[listOfOptionalRotorsByInt.get(i).length];
                for (int j = 0; j < listOfOptionalRotorsByInt.get(i).length; j++) {

                    rotors[j] = String.valueOf(listOfOptionalRotorsByInt.get(i)[j]);

                }
                listOfOptionalRotors.add(i, rotors);
            }
        }
        return listOfOptionalRotors;
    }

    private void updateEngineManager(EngineManager engineManagerCopy) {
        engineManager.setStartingPositionValues(engineManagerCopy.getTheLastStartingPos(), engineManagerCopy.getLastIndex(), engineManagerCopy.getFirstList(), engineManagerCopy.getCountPossibleStartingPosition());
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 1, n, 0);
        return combinations;
    }

    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }

    public void createLowLevelMission() throws Exception {
        threadPoolExecutor.prestartAllCoreThreads();
        int missionsCounter = 0;
        engineManager.getInitialStartingPosition();
        for (int i = 0; i < amountOfSubListsToCreate && (!stopProperty.get()); i++) {
            while (pauseProperty.get()) {
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ignore) {
                    }
                }
            }
            missionsCounter++;
            missionIndex = i;
            EngineManager engineManagerCopy = engineManager.cloneEngineManager();
            TheMission theMission = new TheMission(pauseProperty, stopProperty, maxAmountOfMissions, amountOfMissionsProceeded, missionFinishedProperty, currentProgress, onFinishProperty, missionProcessTime, amountOfMissionsCounter, uiAdapterInterface, missionsCounter, engineManagerCopy, engineManagerCopy.createPossibleStartingPositionList(sizeOfMission), stringToConvert, getAgent, resultsBlockingQueue, isDecryptionInfoDTOAddedToBlockingQueue);
            updateEngineManager(engineManagerCopy);
            blockingQueue.put(theMission);
        }
        shutDown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
        isMissionsFinished = true;
        setAverageOnFinish();
        setValuesOnFinish();
    }

    public void createMediumLevelMission() throws Exception {
        threadPoolExecutor.prestartAllCoreThreads();
        int missionsCounter = 0;
        List<ConvertedStringDTO> convertedStringDTOList = new ArrayList<>();
        for (String reflectorId : engineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
            engineManager.chooseManuallyReflect(reflectorId);
            engineManager.getInitialStartingPosition();
            if (!stopProperty.get()) {
                for (int i = 0; i < amountOfSubListsToCreate && (!stopProperty.get()); i++) {
                    while (pauseProperty.get()) {
                        synchronized (pauseLock) {
                            try {
                                pauseLock.wait();
                            } catch (InterruptedException ignore) {
                            }
                        }
                    }
                    missionsCounter++;
                    missionIndex = i;
                    EngineManager engineManagerCopy = engineManager.cloneEngineManager();
                    TheMission theMission = new TheMission(pauseProperty, stopProperty, maxAmountOfMissions, amountOfMissionsProceeded, missionFinishedProperty, currentProgress, onFinishProperty, missionProcessTime, amountOfMissionsCounter, uiAdapterInterface, missionsCounter, engineManagerCopy, engineManagerCopy.createPossibleStartingPositionList(sizeOfMission), stringToConvert, getAgent, resultsBlockingQueue, isDecryptionInfoDTOAddedToBlockingQueue);
                    updateEngineManager(engineManagerCopy);
                    blockingQueue.put(theMission);
                }
            }
        }
        shutDown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
        isMissionsFinished = true;
        setAverageOnFinish();
        setValuesOnFinish();
    }

    public void createHighLevelMission() throws Exception {
        threadPoolExecutor.prestartAllCoreThreads();
        int missionsCounter = 0;
        String[] concatRotorsPosition = engineManager.getTheMachineEngine().getUsedRotorsId();
        List<String[]> optionalRotorsPositionList = new ArrayList<>();
        getAllPermutationsOfRotorsPosition(concatRotorsPosition.length, concatRotorsPosition, optionalRotorsPositionList);
        for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
            if (!stopProperty.get()) {
                for (String reflectorId : engineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
                    engineManager.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                    engineManager.getInitialStartingPosition();

                    for (int i = 0; i < amountOfSubListsToCreate && (!stopProperty.get()); i++) {
                        while (pauseProperty.get()) {
                            synchronized (pauseLock) {
                                try {
                                    pauseLock.wait();
                                } catch (InterruptedException ignore) {
                                }
                            }
                        }
                        missionsCounter++;
                        missionIndex = i;
                        EngineManager engineManagerCopy = engineManager.cloneEngineManager();
                        engineManagerCopy.chooseManuallyReflect(reflectorId);
                        engineManagerCopy.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                        TheMission theMission = new TheMission(pauseProperty, stopProperty, maxAmountOfMissions, amountOfMissionsProceeded, missionFinishedProperty, currentProgress, onFinishProperty, missionProcessTime, amountOfMissionsCounter, uiAdapterInterface, missionsCounter, engineManagerCopy, engineManagerCopy.createPossibleStartingPositionList(sizeOfMission), stringToConvert, getAgent, resultsBlockingQueue, isDecryptionInfoDTOAddedToBlockingQueue);
                        updateEngineManager(engineManagerCopy);
                        blockingQueue.put(theMission);
                    }
                }
            }
        }
        shutDown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
        isMissionsFinished = true;
        setAverageOnFinish();
        setValuesOnFinish();
    }

    public void createImpossibleLevelMission() throws Exception {
        threadPoolExecutor.prestartAllCoreThreads();
        int missionsCounter = 0;
        List<String[]> optionalRotorsList = new ArrayList<>();
        optionalRotorsList = getOptionalRotors();
        for (String[] optionalRotors : optionalRotorsList) {
            List<String[]> optionalRotorsPositionList = new ArrayList<>();
            getAllPermutationsOfRotorsPosition(optionalRotors.length, optionalRotors, optionalRotorsPositionList);
            if (stopProperty.get()) {
                shutDown();
                break;
            }
            for (String[] optionalRotorsPosition : optionalRotorsPositionList) {
                if (stopProperty.get()) {
                    shutDown();
                    break;
                }
                for (String reflectorId : engineManager.getTheMachineEngine().getReflectorsSet().getReflectorsId()) {
                    engineManager.chooseManuallyReflect(reflectorId);
                    engineManager.getInitialStartingPosition();
                    if (!stopProperty.get()) {
                        for (int i = 0; i < amountOfSubListsToCreate && (!stopProperty.get()); i++) {
                            while (pauseProperty.get()) {
                                synchronized (pauseLock) {
                                    try {
                                        pauseLock.wait();
                                    } catch (InterruptedException ignore) {
                                    }
                                }
                            }
                            missionsCounter++;
                            missionIndex = i;
                            EngineManager engineManagerCopy = engineManager.cloneEngineManager();
                            engineManagerCopy.getTheMachineEngine().updateUsedRotors(optionalRotorsPosition);
                            TheMission theMission = new TheMission(pauseProperty, stopProperty, maxAmountOfMissions, amountOfMissionsProceeded, missionFinishedProperty, currentProgress, onFinishProperty, missionProcessTime, amountOfMissionsCounter, uiAdapterInterface, missionsCounter, engineManagerCopy, engineManagerCopy.createPossibleStartingPositionList(sizeOfMission), stringToConvert, getAgent, resultsBlockingQueue, isDecryptionInfoDTOAddedToBlockingQueue);
                            updateEngineManager(engineManagerCopy);
                            blockingQueue.put(theMission);
                        }
                    } else {
                        shutDown();
                        break;
                    }
                }
            }
        }
        shutDown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);
        isMissionsFinished = true;
        setAverageOnFinish();
        setValuesOnFinish();
    }

    private void setValuesOnFinish() {
        if (clearProperty.get()) {
            uiAdapterInterface.updateProgressBarDuringTask(0.0);
            uiAdapterInterface.updateProcessTimeOnFinish(-1);
            uiAdapterInterface.initAmountOfMissionsPerLevel();
        } else {
        }
    }
    private void setAverageOnFinish() {
        if (!clearProperty.get()) {
            long end = System.currentTimeMillis();
            long res = end - start;
            uiAdapterInterface.updateProcessTimeOnFinish(res);
            uiAdapterInterface.updateAverageOfMissionProcessTime((double) missionProcessTimeTotal / amountOfMissionsProceeded.getValue());

        }
    }
}