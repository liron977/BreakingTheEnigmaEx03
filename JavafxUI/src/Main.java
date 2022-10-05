import Controllers.MainWindowController;
import machineEngine.EngineManager;
import machineEngine.EngineManagerInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import mediator.Mediator;

import java.net.URL;


public class Main extends Application {

    public static void main(String[] args){
        Thread.currentThread().setName("main");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        EngineManagerInterface engineManager=new EngineManager();
        Mediator mediator=new Mediator(engineManager);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/Fxml/MainWindow.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        MainWindowController mainWindowController =fxmlLoader.getController();
        mainWindowController.setMediator(mediator);

        Scene scene = new Scene(root,900,630);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/CSS/BlueStyle.css").toExternalForm());

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("You're about to exit!");
            alert.setContentText("Are you sure that you want to exit?");
            alert.getDialogPane().setExpanded(true);
            if(alert.showAndWait().get()== ButtonType.OK){
                primaryStage.close();
            }
        });

    }
}