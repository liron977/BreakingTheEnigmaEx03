package component.AgentDashboard;

import BruteForce.UiAdapterInterface;
import bruteForce.BruteForceResultDTO;
import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class  UIAdapter implements UiAdapterInterface {

  // private Consumer<List<BruteForceResultDTO>> updateServer;
   // private Consumer<BlockingQueue<BruteForceResultDTO>> saveResultsOnServer;
   private Consumer<BlockingQueue<BruteForceResultDTO>> updateResultsOnAgent;

    //To delete!
    int count = 0;


    public UIAdapter(Consumer<BlockingQueue<BruteForceResultDTO>> updateResultsOnAgent) {
       // this.saveResultsOnServer = saveResultsOnServer;
      this.updateResultsOnAgent=updateResultsOnAgent;
     /*   this.updateServer = updateServer;*/

    }

   /* @Override
    public void updateServer(List<BruteForceResultDTO> bruteForceResultDTO) {
        Platform.runLater(()->updateServer.accept(bruteForceResultDTO));
    }*/

/*    @Override
    public void saveResultsOnServer(BlockingQueue<BruteForceResultDTO> bruteForceResultDTO) {
        saveResultsOnServer.accept(bruteForceResultDTO);
        System.out.println(Thread.currentThread().getId());
    }*/
    @Override
    public void updateResultsOnAgent(BlockingQueue<BruteForceResultDTO> bruteForceResultDTO) {
       Platform.runLater(()-> updateResultsOnAgent.accept(bruteForceResultDTO));
    }





}