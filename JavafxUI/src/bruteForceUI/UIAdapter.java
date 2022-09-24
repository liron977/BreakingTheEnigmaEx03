package bruteForceUI;

import bruteForceLogic.UiAdapterInterface;
import bruteForce.DecryptionInfoDTO;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter implements UiAdapterInterface {

    private Consumer<DecryptionInfoDTO> updateTileOnDisplay;
    private Consumer<Integer> countDecryption;
    private Consumer<Double> updateProgressBar;
    private Consumer<Long> processTime;
    private Consumer<String> amountOfMissionsPerLevel;
    private Consumer<Double> averageOfMissionProcessingTime;
    Consumer<String> initAmountOfMissionsPerLevel;
    //To delete!
    int count=0;


    public UIAdapter(Consumer<DecryptionInfoDTO> updateTaskDisplay,Consumer<Integer> countDecryption,Consumer<Double> updateProgressBar,Consumer<Long> processTime,Consumer<String> amountOfMissionsPerLevel,Consumer<String> initAmountOfMissionsPerLevel,Consumer<Double> averageOfMissionProcessingTime) {
        this.updateTileOnDisplay = updateTaskDisplay;
        this.countDecryption=countDecryption;
        this.updateProgressBar=updateProgressBar;
        this.amountOfMissionsPerLevel=amountOfMissionsPerLevel;
        this.processTime=processTime;
        this.initAmountOfMissionsPerLevel=initAmountOfMissionsPerLevel;
        this.averageOfMissionProcessingTime=averageOfMissionProcessingTime;
    }
    @Override
    public void updateTileOnDisplay(DecryptionInfoDTO decryptionInfoDTO) {
        Platform.runLater(() -> updateTileOnDisplay.accept(decryptionInfoDTO));
        count++;

    }

    public int getCount() {
        return count;
    }

    @Override
    public void updateCounterOnDisplay(Integer count) {
        Platform.runLater(() -> countDecryption.accept(count));
    }
    @Override
    public void updateProgressBarDuringTask(Double progress){
        Platform.runLater(()->updateProgressBar.accept(progress));
    }
    @Override
    public void updateProcessTimeOnFinish(long progressTime){
        Platform.runLater(()->processTime.accept(progressTime));
    }
    @Override
    public void updateAmountOfMissionsPerLevel(String amountOfMissionsPerLevelIn){
        Platform.runLater(()->amountOfMissionsPerLevel.accept(amountOfMissionsPerLevelIn));
    }
    @Override
    public void initAmountOfMissionsPerLevel(){
        Platform.runLater(()->initAmountOfMissionsPerLevel.accept(""));
    }
    @Override
    public void updateAverageOfMissionProcessTime(Double amountOfMissionsPerLevelIn) {
        Platform.runLater(()->averageOfMissionProcessingTime.accept(amountOfMissionsPerLevelIn));
    }

}