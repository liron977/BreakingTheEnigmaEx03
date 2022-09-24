package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import machineDTO.TheMachineSettingsDTO;
import mediator.Mediator;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class MachineDetailsController implements EventsHandler {
    Mediator mediator;
    @FXML
    private GridPane grid;
    @FXML
    private VBox vbox1;
    @FXML
    private AnchorPane reflectorInfo;
    @FXML
    private Label ReflectorsAmountLable;
    @FXML
    private ImageView wheelsImage;
    @FXML
    private AnchorPane messagesAmount;
    @FXML
    private Label messagesAmountLabel;
    @FXML
    private AnchorPane anchor1;

    @FXML
    private Label rotorsAmountLable;
    @FXML
    private MachineTabController mainController;
    int column=0;
    int  row=0;
    private TheMachineSettingsDTO theMachineSettingsDTO;
    List<String> descriptions=new ArrayList<>();
    @FXML
    public void initialize() throws Exception{

    }
    public void setValues() throws Exception {
        descriptions = mediator.getMachineConfiguration();
        theMachineSettingsDTO = mediator.getTheMachineSettingsDTO();
        rotorsAmountLable.setText(theMachineSettingsDTO.getAmountOfUsedRotors() + " " + descriptions.get(0) + " " + theMachineSettingsDTO.getMaxAmountOfRotors());
        ReflectorsAmountLable.setText(String.valueOf(theMachineSettingsDTO.getAmountOfReflectors()) + " " + descriptions.get(1));
        messagesAmountLabel.setText(String.valueOf(theMachineSettingsDTO.getAmountOfProcessedMessages()) + " " + descriptions.get(2));
    }
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public void setMainController(MachineTabController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void eventHappened(EventObject event) throws Exception {
       setValues();
    }
}