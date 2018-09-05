package com.gondragon.shoot2.myplane;

import android.graphics.PointF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.GraphicPad;
import com.gondragon.shoot2.myshot.ShotGenerator;
import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

import javax.microedition.khronos.opengles.GL10;

public class MyPlane implements CallbackOfMyPlane{

    @Override
    public Int2Vector getMyPlanePos() {

        return new Int2Vector(x, y);
    }

    @Override
    public void setMyPlanePos(Int2Vector planePos) {

        x = planePos.x;
        y = planePos.y;
    }

    public PlaneState state;

    public class PlaneState {

        public boolean isNowCharging, isAlreadyCharged;
        public boolean isShielding;
        public boolean isNowConversion;
        public boolean isBurnerOn;
        public boolean isAutoCruisingMode;

        public PlaneState(){

        }

        public void reset(){

            isNowCharging = false;
            isAlreadyCharged = false;
            isShielding = false;
            isNowConversion = false;
            isBurnerOn = false;
            isAutoCruisingMode = false;
        }

        public void setChargeFinish(){

            isNowCharging = false;
            isAlreadyCharged = true;
        }
    }

    public static final float maxHP = 500;
    public int hitPoints = (int)maxHP;

    final int collisionRadius = 16;

    public final int maxSpeed = 6;

    public int x, y;
    public Double2Vector velocity;

    public ShotGenerator shotGenerator;
    private CruisingProgram cruisingProgram;
    public MyPlaneDrawer drawer;

    public MyPlane(){

        state = new PlaneState();
        velocity = new Double2Vector();
        shotGenerator = new ShotGenerator(this);
        drawer = new MyPlaneDrawer(this);

        initialize();
    }

    public void initialize(){

        x = Global.virtualScreenSize.x / 2;
        y = Global.virtualScreenSize.y / 4 * 3;
    }

    public void burnerOn(){

        state.isBurnerOn = true;
    }

    public void periodicalProcess(GraphicPad pad){

        if(state.isAutoCruisingMode)
            state.isAutoCruisingMode = cruisingProgram.crusing();

        else getPadInput(pad);

        drawer.changeAnimeFrame();

        shotGenerator.periodicalProcess(pad);
    }

    public void setDamaged(int enemyAtackPoint){

        if(!state.isShielding){

            hitPoints -= enemyAtackPoint;

            state.isShielding = true;
            drawer.setShildOn();

            //SoundEffect.play(SoundKind.SHIELDING);
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

        state.isAlreadyCharged = false;
        state.isNowCharging = false;
        drawer.resetCharge();
    }

    public void setConversion(boolean sw){

        if(sw && (hitPoints>1)) state.isNowConversion = true;

        else {
            state.isNowConversion = false;
            drawer.resetConversion();
        }
    }

    public boolean executeConversionDamege(){

        if(hitPoints>1){

            hitPoints -=1;
            return true;
        }

        return false;
    }

    public void setAutoCruising(CruisingProgram program){

        if(program==null){
            state.isAutoCruisingMode = false;
            return;
        }

        drawer.resetAnimeState();
        state.isAutoCruisingMode = true;

        cruisingProgram = program;
        cruisingProgram.initialize();
    }

    private void getPadInput(GraphicPad pad){

        if(pad==null || pad.isSetLeftPadCenter==false){

            velocity.set(0, 0);
            return;
        }

        velocity.set(pad.leftPadDirVector.x, pad.leftPadDirVector.y);
        velocity.limit(maxSpeed);

        x += velocity.x;
        y += velocity.y;

        drawer.limitPosition(this);
    }
}
