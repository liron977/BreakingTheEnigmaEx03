package console;

import mediator.Mediator;
import menus.*;

import java.util.Scanner;

public class Menu {

    private final int FIRST_OPTION = 1;
    private final int EXIT_OPTION = 8;
    private final int LAST_OPTION = 10;
   private Mediator mediator;

    public Menu(Mediator mediator) {
        this.mediator = mediator;
    }

    public void printMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("Select option (press number " + FIRST_OPTION + "-" + LAST_OPTION + "):");
        System.out.println("1. Load new file");
        System.out.println("2. Get general information about the machine specifications");
        System.out.println("3. Choose an initial code configuration (manually)");
        System.out.println("4. Choose an initial code configuration (automatically)");
        System.out.println("5. Input processing");
        System.out.println("6. Resetting the current code");
        System.out.println("7. History and statistics");
        System.out.println("8. Exit");
        System.out.println("9. Save the current machine state to file");
        System.out.println("10. Read the current machine state from file");
        System.out.println("-------------------------------------------------");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        do {
            printMenu();
            try {
                userChoice = scanner.nextInt();
                if (userChoice <= LAST_OPTION && userChoice >= FIRST_OPTION) {
                    executeMenu(userChoice);
                } else if (userChoice != EXIT_OPTION) {
                    System.out.println("please select a valid option,between [" + FIRST_OPTION + "-" + LAST_OPTION + "]");
                }
            }catch (Exception e){
                System.out.println("Insert only numbers between [" + FIRST_OPTION + "-" + LAST_OPTION + "]");
                scanner.nextLine();
            }
        } while (userChoice != EXIT_OPTION);
        System.out.println("Thanks,bye");
    }
    public void executeMenu(int userChoice) throws Exception {
        switch (userChoice) {
            case 1:
                MenuManager loadNewFile = new LoadNewFile(mediator);
                loadNewFile.execution();
                break;
       case 2:
           MenuManager displayMachineConfiguration=new DisplayMachineConfiguration(mediator);
           displayMachineConfiguration.execution();
           break;
        case 3:
            MenuManager initCodeConfigurationManually  =new InitCodeConfigurationManually(mediator);
            initCodeConfigurationManually.execution();
            break;
      case 4:
          MenuManager initCodeConfigurationAutomatically=new InitCodeConfigurationAutomatically(mediator);
          initCodeConfigurationAutomatically.execution();
            break;
          case 5:
              MenuManager inputProcess=new InputProcess(mediator);
              inputProcess.execution();
              break;
       case 6:
           MenuManager resetCurrentCode=new ResetCurrentCode(mediator);
           resetCurrentCode.execution();
           break;
        case 7:
            MenuManager historyAndStatistics=new DisplayHistoryAndStatistics(mediator);
            historyAndStatistics.execution();
            break;

        case 9:
        MenuManager writeCurrentStateToFile=new WriteCurrentStateToFile(mediator);
            writeCurrentStateToFile.execution();
        break;

        case 10:
    MenuManager readCurrentStateFromFile=new ReadCurrentStateFromFile(mediator);
            readCurrentStateFromFile.execution();
            break;

}
    }
}