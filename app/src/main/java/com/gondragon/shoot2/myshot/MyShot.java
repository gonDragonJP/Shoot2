package com.gondragon.shoot2.myshot;

import android.graphics.Point;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.vector.Double2Vector;

public class MyShot {

    //GC抑制の為、弾オブジェクトは使いまわします。isNowUsedは現在使用されているかどうかの判断フラグです。
    public boolean isNowUsed;

    public int x, y;
    public Double2Vector velocity = new Double2Vector();

    public boolean isInScreen;
    public boolean isInExplosion;

    public int shotPower, shotRadius;

    public MyShotDrawer drawer;
    private CollisionChecker collisonChecker;

    public MyShot(){

        isNowUsed = false; //作成直後は使用されていない
        drawer = new MyShotDrawer(this);
        collisonChecker = new CollisionChecker(this);
    }

    public void initialize(){//弾オブジェクトとして使用開始される時に呼び出されます

        isNowUsed = true;
        isInScreen = true;
        isInExplosion = false;
        collisonChecker.setActive(true);
    }

    public void setNotUsed(){

        isNowUsed = false;
        collisonChecker.setActive(false);
    }

    public void setShape(MyShotDrawer.Shape shape){

        drawer.setShape(shape);
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
        isInScreen = drawer.checkScreenLimit(x, y) && drawer.animate();
    }

    public void flyAhead(){

        x += velocity.x;
        y += velocity.y;
    }
}
