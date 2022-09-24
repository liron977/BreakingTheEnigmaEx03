package menus;


import mediator.Mediator;

import java.util.Scanner;

public class ReadCurrentStateFromFile implements  MenuManager{
    private Mediator mediator;
    public ReadCurrentStateFromFile(Mediator mediator){
        this.mediator=mediator;
    }
    @Override
    public void execution() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the full path name of the file without the suffix\nEnter Exit to return to the main menu");
        String fileName = scanner.nextLine().trim();
        if (!fileName.equals("")) {
            fileName += ".txt";
            mediator.readCurrentStateFromFile(fileName);

        }
    }
}
