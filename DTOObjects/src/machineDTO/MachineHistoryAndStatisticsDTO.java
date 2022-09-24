package machineDTO;

import java.util.List;

public class MachineHistoryAndStatisticsDTO {
    private CodeDescriptionDTO codeDescriptionDTO;
    private List<String> historyToDisplay;
    private HistoryAndStatisticsDTO historyAndStatisticsDTO;
    public MachineHistoryAndStatisticsDTO(CodeDescriptionDTO codeDescriptionDTO, HistoryAndStatisticsDTO historyAndStatisticsDTO){
        this.historyAndStatisticsDTO=historyAndStatisticsDTO;
        this.codeDescriptionDTO = codeDescriptionDTO;
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
    public CodeDescriptionDTO getCurrentCodeDescriptionDTO() {
        return codeDescriptionDTO;
    }
}