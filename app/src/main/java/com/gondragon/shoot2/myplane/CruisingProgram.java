package com.gondragon.shoot2.myplane;

import com.gondragon.shoot2.Global;

public class CruisingProgram {

    float speedX, speedY, accY;
    boolean isFinished;

    int frameToCenter, frameOfBurner, frameToTop;

    private MyPlane plane;

    public CruisingProgram(MyPlane plane){

        this.plane = plane;
        initialize();
    }

    public void initialize(){

        isFinished = false;
        speedX = 2;
        speedY = 0;
        accY = -0.3f;
        frameOfBurner = 40;

        calcFrameToCenter();
        calcFrameToTop();
    }

    private void calcFrameToCenter(){

        float dx = Global.virtualScreenSize.x - plane.x;
        if(dx<0) speedX *= -1;

        frameToCenter = Math.abs((int)(dx / speedX));
    }

    private void calcFrameToTop(){

        int dy = plane.y -(int)Global.virtualScreenLimit.top;
        float tempSpeedY = speedY;
        frameToTop = 0;

        do{
            dy += tempSpeedY;
            tempSpeedY += accY;
            frameToTop++;
        }while(dy>0);
    }

    public boolean crusing(){

        if(frameToCenter-->0){

            plane.velocity.x = speedX;
            plane.x +=speedX;
        }
        else if(frameOfBurner-->0){

            plane.velocity.x = 0;
            plane.burnerOn();
        }
        else if(frameToTop-->0){

            plane.y +=speedY;
            speedY += accY;
        }
        else{
            finish();
            return false;
        }

        return true;
    }

    private void finish(){

        isFinished = true;
        plane.burnerOn();
    }
}
