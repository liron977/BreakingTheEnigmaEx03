package menus;

import mediator.Mediator;

import java.util.Scanner;

public class InitCodeConfigurationManually implements MenuManager {
    public Mediator mediator;
    private Scanner scanner;
    boolean isUserInputRotorsIsValid;
   private boolean isUserInputReflectIdIsValid;
    private boolean isStartingPositionAreValid;
    private boolean isPlugBoardIsValid;
    private boolean isUserWantsToExit;
    private  boolean isUserInputIsValid ;
    private String selectedRotors;
    private String selectedReflector;
    private String selectedStartingPosition;
    private String selectedPlugBoardPairs="";




    public InitCodeConfigurationManually(Mediator mediator) {
         scanner = new Scanner(System.in);
        this.mediator = mediator;
         isUserInputRotorsIsValid = false;
         isUserInputReflectIdIsValid = false;
         isStartingPositionAreValid = false;
         isPlugBoardIsValid = false;
        isUserInputIsValid = false;
    }
    @Override
    public void execution() throws Exception {
        initCodeConfiguration();
    }

    private void printRotorDescription() {
        int amountOfRotors=mediator.getAmountOfUsedRotors();
        String str = "Please enter "+amountOfRotors+" rotors ID numbers + order between them. \n(for " +
                "example:if you choose 2 rotors with id:7,3 and the rotor(7) appears on the far right, insert <3,7>)";
        System.out.println(str);
    }

    private void printStartPositionDescriptionDescription() {
        int amountOfRotors=mediator.getAmountOfUsedRotors();
        String str = "Please enter the initial position for "+amountOfRotors+" selected rotors,series of valid characters from the ABC of the machine without separation" +
                " between them (for example <AO>,A position is for rotor(7) abf O for rotor(3))";

        System.out.println(str);
    }

    private void printPlugBoardDescription() {
        String str = "Please enter pairs of characters from the ABS of the machine that will be replaced (for example if you insert AZ ,A and Z will be replaced)";
        System.out.println(str);
    }

    private void printReflectorIdDescription() throws Exception {
        String str = "Please choose a reflector:\n"+mediator.getAvailableReflectorsId();
        System.out.println(str);
    }
    private void initCodeConfiguration() throws Exception {
     /*  // if (mediator.isMachineWasDefined()) {
            printRotorDescription();
            String loadStart = scanner.nextLine();
            while (!isUserInputRotorsIsValid) {
                if (mediator.isRotorsIDinInitCodeManuallyIsValid(loadStart)) {
                    isUserInputRotorsIsValid = true;
                    System.out.println("Rotors id updated successfully");
                    printStartPositionDescriptionDescription();
                    loadStart = scanner.nextLine();
                    while (!isStartingPositionAreValid) {
                        if (mediator.isStartingPositionInitCodeManuallyIsValid(loadStart)) {
                            isStartingPositionAreValid = true;
                            //mediator.initStartingPositionConfigurationManually(loadStart);
                            selectedStartingPosition=loadStart;
                            System.out.println("Starting position for each rotor updated successfully");
                            printReflectorIdDescription();
                            loadStart = scanner.nextLine();
                            while (!isUserInputReflectIdIsValid) {
                                if (mediator.isReflectoIDinInitCodeManuallyIsValid(loadStart)) {
                                    isUserInputReflectIdIsValid = true;
                                    System.out.println("Reflector id updated successfully");
                                } else {
                                    System.out.println("Please try again \nIf you want to exit please press ENTER(The configuration will not be saved)");
                                    loadStart = scanner.nextLine();
                                    exitFromInitCodeManually(loadStart);
                                }
                            }
                            if (!isUserWantsToExit) {
                                if (isPlayerWantsPlugBoard()) {
                                    printPlugBoardDescription();
                                    loadStart = scanner.nextLine();
                                   // selectedPlugBoardPairs= loadStart;
                                    while (!isPlugBoardIsValid) {
                                        if (!loadStart.equals("")) {
                                            if (mediator.isPlagBoardinInitCodeManuallyIsValid(loadStart) && !mediator.isChooseToExit(loadStart)) {
                                                isPlugBoardIsValid = true;
                                                selectedPlugBoardPairs= loadStart;

                                                System.out.println("Plug board updated successfully");
                                                saveCodeConfiguration();

                                            } else {
                                                System.out.println("Please try again\nIf you want to exit please press ENTER(The configuration will not be saved)");
                                                loadStart = scanner.nextLine();
                                                //selectedPlugBoardPairs= loadStart;
                                                exitFromInitCodeManually(loadStart);
                                            }
                                        } else {
                                            isPlugBoardIsValid = true;
                                            mediator.resetPlugBoard();
                                            System.out.println("Plug board was not updated");
                                            saveCodeConfiguration();
                                        }
                                    }
                                }
                                else {
                                    mediator.resetPlugBoard();
                                    saveCodeConfiguration();
                                }

                            }
                        } else {
                            System.out.println("Please try again\nIf you want to exit please press ENTER(The configuration will not be saved)");
                            loadStart = scanner.nextLine();
                            //selectedPlugBoardPairs= loadStart;
                            exitFromInitCodeManually(loadStart);
                        }
                    }
                } else {
                    System.out.println("Please try again \nIf you want to exit please press ENTER(The configuration will not be saved)");
                    loadStart = scanner.nextLine();
                   // selectedPlugBoardPairs= loadStart;
                    exitFromInitCodeManually(loadStart);
                }
            }
            if (!isUserWantsToExit) {
                mediator.setIsCodeConfigurationWasdefine();
                mediator.printCodeConfiguration();
            }
        }*/
    }
private void saveCodeConfiguration(){
        mediator.saveRotors();
    mediator.initStartingPositionConfigurationManually(selectedStartingPosition);
    mediator.saveReflector("I");
    mediator.initPlugBoardConfigurationManually(selectedPlugBoardPairs);

}
    private boolean isPlayerWantsPlugBoard() {
        System.out.println("Do you want to define a plug board \n 1)Yes \n 2)No");
        String userInput = scanner.nextLine();
        while (!isUserInputIsValid) {
            isUserInputIsValid = mediator.isPlayerDefinePlugBoardIsValid(userInput);
            if (isUserInputIsValid) {
                if (userInput.equals("1")) {
                    return true;
                } else {
                    return false;

                }
            } else {
                System.out.println("Please try again \nIf you want to exit please press ENTER(The configuration will not be saved)");
                userInput = scanner.nextLine();
                exitFromInitCodeManually(userInput);
            }
        }
        return false;
    }
    private void exitFromInitCodeManually(String loadStart){
        if(loadStart.equals("")){
            isUserInputRotorsIsValid = true;
            isUserInputReflectIdIsValid = true;
            isStartingPositionAreValid = true;
            isPlugBoardIsValid = true;
            isUserWantsToExit =true;
            isUserInputIsValid=true;
        }
    }
}