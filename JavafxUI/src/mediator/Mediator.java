package mediator;

import engineManager.EngineManager;
import machineDTO.FullCodeDescriptionDTO;
import machineDTO.ListOfExceptionsDTO;
import engineManager.EngineManagerInterface;
import machineDTO.MachineHistoryAndStatisticsDTO;
import machineDTO.TheMachineSettingsDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Mediator implements Serializable {
    private EngineManagerInterface engineManager;

    public Mediator(EngineManagerInterface engineManager) {
        this.engineManager = engineManager;
    }

    public boolean fileNameValidation(String str) {

        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.getAllErrorsRelatedToFilePath(str);
        if (listOfExceptionsDTO.getListOfException().size() == 0) {
            return true;
        } else {
            printListOfException(listOfExceptionsDTO.getListOfException());
            return false;
        }
    }

    public ListOfExceptionsDTO isFileLoadSuccessfully(String str) throws Exception {
        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.load(str);
        return listOfExceptionsDTO;
    }

    private void printListOfException(List<Exception> errors) {
        for (Exception exception : errors) {
            System.out.println(exception.getMessage());
            /*System.out.println("******************");*/
        }
    }

    public ListOfExceptionsDTO isRotorsIDinInitCodeManuallyIsValid(String str) {
        return engineManager.getAllErrorsRelatedToChosenManuallyRotors(str);

    }

    public boolean isChooseToExit(String str) {
        return engineManager.isChooseToExit(str);
    }


    public ListOfExceptionsDTO isStartingPositionInitCodeManuallyIsValid(String str) {
        return engineManager.getAllErrorsRelatedToChosenManuallyStartingPosition(str);

    }

    public boolean isReflectoIDinInitCodeManuallyIsValid(String str) {
        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.getAllErrorsRelatedToChosenManuallyReflectorId(str);
        if (listOfExceptionsDTO.getListOfException().size() == 0) {
            return true;
        } else {
            printListOfException(listOfExceptionsDTO.getListOfException());
            return false;
        }
    }

    public void saveRotors() {
        engineManager.chooseManuallyRotors();
    }
    public void saveReflector(String selectedReflectorId) {
        engineManager.chooseManuallyReflect(selectedReflectorId);
    }
    public ListOfExceptionsDTO isPlagBoardinInitCodeManuallyIsValid(String str) {
        return engineManager.getAllErrorsRelatedToChosenManuallyPlugBoard(str);
    }
    public boolean isUserStringToProcessIsValid(String str) {
        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.getAllErrorsConvertingInputProcess(str);
        if (listOfExceptionsDTO.getListOfException().size() == 0) {
            return true;
        } else {
            printListOfException(listOfExceptionsDTO.getListOfException());
            return false;
        }
    }
    public ListOfExceptionsDTO getAllErrorsRelatedToUserStringToConvert(String str) {
        return engineManager.getAllErrorsConvertingInputProcess(str);
    }
    public String getConvertedString(String userInput) {
        return engineManager.getConvertedString(userInput).getConvertedString();
    }

    public void initCodeConfigurationAutomatically() {
        FullCodeDescriptionDTO fullCodeDescriptionDTO = engineManager.initCodeAutomatically();
        List<String> notchList = engineManager.getNotchList();
      /*  System.out.println("Selection of initial code configuration (automatically) performed successfully");
        System.out.println("The current code configuration is: " + engineManager.getCodeDescription(codeDescriptionDTO, notchList, engineManager.getCodeDescriptionDTO().getCurrentStartingPosition()));
 */   }

    public void addToHistoryAndStatistics(String userInputString, String convertedString) {
        engineManager.addToHistoryAndStatistics(userInputString, convertedString);
    }


    public FullCodeDescriptionDTO getCodeDescriptionDTO() {
        return engineManager.getCodeDescriptionDTO();
    }


    public String getAvailableReflectorsId() throws Exception {
        String availableReflectorsId = "";
        List<String> availableReflectorsIdList = engineManager.getTheMachineSettingsDTO().getReflectorsId();
        int index = 1;
        for (int i = 0; i < availableReflectorsIdList.size(); i++) {
            index = getReflectorIdIndex(availableReflectorsIdList.get(i));
            availableReflectorsId = availableReflectorsId + index + ") " + availableReflectorsIdList.get(i) + "\n";
        }
        return availableReflectorsId;

    }

    private int getReflectorIdIndex(String availableReflectorsId) {
        int index = 1;
        switch (availableReflectorsId) {
            case "I":
                index = 1;
                break;
            case "II":
                index = 2;
                break;
            case "III":
                index = 3;
                break;
            case "IV":
                index = 4;
                break;
            case "V":
                index = 5;
                break;
        }
        return index;
    }

    public void initStartingPositionConfigurationManually(String userInput) {
        engineManager.chooseManuallyStartingPosition(userInput);
    }

    public void initPlugBoardConfigurationManually(String userInput) {
        engineManager.chooseManuallyPlugBoard(userInput);

    }

    public void resetPlugBoard() {
        engineManager.resetPlugBoard();
    }

    public void resetCurrentCode() {
        engineManager.resetCurrentCode();
    }

    public ListOfExceptionsDTO isMachineWasDefined() {
        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.getAllErrorsRelatedToMachineMenuValidator();
        return listOfExceptionsDTO;
    }

    public ListOfExceptionsDTO isCodeWasDefined() {
        return engineManager.getAllErrorsRelatedToInitCodeMenuValidator();

    }

    public TheMachineSettingsDTO getTheMachineSettingsDTO() throws Exception {
        return engineManager.getTheMachineSettingsDTO();
    }

    public List<String> getMachineConfiguration() throws Exception {
        engineManager.createCurrentCodeDescriptionDTO();
        List<String> machineConfiguration = new ArrayList<>();
        TheMachineSettingsDTO theMachineSettingsDTO = engineManager.getTheMachineSettingsDTO();
        machineConfiguration.add("Rotors out of");//+ theMachineSettingsDTO.getAmountOfUsedRotors() + "\\" + theMachineSettingsDTO.getMaxAmountOfRotors() + "\n";
        machineConfiguration.add("Reflectors"); //+ theMachineSettingsDTO.getAmountOfReflectors() + "\n";
        machineConfiguration.add("proceeded messages");// + theMachineSettingsDTO.getAmountOfProcessedMessages() + "\n";
        if (engineManager.getIsCodeConfigurationSet()) {
            machineConfiguration.add("The original code description: ");// + getCurrentCodeDescription(theMachineSettingsDTO.getCurrentCodeDescriptionDTO(), theMachineSettingsDTO.getCurrentCodeDescriptionDTO().getOriginalNotchPosition(), engineManager.getCodeDescriptionDTO().getChosenStartingPosition()) + "\n";
            machineConfiguration.add("The current code description: ");//+ getCurrentCodeDescription(theMachineSettingsDTO.getCurrentCodeDescriptionDTO(), theMachineSettingsDTO.getCurrentCodeDescriptionDTO().getNotchPosition(), engineManager.getCodeDescriptionDTO().getCurrentStartingPosition()) + "\n";
        }
        return machineConfiguration;
    }
    public List<MachineHistoryAndStatisticsDTO> getHistoryAndStatistics() {
        List<MachineHistoryAndStatisticsDTO> listOfMachineHistory = engineManager.getHistoryAndStatisticsDTO();
        for (MachineHistoryAndStatisticsDTO machineHistory : listOfMachineHistory) {
            List<String> historyToDisplay = new ArrayList<>();
            if (machineHistory.getCurrentCodeDescriptionDTO() != null) {
                historyToDisplay.add("The strings that proceeded for " + engineManager.getCodeDescription(machineHistory.getCurrentCodeDescriptionDTO(), machineHistory.getCurrentCodeDescriptionDTO().getOriginalNotchPosition(), machineHistory.getCurrentCodeDescriptionDTO().getChosenStartingPosition()) + " are :\n");
                String[] userInput = machineHistory.getHistoryAndStatisticsDTO().getUserInput();
                String[] convertedStrings = machineHistory.getHistoryAndStatisticsDTO().getConvertedString();
                String[] timeToProcess = machineHistory.getHistoryAndStatisticsDTO().getTimeToProcess();
                if (userInput.length == 0) {
                    historyToDisplay.add("none\n");
                } else {
                    for (int i = 0; i < userInput.length; i++) {
                        historyToDisplay.add((i + 1) + ".<" + userInput[i] + "> --> <" + convertedStrings[i] + "> (" + timeToProcess[i] + " nano-seconds)\n");
                    }
                }
            }
            machineHistory.setHistoryToDisplay(historyToDisplay);
        }
        return listOfMachineHistory;


    }
    public String getNotchPositionByIndex(List<String> notchPositions) {
        String notchPositionByPairs = "";
        for (String notchPair : notchPositions) {
            notchPositionByPairs = notchPositionByPairs + "The notch position for rotor id " + notchPair.charAt(0) + " is : " + notchPair.substring(1, notchPair.length()) + "\n";

        }
        return notchPositionByPairs;
    }
    public void setIsCodeConfigurationWasdefine() {
        this.engineManager.DefineIsCodeConfigurationSetValueToTrue();
    }

    public Boolean isPlayerDefinePlugBoardIsValid(String str) {
        ListOfExceptionsDTO listOfExceptionsDTO = engineManager.getAllErrorsRelatedToUserDefinePlugBoard(str);
        if (listOfExceptionsDTO.getListOfException().size() == 0) {
            return true;
        } else {
            printListOfException(listOfExceptionsDTO.getListOfException());
            return false;
        }
    }

    public void writeCurrentStateToFile(String fileName) {
        try {
            engineManager.writeToFile(fileName);
        } catch (IOException ignore) {
            System.out.println("Error! can not write to the file\n");
            return;
        }
        System.out.println("Saved successfully!\n");
    }

    public void readCurrentStateFromFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("\nthe file full path name does not exist!\nPlease try again\n");
            return;
        }
        try {
            engineManager.readFromFile(fileName);
        } catch (IOException | ClassNotFoundException ignore) {
            System.out.println("Error! can not load from a file\n");
            return;
        }
        System.out.println("Load successfully!\n");
    }

    public int getAmountOfUsedRotors() {
        return engineManager.getAmountOfUsedRotors();
    }

    public String[] getRotorsId() {
        return engineManager.getTheMachineEngine().getRotorsId();
    }


    public boolean getisMachineWasDefine() {
        return engineManager.getMachineDefined();
    }

    public List<MachineHistoryAndStatisticsDTO> getHistoryAndStatisticsList() {
        return engineManager.getHistoryAndStatisticsDTO();
    }

    public EngineManager getEngineManger() {
        return (EngineManager) engineManager;
    }
}