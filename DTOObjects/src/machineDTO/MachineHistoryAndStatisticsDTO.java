package machineDTO;

import java.util.List;

public class MachineHistoryAndStatisticsDTO {
    private FullCodeDescriptionDTO fullCodeDescriptionDTO;
    private List<String> historyToDisplay;
    private HistoryAndStatisticsDTO historyAndStatisticsDTO;
    public MachineHistoryAndStatisticsDTO(FullCodeDescriptionDTO fullCodeDescriptionDTO, HistoryAndStatisticsDTO historyAndStatisticsDTO){
        this.historyAndStatisticsDTO=historyAndStatisticsDTO;
        this.fullCodeDescriptionDTO = fullCodeDescriptionDTO;
    }
    public void setHistoryToDisplay(List<String> historyToDisplay) {
        this.historyToDisplay = historyToDisplay;
    }
    public List<String> getHistoryToDisplay() {
        return historyToDisplay;
    }
    public HistoryAndStatisticsDTO getHistoryAndStatisticsDTO() {
        return historyAndStatisticsDTO;
    }
    public FullCodeDescriptionDTO getCurrentCodeDescriptionDTO() {
        return fullCodeDescriptionDTO;
    }
}