package Controller;
import UI.*;
import Utills.OptionsPackage;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Controller {
    private AI ai;
    private Stage stage;
    private Stack<Thread> threads = new Stack<>();
    private boolean start=false;
    public Controller(AI ai){
       this.ai = ai;
    }
    public void run(Stage stage) throws InterruptedException {
        this.stage = stage;
        stage.show();
        Scene scene= stage.getScene();

        Button buttonRun= (Button) scene.lookup("#buttonRun");
        Button buttonStop= (Button) scene.lookup("#buttonStop");
        Button buttonTakeOff= (Button) scene.lookup("#buttonTakeOff");
        Button buttonMaintainAltitude=(Button) scene.lookup("#buttonMaintainAltitude");
        Button buttonLand= (Button) scene.lookup("#buttonLand");
        Button buttonOptions = (Button) scene.lookup("#buttonOptions");
        buttonStop.setDisable(true);
        buttonTakeOff.setDisable(true);
        buttonMaintainAltitude.setDisable(true);
        buttonLand.setDisable(true);

        buttonRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buttonTakeOff.setDisable(false);
                buttonStop.setDisable(false);
                buttonRun.setDisable(true);
                buttonOptions.setDisable(true);
                intreruptThreds();
                ai.run();
                setStart(true);
            }
        });
        buttonStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                intreruptThreds();
                ai.setIntrerruptFlag(true);
                buttonRun.setDisable(false);
                buttonTakeOff.setDisable(true);
                buttonMaintainAltitude.setDisable(true);
                buttonLand.setDisable(true);
                buttonOptions.setDisable(false);
                buttonStop.setDisable(true);
                ai.writeRaport();
                resetAi();
                //buttonStop.setDisable(true);

            }
        });
        buttonTakeOff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                intreruptThreds();
                buttonTakeOff.setDisable(true);
                buttonMaintainAltitude.setDisable(true);
                buttonLand.setDisable(true);
                if(!isStart()) {
                    ai.setIntrerruptFlag(true);
                }
                setStart(false);
                Thread thread = new Thread(getTakeOffTask());
                addThreadToStack(thread);
                synchronized (ai) {
                    thread.start();
                }
                }
        });

        buttonMaintainAltitude.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                intreruptThreds();
                ai.setIntrerruptFlag(true);
                Thread thread = new Thread(getMaintainAltitudeTask());
                addThreadToStack(thread);
                synchronized (ai) {
                    thread.start();
                }
            }
        });
        buttonLand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                intreruptThreds();
                ai.setIntrerruptFlag(true);
                Thread thread = new Thread(getLandTask());
                addThreadToStack(thread);
                synchronized (ai) {
                    thread.start();
                }
            }
        });
        buttonOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showOptions();
            }
        });
    }
    public Task getTakeOffTask(){
        Task task = new Task<Void>() {
            @Override public Void call() {
                ai.takeOff();
                return null;
            }
        };
        return task;
    }
    public Task getMaintainAltitudeTask(){
        Task task= new Task<Void>() {
            @Override public Void call() {
                ai.maintainAltitude(ai.getDesiredAltitude());
                return null;
            }
        };
        return task;
    }
    public Task getLandTask(){
        Task task = new Task<Void>() {
            @Override public Void call() {
                ai.land();
                return null;
            }
        };
        return task;
    }
    public void intreruptThreds(){
        while(!this.threads.empty()){
            Thread trd = threads.pop();
            trd.interrupt();
        }
    }
    public void resetAi(){
        this.ai.getUI().clearChart();
        UI ui = new UI(this.stage);
        ui.clearChart();
        this.ai = new AI(new AirplaneController(),ui);

    }
    public void showOptions(){
        AirplaneController airctrl = ai.getAirplaneCtrl();
        OptionsUI options = new OptionsUI(airctrl.getAirDensity(),airctrl.getWind(),airctrl.getWingSurface(),airctrl.getCylinderMaxTemp(),ai.getAutopilotSafeAltitude());
        OptionsPackage opt = options.run();
        airctrl.setAirDensity(opt.getAirDensity());
        airctrl.setWind(opt.getWind());
        airctrl.setWingSurface(opt.getWingSurface());
        airctrl.setCylinderMaxTemp(opt.getMaxCilinderTemp());
        ai.setAutopilotSafeAltitude(opt.getAutopilotSafeAlt());
    }
    public void addThreadToStack(Thread trd){
        this.threads.push(trd);
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
