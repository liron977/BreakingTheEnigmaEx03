package machineDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FullCodeDescriptionDTO implements Serializable {
    private String[] usedRotorsId;
    private String currentStartingPosition;
    private String chosenStartingPosition;
    private String reflectorId;
    private List<String> pairsOfSwappingCharacter=new ArrayList<>();
    private List<String> notchPosition=new ArrayList<>();
    private  List<String> originalNotchPosition=new ArrayList<>();
    public FullCodeDescriptionDTO(List<String> pairsOfSwappingCharacter, String reflectorId, String chosenStartingPosition, String currentStartingPosition, String[] usedRotorsId, List<String> originalNotchPosition, List<String> notchPosition) {
        this.pairsOfSwappingCharacter = pairsOfSwappingCharacter;
        this.reflectorId = reflectorId;
        this.chosenStartingPosition = chosenStartingPosition;
        this.usedRotorsId = usedRotorsId;
        this.originalNotchPosition = originalNotchPosition;
        this.notchPosition = notchPosition;
        this.currentStartingPosition = currentStartingPosition;
    }

    public List<String> getPairsOfSwappingCharacter() {
        return pairsOfSwappingCharacter;
    }

    public String getChosenStartingPosition() {
        return chosenStartingPosition;
    }

    public String getCurrentStartingPosition() {
        return currentStartingPosition;
    }

    public String getReflectorId() {
        return reflectorId;
    }

    public String[] getUsedRotorsId() {
        return usedRotorsId;
    }
    public List<String> getNotchPosition() {
        return notchPosition;
    }

    public List<String> getOriginalNotchPosition(){return originalNotchPosition;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullCodeDescriptionDTO that = (FullCodeDescriptionDTO) o;
        return Arrays.equals(usedRotorsId, that.usedRotorsId) && Objects.equals(chosenStartingPosition, that.chosenStartingPosition) && Objects.equals(reflectorId, that.reflectorId) && Objects.equals(pairsOfSwappingCharacter, that.pairsOfSwappingCharacter)  && Objects.equals(originalNotchPosition, that.originalNotchPosition);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(chosenStartingPosition, reflectorId, pairsOfSwappingCharacter);
        result = 31 * result + Arrays.hashCode(usedRotorsId);
        return result;
    }
}