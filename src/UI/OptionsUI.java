package UI;

import Utills.OptionsPackage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.soap.Text;

import java.io.IOException;

public class OptionsUI {
    private float airDensity;
    private float wind;
    private float wingSurface;
    private float maxCilinderTemp;
    private int autopilotSafeAlt;
    private Button backButton,applyButton;
    OptionsPackage pack;
    private TextField airDensityTextField,windTextField,wingSurfaceTextField,maxCilinderTempTextField,autopilotSafeAltTextField;
    public OptionsUI(float airDensity,float wind,float wingSurface,float maxCilinderTemp,int autopilotSafeAlt){
            this.airDensity = airDensity;
            this.wind = wind;
            this.wingSurface = wingSurface;
            this.maxCilinderTemp = maxCilinderTemp;
            this.autopilotSafeAlt = autopilotSafeAlt;
            pack = new OptionsPackage(airDensity,wind,wingSurface,maxCilinderTemp,autopilotSafeAlt);

    }

    public OptionsPackage run() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../FxmlFiles/optionsUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 250);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            //stage.show();
            this.backButton = (Button) scene.lookup("#backButton");
            this.applyButton = (Button) scene.lookup("#applyButton");

            this.airDensityTextField = (javafx.scene.control.TextField) scene.lookup("#airDensityTextField");
            this.windTextField = (javafx.scene.control.TextField) scene.lookup("#windTextField");
            this.wingSurfaceTextField = (javafx.scene.control.TextField) scene.lookup("#wingSurfaceTextField");
            this.maxCilinderTempTextField = (javafx.scene.control.TextField) scene.lookup("#maxCilinderTempTextField");
            this.autopilotSafeAltTextField = (javafx.scene.control.TextField) scene.lookup("#autopilotSafeAltTextField");

            this.airDensityTextField.setText(Float.toString(airDensity));
            this.windTextField.setText(Float.toString(wind));
            this.wingSurfaceTextField.setText(Float.toString(wingSurface));
            this.maxCilinderTempTextField.setText(Float.toString(maxCilinderTemp));
            this.autopilotSafeAltTextField.setText(Integer.toString(autopilotSafeAlt));
            //pack = new OptionsPackage(airDensity,wind,wingSurface,maxCilinderTemp,autopilotSafeAlt);
        } catch (IOException e) {
        }
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.close();
            }
        });
        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                try {
                    airDensity = Float.parseFloat(airDensityTextField.getText());
                    wind = Float.parseFloat(windTextField.getText());
                    wingSurface = Float.parseFloat(wingSurfaceTextField.getText());
                    maxCilinderTemp = Float.parseFloat(maxCilinderTempTextField.getText());
                    autopilotSafeAlt = Integer.parseInt(autopilotSafeAltTextField.getText());
                    pack = new OptionsPackage(airDensity, wind, wingSurface, maxCilinderTemp, autopilotSafeAlt);
                } catch (Exception e){

                }
                stage.close();
            }
        });
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.showAndWait();
        return pack;
    }
}
