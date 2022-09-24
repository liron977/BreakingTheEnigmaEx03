package Controllers;

import engineManager.EngineManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import machineDTO.ListOfExceptionsDTO;
import machineDTO.TheMachineSettingsDTO;
import mediator.Mediator;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class SetCodeConfigurationController implements EventsHandler {
    Mediator mediator;

    @FXML
    private HBox rotorsIdHBox;

    @FXML
    private HBox startingPositionHBox;
    @FXML
    private HBox reflectorHBox;
    @FXML
    private Button setRandomCodeButton;
    @FXML
    private HBox plugBoardHBox;
    @FXML
    private MachineTabController mainController;
    @FXML
    private Button setManuallyCodeButton;
    @FXML
    private Button clearButton;
    List<ComboBox> rotorsIdListComboBox;
    List<ComboBox> startingPositionListComboBox;
    List<ComboBox> plugBoardPairsListComboBox;
    ToggleGroup reflectorTg;
    ListOfExceptionsDTO listOfExceptionsDTO;
    TheMachineSettingsDTO theMachineSettingsDTO;
    private List<EventsHandler> handlers;
    boolean isMachineWasDefine;

    int plugBoardPairCounter;
    private SimpleBooleanProperty isInitNeeded;
    private SimpleBooleanProperty isCodeDefined;
    @FXML
    public void initialize() {
        clearButton.setDisable(true);
    }
    public SetCodeConfigurationController() {
        isCodeDefined = new SimpleBooleanProperty(false);
        plugBoardPairCounter = 0;
        handlers = new ArrayList<>();
        isMachineWasDefine = false;
        reflectorTg = new ToggleGroup();
        rotorsIdListComboBox = new ArrayList<>();
        startingPositionListComboBox = new ArrayList<>();
        plugBoardPairsListComboBox = new ArrayList<>();
        isInitNeeded = new SimpleBooleanProperty(false);
    }

    public SimpleBooleanProperty isCodeDefinedProperty() {
        return isCodeDefined;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;

    }

    public void addHandler(EventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    private void fireEvent() throws Exception {
        EventObject myEvent = new EventObject(this);
        List<EventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (EventsHandler handler : handlers) {
            handler.eventHappened(myEvent);
        }
    }

    public void setCodeCalibration() throws Exception {
        rotorsIdListComboBox = new ArrayList<>();
        startingPositionListComboBox = new ArrayList<>();
        plugBoardPairsListComboBox = new ArrayList<>();
        theMachineSettingsDTO = mediator.getTheMachineSettingsDTO();
        int amountOfUsedRotors = theMachineSettingsDTO.getAmountOfUsedRotors();
        setRotorsIdHBox(amountOfUsedRotors);
        setStartingPositionHBox(amountOfUsedRotors);
        setReflectorHBox();
        plugBoardHBox.getChildren().clear();
        plugBoardPairCounter = 0;
        Button addPlugBoardPairButton = new Button("Add pair");
        plugBoardHBox.getChildren().add(addPlugBoardPairButton);
        addPlugBoardPairButton.idProperty().set("littleButton");
        addPlugBoardPairButton.setOnAction(e -> {
            setPlugBoardHBox();
        });
        //plugBoardHBox.setHgrow(addPlugBoardPairButton, Priority.NEVER);
    }

    @FXML
    void setManuallyCodeButtonActionListener(ActionEvent event) throws Exception {
        isInitNeeded.setValue(true);
        listOfExceptionsDTO = mediator.isMachineWasDefined();
        List<Exception> listOfExceptions = listOfExceptionsDTO.getListOfException();
        if (listOfExceptions.size() == 0) {
            String rotorsId = getRotorsId();
            String startingPosition = getStartingPosition();
            String reflector = getReflector();
            String plugBoardPairs = getPlugBoardPairs();
            listOfExceptions = mediator.isStartingPositionInitCodeManuallyIsValid(startingPosition).getListOfException();
            listOfExceptions.addAll(mediator.isRotorsIDinInitCodeManuallyIsValid(rotorsId).getListOfException());
            boolean isReflectoIDValid = reflector != "";
            listOfExceptions.addAll(mediator.isPlagBoardinInitCodeManuallyIsValid(plugBoardPairs).getListOfException());
            if (listOfExceptions != null) {
                if (listOfExceptions.size() == 0 && isReflectoIDValid) {
                    mediator.saveRotors();
                    mediator.initStartingPositionConfigurationManually(startingPosition);
                    mediator.saveReflector(reflector);
                    if (plugBoardPairs.equals("")) {
                        mediator.getEngineManger().getTheMachineEngine().initEmptyPlugBoard();
                    } else {
                        mediator.initPlugBoardConfigurationManually(plugBoardPairs);
                    }
                    mediator.setIsCodeConfigurationWasdefine();
                    isCodeDefinedProperty().set(true);

                    fireEvent();
                   /*DecryptionManager decryptionManager=new DecryptionManager(mediator.getEngineManger());
                   List<String> listOfOptionalStartingPosition=decryptionManager.createPossibleStartingPositionList();
                   List<ConvertedStringDTO>  convertedStringDTOList= decryptionManager.getAgentConvertedStringFoundedHighLevel(listOfOptionalStartingPosition,"PYEJZCFMXMYB'JMSMKAZFCBHBHJR?SEKXQYDYCQBPGUXOH G?DORMCFQQUDHOBLMP");
                   for (ConvertedStringDTO con:convertedStringDTOList) {
                       System.out.println(con.getConvertedString());
                   }*/
                } else {
                    printListOfExceptions(listOfExceptions);
                }
            }
        }
    }

    public String getStartingPosition() {
        String startingPosition = "";
        for (int i = 0; i < startingPositionListComboBox.size(); i++) {
            startingPosition = startingPosition + startingPositionListComboBox.get(i).getValue();

        }
        return startingPosition;
    }

    public String getReflector() {
        RadioButton selectedRadioButton = (RadioButton) reflectorTg.getSelectedToggle();
        if (selectedRadioButton != null) {
            String toogleGroupValue = selectedRadioButton.getText();
            return toogleGroupValue;
        }
        return "";
    }

    public String getPlugBoardPairs() {
        String plugBoardPairs = "";
        for (int i = 0; i < plugBoardPairsListComboBox.size(); i++) {
            if (!plugBoardPairsListComboBox.get(i).getValue().equals("Empty")) {


                plugBoardPairs = plugBoardPairs + plugBoardPairsListComboBox.get(i).getValue();
            }
        }
        return plugBoardPairs;
    }

    public String getRotorsId() {
        String rotorsId = "";
        for (int i = 0; i < rotorsIdListComboBox.size(); i++) {
            if (!rotorsId.equals("")) {
                rotorsId = rotorsId + ",";
            }
            rotorsId = rotorsId + rotorsIdListComboBox.get(i).getValue();
        }
        /*System.out.println(rotorsId);*/
        return rotorsId;
    }

    public void setRotorsIdHBox(int amountOfUsedRotors) {
        rotorsIdHBox.getChildren().clear();
        String[] rotorsId = theMachineSettingsDTO.getRotorsId();
        for (int i = 0; i < amountOfUsedRotors; i++) {
            ComboBox<String> rotorsChoiceBox = new ComboBox<String>();
            rotorsIdListComboBox.add(rotorsChoiceBox);
            for (int j = 0; j < rotorsId.length; j++) {
                rotorsChoiceBox.getItems().add(rotorsId[j]);
            }
            rotorsChoiceBox.setValue("");
            rotorsIdHBox.getChildren().add(rotorsChoiceBox);
        }
    }

    public void setReflectorHBox() {
        reflectorTg = new ToggleGroup();
        reflectorHBox.getChildren().clear();
        List<String> reflectorsId = theMachineSettingsDTO.getReflectorsId();
        for (int i = 0; i < reflectorsId.size(); i++) {
            RadioButton reflectorRadioButton = new RadioButton(reflectorsId.get(i));
            reflectorRadioButton.setToggleGroup(reflectorTg);
            reflectorHBox.getChildren().add(reflectorRadioButton);
        }
        if (!reflectorTg.getToggles().get(0).isSelected()) {
            reflectorTg.getToggles().get(0).setSelected(true);
        }
    }

    public void setPlugBoardHBox() {

        String keyboard = theMachineSettingsDTO.getKeyboard();
        int pairCounter = plugBoardPairCounter * 2;

        if (pairCounter < keyboard.length()) {
            plugBoardPairCounter++;


            for (int i = 0; i < 2; i++) {
                ComboBox<String> plugBoardChoiceBox = new ComboBox<String>();
                plugBoardPairsListComboBox.add(plugBoardChoiceBox);
                for (int j = 0; j < keyboard.length(); j++) {
                    if (j == 0) {
                        plugBoardChoiceBox.getItems().add("Empty");
                    }
                    plugBoardChoiceBox.getItems().add(String.valueOf(keyboard.charAt(j)));
                }
                plugBoardChoiceBox.setValue("");
                plugBoardHBox.getChildren().add(plugBoardChoiceBox);
                //plugBoardHBox.setHgrow(plugBoardChoiceBox, Priority.NEVER);
            }
            plugBoardHBox.setFillHeight(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You cant add more pairs");
            alert.getDialogPane().setExpanded(true);

            alert.showAndWait();
        }

    }


    public void setStartingPositionHBox(int amountOfUsedRotors) {
        startingPositionHBox.getChildren().clear();
        String keyboard = theMachineSettingsDTO.getKeyboard();
        for (int i = 0; i < amountOfUsedRotors; i++) {
            ComboBox<String> startingPositionChoiceBox = new ComboBox<String>();
            startingPositionListComboBox.add(startingPositionChoiceBox);
            for (int j = 0; j < keyboard.length(); j++) {
                startingPositionChoiceBox.getItems().add(String.valueOf(keyboard.charAt(j)));
            }

            startingPositionChoiceBox.setValue("");
            startingPositionHBox.getChildren().add(startingPositionChoiceBox);
        }

    }

    public void setMainController(MachineTabController mainController) {
        this.mainController = mainController;
    }

    public SimpleBooleanProperty getIsInitNeeded() {
        return isInitNeeded;
    }

    @FXML
    void setRandomCodeButtonActionListener(ActionEvent event) throws Exception {
        isInitNeeded.setValue(true);
        listOfExceptionsDTO = mediator.isMachineWasDefined();
        List<Exception> listOfExceptions = listOfExceptionsDTO.getListOfException();
        if (listOfExceptions.size() == 0) {
            mediator.initCodeConfigurationAutomatically();
            isCodeDefinedProperty().set(true);
            fireEvent();
            EngineManager engineManager = mediator.getEngineManger();

        } else {
            printListOfExceptions(listOfExceptions);
        }
    }

    private void printListOfExceptions(List<Exception> exceptionList) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String errorMessage = "";
        for (Exception message : exceptionList) {
            errorMessage = errorMessage + message.getMessage() + "\n";
        }
        alert.setContentText(errorMessage);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    @Override
    public void eventHappened(EventObject event) throws Exception {
        setCodeCalibration();
        clearButton.setDisable(false);
    }

    @FXML
    void setClearButtonActionListener(ActionEvent event) throws Exception {
        setCodeCalibration();
    }
}