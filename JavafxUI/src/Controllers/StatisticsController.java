
package Controllers;

        import javafx.fxml.FXML;
        import javafx.scene.control.Label;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.VBox;
        import machineDTO.CodeDescriptionDTO;
        import machineDTO.HistoryAndStatisticsDTO;
        import machineDTO.MachineHistoryAndStatisticsDTO;
        import mediator.Mediator;

        import java.util.ArrayList;
        import java.util.EventObject;
        import java.util.List;

public class StatisticsController  implements EventsHandler {

    private Mediator mediator;
    @FXML
    private HBox processedStringsHBox;
    @FXML
    private VBox history;

    @FXML
    private EncryptDecryptTabController encryptDecryptTabController;
    List<Label> configurationListLabel = new ArrayList<>();
    List<Label> historyPerConfigurationListLabel = new ArrayList<>();
    private List<EventsHandler> handlers = new ArrayList<>();

    private List<MachineHistoryAndStatisticsDTO> historyAndStatisticsList;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
        showHistory();
    }

    public void initialize() {

    }
    public void displayHistory() {
        CodeDescriptionDTO codeDescriptionDTO;
        HistoryAndStatisticsDTO historyAndStatisticsDTO;
        historyAndStatisticsList = mediator.getHistoryAndStatisticsList();
        int amountOfConfigurations = historyAndStatisticsList.size();
        processedStringsHBox.getChildren().clear();
        for (MachineHistoryAndStatisticsDTO machineHistoryAndStatisticsDTO : historyAndStatisticsList) {
            codeDescriptionDTO = machineHistoryAndStatisticsDTO.getCurrentCodeDescriptionDTO();
            historyAndStatisticsDTO = machineHistoryAndStatisticsDTO.getHistoryAndStatisticsDTO();
            Label currentDescription = new Label();
            currentDescription.setId("#GeneralLabel");
            configurationListLabel.add(currentDescription);
            Label historyAndStatistics = new Label();
            historyAndStatistics.setId("#GeneralLabel");
            historyPerConfigurationListLabel.add(historyAndStatistics);
            processedStringsHBox.getChildren().add(currentDescription);

        }
    }

    public void showHistory() {
        CodeDescriptionDTO codeDescriptionDTO;
        HistoryAndStatisticsDTO historyAndStatisticsDTO;
        historyAndStatisticsList = mediator.getHistoryAndStatistics();
        history.maxHeightProperty().set(Double.MAX_VALUE);
        history.getChildren().removeAll(history.getChildren());
        for (MachineHistoryAndStatisticsDTO machineHistory : historyAndStatisticsList) {
            List<String> historyStrings = machineHistory.getHistoryToDisplay();
            if (historyStrings != null) {
                for (int i = 0; i < historyStrings.size(); i++) {
                    Label currentDescription = new Label();
                    currentDescription.setText(historyStrings.get(i));
                    history.getChildren().add(currentDescription);
                }
                int x = 0;
            }
        }
    }

    public void setEncryptDecryptWindowController(EncryptDecryptTabController encryptDecryptTabController) {
        this.encryptDecryptTabController = encryptDecryptTabController;

    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
        resetStatisticsController();
        showHistory();
    }
    public void resetStatisticsController(){
        history.getChildren().clear();
    }
}