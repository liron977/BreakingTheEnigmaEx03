package component.AgentDashboard;

import javafx.concurrent.Task;

public class AgentThreadTask extends Task<Boolean> {
    private String selectedAlliesTeamName;
    private String amountOfMissionsPerAgent;
    private int amountOfThreads;

    public AgentThreadTask(String selectedAlliesTeamName, String amountOfMissionsPerAgent, int amountOfThreads) {
        this.selectedAlliesTeamName = selectedAlliesTeamName;
        this.amountOfMissionsPerAgent = amountOfMissionsPerAgent;
        this.amountOfThreads = amountOfThreads;
    }

    @Override
    protected Boolean call() throws Exception {
        AgentDashboardController agentDashboardController = new AgentDashboardController(selectedAlliesTeamName, amountOfMissionsPerAgent, amountOfThreads);
        agentDashboardController.getGetMissions();
    }
}
