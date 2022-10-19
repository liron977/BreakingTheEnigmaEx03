package bruteForce;

public class AlliesConfirmedDTO {
    private String alliesName;
    private boolean isAlliesConfirmedGameOver;

    public AlliesConfirmedDTO(String alliesName,boolean isAlliesConfirmedGameOver){
        this.alliesName=alliesName;
        this.isAlliesConfirmedGameOver=isAlliesConfirmedGameOver;

    }

    public String getAlliesName() {
        return alliesName;
    }
     public boolean getIsAlliesConfirmedGameOver() {
        return isAlliesConfirmedGameOver;
    }

    public void setAlliesName(String alliesName) {
        this.alliesName = alliesName;
    }

    public void setAlliesConfirmedGameOver(boolean alliesConfirmedGameOver) {
        isAlliesConfirmedGameOver = alliesConfirmedGameOver;
    }
}