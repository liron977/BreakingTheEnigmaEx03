package BruteForce;

import MachineEngine.MachineEngine;
import bruteForce.BruteForceResultDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import machineDTO.ConvertedStringDTO;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentMissionRunnable implements Runnable {
    private BlockingQueue<BruteForceResultDTO> resultsBlockingQueue;
    private MachineEngine machineEngineCopy;
    private String initialStartingPosition;
    private String stringToConvert;
    private String alliesTeamName;
    private String finalPosition;
    private int sizeOfMission;
    private List<String> listOfPossiblePosition;
    List<BruteForceResultDTO> resultDTOList;

    UiAdapterInterface uiAdapterInterface;
    int missionNumber=0;
    String lastStartingPos;
    public AgentMissionRunnable(String lastStartingPos,int missionNumber,UiAdapterInterface uiAdapterInterface,MachineEngine machineEngineCopy,
                                String stringToConvert, String alliesTeamName
            , String initialStartingPosition, int sizeOfMission) {

        this.machineEngineCopy = machineEngineCopy;
        this.stringToConvert = stringToConvert;
        this.initialStartingPosition = initialStartingPosition;
        this.alliesTeamName = alliesTeamName;
        this.sizeOfMission=sizeOfMission;
        this.resultsBlockingQueue= new LinkedBlockingQueue<>();
        this.missionNumber=missionNumber;
        this.lastStartingPos=lastStartingPos;

        this.uiAdapterInterface=uiAdapterInterface;
    }

    public void setResultsBlockingQueue(BlockingQueue<BruteForceResultDTO> resultsBlockingQueue) {
        this.resultsBlockingQueue = resultsBlockingQueue;
    }

    public BlockingQueue<BruteForceResultDTO> getResultsBlockingQueue() {
        return resultsBlockingQueue;
    }

    public synchronized void getConvertedStringsFounded() throws InterruptedException {
        int index = 0;
Thread.currentThread().setName("AgentMissionRunnable "+missionNumber);
        while (index<sizeOfMission){
            machineEngineCopy.chooseManuallyStartingPosition(initialStartingPosition);
            machineEngineCopy.createCurrentCodeDescriptionDTO();
            String convertedStringCode = machineEngineCopy.getCurrentCodeDescription();
            ConvertedStringDTO convertedStringDTOTemp = machineEngineCopy.getConvertedString(stringToConvert);
            if (machineEngineCopy.getTheMachineEngine().getDictionary().isStringExistsInTheDictionary(convertedStringDTOTemp.getConvertedString())) {
                BruteForceResultDTO bruteForceResultDTO = new BruteForceResultDTO(missionNumber,convertedStringDTOTemp.getConvertedString(), alliesTeamName, convertedStringCode);
                bruteForceResultDTO.setTheMissionNumber(missionNumber);
                resultsBlockingQueue.put(bruteForceResultDTO);
               /* System.out.println(bruteForceResultDTO.getConvertedString());
                System.out.println(bruteForceResultDTO.getCodeDescription());*/
            }
            if(index==999999998){
                int x=0;
            }
            if(initialStartingPosition.equals(lastStartingPos)){
                break;
            }
            initialStartingPosition= machineEngineCopy.createPossiblePosition(initialStartingPosition);
            index++;
        }
        publishResults();

    }
    private void publishResults() throws InterruptedException {
        synchronized (this){
            if(resultsBlockingQueue.size()>0) {
                for (BruteForceResultDTO brute:resultsBlockingQueue) {
                    if(brute.getConvertedString().equals("OR")){
                        int X=0;
                    }
                }
                getBruteForceResultDTOList();
                saveResultsInServer();

                uiAdapterInterface.updateResultsOnAgent(resultsBlockingQueue);
                System.out.println(Thread.currentThread().getId()+"runnable");

          /*      for (BruteForceResultDTO brute:resultsBlockingQueue) {
                    System.out.println("in runnable"+Thread.currentThread().getName());
                    System.out.println(brute.getConvertedString()+" "+brute.getCodeDescription()+" "+brute.getTheMissionNumber());
                }*/

                //uiAdapterInterface.updateResultsOnAgent(resultsBlockingQueue);
            }
        }
       // System.out.println(index+"index");
    }
    public void getBruteForceResultDTOList(){
        resultDTOList=new ArrayList<>();
        while (resultsBlockingQueue.size()!=0) {
            resultDTOList.add(resultsBlockingQueue.poll());
        }
    }

    public void saveResultsInServer(){
        System.out.println(Thread.currentThread().getId() +"saveResultsInServer");
        List<BruteForceResultDTO> bruteForceResultDTOList=resultDTOList;
        String bruteForceResultDTOListGson = Constants.GSON_INSTANCE.toJson(bruteForceResultDTOList);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), bruteForceResultDTOListGson);

        String finalUrl = HttpUrl
                .parse(Constants.BRUTE_FORCE_RESULTS)
                .newBuilder()
                .addQueryParameter("alliesTeamName", alliesTeamName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpClientUtil.getOkHttpClient().newCall(request);
        try {
            // Response response = call.execute();
            HttpClientUtil.runAsyncPost(finalUrl,body, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("not ok");
                            alert.getDialogPane().setExpanded(true);
                            alert.showAndWait();
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
        public void run() {
            try {
                getConvertedStringsFounded();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }