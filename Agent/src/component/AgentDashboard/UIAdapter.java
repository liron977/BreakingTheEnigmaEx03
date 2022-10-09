package component.AgentDashboard;

import BruteForce.UiAdapterInterface;
import bruteForce.BruteForceResultDTO;
import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class  UIAdapter implements UiAdapterInterface {

    /*    private Consumer<List<BruteForceResultDTO>> updateServer;*/
    private Consumer<List<BruteForceResultDTO>> saveResultsOnServer;
    private Consumer<Integer> amountDoneMissionsPerAgent;
    private Consumer<Integer> amountOfMissionsInTheQueuePerAgent;
//    private Consumer<BlockingQueue<BruteForceResultDTO>> updateResultsOnAgent;

    //To delete!
    int count = 0;


    public UIAdapter(Consumer<List<BruteForceResultDTO>> saveResultsOnServer,Consumer<Integer> amountDoneMissionsPerAgent,Consumer<Integer> amountOfMissionsInTheQueuePerAgent) {
        this.saveResultsOnServer = saveResultsOnServer;
        this.amountDoneMissionsPerAgent=amountDoneMissionsPerAgent;
        this.amountOfMissionsInTheQueuePerAgent=amountOfMissionsInTheQueuePerAgent;
//        this.updateResultsOnAgent=updateResultsOnAgent;
        /*   this.updateServer = updateServer;*/

    }

   /* @Override
    public void updateServer(List<BruteForceResultDTO> bruteForceResultDTO) {
        Platform.runLater(()->updateServer.accept(bruteForceResultDTO));
    }*/

    @Override
    public void saveResultsOnServer(List<BruteForceResultDTO> bruteForceResultDTO) {
        saveResultsOnServer.accept(bruteForceResultDTO);
    }
  /*  @Override
    public void updateResultsOnAgent(BlockingQueue<BruteForceResultDTO> bruteForceResultDTO) {
       Platform.runLater(()-> updateResultsOnAgent.accept(bruteForceResultDTO));
    }*/


    public void updateAmountDoneMissionsPerAgent(int amountDoneMissionsPerAgentIn) {
        Platform.runLater(()->amountDoneMissionsPerAgent.accept(amountDoneMissionsPerAgentIn));
    }
    public void updateAmountMissionsInTheQueuePerAgent(int amountMissionsInTheQueue){
        Platform.runLater(()->amountOfMissionsInTheQueuePerAgent.accept(amountMissionsInTheQueue));

    }


}