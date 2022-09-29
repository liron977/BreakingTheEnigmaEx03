package engineManager;

import bruteForce.AlliesDTO;
import engine.theEnigmaEngine.*;
import engine.validator.*;
import historyAndStatistics.HistoryOfProcess;
import historyAndStatistics.MachineHistoryAndStatistics;
import schemaGenerated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

import machineDTO.*;

import static java.lang.Character.toUpperCase;


public class EngineManager implements EngineManagerInterface,Serializable {

    private ListOfExceptionsDTO listOfExceptionsDTO;
    private FullCodeDescriptionDTO fullCodeDescriptionDTO;
    private TheMachineEngine theMachineEngine;
    private MachineHistoryAndStatistics machineHistoryAndStatistics = new MachineHistoryAndStatistics();
    boolean isCodeConfigurationSet = false;
    private int amountOfProcessedMessages = 0;
    List<Rotor> listOfRotors=new ArrayList<>();
    int countPossibleStartingPosition=0;
    String reflectorId="";
    String theLastStartingPos;
    boolean firstList=true;

    int lastIndex=0;
    long ProcessTimeToConvert;
    private MenuValidator menuValidator = new MenuValidator();
    private final String JAXB_XML_GAME_PACKAGE_NAME = "schemaGenerated";
    // private  CTEEnigma cteEnigma;
    //private SchemaGenerated schemaGenerated;
    private String filePath;
    private Agents agents;
    private Long amountOfPossibleStartingPositionList=0L;
    private List<String> possibleStartingPositionList;
    private Agents usedAgents;

    private UBoatBattleField battleField;
    private String uploadedBy;
    private int amountOfNeededDecryptionAliesTeams;



    @Override
    public ListOfExceptionsDTO load(String filePath) throws Exception {
      CTEEnigma cteEnigma = readFromXmlFile(filePath);
     /*     Validator xmlReflectorValidator = new XmlReflectorValidator(cteEnigma);
        Validator xmlRotorValidator = new XmlRotorValidator((cteEnigma));
        Validator xmlKeyboardValidator = new XmlKeyboardValidator(cteEnigma);
       // Validator xmlAgentsValidator = new XmlAgentValidator(cteEnigma);
        Validator xmlDictionaryValidator = new XmlDictionaryValidator(cteEnigma);

        List<Validator> validators = new ArrayList<>();
        validators.add(xmlKeyboardValidator);
        validators.add(xmlReflectorValidator);
        validators.add((xmlRotorValidator));
        //validators.add(xmlAgentsValidator);
        validators.add(xmlDictionaryValidator);
        ValidatorRunner validatorRunner = new ValidatorRunner(validators);
        List<Exception> exceptions = validatorRunner.run();*/
        List<Exception> exceptions =fileValidator(cteEnigma);
        if (exceptions.size() == 0) {
            this.filePath = filePath;
            menuValidator.reset();
            isCodeConfigurationSet = false;
            amountOfProcessedMessages = 0;
            machineHistoryAndStatistics = new MachineHistoryAndStatistics();
            menuValidator.setTrueValueToIsMachineDefined();
            theMachineEngine = buildTheMachineEngine();
            theLastStartingPos=getInitialStartingPosition();
        }
        listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        amountOfPossibleStartingPositionList=0L;
        possibleStartingPositionList=new ArrayList<>();

        return listOfExceptionsDTO;
    }

    public Boolean getMachineDefined() {
        return menuValidator.getMachineDefined();
    }


    public List<String> getNotchList() {
        return theMachineEngine.getListOfNotch();
    }

    public TheMachineEngine buildTheMachineEngine() throws Exception {
        CTEEnigma cteEnigma = readFromXmlFile(filePath);
        SchemaGenerated schemaGenerated = new SchemaGenerated(cteEnigma);
        this.battleField=schemaGenerated.createBattleField();
        TheMachineEngine theMachineEngine = new TheMachineEngine(schemaGenerated.createRotorsSet(), schemaGenerated.createReflectorsSet(), schemaGenerated.createKeyboard(), schemaGenerated.getAmountOfUsedRotors(), schemaGenerated.createDictionary(),battleField);
        //this.agents=schemaGenerated.createAgents();

        return theMachineEngine;
    }

    public TheMachineEngine buildTheMachineEngineUboat(CTEEnigma cteEnigma,String uploadedBy) throws Exception {
       // CTEEnigma cteEnigma = readFromXmlFile(filePath);
        SchemaGenerated schemaGenerated = new SchemaGenerated(cteEnigma);
        this.battleField=schemaGenerated.createBattleField();
        this.battleField.setUploadedBy(uploadedBy);
        TheMachineEngine theMachineEngine = new TheMachineEngine(schemaGenerated.createRotorsSet(), schemaGenerated.createReflectorsSet(), schemaGenerated.createKeyboard(), schemaGenerated.getAmountOfUsedRotors(), schemaGenerated.createDictionary(),battleField);
        //this.agents=schemaGenerated.createAgents();

        return theMachineEngine;
    }

    public Agents getAgents() {
        return agents;
    }

    public TheMachineEngine getTheMachineEngine() {
        return theMachineEngine;
    }

    public boolean getIsCodeConfigurationSet() {
        return isCodeConfigurationSet;
    }

    public void DefineIsCodeConfigurationSetValueToTrue() {
        this.isCodeConfigurationSet = true;
        menuValidator.setTrueValueToUpdateIsCodeDefined();
        reverseUsedRotors(theMachineEngine);
        createCurrentCodeDescriptionDTO();
        machineHistoryAndStatistics.addNewMachineSettings(fullCodeDescriptionDTO);
    }

    private void reverseUsedRotors(TheMachineEngine theMachineEngine) {
        theMachineEngine.reverseUsedRotors();

    }

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyRotors(String str) {
        UserInputRotorsValidator userInputRotorsValidator = new UserInputRotorsValidator(str, theMachineEngine);
        userInputRotorsValidator.validate();
        listOfRotors = new ArrayList<>();
        String[] rotorsId = userInputRotorsValidator.getFilteredUserInput();
        if (rotorsId != null) {
            for (String rotorId : rotorsId) {
                listOfRotors.add(theMachineEngine.getRotorsSet().getRotorById(rotorId));
            }
        }
        List<Exception> exceptions = userInputRotorsValidator.getListOfException();
        ListOfExceptionsDTO listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyStartingPosition(String str) {

        Validator userInputStartingPositionValidator = new UserInputStartingPositionValidator(str, theMachineEngine);
        userInputStartingPositionValidator.validate();
        List<Exception> exceptions = userInputStartingPositionValidator.getListOfException();
        ListOfExceptionsDTO listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyReflectorId(String str) {
        UserInputReflectorValidator userInputReflectorValidator = new UserInputReflectorValidator(str, theMachineEngine);
        userInputReflectorValidator.validate();
        reflectorId = userInputReflectorValidator.getReflectorId();
        // chooseManuallyReflect(reflectorId);
        List<Exception> exceptions = userInputReflectorValidator.getListOfException();
        ListOfExceptionsDTO listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToChosenManuallyPlugBoard(String str) {
        Validator userInputPlugBoardValidator = new UserInputPlugBoardValidator(str, theMachineEngine);
        userInputPlugBoardValidator.validate();
        List<Exception> exceptions = userInputPlugBoardValidator.getListOfException();
        ListOfExceptionsDTO listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public void chooseManuallyRotors() {
        if (listOfRotors.size() > 0) {
            theMachineEngine.createUsedRotorsSet(listOfRotors);
        }
    }

    public void chooseManuallyStartingPosition(String userInput) {
        userInput = userInput.toUpperCase();
        String startingPosition;
        int i = 0;
        List<Rotor> rotorsSet = theMachineEngine.getUsedRotors().getListOfRotors();
        for (Rotor rotor : rotorsSet) {
            startingPosition = String.valueOf(userInput.charAt(i));
            rotor.setRotorStartingPosition(startingPosition);
            i++;
        }
    }

    public void chooseManuallyReflect(String selectedReflectorId) {
        if (selectedReflectorId != "") {
            theMachineEngine.addSelectedReflector(selectedReflectorId);
            reflectorId=selectedReflectorId;

        }
    }

    public void chooseManuallyPlugBoard(String userInput) {
        if (!userInput.equals("")) {
            userInput = userInput.toUpperCase();
            List<Pair> pairsOfSwappingLetters = new ArrayList<>();
            String firstSignal, secondSignal;
            int amountOfSwappingPairs = userInput.length() / 2;
            if (amountOfSwappingPairs > 0) {
                for (int i = 0; i < userInput.length() - 1; i += 2) {
                    firstSignal = String.valueOf(userInput.charAt(i));
                    secondSignal = String.valueOf(userInput.charAt(i + 1));
                    Pair pair = new Pair(firstSignal, secondSignal);
                    pairsOfSwappingLetters.add(pair);
                }
            }
            PlugsBoard plugsBoard = new PlugsBoard(theMachineEngine.getKeyboard(), pairsOfSwappingLetters);
            theMachineEngine.addPlugsBoardTOTheMachine(plugsBoard);
        }

    }

    public void resetPlugBoard() {
        List<Pair> pairsOfSwappingLetters = new ArrayList<>();
        PlugsBoard plugsBoard = new PlugsBoard(theMachineEngine.getKeyboard(), pairsOfSwappingLetters);
        theMachineEngine.addPlugsBoardTOTheMachine(plugsBoard);
    }

    public FullCodeDescriptionDTO getCodeDescriptionDTO() {
        return fullCodeDescriptionDTO;
    }

    /*    public int getRotorsAmount(){


            return cteEnigma.getCTEMachine().getRotorsCount();
        }*/
    public FullCodeDescriptionDTO initCodeAutomatically() {
        //CTEEnigma cteEnigma =readFromXmlFile(filePath);
        //theMachineEngine = buildTheMachineEngine();
        chooseAutomaticallyRotors(theMachineEngine);
        initRotorsPositionAutomatically(theMachineEngine);
        chooseAutomaticallyReflector(theMachineEngine);
        choosePlugBoardSettings(theMachineEngine);
        isCodeConfigurationSet = true;
        menuValidator.setTrueValueToUpdateIsCodeDefined();
        reverseUsedRotors(theMachineEngine);
        createCurrentCodeDescriptionDTO();
        machineHistoryAndStatistics.addNewMachineSettings(fullCodeDescriptionDTO);
        return fullCodeDescriptionDTO;
    }

    private void chooseAutomaticallyRotors(TheMachineEngine theMachineEngine) {
        List<Rotor> listOfRotors = theMachineEngine.getRotorsSet().getListOfRotors();
        Random randomGenerator = new Random();
        List<Rotor> listOfRandomRotors = new ArrayList<>();
        HashMap<String, Integer> rotorsHashMap = new HashMap<>();
        String rotorId;
        Rotor randomSelectedRotor;
        for (int i = 0; i < theMachineEngine.getAmountOfUsedRotors(); i++) {
            randomSelectedRotor = listOfRotors.get((randomGenerator.nextInt(listOfRotors.size())));
            rotorId = randomSelectedRotor.getRotorId();
            while (((rotorsHashMap.get(rotorId)) != null) && (rotorsHashMap.get(rotorId) >= 1)) {
                randomSelectedRotor = listOfRotors.get((randomGenerator.nextInt(listOfRotors.size())));
                rotorId = randomSelectedRotor.getRotorId();


            }
            rotorsHashMap.put(rotorId, 1);
            listOfRandomRotors.add(randomSelectedRotor);
        }
        theMachineEngine.createUsedRotorsSet(listOfRandomRotors);
    }

    private void chooseAutomaticallyReflector(TheMachineEngine theMachineEngine) {
        List<Reflector> listOfReflectors = theMachineEngine.getReflectorsSet().getListOfReflectors();
        Random randomGenerator = new Random();
        Reflector randomSelectedReflector = listOfReflectors.get((randomGenerator.nextInt(listOfReflectors.size())));
        theMachineEngine.addSelectedReflector(randomSelectedReflector.getReflectorId());
    }

    private void initRotorsPositionAutomatically(TheMachineEngine theMachineEngine) {
        String keyboard = theMachineEngine.getKeyboard();
        Random randomGenerator = new Random();
        String randomSelectedPosition;
        List<Rotor> rotorsSet = theMachineEngine.getUsedRotors().getListOfRotors();
        for (Rotor rotor : rotorsSet) {
            randomSelectedPosition = String.valueOf(keyboard.charAt((toUpperCase(randomGenerator.nextInt(keyboard.length())))));
            rotor.setRotorStartingPosition(randomSelectedPosition);
        }
    }

    private void choosePlugBoardSettings(TheMachineEngine theMachineEngine) {
        String keyboard = theMachineEngine.getKeyboard();
        Random randomGenerator = new Random();
        HashMap<String, Integer> plugBoardHashMap = new HashMap<>();
        List<Pair> pairsOfSwappingLetters = new ArrayList<>();
        int amountOfSwappingPairs = randomGenerator.nextInt(keyboard.length() / 2);
        String firstSignal, secondSignal;
        if (amountOfSwappingPairs > 0) {
            for (int i = 0; i < amountOfSwappingPairs; i++) {
                firstSignal = String.valueOf(keyboard.charAt(randomGenerator.nextInt(keyboard.length())));
                while (((plugBoardHashMap.get(firstSignal)) != null) && (plugBoardHashMap.get(firstSignal) == 1)) {
                    firstSignal = String.valueOf(keyboard.charAt(randomGenerator.nextInt(keyboard.length())));
                }
                plugBoardHashMap.put(firstSignal, 1);
                secondSignal = String.valueOf(keyboard.charAt(randomGenerator.nextInt(keyboard.length())));
                while (((plugBoardHashMap.get(secondSignal)) != null) && (plugBoardHashMap.get(secondSignal) == 1)) {
                    secondSignal = String.valueOf(keyboard.charAt(randomGenerator.nextInt(keyboard.length())));
                }
                plugBoardHashMap.put(secondSignal, 1);
                Pair pair = new Pair(firstSignal, secondSignal);
                pairsOfSwappingLetters.add(pair);
            }

        }
        PlugsBoard plugsBoard = new PlugsBoard(keyboard, pairsOfSwappingLetters);
        theMachineEngine.addPlugsBoardTOTheMachine(plugsBoard);


    }

    public ListOfExceptionsDTO getAllErrorsRelatedToFilePath(String filePath) {
        XmlFileValidator xmlFileValidator = new XmlFileValidator(filePath);
        xmlFileValidator.validate();
        List<Exception> exceptions = xmlFileValidator.getListOfException();
        listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public CTEEnigma readFromXmlFile(String filePath) throws Exception {
        File file = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CTEEnigma cteEnigma = (CTEEnigma) jaxbUnmarshaller.unmarshal(inputStream);
            // this.cteEnigma=cteEnigma;
            return cteEnigma;
        } catch (JAXBException e) {
            throw new Exception("The file is not valid,please enter other file");
        } catch (FileNotFoundException e) {
            throw new Exception("The file did not find in this path" + filePath);
        }

    }

    public ListOfExceptionsDTO getAllErrorsConvertingInputProcess(String userInput) {
        List<Exception> inputListOfException;
        Validator userInputValidator = new UserStringProcessorValidator(userInput.toUpperCase(), theMachineEngine);
        userInputValidator.validate();
        inputListOfException = userInputValidator.getListOfException();
        ListOfExceptionsDTO inputListOfExceptionDTO = new ListOfExceptionsDTO(inputListOfException);
        return inputListOfExceptionDTO;

    }

    public boolean isChooseToExit(String userInput) {
        if (userInput.equals("")) {
            return true;

        }
        return false;
    }

    public ConvertedStringDTO getConvertedString(String userInputString) {
        long begin = System.nanoTime();

        String convertedString = "";
        userInputString = userInputString.toUpperCase();
        for (int i = 0; i < userInputString.length(); i++) {
            String userInputByString = String.valueOf(userInputString.charAt(i));
            String convertedCharByString = theMachineEngine.manageDecode(userInputByString);
            convertedString = convertedString.concat(convertedCharByString);
        }
        long end = System.nanoTime();
        ProcessTimeToConvert = ProcessTimeToConvert + (end - begin);
        amountOfProcessedMessages++;
        ConvertedStringDTO convertedStringDTO = new ConvertedStringDTO(convertedString);


        return convertedStringDTO;
    }

    public void addToHistoryAndStatistics(String userInputString, String convertedString) {


        /*amountOfProcessedMessages++;*/

        createCurrentCodeDescriptionDTO();
        machineHistoryAndStatistics.addNewProcess(fullCodeDescriptionDTO, new HistoryOfProcess(userInputString, convertedString, ProcessTimeToConvert));
        ProcessTimeToConvert = 0;


    }

    public List<MachineHistoryAndStatisticsDTO> getHistoryAndStatisticsDTO() {
        List<MachineHistoryAndStatisticsDTO> listOfMachineHistoryAndStatisticsDTO = new ArrayList<>();
        Map<FullCodeDescriptionDTO, List<HistoryOfProcess>> machineHistory = machineHistoryAndStatistics.getMachineHistory();
        for (Map.Entry<FullCodeDescriptionDTO, List<HistoryOfProcess>> entry : machineHistory.entrySet()) {
            String[] userInput = new String[entry.getValue().size()];
            String[] convertedString = new String[entry.getValue().size()];
            ;
            String[] timeToProcess = new String[entry.getValue().size()];
            ;

            for (int i = 0; i < entry.getValue().size(); i++) {
                userInput[i] = entry.getValue().get(i).getUserInput();
                convertedString[i] = entry.getValue().get(i).getConvertedInput();
                timeToProcess[i] = String.valueOf(entry.getValue().get(i).getTimeToProcess());
            }
            HistoryAndStatisticsDTO historyAndStatisticsDTO = new HistoryAndStatisticsDTO(userInput, convertedString, timeToProcess);
            MachineHistoryAndStatisticsDTO machineHistoryAndStatisticsDTO = new MachineHistoryAndStatisticsDTO(entry.getKey(), historyAndStatisticsDTO);
            listOfMachineHistoryAndStatisticsDTO.add(machineHistoryAndStatisticsDTO);
        }
        return listOfMachineHistoryAndStatisticsDTO;
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToUserDefinePlugBoard(String userInput) {
        menuValidator.setTrueValueToUpdateIsCodeDefined();
        UserInputPlugBoardValidator userInputPlugBoardValidator = new UserInputPlugBoardValidator(userInput, theMachineEngine);
        userInputPlugBoardValidator.isUserChosenInputToDefineAPlugBoardIsValid();
        List<Exception> exceptions = userInputPlugBoardValidator.getListOfException();
        listOfExceptionsDTO = new ListOfExceptionsDTO(exceptions);
        return listOfExceptionsDTO;
    }

    public void resetCurrentCode() {
        theMachineEngine.resetCurrentRotorSetCode();
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToMachineMenuValidator() {
        menuValidator.isXmlLoaded();
        return new ListOfExceptionsDTO(menuValidator.getListOfException());
    }

    public ListOfExceptionsDTO getAllErrorsRelatedToInitCodeMenuValidator() {
        menuValidator.isCodeConfigurationWasDefined();
        return new ListOfExceptionsDTO(menuValidator.getListOfException());
    }

    public TheMachineSettingsDTO getTheMachineSettingsDTO() throws Exception {
        if (theMachineEngine == null) {
            CTEEnigma cteEnigma = readFromXmlFile(filePath);
            theMachineEngine = buildTheMachineEngine();
        }
        List<String> reflectorsId = theMachineEngine.getReflectorsId();
        int amountOfUsedRotors = theMachineEngine.getAmountOfUsedRotors();
        int maxAmountOfRotors = theMachineEngine.getMaxAmountOfRotors();
        int amountOfReflectors = theMachineEngine.getReflectorsAmount();
        String keyboard = theMachineEngine.getKeyboard();
        String[] rotorsId = theMachineEngine.getRotorsId();
        createCurrentCodeDescriptionDTO();
        TheMachineSettingsDTO theMachineSettingsDTO = new TheMachineSettingsDTO(amountOfUsedRotors, maxAmountOfRotors, amountOfReflectors, amountOfProcessedMessages, fullCodeDescriptionDTO, reflectorsId, rotorsId, keyboard);
        return theMachineSettingsDTO;
    }

    public void createCurrentCodeDescriptionDTO() {
        FullCodeDescriptionDTO fullCodeDescriptionDTO = null;
        if (isCodeConfigurationSet) {
            String[] usedRotorsId = theMachineEngine.getArrayOfRotorsId();
            String currentStartingPosition = theMachineEngine.getUsedRotors().getCurrentRotorsStartingPositions();

            String chosenStartingPosition = theMachineEngine.getUsedRotors().getRotorsOriginalStartingPositions();
            String reflectorId = theMachineEngine.getReflector().getReflectorId();
            List<String> notchPosition = theMachineEngine.getListOfNotch();
            List<String> originalNotchPosition = theMachineEngine.getOriginalNotchPositionList();
            List<String> pairsOfSwappingCharacter = theMachineEngine.getStringPairsOfSwappingCharacter();
            fullCodeDescriptionDTO = new FullCodeDescriptionDTO(pairsOfSwappingCharacter, reflectorId, chosenStartingPosition, currentStartingPosition, usedRotorsId, originalNotchPosition, notchPosition);
        }
        this.fullCodeDescriptionDTO = fullCodeDescriptionDTO;


    }

    public void updateExceptionListMenuValidator() {
        menuValidator.updateExceptionList();
    }

    public void writeToFile(String FileName) throws IOException {
        ArrayList<EngineManager> meds = new ArrayList();
        meds.add(this);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileName));
        Throwable var = null;

        try {
            out.writeObject(meds);
            out.flush();
        } catch (Throwable error) {
            var = error;
            throw error;
        } finally {
            if (out != null) {
                if (var != null) {
                    try {
                        out.close();
                    } catch (Throwable error2) {
                        var.addSuppressed(error2);
                    }
                } else {
                    out.close();
                }
            }

        }

    }

    public int getAmountOfUsedRotors() {
        return theMachineEngine.getAmountOfUsedRotors();
    }

    public void readFromFile(String FileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileName));
        Throwable var = null;
        try {
            ArrayList<EngineManager> meds = (ArrayList) in.readObject();
            this.listOfExceptionsDTO = ((EngineManager) meds.get(0)).listOfExceptionsDTO;
            this.fullCodeDescriptionDTO = ((EngineManager) meds.get(0)).fullCodeDescriptionDTO;
            this.theMachineEngine = ((EngineManager) meds.get(0)).theMachineEngine;
            this.machineHistoryAndStatistics = ((EngineManager) meds.get(0)).machineHistoryAndStatistics;
            // this.cteEnigma = ((EngineManager)meds.get(0)).cteEnigma;
            // this.schemaGenerated = ((EngineManager)meds.get(0)).schemaGenerated;
            this.isCodeConfigurationSet = ((EngineManager) meds.get(0)).isCodeConfigurationSet;
            this.amountOfProcessedMessages = ((EngineManager) meds.get(0)).amountOfProcessedMessages;
            this.menuValidator = ((EngineManager) meds.get(0)).menuValidator;
            this.listOfRotors = ((EngineManager) meds.get(0)).listOfRotors;
            this.reflectorId = ((EngineManager) meds.get(0)).reflectorId;

        } catch (Throwable error) {
            var = error;
            throw error;
        } finally {
            if (in != null) {
                if (var != null) {
                    try {
                        in.close();
                    } catch (Throwable error2) {
                        var.addSuppressed(error2);
                    }
                } else {
                    in.close();
                }
            }

        }

    }

    public void initConfigurationForAgent(String rotorsId, String startingPosition, String reflectorId) {
        getAllErrorsRelatedToChosenManuallyRotors(rotorsId);
        chooseManuallyRotors();
        chooseManuallyStartingPosition(startingPosition);
        chooseManuallyReflect(reflectorId);
    }
    public EngineManager cloneEngineManager() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (EngineManager) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    public List<String> getPossibleStartingPositionList(){
        return possibleStartingPositionList;
    }

    public Long getAmountOfPossibleStartingPositionList() {
        amountOfPossibleStartingPositionList=(long) Math.pow(theMachineEngine.getKeyboard().length(),theMachineEngine.getAmountOfUsedRotors());
        return amountOfPossibleStartingPositionList;
    }

    public List<String> createPossibleStartingPositionList(int sizeOfSubList) {

        possibleStartingPositionList = new ArrayList<>();
        StringBuilder stringToAdd = new StringBuilder();
        String initialStartingPosition = theLastStartingPos;
        String keyboard = getTheMachineEngine().getKeyboard();
        // List<String> possibleStartingPositionList = new ArrayList<>();
        int nextSignalIndex = 0;
        String lastSignal = "";
        int amountOfStartingPos = 0;
        boolean happened = true;


        stringToAdd.append(initialStartingPosition, 0, initialStartingPosition.length());
        for (int i = 0; i < initialStartingPosition.length(); i++) {
            lastSignal = lastSignal + String.valueOf(keyboard.charAt(keyboard.length() - 1));
        }
        while (amountOfStartingPos < sizeOfSubList && !possibleStartingPositionList.contains(lastSignal)) {

            if (sizeOfSubList > keyboard.length()) {
                for (int j = 0; j < keyboard.length() & amountOfStartingPos < (sizeOfSubList) && lastIndex < keyboard.length(); j++) {
                    stringToAdd.setCharAt(initialStartingPosition.length() - 1, keyboard.charAt(lastIndex));
                    lastIndex++;
                    if (!stringToAdd.toString().equals(theLastStartingPos) || firstList) {
                        firstList = false;
                        possibleStartingPositionList.add(stringToAdd.toString());
                        amountOfStartingPos++;
                    } else {
            /*happened = false;
            if (!happened && !firstList) {
                sizeOfSubList++;
                happened = true;
            }*/
                    }


                }

            } else {

                for (int j = 0; (j < (sizeOfSubList) && (lastIndex < keyboard.length()) && (amountOfStartingPos < sizeOfSubList)); j++) {
                    stringToAdd.setCharAt(initialStartingPosition.length() - 1, keyboard.charAt(lastIndex));
                    lastIndex++;
                    if (!stringToAdd.toString().equals(theLastStartingPos) || firstList) {
                        firstList = false;
                        possibleStartingPositionList.add(stringToAdd.toString());
                        amountOfStartingPos++;
                    } else {
                        happened = false;
                        if (!happened && !firstList) {
                            sizeOfSubList++;
                            happened = true;
                        }
                    }
                }
                if (!firstList) {
                    sizeOfSubList--;
                }
            }

            for (int i = stringToAdd.length() - 1; amountOfStartingPos <= sizeOfSubList && i > 0; i--) {
                lastIndex = 0;
                if (stringToAdd.charAt(i) != keyboard.charAt(keyboard.length() - 1) || amountOfStartingPos >= sizeOfSubList) {
                    break;
                } else {
                    if (stringToAdd.charAt(i - 1) != keyboard.charAt(keyboard.length() - 1)) {
                        char signalToReplace = stringToAdd.charAt(i - 1);
                        nextSignalIndex = keyboard.indexOf(signalToReplace) + 1;
                        for (int k = i; k <= stringToAdd.length() - 1; k++) {
                            stringToAdd.setCharAt(k, keyboard.charAt(0));
                        }
                        stringToAdd.setCharAt(i - 1, keyboard.charAt(nextSignalIndex));
                        i = 0;
                    }
                }
            }
        }
        this.amountOfPossibleStartingPositionList = Long.valueOf(possibleStartingPositionList.size());
        if(possibleStartingPositionList.contains("'''")){
            int x=0;
        }
        theLastStartingPos = possibleStartingPositionList.get(possibleStartingPositionList.size() - 1);
        lastIndex = keyboard.indexOf(theLastStartingPos.charAt(theLastStartingPos.length() - 1));
        //return possibleStartingPositionList;
         /*  for (String s : possibleStartingPositionList) {
               System.out.println(s);
           }
         */  countPossibleStartingPosition=countPossibleStartingPosition+possibleStartingPositionList.size();


        /* System.out.println("*************");*/

        return possibleStartingPositionList;
    }


    public String getInitialStartingPosition(){

        String initialStartingPosition="";
        int amountOfRotors=getTheMachineEngine().getAmountOfUsedRotors();
        char firstSignal=getTheMachineEngine().getKeyboard().charAt(0);
        for(int i=0;i<amountOfRotors;i++){
            initialStartingPosition=initialStartingPosition+firstSignal;
        }
        theLastStartingPos=initialStartingPosition;
        lastIndex=0;
        firstList=true;
        countPossibleStartingPosition=0;

        return initialStartingPosition;
    }
    public void setStartingPositionValues(String theLastStartingPos,int lastIndex,boolean firstList,int countPossibleStartingPosition){
        this.countPossibleStartingPosition=countPossibleStartingPosition;
        this.lastIndex=lastIndex;
        this.theLastStartingPos=theLastStartingPos;
        this.firstList=firstList;

    }

    public int getLastIndex() {
        return lastIndex;
    }

    public String getTheLastStartingPos() {
        return theLastStartingPos;
    }
    public boolean getFirstList(){
        return firstList;
    }
    public int getCountPossibleStartingPosition(){
        return countPossibleStartingPosition;
    }
    public Long setMaxAmountOfMissions(String level,int sizeOfMission){
        Long maxAmountOfMissions=0L;
        Long numberOfMission=Long.valueOf((long) (Math.pow(getTheMachineEngine().getKeyboard().length(),getAmountOfUsedRotors()))/sizeOfMission);
        if(((Math.pow(getTheMachineEngine().getKeyboard().length(),getAmountOfUsedRotors())%sizeOfMission))!=0){
            numberOfMission++;
        }
        Long numberOfMissionMedium=numberOfMission*getTheMachineEngine().getReflectorsSet().getReflectorsAmount();
        Long numberOfMissionHigh=numberOfMissionMedium*factorial(getTheMachineEngine().getAmountOfUsedRotors());
        Long numberOfMissionImpossible= numberOfMissionHigh*binomialCoeff(getTheMachineEngine().getMaxAmountOfRotors(),getAmountOfUsedRotors());

        switch(level) {
            case "Low":
                maxAmountOfMissions=numberOfMission;
                break;
            case "Medium":
                maxAmountOfMissions=numberOfMissionMedium;
                break;
            case "High":
                maxAmountOfMissions=numberOfMissionHigh;
                break;
            default:
                maxAmountOfMissions=numberOfMissionImpossible;
        }
        return maxAmountOfMissions;

    }

    public String displayMaxAmountOfMissionsWithCommas(Long maxAmountOfMissions){
        StringBuilder maxAmountOfMissionsWithCommas= new StringBuilder("");
        int counter=0;
        if(maxAmountOfMissions==0){
            return "0";
        }
        while (maxAmountOfMissions>0){
            if((counter%3==0)&&(counter!=0)){
                maxAmountOfMissionsWithCommas=maxAmountOfMissionsWithCommas.append(",");
            }
            counter++;
            maxAmountOfMissionsWithCommas=maxAmountOfMissionsWithCommas.append(maxAmountOfMissions%10);
            maxAmountOfMissions=maxAmountOfMissions/10;
        }

      return maxAmountOfMissionsWithCommas.reverse().toString();

    }
    public long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }
    int binomialCoeff(int n, int k)
    {
        // Base Cases
        if (k > n)
            return 0;
        if (k == 0 || k == n)
            return 1;

        // Recur
        return binomialCoeff(n - 1, k - 1)
                + binomialCoeff(n - 1, k);
    }
    public String getOriginalCodeDescription() throws Exception {
        TheMachineSettingsDTO theMachineSettingsDTO = getTheMachineSettingsDTO();
        createCurrentCodeDescriptionDTO();
        return getCodeDescription(theMachineSettingsDTO.getCurrentCodeDescriptionDTO(), theMachineSettingsDTO.getCurrentCodeDescriptionDTO().getOriginalNotchPosition(),getCodeDescriptionDTO().getChosenStartingPosition());
    }
    public String getCodeDescription(FullCodeDescriptionDTO fullCodeDescriptionDTO, List<String> notchPosition, String startingPosition) {
        String currentCodeDescription = "";
        currentCodeDescription = currentCodeDescription + "<" + getRotorsInfo(fullCodeDescriptionDTO.getUsedRotorsId()) + ">";
        StringBuilder startingPositionRevers = new StringBuilder();
        startingPositionRevers.append(startingPosition);
        startingPositionRevers.reverse();
        currentCodeDescription = currentCodeDescription + "<" + getWindowInfoId(startingPositionRevers, notchPosition) + ">";
        currentCodeDescription = currentCodeDescription + "<" + fullCodeDescriptionDTO.getReflectorId() + ">";
        List<String> pairsOfSwappingCharacter = fullCodeDescriptionDTO.getPairsOfSwappingCharacter();
        if ((pairsOfSwappingCharacter != null) && (pairsOfSwappingCharacter.size() != 0)) {
            currentCodeDescription = currentCodeDescription + "<" + getPairsOfSwappingCharacter(fullCodeDescriptionDTO.getPairsOfSwappingCharacter()) + ">";
        }
        return currentCodeDescription;
    }
    public LimitedCodeConfigurationDTO createCurrentCodeConfigurationTableViewDTO(){
        createCurrentCodeDescriptionDTO();
        String rotors=getRotorsInfo(fullCodeDescriptionDTO.getUsedRotorsId());
        StringBuilder startingPositionRevers = new StringBuilder();
        startingPositionRevers.append(new StringBuilder(getCodeDescriptionDTO().getCurrentStartingPosition()));
        startingPositionRevers.reverse();
        String positionsAndNotch=getWindowInfoId(startingPositionRevers, getNotchList());
        String reflector= fullCodeDescriptionDTO.getReflectorId();
        String plugBoardPairs=getPairsOfSwappingCharacter(fullCodeDescriptionDTO.getPairsOfSwappingCharacter());
        LimitedCodeConfigurationDTO limitedCodeConfigurationDTO =new LimitedCodeConfigurationDTO(rotors,positionsAndNotch,reflector,plugBoardPairs,getCurrentCodeDescription());
        return limitedCodeConfigurationDTO;
    }
    public LimitedCodeConfigurationDTO createOriginalCodeConfigurationTableViewDTO() throws Exception {
        TheMachineSettingsDTO theMachineSettingsDTO = getTheMachineSettingsDTO();
        createCurrentCodeDescriptionDTO();

        String rotors=getRotorsInfo(fullCodeDescriptionDTO.getUsedRotorsId());
        StringBuilder startingPositionRevers = new StringBuilder();
        startingPositionRevers.append(new StringBuilder(getCodeDescriptionDTO().getChosenStartingPosition()));
        startingPositionRevers.reverse();
        String positionsAndNotch=getWindowInfoId(startingPositionRevers, theMachineSettingsDTO.getCurrentCodeDescriptionDTO().getOriginalNotchPosition());
        String reflector= fullCodeDescriptionDTO.getReflectorId();
        String plugBoardPairs=getPairsOfSwappingCharacter(theMachineSettingsDTO.getCurrentCodeDescriptionDTO().getPairsOfSwappingCharacter());
        LimitedCodeConfigurationDTO limitedCodeConfigurationDTO =new LimitedCodeConfigurationDTO(rotors,positionsAndNotch,reflector,plugBoardPairs,getOriginalCodeDescription());
        return limitedCodeConfigurationDTO;
    }
    private String getPairsOfSwappingCharacter(List<String> pairsOfSwappingCharacter) {
        String seperatedPairsOfSwappingCharacter = "";
        if(pairsOfSwappingCharacter!=null) {
            for (String pairOfSwappingCharacter : pairsOfSwappingCharacter) {
                seperatedPairsOfSwappingCharacter = seperatedPairsOfSwappingCharacter + pairOfSwappingCharacter.charAt(3) + "|" + pairOfSwappingCharacter.charAt(1);
                seperatedPairsOfSwappingCharacter = seperatedPairsOfSwappingCharacter + ",";
            }
            if (seperatedPairsOfSwappingCharacter.length() != 0) {
                seperatedPairsOfSwappingCharacter = seperatedPairsOfSwappingCharacter.substring(0, seperatedPairsOfSwappingCharacter.length() - 1);
            }
        }
        return seperatedPairsOfSwappingCharacter;
    }
    private String getWindowInfoId(StringBuilder startingPositionRevers, List<String> notchPosition) {
        String usedRotors = "";
        String windowInfo = "";
        int index = 0;
        Collections.reverse(notchPosition);
        for (int i = 0; i < startingPositionRevers.length(); i++) {
            windowInfo = windowInfo + startingPositionRevers.charAt(i) + "(" + notchPosition.get(index) + ")";

            index++;
        }
        Collections.reverse(notchPosition);
        return windowInfo;
    }
    private String getRotorsInfo(String[] rotors) {
        /*    List<String> rotorsInfo= getUsedRotorsId(usedRotorsId,notchPosition);*/
        Collections.reverse(Arrays.asList(rotors));
        String rotor = "";
        for (int i = 0; i < rotors.length; i++) {

            rotor = rotor + rotors[i] + ",";

        }
        rotor = rotor.substring(0, rotor.length() - 1);
        Collections.reverse(Arrays.asList(rotors));
        return rotor;
    }
    public String getCurrentCodeDescription() {

        List<String> notchList =getNotchList();
        // createCurrentCodeDescriptionDTO();
        return getCodeDescription(getCodeDescriptionDTO(), notchList, getCodeDescriptionDTO().getCurrentStartingPosition());
    }
    public String isStringIncludeIllegalSignal(String str){
        for(int i=0;i<theMachineEngine.getKeyboard().length();i++){
            str =str.replace(String.valueOf(theMachineEngine.getKeyboard().charAt(i)),"");
        }
        return str;
    }
    public Agents setUsedAgents(int numberOfUsedAgents) {
        this.usedAgents=new Agents(numberOfUsedAgents);
        return usedAgents;

    }
    public Agents getUsedAgents() {
        return usedAgents;
    }
    public UBoatBattleField getBattleField() {
        return battleField;
    }
/*    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }*/
    public ListOfExceptionsDTO loadFileByInputStream(InputStream inputStream,String uploadedBy) throws Exception {
        CTEEnigma cteEnigma = this.deserializeFrom(inputStream);
        List<Exception> exceptionList=fileValidator(cteEnigma);
      if(exceptionList.size()==0) {
          machineHistoryAndStatistics = new MachineHistoryAndStatistics();
          theMachineEngine = buildTheMachineEngineUboat(cteEnigma,uploadedBy);
          theLastStartingPos = getInitialStartingPosition();
          menuValidator.setTrueValueToIsMachineDefined();
      }
          listOfExceptionsDTO = new ListOfExceptionsDTO(exceptionList);
          amountOfPossibleStartingPositionList = 0L;
          possibleStartingPositionList = new ArrayList<>();

        return listOfExceptionsDTO;
       // this.OriginalTargetsGraph = new Graph(descriptor);
    }
    private List<Exception> fileValidator(CTEEnigma cteEnigma){
        Validator xmlReflectorValidator = new XmlReflectorValidator(cteEnigma);
        Validator xmlRotorValidator = new XmlRotorValidator((cteEnigma));
        Validator xmlKeyboardValidator = new XmlKeyboardValidator(cteEnigma);
        Validator xmlDictionaryValidator = new XmlDictionaryValidator(cteEnigma);
        List<Validator> validators = new ArrayList<>();
        validators.add(xmlKeyboardValidator);
        validators.add(xmlReflectorValidator);
        validators.add((xmlRotorValidator));
        //validators.add(xmlAgentsValidator);
        validators.add(xmlDictionaryValidator);
        ValidatorRunner validatorRunner = new ValidatorRunner(validators);
        List<Exception> exceptions = validatorRunner.run();
        return exceptions;
    }
    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("schemaGenerated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma)u.unmarshal(in);
    }
    public String getBattleName() {
        return this.theMachineEngine.getBattleFieldName();
    }
    public String getUploadedBy(){
        return uploadedBy;
   }
   public void setConvertedStringInBattleField(String convertedString){
    this.battleField.setConvertedString(convertedString);
   }
   public boolean addAlliesToContest(Allies allies){
        return battleField.addAllies(allies);

   }
   public List<Allies> getRegisteredAlliesList(){
      return  battleField.getAlliesRegisteredToContest();

    }
    public void addAgentToAllies(AlliesAgent alliesAgent,String alliesTeamName){
        battleField.addAgentToAllies(alliesAgent,alliesTeamName);

    }
    public boolean isAlliesExists(String alliesTeamName){
       return battleField.isAlliesExists(alliesTeamName);

    }
    public String getLevel(){
        return battleField.getLevel();
    }
}