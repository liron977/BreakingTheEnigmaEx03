package MachineEngine;

import engine.theEnigmaEngine.Rotor;
import engine.theEnigmaEngine.TheMachineEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import machineDTO.ConvertedStringDTO;
import machineDTO.FullCodeDescriptionDTO;

public class MachineEngine implements Serializable {

   private TheMachineEngine theMachineEngine;
   private String theLastStartingPos;
   private int lastIndex;
   private boolean firstList;
   private int countPossibleStartingPosition;
    int[] currentPosition;
   private String[] keyboard;
   private String reflectorId;
   private FullCodeDescriptionDTO fullCodeDescriptionDTO;

    public MachineEngine(TheMachineEngine theMachineEngine){
        this.theMachineEngine=theMachineEngine;
        currentPosition=new int[theMachineEngine.getAmountOfUsedRotors()];
        getInitialStartingPosition();
       this.keyboard=theMachineEngine.getKeyboardAsArray();
    }
    public int getCountPossibleStartingPosition(){
        return countPossibleStartingPosition;
    }

    public TheMachineEngine getTheMachineEngine() {
        return theMachineEngine;
    }

    public String getInitialStartingPosition(){

        String initialStartingPosition="";
        int amountOfRotors=theMachineEngine.getAmountOfUsedRotors();
        char firstSignal=theMachineEngine.getKeyboard().charAt(0);
        for(int i=0;i<amountOfRotors;i++){
            initialStartingPosition=initialStartingPosition+firstSignal;
            currentPosition[i]=0;
        }
        theLastStartingPos=initialStartingPosition;
        lastIndex=0;
        firstList=true;
        countPossibleStartingPosition=0;

        return initialStartingPosition;
    }
    public MachineEngine cloneMachineEngine() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (MachineEngine) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    public String createPossiblePosition(String initialStartingPosition){
       int[] currentPosition = getIndexArrayFromString(initialStartingPosition);
       int[] nextPosition=getNextStartingPosition(1, currentPosition);
       return getStartingPositionStringFromIndexes(nextPosition);
    }

    public  int[] getNextStartingPosition(int missionSize, int[] currentPosition) {
        while (missionSize != 0) {
            while (currentPosition[currentPosition.length - 1] < keyboard.length - 1) {

                currentPosition[currentPosition.length - 1] = currentPosition[currentPosition.length - 1] + 1;
                missionSize--;
                if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {

                    break;
                }

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

                        break;
                    }

                    if (missionSize == 0 || isTheLastStartingPosition(currentPosition, keyboard.length)) {
                        break;
                    }

                }

            }

        }
        return currentPosition;
    }

    public  boolean isTheLastStartingPosition(int[] currentPosition, int keyboardSize) {
        for (int i = 0; i < currentPosition.length; i++) {
            if (!(currentPosition[i] == keyboardSize - 1)) {
                return false;
            }
        }
        return true;
    }

    public  List<Integer> initialStartingPosition(int rotorAmount) {
        List<Integer> currentPosition = new ArrayList<>();
        currentPosition.add(0);
        for (int i = 0; i < rotorAmount; i++) {
            currentPosition.add(0);
        }
        return currentPosition;
    }
    public  void printRes(int[] arr, String[] keyboard) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(keyboard[arr[i]]);
        }
        System.out.println();
        System.out.println("*****");
    }
    public  String getRes(int[] arr, String[] keyboard) {
        String currentPosition="";
        for (int i = 0; i < arr.length; i++) {
            currentPosition=currentPosition.concat(keyboard[arr[i]]);
        }
        return currentPosition;
    }
    public int[] getIndexArrayFromString(String position) {
        int[] indexArrayFromString = new int[position.length()];
        for (int i = 0; i < position.length(); i++) {
            indexArrayFromString[i] = Arrays.asList(keyboard).indexOf(String.valueOf(position.charAt(i)));
        }
        return indexArrayFromString;
    }
    public String getStartingPositionStringFromIndexes(int[] positionByIndex) {
        String startingPositionString ="";
        for (int i = 0; i < positionByIndex.length; i++) {
            startingPositionString+=keyboard[positionByIndex[i]];
        }
        return startingPositionString;

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
    public void createCurrentCodeDescriptionDTO() {
        FullCodeDescriptionDTO fullCodeDescriptionDTO = null;
            String[] usedRotorsId = theMachineEngine.getArrayOfRotorsId();
            String currentStartingPosition = theMachineEngine.getUsedRotors().getCurrentRotorsStartingPositions();

            String chosenStartingPosition = theMachineEngine.getUsedRotors().getRotorsOriginalStartingPositions();
            String reflectorId = theMachineEngine.getReflector().getReflectorId();
            List<String> notchPosition = theMachineEngine.getListOfNotch();
            List<String> originalNotchPosition = theMachineEngine.getOriginalNotchPositionList();
            List<String> pairsOfSwappingCharacter = theMachineEngine.getStringPairsOfSwappingCharacter();
            fullCodeDescriptionDTO = new FullCodeDescriptionDTO(pairsOfSwappingCharacter, reflectorId, chosenStartingPosition, currentStartingPosition, usedRotorsId, originalNotchPosition, notchPosition);
           this.fullCodeDescriptionDTO = fullCodeDescriptionDTO;
    }
    public String getCurrentCodeDescription() {

        List<String> notchList =getNotchList();
        // createCurrentCodeDescriptionDTO();
        return getCodeDescription(getCodeDescriptionDTO(), notchList, getCodeDescriptionDTO().getCurrentStartingPosition());
    }
    public List<String> getNotchList() {
        return theMachineEngine.getListOfNotch();
    }
    public FullCodeDescriptionDTO getCodeDescriptionDTO() {
        return fullCodeDescriptionDTO;
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
        ConvertedStringDTO convertedStringDTO = new ConvertedStringDTO(convertedString);


        return convertedStringDTO;
    }
    public void chooseManuallyReflect(String selectedReflectorId) {
        if (selectedReflectorId != "") {
            theMachineEngine.addSelectedReflector(selectedReflectorId);
            reflectorId=selectedReflectorId;

        }
    }
    public int getAmountOfUsedRotors() {
        return theMachineEngine.getAmountOfUsedRotors();
    }
    public String getBattlefieldLevel(){
        return theMachineEngine.getBattleFieldLevel();
    }




}
