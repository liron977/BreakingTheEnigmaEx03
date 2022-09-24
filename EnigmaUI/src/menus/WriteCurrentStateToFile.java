package menus;


import mediator.Mediator;

import java.util.Scanner;

public class WriteCurrentStateToFile implements  MenuManager{
    private Mediator mediator;
    public WriteCurrentStateToFile(Mediator mediator){
        this.mediator=mediator;
    }
    @Override
    public void execution() {
      //  if(mediator.isMachineWasDefined()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the full path name of the file without the suffix\n" +
                    "Enter ENTER for return to the main menu");
            String fileName = scanner.nextLine().trim();
            if (!fileName.equals("")) {
                fileName += ".txt";
                mediator.writeCurrentStateToFile(fileName);
            }
        }
    }
//}