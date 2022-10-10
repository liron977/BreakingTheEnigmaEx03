package bruteForce;

public class DMAmountOfMissionsInfoDTO {
    private Long totalAmountOfCreatedMissions;
    private Long amountOfCreatedMissions;
    public DMAmountOfMissionsInfoDTO(Long totalAmountOfCreatedMissions,Long amountOfCreatedMissions){
        this.amountOfCreatedMissions=amountOfCreatedMissions;
        this.totalAmountOfCreatedMissions=totalAmountOfCreatedMissions;
    }

    public Long getAmountOfCreatedMissions() {
        return amountOfCreatedMissions;
    }

    public Long getTotalAmountOfCreatedMissions() {
        return totalAmountOfCreatedMissions;
    }
}
