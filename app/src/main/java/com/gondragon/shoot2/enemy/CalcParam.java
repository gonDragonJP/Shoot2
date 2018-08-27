package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

public class CalcParam {

    public Int2Vector myPlanePosition;
    public Int2Vector enemyPosition;
    public boolean hasParent;

    public Int2Vector startPosition;
    public Double2Vector startVelocity;
    public Double2Vector startAcceleration;

    public Double2Vector parentVelocity;
    public Double2Vector parentAcceleration;
    public Double2Vector parentFaceOnVector;

    public Int2Vector startPossitionAttribute;
    public Int2Vector startVelocityAttribute;
    public Int2Vector startAccelerationAttribute;

    public CalcParam(){

        myPlanePosition = new Int2Vector();
        enemyPosition = new Int2Vector();

        startPosition = new Int2Vector();
        startVelocity = new Double2Vector();
        startAcceleration = new Double2Vector();

        parentVelocity = new Double2Vector();
        parentAcceleration = new Double2Vector();
        parentFaceOnVector = new Double2Vector();

        startPossitionAttribute = new Int2Vector();
        startVelocityAttribute = new Int2Vector();
        startAccelerationAttribute = new Int2Vector();

        initialize();
    }

    public void initialize(){

        myPlanePosition.set(0, 0);
        enemyPosition.set(0, 0);
        hasParent = false;

        startPosition.set(0, 0);
        startVelocity.set(0, 0);
        startAcceleration.set(0, 0);

        parentVelocity.set(0, 0);
        parentAcceleration.set(0, 0);
        parentFaceOnVector.set(0, 0);

        startPossitionAttribute.set(0, 0);
        startVelocityAttribute.set(0, 0);
        startAccelerationAttribute.set(0, 0);
    }
}
