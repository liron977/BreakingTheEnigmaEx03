package Controllers.SingleDecryptionInfo;

import bruteForce.DecryptionInfoDTO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleDecryptionInfoController extends DecryptionInfo {

    @FXML
    private Label decryptionLabel;
    @FXML
    private Label codeConfigurationLabel;
    @FXML
    private Label agentIdLabel;
    public SingleDecryptionInfoController() {
        super("", "",-1);
    }
    @FXML
    private void initialize() {
        decryptionLabel.textProperty().bind(Bindings.concat("<", convertedStringProperty, ">"));
        codeConfigurationLabel.textProperty().bind(codeConfigurationProperty);
        agentIdLabel.textProperty().bind(Bindings.concat("Agent id: ",agentIdProperty.asString()));
    }
}
