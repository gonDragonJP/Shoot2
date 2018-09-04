package com.gondragon.shoot2.myplane;

import android.graphics.PointF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.GraphicPad;
import com.gondragon.shoot2.vector.Int2Vector;

import javax.microedition.khronos.opengles.GL10;

public class MyPlane {

    public interface CallbackOfMyPlane {

        Int2Vector getMyPlanePos();
        void setMyPlanePos(Int2Vector planePos);
    }


    final int collisionRadius = 16;
    ;


    private GraphicPad pad;

    public int x, y;
    public static final float maxHP = 500;
    public int hitPoints = (int)maxHP;
    public boolean isNowCharging, isAlreadyCharged;
    public boolean isShielding = false;
    public boolean isNowConversion;
    public boolean isBurnerOn=false;

    private boolean isAutoCruisingMode = false;
    private CruisingProgram cruisingProgram;

    public Double2Vector velocity = new Double2Vector();
    private int maxSpeed = 6;
    private double animeDivSpeed = maxSpeed * 2 / 5d;


    public MyPlane(){
    }

    public void initialize(ObjectsContainer objectsContainer){

        animationManager = objectsContainer.animationManager;
        pad = objectsContainer.pad;

        setStartingState();
    }

    public void setStartingState(){

        x = Global.virtualScreenSize.x / 2;
        y = Global.virtualScreenSize.y / 4 * 3;

        resetState();
    }

    private void resetState(){

        isNowCharging = isAlreadyCharged = isShielding = isNowConversion = false;

        moveAnimeFrameIndex = 2;
        chargeBallAnimeFrameIndex = chargeAnimeFrame = 0;
        conversionAnimeFrameIndex = conversionAnimeFrame = 0;
        shieldAnimeFrameIndex = shieldAnimeFrame = 0;
    }

    final float[] shadowColor = {0, 0, 0, 0};

    synchronized public void periodicalProcess(){

        if(isAutoCruisingMode)
            isAutoCruisingMode = cruisingProgram.crusing();
        else getPadInput();

        changeAnimeFrame();
    }

    public void setDamaged(int enemyAtackPoint){

        if(!isShielding){

            hitPoints -= enemyAtackPoint;

            isShielding = true;
            shieldAnimeFrameIndex = 0;
            shieldAnimeFrame = 0;

            SoundEffect.play(SoundKind.SHIELDING);
        }

        if(hitPoints<0){

            hitPoints=0;
        }
    }

    public int getShieldEnergy(int energy){

        hitPoints += energy;

        int overPoints = hitPoints - (int)maxHP;
        if(overPoints>0){
            hitPoints = (int)maxHP;
            return overPoints;
        }

        return 0;
    }

    public void resetCharging(){

        isAlreadyCharged = false;
        isNowCharging = false;
        chargeAnimeFrame = 0;
    }

    public void setConversion(boolean isNowConversion){

        if((isNowConversion)&&(hitPoints>1)) this.isNowConversion = true;
        else {
            this.isNowConversion = false;
            conversionAnimeFrameIndex = 0;
            conversionAnimeFrame = 0;
        }
    }

    public boolean requestConversion(){

        if(hitPoints>1){

            hitPoints -=1;
            return true;
        }

        return false;
    }

    public void setAutoCruising(CruisingProgram program){

        if(program==null){
            isAutoCruisingMode = false;
            return;
        }

        cruisingProgram = program;
        resetAnimeState();
        isAutoCruisingMode = true;
        cruisingProgram.initialize();
    }

    private void getPadInput(){

        if(pad==null || pad.isSetLeftPadCenter==false){

            velocity.set(0, 0);
            return;
        }

        velocity.set(pad.leftPadDirVector.x, pad.leftPadDirVector.y);
        velocity.limit(maxSpeed);

        x += velocity.x;
        y += velocity.y;

        limitPosition();
    }

    private void limitPosition(){

        if(x<0) x=0;
        if(y<0) y=0;
        if(x>screenX) x=screenX;
        if(y>screenBottomLimit) y=screenBottomLimit;
    }

}
