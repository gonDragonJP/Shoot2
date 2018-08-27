package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

public class EnemyCalculator {

    static private Double2Vector tendToPlaneVelocity     = new Double2Vector();
    static private Double2Vector tendToPlaneAcceleration = new Double2Vector();			// 自機方向

    static private Double2Vector tendParentAheadVelocity     = new Double2Vector();
    static private Double2Vector tendParentAheadAcceleration = new Double2Vector();		//　親機進行方向

    static private Double2Vector tendParentFaceOnVelocity     = new Double2Vector();
    static private Double2Vector tendParentFaceOnAcceleration = new Double2Vector();	// 親機正面方向

    static private Double2Vector examinedVel = new Double2Vector();
    static private Double2Vector examinedAcc = new Double2Vector();
    static private boolean signToCenter, signToPlane;
    static private double tendToPlaneFactor, tendParentAheadFactor, tendParentFaceOnFactor;

    private EnemyCalculator(){

    }

    public static Int2Vector getStartPositionWithAtrib
            (Int2Vector myPlanePos, Int2Vector startPos, Int2Vector startPosAttrib){

        Int2Vector resultPos = new Int2Vector();
        EnemyData.StartPositionAtrib atrib;
        int screenRange, screenStart;

        atrib = EnemyData.StartPositionAtrib.getFromID(startPosAttrib.x);
        screenRange = Global.virtualScreenLimit.width();
        screenStart = Global.virtualScreenLimit.left;
        resultPos.x = screenStart +
                getEvaluatePositionValue(startPos.x, screenRange, myPlanePos.x - screenStart, atrib);

        atrib = EnemyData.StartPositionAtrib.getFromID(startPosAttrib.y);
        screenRange = (int)Global.virtualScreenLimit.height();
        screenStart = (int)Global.virtualScreenLimit.top;
        resultPos.y = screenStart +
                getEvaluatePositionValue(startPos.y, screenRange, myPlanePos.y - screenStart, atrib);

        return resultPos;
    }

    public static Double2Vector getStartVelocityWithAtrib(CalcParam calcParam){

        setSpecialVelocityVectors(calcParam);

        EnemyData.StartVectorAtrib atrib;

        atrib = EnemyData.StartVectorAtrib.getFromID(calcParam.startVelocityAttribute.x);
        signToPlane = calcParam.myPlanePosition.x > calcParam.enemyPosition.x;
        signToCenter = Global.virtualScreenLimit.centerX() > calcParam.enemyPosition.x;
        tendToPlaneFactor = tendToPlaneVelocity.x;
        tendParentAheadFactor = tendParentAheadVelocity.x;
        tendParentFaceOnFactor = tendParentFaceOnVelocity.x;

        examinedVel.x = getEvaluateVectorValue(calcParam.startVelocity.x, atrib);

        atrib = EnemyData.StartVectorAtrib.getFromID(calcParam.startVelocityAttribute.y);
        signToPlane = calcParam.myPlanePosition.y > calcParam.enemyPosition.y;
        signToCenter = Global.virtualScreenLimit.centerY() > calcParam.enemyPosition.y;
        tendToPlaneFactor = tendToPlaneVelocity.y;
        tendParentAheadFactor = tendParentAheadVelocity.y;
        tendParentFaceOnFactor = tendParentFaceOnVelocity.y;

        examinedVel.y = getEvaluateVectorValue(calcParam.startVelocity.y, atrib);

        return examinedVel;
    }

    public static Double2Vector getStartAccelerationWithAtrib(CalcParam calcParam){

        setSpecialAccelerationVectors(calcParam);

        EnemyData.StartVectorAtrib atrib;

        atrib = EnemyData.StartVectorAtrib.getFromID(calcParam.startAccelerationAttribute.x);
        signToPlane = calcParam.myPlanePosition.x > calcParam.enemyPosition.x;
        signToCenter = Global.virtualScreenLimit.centerX() > calcParam.enemyPosition.x;
        tendToPlaneFactor = tendToPlaneAcceleration.x;
        tendParentAheadFactor = tendParentAheadAcceleration.x;
        tendParentFaceOnFactor = tendParentFaceOnAcceleration.x;

        examinedAcc.x = getEvaluateVectorValue(calcParam.startAcceleration.x, atrib);

        atrib = EnemyData.StartVectorAtrib.getFromID(calcParam.startAccelerationAttribute.y);
        signToPlane = calcParam.myPlanePosition.y > calcParam.enemyPosition.y;
        signToCenter = Global.virtualScreenLimit.centerY() > calcParam.enemyPosition.y;
        tendToPlaneFactor = tendToPlaneAcceleration.y;
        tendParentAheadFactor = tendParentAheadAcceleration.y;
        tendParentFaceOnFactor = tendParentFaceOnAcceleration.y;

        examinedAcc.y = getEvaluateVectorValue(calcParam.startAcceleration.y, atrib);

        return examinedAcc;
    }

    private static void setSpecialVelocityVectors(CalcParam calcParam){

        double speed = calcParam.startVelocity.length();

        tendToPlaneVelocity.set(
                calcParam.myPlanePosition.x - calcParam.enemyPosition.x,
                calcParam.myPlanePosition.y - calcParam.enemyPosition.y
        );
        tendToPlaneVelocity.normalize(speed);

        if(calcParam.hasParent){
            tendParentAheadVelocity.set(
                    calcParam.parentVelocity.x,
                    calcParam.parentVelocity.y
            );
            tendParentAheadVelocity.normalize(speed);

            tendParentFaceOnVelocity.copy(calcParam.parentFaceOnVector);
            tendParentFaceOnVelocity.normalize(speed);
        }
    }

    private static void setSpecialAccelerationVectors(CalcParam calcParam){

        double acceleration = calcParam.startAcceleration.length();

        tendToPlaneAcceleration.set(
                calcParam.myPlanePosition.x - calcParam.enemyPosition.x,
                calcParam.myPlanePosition.y - calcParam.enemyPosition.y
        );
        tendToPlaneAcceleration.normalize(acceleration);

        if(calcParam.hasParent){
            tendParentAheadAcceleration.set(
                    calcParam.parentAcceleration.x,
                    calcParam.parentAcceleration.y
            );
            tendParentAheadAcceleration.normalize(acceleration);

            tendParentFaceOnAcceleration.copy(calcParam.parentFaceOnVector);
            tendParentFaceOnAcceleration.normalize(acceleration);
        }
    }

    private static double getEvaluateVectorValue
            (double evaFactor, EnemyData.StartVectorAtrib atrib){

        double positiveVal = Math.abs(evaFactor);
        double negativeVal = -positiveVal;

        switch(atrib){

            case DEFAULT:

                return evaFactor;

            case ADOPT_TO_CENTER:

                if(signToCenter)
                    return positiveVal;
                else
                    return negativeVal;

            case ADOPT_TO_COUNTERCENTER:

                if(signToCenter)
                    return negativeVal;
                else
                    return positiveVal;

            case TEND_TO_PLANE:

                return tendToPlaneFactor;

            case TEND_PARENT_AHEAD:

                return tendParentAheadFactor;

            case ADOPT_TO_PLANE:

                if(signToPlane)
                    return positiveVal;
                else
                    return negativeVal;

            case ADOPT_TO_COUNTERPLANE:

                if(signToPlane)
                    return negativeVal;
                else
                    return positiveVal;

            case TEND_PARENT_FACEON:

                return tendParentFaceOnFactor;
        }

        return evaFactor;
    }

    private static int getEvaluatePositionValue
            (int posRate, int screenRange, int planePos, EnemyData.StartPositionAtrib atrib){

        // planePosは画面左端からの距離

        int defVal    = (int)(screenRange * posRate / 100);
        int cntVal    = screenRange - defVal;
        int centerVal = screenRange / 2;

        boolean isSameSide =(defVal>centerVal && planePos>centerVal)||
                (defVal<centerVal && planePos<centerVal);

        switch(atrib){

            case DEFAULT:

                return defVal;

            case PLANE_SIDE:

                if(isSameSide)
                    return defVal;
                else
                    return cntVal;

            case COUNTERPLANE_SIDE:

                if(isSameSide)
                    return cntVal;
                else
                    return defVal;

            case POS_PLANE:

                return planePos;
        }
        return defVal;
    }
}
