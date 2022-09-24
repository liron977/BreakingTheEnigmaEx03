package menus;

import mediator.Mediator;

import java.util.Scanner;

public class InputProcess implements MenuManager{
    private String userInput;
    private Mediator mediator;
    private boolean isValidInputProcess;
    private boolean isMachineWasDefined;
    private boolean isCodeWasDefined;
    public InputProcess(Mediator mediator){
        this.mediator = mediator;
        //isMachineWasDefined= mediator.isMachineWasDefined();
        //isCodeWasDefined= mediator.isCodeWasDefined();
        isValidInputProcess=false;
    }
    @Override
    public void execution() {
        if(isMachineWasDefined&&isCodeWasDefined) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the string to process");
            userInput = scanner.nextLine();
            while (!isValidInputProcess) {
                isValidInputProcess = mediator.isUserStringToProcessIsValid(userInput);
                if (isValidInputProcess) {
                    System.out.println("The converted string is (displayed in <>): \n<" + mediator.getConvertedString(userInput) +">");
                } else {
                    System.out.println("Please try again,if you want to exit please press ENTER");
                    userInput = scanner.nextLine();
                    exitFromInitCodeManually(userInput);
                }
            }
        }
    }
    private void exitFromInitCodeManually(String loadStart){
        if(loadStart.equals("")){
             isMachineWasDefined= false;
             isCodeWasDefined= false;
            isValidInputProcess=true;
        }
    }
}