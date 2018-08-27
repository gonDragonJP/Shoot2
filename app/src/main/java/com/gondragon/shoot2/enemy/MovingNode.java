package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

public class MovingNode {

    public Double2Vector startVelocity;
    public Double2Vector startAcceleration;
    public Double2Vector homingAcceleration;
    public int nodeDurationFrame;
    public Int2Vector startVelAttrib;
    public Int2Vector startAccAttrib;

    public MovingNode(){

        startVelocity = new Double2Vector();
        startAcceleration = new Double2Vector();
        homingAcceleration = new Double2Vector();
        nodeDurationFrame = 0;
        startVelAttrib = new Int2Vector(0,0);
        startAccAttrib = new Int2Vector(0,0);
    }

    public MovingNode(MovingNode src){

        this();
        this.copy(src);
    }

    public void copy(MovingNode srcNode){

        startVelocity.copy(srcNode.startVelocity);
        startAcceleration.copy(srcNode.startAcceleration);
        homingAcceleration.copy(srcNode.homingAcceleration);
        nodeDurationFrame = srcNode.nodeDurationFrame;
        startVelAttrib.set
                (srcNode.startVelAttrib.x, srcNode.startVelAttrib.y);
        startAccAttrib.set
                (srcNode.startAccAttrib.x, srcNode.startAccAttrib.y);
    }
}
