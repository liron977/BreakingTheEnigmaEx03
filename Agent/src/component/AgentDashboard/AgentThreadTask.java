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
    private void setIsThreadTaskCreated(Boolean value){
        this.agentDashboardController.setIsThreadTaskCreatedProperty(value);
    }

    @Override
    protected Boolean call() throws Exception {
        Thread.currentThread().setName("AgentThreadTask");
        boolean isMissionsEnded=false;
        while (!isMissionsEnded) {
            isMissionsEnded=agentDashboardController.getMissions();
            setIsThreadTaskCreated(true);
        }
        System.out.println(Boolean.TRUE+"Boolean.TRUE");
        setIsThreadTaskCreated(false);
        return Boolean.TRUE;

    }
}