package utils;

import bruteForce.BruteForceResultDTO;

import java.io.*;
import java.util.List;

public class BruteForceResultAndVersion implements Serializable {
         private List<BruteForceResultDTO> entries;
         private int version;

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
    public BruteForceResultAndVersion cloneBruteForceResultAndVersion() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (BruteForceResultAndVersion) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}