package machineEngine;

import engine.theEnigmaEngine.Agents;
import engine.theEnigmaEngine.TheMachineEngine;
import machineDTO.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public interface EngineManagerInterface extends Serializable {

    public ListOfExceptionsDTO load(String filePath) throws Exception; //throws XmlException;

    public ListOfExceptionsDTO getAllErrorsRelatedToFilePath(String filePath);

    public void chooseManuallyRotors();

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyRotors(String str);

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyStartingPosition(String str);

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyReflectorId(String str);

    public void chooseManuallyReflect(String selectedReflectorId);

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyPlugBoard(String str);

    public FullCodeDescriptionDTO initCodeAutomatically();

    public TheMachineSettingsDTO getTheMachineSettingsDTO() throws Exception;

    public boolean isChooseToExit(String userInput);

    public Boolean getMachineDefined();
    public int getCountPossibleStartingPosition();
    public boolean getFirstList();
    public String getTheLastStartingPos();
    public int getLastIndex();
    public Long maxAmountOfMissionscalculation(String level, int sizeOfMission);

        public ListOfExceptionsDTO getAllErrorsConvertingInputProcess(String str);

    public ConvertedStringDTO getConvertedString(String str);

    public void addToHistoryAndStatistics(String userInputString, String convertedString);

    public void resetCurrentCode();

    public boolean getIsCodeConfigurationSet();

    public List<MachineHistoryAndStatisticsDTO> getHistoryAndStatisticsDTO();

    public void chooseManuallyStartingPosition(String userInput);

    public void chooseManuallyPlugBoard(String userInput);

    public void resetPlugBoard();

    public void DefineIsCodeConfigurationSetValueToTrue();

    public FullCodeDescriptionDTO getCodeDescriptionDTO();

    public TheMachineEngine buildTheMachineEngine() throws Exception;

    public ListOfExceptionsDTO getAllErrorsRelatedToUserDefinePlugBoard(String userInput);

    public ListOfExceptionsDTO getAllErrorsRelatedToMachineMenuValidator();

    public void updateExceptionListMenuValidator();

    public ListOfExceptionsDTO getAllErrorsRelatedToInitCodeMenuValidator();

    public void writeToFile(String FileName) throws IOException;

    public void readFromFile(String FileName) throws IOException, ClassNotFoundException;

    public List<String> getNotchList();

    public void createCurrentCodeDescriptionDTO();

    public int getAmountOfUsedRotors();

    public TheMachineEngine getTheMachineEngine();

    public EngineManager cloneEngineManager();

    public Agents getAgents();

    public String getCodeDescription(FullCodeDescriptionDTO fullCodeDescriptionDTO, List<String> notchPosition, String startingPosition);
    public Long getAmountOfPossibleStartingPositionList();
    public List<String> getPossibleStartingPositionList();
    public ListOfExceptionsDTO loadFileByInputStream(InputStream inputStream,String uploadedBy) throws Exception;
    public String getBattleName();
    public void setConvertedStringInBattleField(String convertedString);
    public InputStream getInputStream();
}