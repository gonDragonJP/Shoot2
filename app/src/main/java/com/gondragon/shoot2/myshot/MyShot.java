package com.gondragon.shoot2.myshot;

import android.graphics.Point;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.vector.Double2Vector;

public class MyShot {

    public int x, y;
    public Double2Vector velocity = new Double2Vector();

    public boolean isInScreen;
    public boolean isInExplosion;
    public boolean isLaser;

    public int shotPower, shotRadius;

    MyShotDrawer drawer;

    public MyShot(){

        drawer = new MyShotDrawer(this);

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
