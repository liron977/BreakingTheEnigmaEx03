package bruteForceLogic;

import bruteForce.DecryptionInfoDTO;

public interface UiAdapterInterface {
    public void updateTileOnDisplay(DecryptionInfoDTO decryptionInfoDTO);
    public void updateCounterOnDisplay(Integer count);
    public void updateProgressBarDuringTask(Double progress);
    public void updateProcessTimeOnFinish(long progressTime);
    public void updateAmountOfMissionsPerLevel(String amountOfMissionsPerLevelIn);
    public void initAmountOfMissionsPerLevel();
    public void updateAverageOfMissionProcessTime(Double amountOfMissionsPerLevelIn);
}