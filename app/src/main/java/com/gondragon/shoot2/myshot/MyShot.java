package com.gondragon.shoot2.myshot;

import android.graphics.Point;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.vector.Double2Vector;

public class MyShot {

    public int x, y;
    public Double2Vector velocity = new Double2Vector();
    public float angle;

    public boolean isInScreen;
    public boolean isInExplosion;
    public boolean isLaser;

    public int shotPower, shotRadius;

    MyShotDrawer drawer;

    public MyShot(){

        drawer = new MyShotDrawer();

        initialize();
    }

    private void initialize(){

        isInScreen = true;
        isInExplosion = false;
    }

    public void setVectors(Point startPos, Double2Vector velocity){

        this.x = startPos.x;
        this.y = startPos.y;

        this.velocity.copy(velocity);

        angle = 90+(float)(Math.atan2(velocity.y, velocity.x) / Global.radian);
    }

    public void setExplosion(){

        isInExplosion = true;
    }

    public void periodicalProcess(){

        flyAhead();
        isInScreen = drawer.checkScreenLimit(x, y);
        isInScreen = drawer.animate();
    }

    private void flyAhead(){

        x += velocity.x;
        y += velocity.y;
    }
}
