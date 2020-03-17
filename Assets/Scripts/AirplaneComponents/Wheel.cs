using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Wheel : MonoBehaviour
{
    private Rigidbody2D wheel;
    [Header("Brake")]
    public Brake brake;
    private Rigidbody2D brakeRB;
    // Start is called before the first frame update
    void Start()
    {
        wheel = GetComponent<Rigidbody2D>();
        brakeRB = brake.GetComponent<Rigidbody2D>();
    }

    // Update is called once per frame
    void Update()
    {
       
    }
    public void getBrake(int brake, Rigidbody2D airplane)
    {
        Vector3 vector = airplane.velocity;
        if (vector.x>0) {
            Vector2 vec = transform.right * brake * 100 * -1;
            brakeRB.AddForce(vec); }
    }
}
