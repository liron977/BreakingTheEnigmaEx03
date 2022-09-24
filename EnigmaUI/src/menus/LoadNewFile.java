package menus;


import mediator.Mediator;

public class LoadNewFile implements MenuManager {

    private Mediator mediator;
   private boolean isFileLoadSuccessfully;
   private boolean isFileNameValid;
    public LoadNewFile(Mediator mediator){
        this.mediator = mediator;
        isFileLoadSuccessfully=false;
        isFileNameValid=false;
    }

    @Override
    public void execution() {
       /* Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert full xml path \n");
        String loadStart = scanner.nextLine();
        loadStart=loadStart.toLowerCase();
        while (!isFileNameValid){
            if (mediator.fileNameValidation(loadStart)) {
                isFileNameValid = true;
                while (!isFileLoadSuccessfully) {
                    try {
                        if (mediator.isFileLoadSuccessfully(loadStart)) {
                            isFileLoadSuccessfully = true;
                            System.out.println("The xml was uploaded successfully");
                            break;
                        }
                        else {
                            System.out.println("Please insert updated xml path,if you want to exit please press ENTER");
                            loadStart = scanner.nextLine();
                            loadStart=loadStart.toLowerCase();
                            exitFromLoadNewFile(loadStart);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage()+" if you want to exit please press ENTER");
                        loadStart = scanner.nextLine();
                        loadStart=loadStart.toLowerCase();
                        exitFromLoadNewFile(loadStart);
                    }
                }
            }
            else {
                System.out.println("Please insert full xml path,if you want to exit please press ENTER");
                loadStart = scanner.nextLine();
                loadStart=loadStart.toLowerCase();
                exitFromLoadNewFile(loadStart);
            }
        }*/
    }
    private void exitFromLoadNewFile(String loadStart){
        if(loadStart.equals("")){
            isFileLoadSuccessfully=true;
            isFileNameValid=true;
        }
    }

}