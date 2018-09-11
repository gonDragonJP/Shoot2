package com.gondragon.shoot2.effect;

import android.graphics.Color;
import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.effect.effectable.WipeScreen;
import com.gondragon.shoot2.myplane.MyPlane;

public class StageEffect {


    private MyPlane plane;

    public StageEffect(){

    }

    public void initialize(MyPlane plane){

        this.plane = plane;
    }

    public static void startStageEffect(){

        float sx = Global.virtualScreenSize.x;
        float sy = Global.virtualScreenSize.y;
        float sxc= sx/2;
        float syc= sy/2;

        float edgeLength = (float)((sx + sy) / Math.sqrt(2));
        float elh = edgeLength/2;

        RectF wipeRect = new RectF(sxc - elh, syc - elh, sxc + elh, syc + elh);
        ScreenEffect.wipeScreen(wipeRect,
                WipeScreen.WipeKind.REEDSCREENWIPE, 45, true,
                0, 2000, 0);

        ScreenEffect.cutinText(
                new RectF(0,0,0,0),
                new RectF(sxc - 100, syc - 25, sxc + 100, syc + 25),
                "Stage 1",
                1000, 1000, 500, 0, 720, null);

        ScreenEffect.cutinText(
                new RectF(sxc - 100, syc - 25, sxc + 100, syc + 25),
                new RectF(sx,0,sx,0),
                "Stage 1",
                2500, 1000, 0, 720, 0, null);
    }
/*
    public void briefing(int eventObjectID){

        ScreenEffect.ChangingColor changingColor
                = ScreenEffect.getChangingColor
                (Color.RED, Color.BLACK, 500, false, 0, 7001, 0);

        ScreenEffect.cutinText(
                new RectF(screenCenterX,0,screenCenterX,0),
                new RectF(screenCenterX - 135, screenCenterY + 30,
                        screenCenterX + 135, screenCenterY - 30),
                " Warning!", 0, 2000, 5000, 0, 0, changingColor
        );

        SoundEffect.play(SoundKind.SIREN);

        ScreenEffect.ChangingColor changingColor2
                = ScreenEffect.getChangingColor
                (Color.argb(255, 0, 50, 50), Color.argb(255, 50, 200, 150),
                        2000, true, 0, 7000, 0);

        ScreenEffect.typeOutText(
                new RectF(screenCenterX - 105, screenCenterY + 60,
                        screenCenterX + 105, screenCenterY + 30),
                "Enormous Object", 100, 2000, 5000, 0, changingColor2
        );

        ScreenEffect.typeOutText(
                new RectF(screenCenterX - 105, screenCenterY + 90,
                        screenCenterX + 105, screenCenterY + 60),
                "Type : Carrier", 100, 3400, 3600, 0, changingColor2
        );
    }

    public class CruisingProgram{

        float speedX, speedY, accY;
        boolean isFinished = false;

        int frameToCenter, frameOfBurner, frameToTop;

        public void initialize(){

            speedX = 2;
            speedY = 0;
            accY = -0.3f;
            frameOfBurner = 40;

            calcFrameToCenter();
            calcFrameToTop();
        }

        private void calcFrameToCenter(){

            float dx = Global.screenCenter.x - plane.x;
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
                plane.isBurnerOn = true;
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
            plane.isBurnerOn = false;
        }
    }

    public void stageEndPlaneCruising(){

        plane.setAutoCruising(new CruisingProgram());
    }*/
}
