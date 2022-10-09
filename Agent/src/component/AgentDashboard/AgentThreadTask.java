package component.AgentDashboard;

import javafx.concurrent.Task;

public class AgentThreadTask extends Task<Boolean> {
 /*   private String selectedAlliesTeamName;
    private String amountOfMissionsPerAgent;
    private int amountOfThreads;*/
    AgentDashboardController agentDashboardController;

    public AgentThreadTask(AgentDashboardController agentDashboardController) {
       /* this.selectedAlliesTeamName = selectedAlliesTeamName;
        this.amountOfMissionsPerAgent = amountOfMissionsPerAgent;
        this.amountOfThreads = amountOfThreads;*/
        this.agentDashboardController=agentDashboardController;
    }

    @Override
    protected Boolean call() throws Exception {
        Thread.currentThread().setName("AgentThreadTask");
        boolean isMissionsEnded=false;
        while (!isMissionsEnded) {
            isMissionsEnded=agentDashboardController.getMissions();
        }
        return Boolean.TRUE;

    }
}