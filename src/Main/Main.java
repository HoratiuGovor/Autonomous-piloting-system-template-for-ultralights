package Main;

import Controller.Controller;
import UI.UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../FxmlFiles/sample.fxml"));
        primaryStage.setTitle("AirplaneAI");
        primaryStage.setScene(new Scene(root, 900, 500));
        UI ui = new UI(primaryStage);
        AirplaneController airplaneCtrl = new AirplaneController();
        AI ai = new AI(airplaneCtrl,ui);
        Controller ctrl = new Controller(ai);
        ctrl.run(primaryStage);
        //primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
