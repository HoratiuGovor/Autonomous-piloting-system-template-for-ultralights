using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CenterOfGravity : MonoBehaviour
{
    private Rigidbody2D cog;
    // Start is called before the first frame update
    void Start()
    {
        cog = GetComponent<Rigidbody2D>();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public Rigidbody2D getCOG() { return this.cog; }
}
