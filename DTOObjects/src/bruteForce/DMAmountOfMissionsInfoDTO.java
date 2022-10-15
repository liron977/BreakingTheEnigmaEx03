package bruteForce;

public class DMAmountOfMissionsInfoDTO {
    private Long totalAmountOfCreatedMissions;
    private Long amountOfCreatedMissions;
    private boolean isUboatExist;
    public DMAmountOfMissionsInfoDTO( boolean isUboatExist,Long totalAmountOfCreatedMissions,Long amountOfCreatedMissions){
        this.amountOfCreatedMissions=amountOfCreatedMissions;
        this.totalAmountOfCreatedMissions=totalAmountOfCreatedMissions;
        this.isUboatExist=isUboatExist;
    }

    public void setUboatExist(boolean uboatExist) {
        isUboatExist = uboatExist;
    }
    public boolean getIsUboatExist(){
        return isUboatExist;
    }

    public Long getAmountOfCreatedMissions() {
        return amountOfCreatedMissions;
    }

    public Long getTotalAmountOfCreatedMissions() {
        return totalAmountOfCreatedMissions;
    }
}