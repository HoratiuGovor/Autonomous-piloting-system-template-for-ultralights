package UI;

import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UI {
    private Button buttonRun,buttonStop,buttonTakeOff,buttonMaintainAltitude,buttonLand,buttonOptions,buttonBrake,buttonStallAlert,buttonCylinderHeatAlert;
    private TextField textBoxMaintainAltitude,textBoxLift,textBoxDrag,textBoxGravity,textBoxThrust,textBoxAOA,textBoxLiftCoeff,textBoxDragCoeff,textBoxSpeedIAS,
            textBoxThrottle,textBoxWindSpeed,textBoxRPM,textBoxAltitude,textBoxVariometer,textBoxCylinderTemp,textBoxAirDensity,textBoxFlaps;
    private Text textAreaStatus;
    private AreaChart<?,?> areaChart;
    private Stage stage;
    private Scene scene;
    private XYChart.Series series= new XYChart.Series();
    private int tableXCoord = 0;

    public UI(Stage stage){
        this.stage = stage;
        this.scene = stage.getScene();
        this.buttonRun = (Button) scene.lookup("#buttonRun");
        this.buttonStop = (Button) scene.lookup("#buttonStop");
        this.buttonTakeOff = (Button) scene.lookup("#buttonTakeOff");
        this.buttonMaintainAltitude = (Button) scene.lookup("#buttonMaintainAltitude");
        this.buttonLand = (Button) scene.lookup("#buttonLand");
        this.buttonOptions = (Button) scene.lookup("#buttonOptions");
        this.buttonBrake = (Button) scene.lookup("#buttonBrake");
        this.buttonStallAlert = (Button) scene.lookup("#buttonStallAlert");
        this.buttonCylinderHeatAlert = (Button) scene.lookup("#buttonClilinderHeatAlert");

        this.textBoxMaintainAltitude = (TextField) scene.lookup("#textBoxMaintainAltitude");
        this.textBoxLift = (TextField) scene.lookup("#textBoxLift");
        this.textBoxDrag = (TextField) scene.lookup("#textBoxDrag");
        this.textBoxGravity = (TextField) scene.lookup("#textBoxGravity");
        this.textBoxThrust = (TextField) scene.lookup("#textBoxThrust");
        this.textBoxAOA = (TextField) scene.lookup("#textBoxAOA");
        this.textBoxLiftCoeff = (TextField) scene.lookup("#textBoxLiftCoeff");
        this.textBoxDragCoeff = (TextField) scene.lookup("#textBoxDragCoeff");
        this.textBoxSpeedIAS = (TextField) scene.lookup("#textBoxSpeedIAS");
        this.textBoxThrottle = (TextField) scene.lookup("#textBoxThrottle");
        this.textBoxWindSpeed = (TextField) scene.lookup("#textBoxWindSpeed");
        this.textBoxRPM = (TextField) scene.lookup("#textBoxRPM");
        this.textBoxAltitude = (TextField) scene.lookup("#textBoxAltitude");
        this.textBoxVariometer = (TextField) scene.lookup("#textBoxVariometer");
        this.textBoxCylinderTemp = (TextField) scene.lookup("#textBoxCylinderTemp");
        this.textBoxAirDensity = (TextField) scene.lookup("#textBoxAirDensity");
        this.textBoxFlaps = (TextField) scene.lookup("#textBoxFlaps");

        this.textAreaStatus = (Text) scene.lookup("#textAreaStatus");
        this.series.setName("Altitude/Distance");
        this.areaChart = (AreaChart) scene.lookup("#areaChart");
        this.areaChart.getData().add(this.series);
    }

    public void updateInformation(float IAS, int throttle, float thrust, float drag, float lift, float gravity, float verticalSpeed,float altitude,float distance,int pitch,int RPM,float liftCoefficient,float dragCoefficient,int flaps,float airDensity,float wind,float cylinderTemp,float brake) {
        this.textBoxSpeedIAS.setText(Float.toString(IAS));
        this.textBoxThrottle.setText(Integer.toString(throttle));
        this.textBoxThrust.setText(Float.toString(thrust));
        this.textBoxDrag.setText(Float.toString(drag));
        this.textBoxLift.setText(Float.toString(lift));
        this.textBoxGravity.setText(Float.toString(gravity));
        this.textBoxVariometer.setText(Float.toString(verticalSpeed));
        this.textBoxAltitude.setText(Float.toString(altitude));
        this.textBoxAOA.setText(Integer.toString(pitch));
        this.textBoxRPM.setText(Integer.toString(RPM));
        this.textBoxLiftCoeff.setText(Float.toString(liftCoefficient));
        this.textBoxDragCoeff.setText(Float.toString(dragCoefficient));
        this.textBoxFlaps.setText(Integer.toString(flaps));
        this.textBoxAirDensity.setText(Float.toString(airDensity));
        this.textBoxWindSpeed.setText(Float.toString(wind));
        this.textBoxCylinderTemp.setText(Float.toString(cylinderTemp));
        this.areaChart.setAnimated(false);
        this.series.getData().add(new XYChart.Data(Float.toString(distance), altitude));
        this.buttonBrake.setStyle("-fx-background-color: #91ff61");
        this.buttonStallAlert.setStyle("-fx-background-color: #91ff61");
        this.buttonCylinderHeatAlert.setStyle("-fx-background-color: #91ff61");
         brakeSensor(brake);
        cylinderTempSensor(cylinderTemp);
        //System.out.println(distance+"  -  "+altitude);
        this.tableXCoord++;
        if(tableXCoord>50){
            series.getData().remove(0);
        }
    }

    private void cylinderTempSensor(float cylinderTemp) {
        if(cylinderTemp>130)
            this.buttonCylinderHeatAlert.setStyle("-fx-background-color: #ff3f44");
        else
            this.buttonCylinderHeatAlert.setStyle("-fx-background-color: #91ff61");
    }

    private void brakeSensor(float brake) {
        if(brake>0)
            this.buttonBrake.setStyle("-fx-background-color: #ff3f44");
        else
            this.buttonBrake.setStyle("-fx-background-color: #91ff61");
    }

    public Button getButtonRun() {
        return buttonRun;
    }

    public Button getButtonStop() {
        return buttonStop;
    }

    public Button getButtonTakeOff() {
        return buttonTakeOff;
    }

    public Button getButtonMaintainAltitude() {
        return buttonMaintainAltitude;
    }

    public Button getButtonLand() {
        return buttonLand;
    }

    public Button getButtonOptions() {
        return buttonOptions;
    }

    public Button getButtonBrake() {
        return buttonBrake;
    }

    public Button getButtonStallAlert() {
        return buttonStallAlert;
    }

    public Button getButtonCylinderHeatAlert() {
        return buttonCylinderHeatAlert;
    }

    public void disableButtonTakeOff(boolean option) {
        this.buttonTakeOff.setDisable(option);
    }
    public void disableButtonMaintainAltitude(boolean option) {
        this.buttonMaintainAltitude.setDisable(option);
    }
    public void disableButtonLand(boolean option) {
        this.buttonLand.setDisable(option);
    }

    public TextField getTextBoxMaintainAltitude() {
        return textBoxMaintainAltitude;
    }

    public void disableButton(Button btn) {
        btn.setDisable(true);
    }
    public void enableButton(Button btn) {
        btn.setDisable(false);
    }
    public void clearChart(){

        this.areaChart.getData().clear();
        this.series = new XYChart.Series();
        this.series.setName("Altitude/Distance");
        this.areaChart.getData().add(this.series);
        /*this.series.setName("Altitude");
        this.areaChart = (AreaChart) scene.lookup("#areaChart");
        this.areaChart.getData().add(this.series);*/
    }

    public void setStallSensor(boolean stallSensor) {
        if(stallSensor)
            this.buttonStallAlert.setStyle("-fx-background-color: #ff3f44");
        else
            this.buttonStallAlert.setStyle("-fx-background-color: #91ff61");
    }

}
