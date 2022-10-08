package component.uBoatContestTab;

import bruteForce.BruteForceResultDTO;

import java.util.List;

public class BruteForceResultAndVersion {
    private int version;
    private List<BruteForceResultDTO> entries;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BruteForceResultDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<BruteForceResultDTO> entries) {
        this.entries = entries;
    }
}
