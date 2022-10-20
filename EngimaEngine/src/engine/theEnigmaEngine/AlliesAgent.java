package engine.theEnigmaEngine;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AlliesAgent implements Serializable {
    private String agentName;
    private int threadsAmount;
    private int missionsAmount;
    private String alliesTeamName;

    private ThreadPoolExecutor threadPoolExecutor;
    private BlockingQueue<Runnable> blockingQueue;
    private int amountOfCandidatesStrings;
    private int amountOfReceivedMissions;
    private int amountOfMissionsToExecute;
    private boolean isDataShouldBeDelete;

    public AlliesAgent( String agentName, int threadsAmount,int missionsAmount, String alliesTeamName){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;
        this.alliesTeamName=alliesTeamName;
        this.amountOfCandidatesStrings=0;
        this.amountOfMissionsToExecute=0;
        this.amountOfReceivedMissions=0;
        this.isDataShouldBeDelete=false;
        /*
        blockingQueue = new LinkedBlockingQueue<Runnable>(missionsAmount);
        this.threadPoolExecutor = new ThreadPoolExecutor(threadsAmount, threadsAmount, 0L, TimeUnit.MILLISECONDS, blockingQueue);
       */
    }
    public void setIsDataShouldBeDelete(boolean isDataShouldBeDelete){
        this.isDataShouldBeDelete=isDataShouldBeDelete;
    }
    public boolean getIsDataShouldBeDelete(){
       return this.isDataShouldBeDelete;
    }
    public void setAmountOfCandidatesStrings(int amountOfCandidatesStrings) {
        this.amountOfCandidatesStrings = amountOfCandidatesStrings;
    }

    public void setAmountOfMissionsToExecute(int amountOfMissionsToExecute) {
        this.amountOfMissionsToExecute = amountOfMissionsToExecute;
    }

    public void setAmountOfReceivedMissions(int amountOfReceivedMissions) {
        this.amountOfReceivedMissions = amountOfReceivedMissions;
    }

    public void setBlockingQueue(BlockingQueue<Runnable> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setMissionsAmount(int missionsAmount) {
        this.missionsAmount = missionsAmount;
    }

    public void setThreadsAmount(int threadsAmount) {
        this.threadsAmount = threadsAmount;
    }

    public String getAlliesTeamName() {
        return alliesTeamName;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getMissionsAmount() {
        return missionsAmount;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public void clearValues(){
        this.amountOfCandidatesStrings=0;
        this.amountOfMissionsToExecute=0;
        this.amountOfReceivedMissions=0;
    }
}