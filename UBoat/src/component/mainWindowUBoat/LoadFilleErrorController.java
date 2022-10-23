package component.mainWindowUBoat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import machineEngine.EngineManager;
import machineEngine.EngineManagerInterface;
import uiMediator.Mediator;

import java.io.IOException;
import java.net.URL;

public class LoadFilleErrorController {

    @FXML
    private TextArea errors;

    @FXML
    private ScrollPane errorsScrollPane;

    public void setErrorMessage(String error){
        errors.setText(error);
    }


}
