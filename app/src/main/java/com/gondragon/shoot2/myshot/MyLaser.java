package com.gondragon.shoot2.myshot;

import android.util.Log;

public class MyLaser extends MyShot {

    private MyLaser previousLaser;

    public MyLaser(){
        super();

        previousLaser = null;
    }

    @Override
    public void initialize(){

        super.initialize();
    }

    public void setShape(MyShotDrawer.Shape shape, int fixAnimeFrame){

        drawer.setShape(shape);
        drawer.setAnimeFrame(fixAnimeFrame);
    }

    @Override
    public void periodicalProcess(){

        flyAhead();
        isInScreen = drawer.checkScreenLimit(x, y);
        drawer.animate();

        if(previousLaser != null) previousLaser.x = x;
    }

    public void setPreviousLaser(MyLaser laser){

        previousLaser = laser;
    }
}
