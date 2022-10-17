package bruteForce;

public class CurrentAlliesStatus {

  private String alliesName;
  private boolean isContestEnded;
  public CurrentAlliesStatus(String alliesName,boolean isContestEnded){
    this.alliesName=alliesName;
    this.isContestEnded=isContestEnded;
  }

  public String getAlliesName() {
    return alliesName;
  }
  public boolean getisContestEnded() {
    return isContestEnded;
  }

  public void setAlliesName(String alliesName) {
    this.alliesName = alliesName;
  }

  public void setContestEnded(boolean contestEnded) {
    isContestEnded = contestEnded;
  }
}
