package engine.theEnigmaEngine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AlliesAgent {
    private String agentName;
    private int threadsAmount;
    private int missionsAmount;
    private String alliesTeamName;

    private ThreadPoolExecutor threadPoolExecutor;
    private BlockingQueue<Runnable> blockingQueue;


    public AlliesAgent( String agentName, int threadsAmount,int missionsAmount, String alliesTeamName){
        this.agentName=agentName;
        this.missionsAmount=missionsAmount;
        this.threadsAmount=threadsAmount;
        this.alliesTeamName=alliesTeamName;
        /*
        blockingQueue = new LinkedBlockingQueue<Runnable>(missionsAmount);
        this.threadPoolExecutor = new ThreadPoolExecutor(threadsAmount, threadsAmount, 0L, TimeUnit.MILLISECONDS, blockingQueue);
       */
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
}