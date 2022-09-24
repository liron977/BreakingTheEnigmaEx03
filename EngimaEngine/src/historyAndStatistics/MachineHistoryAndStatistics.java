package historyAndStatistics;

import machineDTO.CodeDescriptionDTO;

import java.io.Serializable;
import java.util.*;

public class MachineHistoryAndStatistics implements Serializable {
   private Map<CodeDescriptionDTO, List<HistoryOfProcess>> machineHistory = new HashMap<>();
    public void addNewMachineSettings(CodeDescriptionDTO codeDescriptionDTO) {
if(!machineHistory.containsKey(codeDescriptionDTO)) {
    List<HistoryOfProcess> historyOfProcesses = new ArrayList<HistoryOfProcess>();
    machineHistory.put(codeDescriptionDTO, historyOfProcesses);
}
    }
    public Map<CodeDescriptionDTO, List<HistoryOfProcess>> getMachineHistory() {
        return machineHistory;
    }
    public void addNewProcess(CodeDescriptionDTO codeDescriptionDTO, HistoryOfProcess newProcess) {
        List<HistoryOfProcess> historyOfProcesses = machineHistory.get(codeDescriptionDTO);
        if (historyOfProcesses == null) {
            historyOfProcesses = new ArrayList<HistoryOfProcess>();
        }
        historyOfProcesses.add(newProcess);
        machineHistory.put(codeDescriptionDTO, historyOfProcesses);
    }

}