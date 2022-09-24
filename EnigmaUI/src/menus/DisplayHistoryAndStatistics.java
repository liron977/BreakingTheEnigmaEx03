package menus;


import mediator.Mediator;

public class DisplayHistoryAndStatistics implements MenuManager{
    private Mediator mediator;
    public DisplayHistoryAndStatistics(Mediator mediator){
        this.mediator = mediator;
    }
    @Override
    public void execution() {
        //boolean isMachineWasDefined= mediator.isMachineWasDefined();
        //boolean isCodeWasDefined= mediator.isCodeWasDefined();
      //  if(isMachineWasDefined&&isCodeWasDefined) {
            mediator.getHistoryAndStatistics();
      //  }
    }
}