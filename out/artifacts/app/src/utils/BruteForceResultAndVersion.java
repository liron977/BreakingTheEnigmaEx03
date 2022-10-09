package utils;

import bruteForce.BruteForceResultDTO;

import java.util.List;

public class BruteForceResultAndVersion {
        final private List<BruteForceResultDTO> entries;
        final private int version;

        public BruteForceResultAndVersion(List<BruteForceResultDTO> entries, int version) {
            this.entries = entries;
            this.version = version;
        }

    public synchronized int getVersion() {
        return version;
    }

    public synchronized List<BruteForceResultDTO> getEntries() {
        return entries;
    }
}