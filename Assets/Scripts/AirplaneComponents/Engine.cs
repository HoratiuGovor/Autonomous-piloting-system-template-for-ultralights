using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Engine : MonoBehaviour
{
    // Start is called before the first frame update
    private Rigidbody2D engine;
    private float maxRpm =5800f;
    private float minRpm = 2200f;
    private int maxPower = 100;
    private float continousRpm = 5500f;
    private float maxSpeed = 217f;
    public AnimationCurve tractionCurve;
    void Start()
    {
        engine = GetComponent<Rigidbody2D>();
        tractionCurve = AnimationCurve.Linear(0, 0, 100, 100);
        tractionCurve.AddKey(50, 30);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void getTraction(int throttle,Rigidbody2D airplane)
    {
        Vector3 vector = airplane.velocity;
        Debug.Log("veli" + vector.x*3.6f);
        /* if (vector.x*3.6f < maxSpeed)
         {*/
        float traction = tractionCurve.Evaluate(throttle);
            Vector2 vec = transform.right * traction * 50;
            airplane.AddForce(vec);
        //}
    }
    public void getBrake(int brake, Rigidbody2D airplane)
    {
        Vector3 vector = airplane.velocity;
        Vector3 vector3 = transform.InverseTransformDirection(airplane.velocity);
        float veli = vector3.x;
        veli *= 300;
        Debug.Log("veli" +veli);
        if (vector.x > 0)
        {
            Vector2 vec = transform.right * brake * 100 * -1;
            engine.AddForce(vec);
        }
    }
}
