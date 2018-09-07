package com.gondragon.shoot2.myshot;

import android.graphics.Point;
import android.util.Log;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.GraphicPad;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.myplane.MyPlaneDrawer;
import com.gondragon.shoot2.vector.Double2Vector;

import javax.microedition.khronos.opengles.GL10;

public class ShotGenerator {

    private static final int maxShot = 200;
    private static MyShot[] shotList = new MyShot[maxShot];

    private static final int maxLaser = 30;
    private static MyLaser[] laserList = new MyLaser[maxLaser];

    private static Point startPos = new Point();
    private static Double2Vector velocity = new Double2Vector();

    private MyPlane plane;

    public ShotGenerator(MyPlane plane){

        this.plane = plane;

        for(int i=0; i<maxShot; i++){

            shotList[i] = new MyShot();
        }

        for(int i=0; i<maxLaser; i++){

            laserList[i] = new MyLaser();
        }
    }

    public void getWeaponEnergy(int energy){

        weaponEnergy += energy;
        if (weaponEnergy > maxWeaponEnergy) weaponEnergy = (int)maxWeaponEnergy;
    }

    public void onDraw(GL10 gl){

        for(MyShot e: shotList){

            if(e.isNowUsed) e.drawer.onDraw(gl);
        }

        for(MyLaser e : laserList){

            if(e.isNowUsed) e.drawer.onDraw(gl);
        }
    }

    public void periodicalProcess(GraphicPad pad){

        if(!checkLaserNow()){

            padProcess(pad);
        }

        int count=0;

        for(MyShot e: shotList){

            if(e.isNowUsed) { count++;
                e.periodicalProcess();
            }
        }

        for(MyLaser e: laserList){

            if(e.isNowUsed) { count++;
                e.periodicalProcess();
            }
        }

        //Log.e("shot & laser count : ", String.valueOf(count));

        checkPositionLimit();
    }

    Long currentTime=0l, chargeSEStartTime= 0l;
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

        for(MyShot e: shotList){

            if(!e.isInScreen) e.isNowUsed = false;
        }

        for(MyLaser e: laserList){

            if(!e.isInScreen) e.isNowUsed = false;
        }
    }

    private void addAShot(int shotPower){

        MyShot newShot = getShot();
        if(newShot == null) return;

        newShot.setShape(MyShotDrawer.Shape.BULLET);
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

    private static int laserCount;
    private static MyLaser previousLaser;

    private boolean checkLaserNow(){

        if(laserCount==0) return false;

        if(addLaserShot()) laserCount--;

        return true;
    }

    private void startLaser(){

        laserCount = maxLaser;
        previousLaser = null;

        //SoundEffect.play(SoundKind.MYLASER);
    }

    private boolean addLaserShot(){

        MyLaser newLaser = getLaser();

        int fixAnimeFrame;

        switch(laserCount){
            case 1:
                fixAnimeFrame = 2;
                break;

            case maxLaser:
                fixAnimeFrame = 0;
                break;

            default:
                fixAnimeFrame = 1;
        }

        newLaser.setShape(MyShotDrawer.Shape.LASER, fixAnimeFrame);

        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 2);
        velocity.set(0, -16);
        newLaser.setVectors(startPos, velocity);
        newLaser.shotPower = 100;
        newLaser.shotRadius = 16;
        newLaser.setPreviousLaser(previousLaser);

        previousLaser = newLaser;
        return true;
    }

    private MyLaser getLaser(){

        MyLaser laser = laserList[laserCount-1];
        laser.initialize();

        return laser;
    }

    final float maxWeaponEnergy = 500;
    int weaponEnergy = 500;
    int weaponLevel;

    private void checkWeaponLevel(){

        if(weaponEnergy<0) {weaponEnergy=0; weaponLevel=0;}

        int currentLevel = (int)(weaponEnergy / maxWeaponEnergy * 3);

        if(currentLevel>=weaponLevel) weaponLevel = currentLevel;
        if(currentLevel<(weaponLevel-1)) weaponLevel--;
    }

    private void addSingleShot(){

        if(currentTime< shotTime+150) return;

        int shotPower = 70;
        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(0, -10);
        addAShot(shotPower);

        shotTime = currentTime;

        //SoundEffect.play(SoundKind.MYSHOT);
    }

    private void addDualShots(){

        if(currentTime< shotTime2+200) return;

        int shotPower = 50;
        startPos.set(plane.x + 5, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(2, -8);
        addAShot(shotPower);

        startPos.set(plane.x - 5, plane.y - MyPlaneDrawer.planeSize / 3);
        velocity.set(-2, -8);
        addAShot(shotPower);

        shotTime2 = currentTime;
        weaponEnergy -= 3;
    }

    private void addLateralShots(){

        if(currentTime< shotTime3+500) return;

        int shotPower = 50;

        startPos.set(plane.x + 5, plane.y - MyPlaneDrawer.planeSize / 5);
        velocity.set(4, -4);
        addAShot(shotPower);

        startPos.set(plane.x - 5, plane.y - MyPlaneDrawer.planeSize / 5);
        velocity.set(-4, -4);
        addAShot(shotPower);

        shotTime3 = currentTime;
        weaponEnergy -= 4;
    }

    private void addSprayShot(){

        if(currentTime< shotTime4+20) return;

        int shotPower = 100;

        startPos.set(plane.x, plane.y - MyPlaneDrawer.planeSize / 3);
        sprayAngle = (sprayAngle + 15) % 360;
        double angle = Math.cos(sprayAngle * Global.radian) - 1.57;
        double vx = Math.cos(angle) * 10;
        double vy = Math.sin(angle) * 10;
        velocity.set(vx, vy);
        addAShot(shotPower);

        shotTime4 = currentTime;
        weaponEnergy -= 5;
    }
}
