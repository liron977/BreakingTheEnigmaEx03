package BruteForce;

import bruteForce.BruteForceResultDTO;

import java.util.concurrent.BlockingQueue;

public interface UiAdapterInterface {

        /*public void updateServer(List<BruteForceResultDTO> bruteForceResultDTO);*/
        public void saveResultsOnServer(BlockingQueue<BruteForceResultDTO> bruteForceResultDTO);
   /*     public void updateResultsOnAgent(BlockingQueue<BruteForceResultDTO> bruteForceResultDTO);*/



}