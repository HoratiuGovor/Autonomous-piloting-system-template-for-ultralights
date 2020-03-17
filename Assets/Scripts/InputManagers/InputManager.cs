using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InputManager : MonoBehaviour
{
    [Header("Throtle")]
    public int throttle = 0;
    [Header("Brake")]
    public int brake = 0;
    private int pitch = 0;
    private int maxThrottle = 100;
    private int minThrottle = 0;
    void Awake()
    {
      
    }

    void Update()
    {

        int acceleration = 0;
        if (Input.GetKey(KeyCode.RightArrow))
            acceleration = 1;
        if (Input.GetKey(KeyCode.LeftArrow))
            acceleration = -1;
        if (Input.GetKey(KeyCode.UpArrow))
            pitch = 1;
        if (Input.GetKey(KeyCode.DownArrow))
            pitch = -1;
        if (Input.GetKey(KeyCode.Z))
            brake +=1;
        if (Input.GetKey(KeyCode.X))
            brake = 0;

        if (acceleration + throttle > this.maxThrottle)
            this.throttle = 100;
        else if (acceleration + throttle < this.minThrottle)
            this.throttle = 0;
        else
            this.throttle += acceleration;
        Debug.Log(throttle);
        //float pitch = Input.GetAxis("Vertical");
        //Debug.Log(pitch);
        

    }
    public int getPitch() { return this.pitch; }
    public int getThrottle() { return this.throttle; }
    public int getBrake() { return this.brake; }
    public void setPitch(int newPitch) { this.pitch = newPitch; }
    public void setThrottle(int newThrottle) { this.throttle = newThrottle; }
    public void setBrake(int newBrake) { this.brake = newBrake; }
}
