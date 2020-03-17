package Controller;

import java.util.EventListener;
import java.util.HashMap;

public class AirplaneController {
    private float IAS;
    private float lift;
    private float drag;
    private float thrust,mass=450,gravityAcceleration=9.81f,verticalSpeed;
    private float maxThrust=2800;
    private float maxBrake=1400;
    private float altitude=0;
    private float distance=0;
    private float flapsCoefficient;
    private float liftCoefficient=0;
    private float dragCoefficient = 0;
    private float wingSurface = 13.99f;
    private int pitch;
    private int throttle;
    private int maxRPM = 5800;
    private int RPM = 0;
    private int flapsPosition = 0;
    private float airDensity = 1.225f;
    private float wind = 0;
    private float cylinderTemp = 100;
    private int brakeCoefficient = 0;
    private float brake = 0;
    private float cylinderMaxTemp = 130;

    private HashMap<Integer,Float> liftCoefficientChart = new HashMap<Integer, Float>();
    public AirplaneController(){
        importLiftCoefficient();
    }

   public void run(int throttle,int pitch,int flapsPosition,int brakeCoefficient){
           this.throttle = throttle;
           this.pitch = pitch;
           this.flapsPosition = flapsPosition;
           this.brakeCoefficient = brakeCoefficient;
           if (flapsPosition == 2)
               flapsCoefficient = 0.2f;
           else if (flapsPosition == 1)
               flapsCoefficient = 0.1f;
           else
               flapsCoefficient = 0.0f;

           applyBrakes();
           applyThrust(throttle);
           applyDrag();
           applyLift();
           determineCylinderTemp();
   }

    private void determineCylinderTemp() {
       if(this.RPM>5500)
           this.cylinderTemp+=0.1;
       else if (this.cylinderTemp-1.1<100)
           this.cylinderTemp = 100;
       else
           this.cylinderTemp-=1.1;

    }

    public void applyLift()
    {

        try {
            this.liftCoefficient = this.liftCoefficientChart.get(this.pitch);
        }catch (Exception e){
            this.liftCoefficient = 0;
        }
        this.liftCoefficient+=this.flapsCoefficient;
        this.lift  = 0.5f* this.IAS * this.IAS *this.wingSurface*this.airDensity*this.liftCoefficient;
        this.lift -= this.mass*9.81f*cosAngle(pitch);
        this.verticalSpeed = this.lift/this.mass;
        if(this.altitude == 0) {
            if (this.verticalSpeed > 0)
                this.altitude += this.verticalSpeed;
        }else if(this.altitude>0){
            if(this.altitude+this.verticalSpeed<0)
                this.altitude = 0;
            else
                this.altitude+=this.verticalSpeed;
        }
    }
    public void applyBrakes(){
       this.brake =  this.brakeCoefficient*this.maxBrake/100;
    }
    public void applyDrag()
    {
        this.dragCoefficient = 0.5f;
        if(this.pitch<=10)
            this.dragCoefficient = 0.2f;
        if(this.pitch <=5)
            this.dragCoefficient = 0.15f;
        this.dragCoefficient+=this.flapsCoefficient/2;
        this.drag =0.5f* this.IAS * this.IAS *this.wingSurface*this.airDensity*this.dragCoefficient;
        this.drag+=windForce();
    }
    public void applyThrust(int throttle) {
        float acc = 0;
        this.thrust = throttle * maxThrust / 100;
        this.RPM = throttle * maxRPM / 100;
        float horizontal = this.thrust - this.drag - this.mass*9.81f*sinAngle(this.pitch);
        //System.out.println(this.thrust+" "+this.drag+" "+this.mass*9.81f*sinAngle(this.pitch));
        if (this.altitude == 0) {
            acc = (horizontal-this.brake)/ this.mass;
            if (this.IAS + acc < 0)
                this.IAS = 0;

            else
                this.IAS += acc;
        }
        else {
            acc = horizontal / this.mass;
            this.IAS += acc;
        }
        this.distance += this.IAS;
    }
    /*public float getSpeedKMH()
    {
        Vector3 vector = airplane.velocity;
        return vector.x * 3.6f;
    }*/
    public float sinAngle(int degrees){
        return (float) Math.sin(Math.toRadians(degrees));
    }

    public float cosAngle(int degrees){
        return (float) Math.cos(Math.toRadians(degrees));
    }
    public float windForce(){
        return this.wingSurface*this.airDensity*this.wind;
    }
    public float getG2(){
        return this.mass*9.81f*sinAngle(this.pitch);
    }
    public float getIAS() {
        return IAS;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public void setIAS(float IAS) {
        this.IAS = IAS;
    }

    public float getLift() {
        return lift;
    }

    public void setLift(float lift) {
        this.lift = lift;
    }

    public float getDrag() {
        return drag;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }

    public float getThrust() {
        return thrust;
    }

    public void setThrust(float thrust) {
        this.thrust = thrust;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public float getMaxThrust() {
        return maxThrust;
    }

    public void setMaxThrust(float maxThrust) {
        this.maxThrust = maxThrust;
    }

    public float getGravityAcceleration() {
        return gravityAcceleration;
    }

    public void setGravityAcceleration(float gravityAcceleration) {
        this.gravityAcceleration = gravityAcceleration;
    }
    public float getGravityForce(){
        return this.mass*this.gravityAcceleration;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public float getFlapsCoefficient() {
        return flapsCoefficient;
    }

    public void setFlapsCoefficient(float flapsCoefficient) {
        this.flapsCoefficient = flapsCoefficient;
    }

    public int getMaxRPM() {
        return maxRPM;
    }

    public void setMaxRPM(int maxRPM) {
        this.maxRPM = maxRPM;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public float getLiftCoefficient() {
        return liftCoefficient;
    }

    public void setLiftCoefficient(float liftCoefficient) {
        this.liftCoefficient = liftCoefficient;
    }

    public float getDragCoefficient() {
        return dragCoefficient;
    }

    public void setDragCoefficient(float dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
    }

    public int getFlapsPosition() {
        return flapsPosition;
    }

    public void setFlapsPosition(int flapsPosition) {
        this.flapsPosition = flapsPosition;
    }

    public float getAirDensity() {
        return airDensity;
    }

    public void setAirDensity(float airDensity) {
        this.airDensity = airDensity;
    }
    public float getCylinderTemp() {
        return cylinderTemp;
    }

    public float getWind() {
        return wind;
    }

    public float getBrake() {
        return brake;
    }

    public void setBrake(float brake) {
        this.brake = brake;
    }

    private void importLiftCoefficient() {
        this.liftCoefficientChart.put(-5,0.00f);
        this.liftCoefficientChart.put(-4,0.03f);
        this.liftCoefficientChart.put(-3,0.20f);
        this.liftCoefficientChart.put(-2,0.35f);
        this.liftCoefficientChart.put(-1,0.45f);
        this.liftCoefficientChart.put(0,0.46f);
        this.liftCoefficientChart.put(1,0.47f);
        this.liftCoefficientChart.put(2,0.50f);
        this.liftCoefficientChart.put(3,0.55f);
        this.liftCoefficientChart.put(4,0.57f);
        this.liftCoefficientChart.put(5,0.60f);
        this.liftCoefficientChart.put(6,0.65f);
        this.liftCoefficientChart.put(7,0.70f);
        this.liftCoefficientChart.put(8,0.75f);
        this.liftCoefficientChart.put(9,0.90f);
        this.liftCoefficientChart.put(10,1.00f);
        this.liftCoefficientChart.put(11,1.10f);
        this.liftCoefficientChart.put(12,1.15f);
        this.liftCoefficientChart.put(13,1.20f);
        this.liftCoefficientChart.put(14,1.25f);
        this.liftCoefficientChart.put(15,1.30f);
        this.liftCoefficientChart.put(16,1.33f);
        this.liftCoefficientChart.put(17,1.33f);
        this.liftCoefficientChart.put(18,1.30f);
        this.liftCoefficientChart.put(19,1.30f);
        this.liftCoefficientChart.put(20,1.25f);
        this.liftCoefficientChart.put(21,1.20f);
        this.liftCoefficientChart.put(22,1.15f);
        this.liftCoefficientChart.put(23,1.10f);
        this.liftCoefficientChart.put(24,1.00f);
        this.liftCoefficientChart.put(25,0.00f);
    }

    public float getWingSurface() {
        return wingSurface;
    }

    public void setWingSurface(float wingSurface) {
        this.wingSurface = wingSurface;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public void setCylinderTemp(float cylinderTemp) {
        this.cylinderTemp = cylinderTemp;
    }

    public float getCylinderMaxTemp() {
        return cylinderMaxTemp;
    }

    public void setCylinderMaxTemp(float cylinderMaxTemp) {
        this.cylinderMaxTemp = cylinderMaxTemp;
    }
}
