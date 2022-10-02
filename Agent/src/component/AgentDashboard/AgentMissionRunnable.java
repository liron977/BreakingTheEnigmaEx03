package component.AgentDashboard;

import bruteForce.BruteForceResultDTO;
import engineManager.EngineManager;
import machineDTO.ConvertedStringDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentMissionRunnable implements Runnable{
        private BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
        private EngineManager engineManagerCopy;
        private List<String> startingPositions;
        private String stringToConvert;
       private String alliesTeamName;
        private String finalPosition;
        private String initialStartingPosition;
        private int sizeOfMission;
        private List<String> listOfPossiblePosition;

        public AgentMissionRunnable(EngineManager engineManager,
                                    String stringToConvert, String alliesTeamName
                ,List<String> listOfPossiblePosition){

            this.engineManagerCopy=engineManager;
            this.stringToConvert=stringToConvert;
            this.listOfPossiblePosition=listOfPossiblePosition;
            this.alliesTeamName=alliesTeamName;
        }

        public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
            this.resultsBlockingQueue = resultsBlockingQueue;
        }

    public BlockingQueue<BruteForceResultDTO> getResultsBlockingQueue() {
        return resultsBlockingQueue;
    }

    public void getConvertedStringsFounded() throws InterruptedException {

            for (String optionalStartingPosition : startingPositions) {

                engineManagerCopy.chooseManuallyStartingPosition(optionalStartingPosition);
                engineManagerCopy.createCurrentCodeDescriptionDTO();
                String convertedStringCode=engineManagerCopy.getCurrentCodeDescription();
                ConvertedStringDTO convertedStringDTOTemp = engineManagerCopy.getConvertedString(stringToConvert);
                if (engineManagerCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                    BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(convertedStringDTOTemp.getConvertedString(),alliesTeamName ,convertedStringCode);
                    resultsBlockingQueue.put(bruteForceResultDTO);
                }
            }
        }
/*    public void createPossiblePositionList(){
        String startingPosition =
    }

    public static int[] getNextStartingPosition(int missionSize, int[] currentPosition, String[] keyboard, List<String> listOfPossiblePosition) {
        while (missionSize != 0) {
            while (currentPosition[currentPosition.length - 1] < keyboard.length - 1) {

                currentPosition[currentPosition.length - 1] = currentPosition[currentPosition.length - 1] + 1;
                missionSize--;
                if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                    listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                    break;
                }
                listOfPossiblePosition.add(getRes(currentPosition, keyboard));

            }
            if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                // listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                break;
            }
            for (int i = currentPosition.length - 1; i > 0; i--) {
                if (currentPosition[i] == (keyboard.length - 1)) {
                    currentPosition[i] = 0;
                    if (currentPosition[i - 1] < keyboard.length - 1) {
                        currentPosition[i - 1] = currentPosition[i - 1] + 1;
                        missionSize--;
                        listOfPossiblePosition.add(getRes(currentPosition, keyboard));

                        break;
                    }

                    if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                        listOfPossiblePosition.add(getRes(currentPosition, keyboard));
                        break;
                    }

                }

            }

        }
        return currentPosition;
    }

    public static boolean isTheLastStartingPosition(int[] currentPosition, int keyboardSize) {
        for (int i = 0; i < currentPosition.length; i++) {
            if (!(currentPosition[i] == keyboardSize - 1)) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> initialStartingPosition(int rotorAmount) {
        List<Integer> currentPosition = new ArrayList<>();
        currentPosition.add(0);
        for (int i = 0; i < rotorAmount; i++) {
            currentPosition.add(0);
        }
        return currentPosition;
    }
    public static void printRes(int[] arr, String[] keyboard) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(keyboard[arr[i]]);
        }
        System.out.println();
        System.out.println("*****");
    }
    public static String getRes(int[] arr, String[] keyboard) {
        String currentPosition="";
        for (int i = 0; i < arr.length; i++) {
            currentPosition=currentPosition.concat(keyboard[arr[i]]);
        }
        return currentPosition;
    }
    public static int[] getIndexArrayFromString(String position, String[] keyboard) {
        int[] indexArrayFromString = new int[position.length()];
        for (int i = 0; i < position.length(); i++) {
            indexArrayFromString[i] = Arrays.asList(keyboard).indexOf(String.valueOf(position.charAt(i)));
        }
        return indexArrayFromString;
    }*/

    @Override
        public void run() {
            try {
                getConvertedStringsFounded();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }