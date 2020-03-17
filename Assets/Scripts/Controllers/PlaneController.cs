using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlaneController : MonoBehaviour
{ 
    private Rigidbody2D airplane;
    [Header("Input")]
    public InputManager inputCtrl;
    [Header("Center Of Gravity")]
    public CenterOfGravity CenterOfGravity;
    [Header("Engine")]
    public Engine engine;
    [Header("Wheels")]
    public List<Wheel> wheels;
    public PolygonCollider2D coll;
    public AnimationCurve liftCurve;
    public AnimationCurve dragCurve;


    void Start()
    {
        airplane = GetComponent<Rigidbody2D>();
        coll = airplane.GetComponent<PolygonCollider2D>();
        liftCurve = AnimationCurve.Linear(0, 0,360,1.5f);
        liftCurve.AddKey(0.5f, 0.6f);
        liftCurve.AddKey(3, 0.7f);
        liftCurve.AddKey(10, 0.2f);
        liftCurve.AddKey(15, 0.1f);
        liftCurve.AddKey(25, 0);
        dragCurve = AnimationCurve.Linear(0, 0, 360, 1.5f);
        dragCurve.AddKey(0.5f, 0.2f);
        dragCurve.AddKey(3, 0.2f);
        dragCurve.AddKey(10, 0.3f);
        dragCurve.AddKey(17, 0.5f);
        dragCurve.AddKey(30, 1.2f);
        //QualitySettings.vSyncCount = 0;
        //Application.targetFrameRate = 10;
    }

    // Update is called once per frame
    void FixedUpdate()
    {
       
        //float horizontal = Input.GetAxis("Horizontal");
        // airplane.velocity = new Vector2(horizontal, airplane.velocity.y);
        int throttle = inputCtrl.getThrottle();
        engine.getTraction(throttle,airplane);
        Vector2 vec = transform.up * (airplane.mass * Physics2D.gravity.magnitude);
        //engine.getBrake(inputCtrl.getBrake(), airplane);
        foreach (Wheel wheel in wheels){
            int brake = inputCtrl.getBrake();
            wheel.getBrake(inputCtrl.getBrake(),airplane);
        }
        applyLift();
        applyDrag();
        applyPitch();
        // coll.sharedMaterial.friction = inputCtrl.getBrake();
        //airplane.AddForce(vec);
        // engine.getTraction(inputCtrl.getThrottle());
        //Debug.DrawRay(transform.position, transform.right, Color.red);

    }
    void applyLift()
    {
        Rigidbody2D cog = CenterOfGravity.getCOG();
        float liftCoefficient = liftCurve.Evaluate(airplane.rotation);
        /*if (airplane.rotation < 0)
            liftCoefficient = 0.5f;*/
        Vector2 vec = transform.right * 500f;//9.81f;//airplane.velocity.x * airplane.velocity.x * 1/2*13*1.020f*liftCoefficient;
        //Debug.Log(Physics2D.gravity);
        Debug.Log(vec);
        airplane.AddForce(vec);
    }
    void applyDrag()
    {
        Rigidbody2D cog = CenterOfGravity.getCOG();
        float dragCoefficient = dragCurve.Evaluate(airplane.rotation);
        Vector2 vec = transform.right *-1 * airplane.velocity.x * airplane.velocity.x * 1 / 2 * 13 *1.020f*dragCoefficient;
        //Debug.Log(Physics2D.gravity);
        //Debug.Log(vec);
        cog.AddForce(vec);
    }
    public float getSpeedKMH()
    {
        Vector3 vector = airplane.velocity;
        return vector.x * 3.6f;
    }
    void applyPitch()
    {
        Vector3 frw = transform.right;
        frw.y = 0;
        float pitchAngle = Vector3.Angle( transform.right,frw);
        float k = 600*inputCtrl.getPitch();
        Debug.Log("pitch" + k);
        //Vector3 pitchs = transform.right;
       // airplane.rotation = airplane.rotation +inputCtrl.getPitch()*2;
       /* if (airplane.rotation > 30)
            airplane.rotation = 30;
        if (airplane.rotation < -20)
            airplane.rotation = -20;*/
        airplane.AddTorque(k);
    }
}
