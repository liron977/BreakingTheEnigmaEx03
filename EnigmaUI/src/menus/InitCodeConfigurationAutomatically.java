package menus;


import mediator.Mediator;

public class InitCodeConfigurationAutomatically implements MenuManager {
    private Mediator mediator;
    public InitCodeConfigurationAutomatically (Mediator mediator){
        this.mediator = mediator;
    }
    @Override
    public void execution() {
       // if (mediator.isMachineWasDefined()) {
            mediator.initCodeConfigurationAutomatically();
      //  }
    }
}