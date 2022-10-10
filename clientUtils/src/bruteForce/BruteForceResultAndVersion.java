package bruteForce;

import java.io.*;
import java.util.List;

public class BruteForceResultAndVersion implements Serializable{
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