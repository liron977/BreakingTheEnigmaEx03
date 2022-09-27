package historyAndStatistics;

import machineDTO.FullCodeDescriptionDTO;

import java.io.Serializable;
import java.util.*;

public class MachineHistoryAndStatistics implements Serializable {
   private Map<FullCodeDescriptionDTO, List<HistoryOfProcess>> machineHistory = new HashMap<>();
    public void addNewMachineSettings(FullCodeDescriptionDTO fullCodeDescriptionDTO) {
if(!machineHistory.containsKey(fullCodeDescriptionDTO)) {
    List<HistoryOfProcess> historyOfProcesses = new ArrayList<HistoryOfProcess>();
    machineHistory.put(fullCodeDescriptionDTO, historyOfProcesses);
}
    }
    public Map<FullCodeDescriptionDTO, List<HistoryOfProcess>> getMachineHistory() {
        return machineHistory;
    }
    public void addNewProcess(FullCodeDescriptionDTO fullCodeDescriptionDTO, HistoryOfProcess newProcess) {
        List<HistoryOfProcess> historyOfProcesses = machineHistory.get(fullCodeDescriptionDTO);
        if (historyOfProcesses == null) {
            historyOfProcesses = new ArrayList<HistoryOfProcess>();
        }
        historyOfProcesses.add(newProcess);
        machineHistory.put(fullCodeDescriptionDTO, historyOfProcesses);
    }

}