package Controller;

import UI.UI;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AI {
    private AirplaneController airplaneCtrl;
    private UI ui;
    private boolean intrerruptFlag = false;
    public int autopilotSafeAltitude= 250;
    private boolean stallAlert = false;
    private String log ="";

    public AI(AirplaneController airplaneCtrl, UI ui) {
        this.airplaneCtrl = airplaneCtrl;
        this.ui = ui;
    }

    public void run (){
        waitUI();
    }
    public void takeOff() {
        if(!intrerruptFlag)
            takeOffAcceleration();
        if(!intrerruptFlag)
            takeoffLeaveGround();
        if(!intrerruptFlag)
            levelFligt();
        if(!intrerruptFlag)
            climbTo(150);
        if(!intrerruptFlag) {
            ui.disableButtonMaintainAltitude(false);
            ui.disableButtonLand(false);
            maintainAltitude(autopilotSafeAltitude);
        }
}

    public void maintainAltitude(int desiredAltitude) {
        int pitch = 0;
        while(true){
            if(intrerruptFlag)
            {
                intrerruptFlag=false;
                break;
            }
            int throttle = throttleToMaintainSpeed(120);
            if(airplaneCtrl.getAltitude()>desiredAltitude&&airplaneCtrl.getAltitude()<desiredAltitude+1)
                pitch = 0;
            else if(airplaneCtrl.getAltitude()>desiredAltitude+5) {
                if(pitch>-5)
                    pitch--;
            }
            else if(airplaneCtrl.getAltitude()<desiredAltitude-5){
                if(pitch<5)
                    pitch++;
            }
            else if(airplaneCtrl.getAltitude()>desiredAltitude-5&&airplaneCtrl.getAltitude()<desiredAltitude&&airplaneCtrl.getVerticalSpeed()>0)
                pitch = 1;
            else if(airplaneCtrl.getAltitude()<desiredAltitude+5&&airplaneCtrl.getAltitude()>desiredAltitude&&airplaneCtrl.getVerticalSpeed()<0)
                pitch = -1;
            else if(airplaneCtrl.getAltitude()>desiredAltitude-5&&airplaneCtrl.getAltitude()<desiredAltitude)
                pitch++;
            else if(airplaneCtrl.getAltitude()<desiredAltitude+5&&airplaneCtrl.getAltitude()>desiredAltitude)
                pitch--;

            airplaneCtrl.run(throttle, pitch,0,0);
            stallSensor();
            waitUI();
        }
    }


    private void takeOffAcceleration() {
        int pitch = 2;
        airplaneCtrl.run(10, pitch,1,0);
        waitUI();
        airplaneCtrl.run(20, pitch,1,0);
        waitUI();
        airplaneCtrl.run(40, pitch,1,0);
        waitUI();
        airplaneCtrl.run(60, pitch,1,0);
        waitUI();
        airplaneCtrl.run(80, pitch,1,0);
        waitUI();
        airplaneCtrl.run(100, pitch,1,0);
        waitUI();
    }

    private void takeoffLeaveGround() {
        if(airplaneCtrl.getIAS()>22 && !intrerruptFlag) {
            waitUI();
            while (airplaneCtrl.getAltitude()<10 && !intrerruptFlag){
                airplaneCtrl.run(100,5,1,0);
                waitUI();
            }
        }
        else if(!intrerruptFlag){
            airplaneCtrl.run(100,2,1,0);
            waitUI();
            takeoffLeaveGround();
        }
    }

    private void levelFligt() {
        while(this.airplaneCtrl.getIAS()<33 && !intrerruptFlag){
            airplaneCtrl.run(100,0,1,0);
            waitUI();
        }
    }
    private void stallSensor(){
        if(airplaneCtrl.getPitch()>24 || (airplaneCtrl.getVerticalSpeed()<-5&&airplaneCtrl.getPitch()>0)){
            ui.setStallSensor(true);
            this.stallAlert = true;
        }
        else{
            ui.setStallSensor(false);
            this.stallAlert = false;
        }

    }
    private void climbTo(int desiredAltitude) {
        if(this.airplaneCtrl.getAltitude()<50 && !intrerruptFlag){
            int throttle = throttleToMaintainSpeed(125);
            airplaneCtrl.run(100,5,1,0);
            stallSensor();
            waitUI();
            climbTo(desiredAltitude);
        }
        else if (this.airplaneCtrl.getAltitude()<150 && !intrerruptFlag){
            int throttle = throttleToMaintainSpeed(120);
            airplaneCtrl.run(throttle,5,1,0);
            stallSensor();
            waitUI();
            climbTo(desiredAltitude);
        }
        else
            while(this.airplaneCtrl.getAltitude()<desiredAltitude && !intrerruptFlag) {
                int throttle = throttleToMaintainSpeed(120);
                airplaneCtrl.run(throttle, 10, 0,0);
                stallSensor();
                waitUI();
            }
    }
    public int throttleToMaintainSpeed(int speed){
        int throttle = 0;
        float airplaneSpeedKMH = airplaneCtrl.getIAS()*3.6f;
        if(speed == (int) airplaneSpeedKMH)
            throttle = (int) ((airplaneCtrl.getDrag()+airplaneCtrl.getG2()+airplaneCtrl.windForce())*100/airplaneCtrl.getMaxThrust());
        else if(speed+5< airplaneSpeedKMH){
            throttle = (int) ((airplaneCtrl.getDrag()+airplaneCtrl.getG2()+airplaneCtrl.windForce())*100/airplaneCtrl.getMaxThrust())-5;
        }
        else if(speed< airplaneSpeedKMH){
            throttle = (int) ((airplaneCtrl.getDrag()+airplaneCtrl.getG2()+airplaneCtrl.windForce())*100/airplaneCtrl.getMaxThrust())-1;
        }
        else if(speed-5 > airplaneSpeedKMH){
            throttle = (int) ((airplaneCtrl.getDrag()+airplaneCtrl.getG2()+airplaneCtrl.windForce())*100/airplaneCtrl.getMaxThrust())+5;
            //System.out.println("th "+throttle+"dr "+airplaneCtrl.getDrag()+"lf "+airplaneCtrl.getLift());
        }
        else if(speed > airplaneSpeedKMH){
            throttle = (int) ((airplaneCtrl.getDrag()+airplaneCtrl.getG2()+airplaneCtrl.windForce())*100/airplaneCtrl.getMaxThrust())+1;
            //System.out.println("th "+throttle+"dr "+airplaneCtrl.getDrag()+"lf "+airplaneCtrl.getLift());
        }
        return throttle;
    }
    public void gatherAircraftInformation(){
        float IAS = airplaneCtrl.getIAS();
        int throttle = airplaneCtrl.getThrottle();
        float thrust = airplaneCtrl.getThrust();
        float drag = airplaneCtrl.getDrag();
        float lift =  airplaneCtrl.getLift();
        float gravity =airplaneCtrl.getGravityForce();
        float verticalSpeed = airplaneCtrl.getVerticalSpeed();
        float altitude = airplaneCtrl.getAltitude();
        float distance = airplaneCtrl.getDistance();
        int pitch = airplaneCtrl.getPitch();
        int RPM = airplaneCtrl.getRPM();
        float liftCoefficient = airplaneCtrl.getLiftCoefficient();
        float dragCoefficient = airplaneCtrl.getDragCoefficient();
        int flaps = airplaneCtrl.getFlapsPosition();
        float airDensity = airplaneCtrl.getAirDensity();
        float wind = airplaneCtrl.getWind();
        float cylinderTemp = airplaneCtrl.getCylinderTemp();
        float brake = airplaneCtrl.getBrake();
        log += "IAS:"+Float.toString(IAS)+",Altitude:"+altitude+",Distance:"+distance+",Angle of Attack:"+Integer.toString(pitch)+",Flaps:"+Integer.toString(flaps)+",RPM:"+Integer.toString(RPM)+",Throttle:"+Integer.toString(throttle)+",Lift:"+Float.toString(lift)+",Gravity:"+Float.toString(gravity)+",Drag:"+Float.toString(drag)+",Thrust:"+Float.toString(thrust)+",Lift Coeff:"+Float.toString(liftCoefficient)+",Drag Coeff:"+Float.toString(dragCoefficient)+",Air Density:"+Float.toString(airDensity)+",Wind:"+Float.toString(wind)+",Cylinder Temp:"+Float.toString(cylinderTemp)+",Stall Alert:"+Boolean.toString(this.stallAlert)+",Brake:"+Float.toString(brake)+'\n';
        ui.updateInformation(IAS*3.6f,throttle,thrust,drag,lift,gravity,verticalSpeed,altitude,distance,pitch,RPM,liftCoefficient,dragCoefficient,flaps,airDensity,wind,cylinderTemp,brake);

    }
    public void land(){
        int pitch = 0;
        while(airplaneCtrl.getAltitude()>0){
            int throttle = throttleToMaintainSpeed(120);
            if(intrerruptFlag)
            {
                //intrerruptFlag=false;
                break;
            }
            else if(airplaneCtrl.getAltitude()<20) {
                airplaneCtrl.run(0, -2, 2,0);
                waitUI();
            }
            else if(airplaneCtrl.getAltitude()<100) {
                airplaneCtrl.run(20, -3, 2,0);
                stallSensor();
                waitUI();
            }
            else if(airplaneCtrl.getAltitude()<250) {
                airplaneCtrl.run(throttle, -3, 1,0);
                stallSensor();
                waitUI();
            }
            else{
                if(pitch>-5)
                    pitch--;
                airplaneCtrl.run(throttle, pitch, 0,0);
                stallSensor();
                waitUI();
            }
        }
        boolean brakeApplied = false;
        while(airplaneCtrl.getIAS()>0) {
            if(intrerruptFlag)
            {
                intrerruptFlag=false;
                break;
            }
            if(!brakeApplied) {
                airplaneCtrl.run(0, 0, 0, 100);
                brakeApplied = true;
            }
            else {
                airplaneCtrl.run(0, 0, 0, 0);
                brakeApplied=false;
            }
            waitUI();
        }
        if(!intrerruptFlag)
        {
            ui.disableButtonLand(true);
            ui.disableButtonMaintainAltitude(true);
        }
    }
    public void waitUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gatherAircraftInformation();
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public boolean getIntrerruptFlag() {
        return intrerruptFlag;
    }

    public void setIntrerruptFlag(boolean intrerruptFlag) {
        this.intrerruptFlag = intrerruptFlag;
    }

    public int getDesiredAltitude() {
        return Integer.parseInt(ui.getTextBoxMaintainAltitude().getText());
    }

    public void disableButton(Button btn){
        ui.disableButton(btn);
    }
    public void enableButton(Button btn){
        ui.enableButton(btn);
    }

    public void reset() {
        this.airplaneCtrl = new AirplaneController();
    }
    public UI getUI(){return this.ui;}
    public AirplaneController getAirplaneCtrl() {
        return airplaneCtrl;
    }

    public int getAutopilotSafeAltitude() {
        return autopilotSafeAltitude;
    }

    public void setAutopilotSafeAltitude(int autopilotSafeAltitude) {
        this.autopilotSafeAltitude = autopilotSafeAltitude;
    }

    public void writeRaport() {
        String time = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());
        String fileName = "Raport "+time;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(log);
            writer.close();
        } catch (Exception e){}

    }
}
