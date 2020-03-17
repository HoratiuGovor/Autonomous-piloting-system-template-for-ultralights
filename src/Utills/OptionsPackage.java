package Utills;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class OptionsPackage {
    private float airDensity;
    private float wind;
    private float wingSurface;
    private float maxCilinderTemp;
    private int autopilotSafeAlt;

    public OptionsPackage(float airDensity,float wind,float wingSurface,float maxCilinderTemp,int autopilotSafeAlt){
        this.airDensity = airDensity;
        this.wind = wind;
        this.wingSurface = wingSurface;
        this.maxCilinderTemp = maxCilinderTemp;
        this.autopilotSafeAlt = autopilotSafeAlt;

    }

    public float getAirDensity() {
        return airDensity;
    }

    public void setAirDensity(float airDensity) {
        this.airDensity = airDensity;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getWingSurface() {
        return wingSurface;
    }

    public void setWingSurface(float wingSurface) {
        this.wingSurface = wingSurface;
    }

    public float getMaxCilinderTemp() {
        return maxCilinderTemp;
    }

    public void setMaxCilinderTemp(float maxCilinderTemp) {
        this.maxCilinderTemp = maxCilinderTemp;
    }

    public int getAutopilotSafeAlt() {
        return autopilotSafeAlt;
    }

    public void setAutopilotSafeAlt(int autopilotSafeAlt) {
        this.autopilotSafeAlt = autopilotSafeAlt;
    }
}
