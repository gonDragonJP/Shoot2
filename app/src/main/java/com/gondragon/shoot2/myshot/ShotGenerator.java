package com.gondragon.shoot2.myshot;

import android.graphics.Point;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.GraphicPad;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.myplane.MyPlaneDrawer;
import com.gondragon.shoot2.vector.Double2Vector;

import javax.microedition.khronos.opengles.GL10;

public class ShotGenerator {



    float maxWeaponEnergy = 500;
    int weaponEnergy = 500;
    int weaponLevel;



    private static final int maxShot = 200;
    private static MyShot[] shotList = new MyShot[maxShot];

    private static Point startPos = new Point();
    private static Double2Vector velocity = new Double2Vector();

    private MyPlane plane;

    public ShotGenerator(MyPlane plane){

        this.plane = plane;

        for(int i=0; i<maxShot; i++){

            shotList[i] = new MyShot();
        }
    }

    public void getWeaponEnergy(int energy){

        weaponEnergy += energy;
        if (weaponEnergy > maxWeaponEnergy) weaponEnergy = (int)maxWeaponEnergy;
    }

    public void onDraw(GL10 gl){

        for(int i=0; i<maxShot; i++){

            MyShot shot = shotList[i];

            if(shot.isNowUsed) shot.drawer.onDraw(gl);
        }
    }

    public void periodicalProcess(GraphicPad pad){

        if(!checkLaserNow()){

            padProcess(pad);
        }

        int count =0;

        for(int i=0; i<maxShot; i++){

            MyShot shot = shotList[i];


            if(shot.isNowUsed) {
                shot.periodicalProcess();
                count++;
            }
        }

        //Log.e("...............", String.valueOf(count));

        checkPositionLimit();
    }
    
    boolean isLaser;
    int shotPower;

    final int laserNumber = 25;
    int laserCount;
    //MyLaser previousLaser;

    Long currentTime=0l, chargeSEStartTime=0l;
    Long shotTime=0l, shotTime2=0l, shotTime3=0l, shotTime4=0l;
    int sprayAngle;

    private void soundChargingSE(){

        currentTime = System.currentTimeMillis();

        if(currentTime> chargeSEStartTime + 500){

            //SoundEffect.play(SoundKind.CHARGE);
            chargeSEStartTime = currentTime;
        }
    }

    private void padProcess(GraphicPad pad){

        if(!pad.isSetRightPadCenter) {
            if(plane.state.isAlreadyCharged) startLaser();
            plane.resetCharging();
            plane.resetConversion();
            return;
        }

        if(pad.rightPadDirVector.y<-10){

            soundChargingSE();
            conversion();
            return;
        }

        if(pad.rightPadDirVector.y>10){

            soundChargingSE();
            plane.state.isNowCharging = plane.state.isAlreadyCharged ? false : true;
            return;
        }

        checkWeaponLevel();

        currentTime = System.currentTimeMillis();

        switch(weaponLevel){

            case 3: addSprayShot();
            case 2: addLateralShots();
            case 1: addDualShots();
            default: addSingleShot();
        }
    }

    private void conversion(){

        plane.setConversion();

        if(weaponEnergy == (int)maxWeaponEnergy){

            plane.resetConversion();
        }
        else if(plane.executeConversionDamege()) weaponEnergy +=1;
    }

    private void checkPositionLimit(){

        for(int i=maxShot-1; i>=0; i--){

            if(shotList[i].isInScreen == false) shotList[i].isNowUsed = false;
        }
    }

    private boolean checkLaserNow(){

        if(laserCount==0) return false;

        if(addLaserShot()) laserCount--;

        return true;
    }

    private void startLaser(){

        laserCount = laserNumber;
        //previousLaser = null;

        //SoundEffect.play(SoundKind.MYLASER);
    }

    private boolean addLaserShot(){

        isLaser = true;

        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 2);
        velocity.set(0, -16);
        /*
        MyLaser newShot = new MyLaser(objectsContainer);

        switch(laserCount){

            case 1:
                newShot.normalFrameIndex = 2;
                break;

            case laserNumber:
                newShot.normalFrameIndex = 0;
                break;

            default:
                newShot.normalFrameIndex = 1;
        }

        newShot.setShape(isLaser);
        newShot.setVectors(startPos, velocity);
        newShot.shotPower = 100;
        newShot.shotRadius = 16;
        newShot.setFromPreviousLaser(previousLaser);

        shotList.add(newShot);
        previousLaser = newShot;*/
        return true;
    }

    private void checkWeaponLevel(){

        if(weaponEnergy<0) {weaponEnergy=0; weaponLevel=0;}

        int currentLevel = (int)(weaponEnergy / maxWeaponEnergy * 3);

        if(currentLevel>=weaponLevel) weaponLevel = currentLevel;
        if(currentLevel<(weaponLevel-1)) weaponLevel--;
    }

    private void addAShot(){

        isLaser = false;

        MyShot newShot = getShot();
        if(newShot == null) return;

        newShot.drawer.setShape(isLaser);
        newShot.setVectors(startPos, velocity);
        newShot.shotPower = shotPower;
        newShot.shotRadius = 12;
    }

    private MyShot getShot(){

        for(int i=0; i<maxShot; i++){

            MyShot shot = shotList[i];

            if(!shot.isNowUsed){

                shot.initialize();
                return shot;
            }
        }
        return null;
    }

    private void addSingleShot(){

        if(currentTime< shotTime+150) return;

        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(0, -10);
        shotPower = 100;
        addAShot();

        shotTime = currentTime;

        //SoundEffect.play(SoundKind.MYSHOT);
    }

    private void addDualShots(){

        if(currentTime< shotTime2+200) return;

        shotPower = 50;

        startPos.set(plane.x + 5, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(2, -8);
        addAShot();

        startPos.set(plane.x - 5, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(-2, -8);
        addAShot();

        shotTime2 = currentTime;
        weaponEnergy -= 3;
    }

    private void addLateralShots(){

        if(currentTime< shotTime3+500) return;

        shotPower = 50;

        startPos.set(plane.x + 5, plane.y - MyPlaneDrawer.planeSize / 5);
        velocity.set(4, -4);
        addAShot();

        startPos.set(plane.x - 5, plane.y - MyPlaneDrawer.planeSize / 5);
        velocity.set(-4, -4);
        addAShot();

        shotTime3 = currentTime;
        weaponEnergy -= 4;
    }

    private void addSprayShot(){

        if(currentTime< shotTime4+20) return;

        shotPower = 50;

        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 3);
        sprayAngle = (sprayAngle + 15) % 360;
        double angle = Math.cos(sprayAngle * Global.radian) - 1.57;
        double vx = Math.cos(angle) * 10;
        double vy = Math.sin(angle) * 10;
        velocity.set(vx, vy);
        addAShot();

        shotTime4 = currentTime;
        weaponEnergy -= 5;
    }
}
