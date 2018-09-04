package com.gondragon.shoot2.myplane;

import android.graphics.PointF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.animation.AnimationManager;
import com.gondragon.shoot2.animation.AnimationSet;

import javax.microedition.khronos.opengles.GL10;

public class MyPlaneDrawer {

    private int screenX, screenY, screenBottomLimit;
    final int drawSize = 64;
    final PointF size = new PointF(drawSize, drawSize)

    private AnimationManager animationManager;
    private AnimationSet
            animeSet, chargingBallAnimeSet, chargedBallAnimeSet, conversionAnimeSet,
            shieldAnimeSet, burnerAnimeSet;

    private int moveAnimeFrameIndex;
    private int chargeBallAnimeFrameIndex, chargeAnimeFrame;
    private int conversionAnimeFrameIndex, conversionAnimeFrame;
    private int shieldAnimeFrameIndex, shieldAnimeFrame;
    private int burnerAnimeFrameIndex, burnerAnimeFrame;
    private PointF drawCenter = new PointF();

    final float[] shadowColor = {0, 0, 0, 0};

    public MyPlaneDrawer(){

        screenX = (int)Global.virtualScreenSize.x;
        screenY = (int)Global.virtualScreenSize.y;

        screenBottomLimit = screenY - drawSize;
    }

    public void initialize(){

        animeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYPLANE);

        chargingBallAnimeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYCHARGINGBALL);

        chargedBallAnimeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYCHARGEDBALL);

        conversionAnimeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYCONVERSION);

        shieldAnimeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYSHIELD);

        burnerAnimeSet = AnimationManager.AnimeObject.getAnimeSet
                (AnimationManager.AnimeObject.MYBURNER);
    }

    public void resetAnimeState(){

        moveAnimeFrameIndex = 2;
        chargeBallAnimeFrameIndex = chargeAnimeFrame = 0;
        conversionAnimeFrameIndex = conversionAnimeFrame = 0;
        shieldAnimeFrameIndex = shieldAnimeFrame = 0;
    }

    public void setShildOn(){

        shieldAnimeFrameIndex = 0;
        shieldAnimeFrame = 0;
    }

    public void resetCharge(){

        chargeAnimeFrame = 0;
    }

    public void resetConversion(){

        conversionAnimeFrameIndex = 0;
        conversionAnimeFrame = 0;
    }

    public void limitPosition(MyPlane plane){

        if(plane.x <0) plane.x=0;
        if(plane.y <0) plane.y=0;
        if(plane.x > screenX) plane.x = screenX;
        if(plane.y > screenBottomLimit) plane.y=screenBottomLimit;
    }

    synchronized public void onDrawShadow(GL10 gl){

        InitGL.changeTexColor(shadowColor);

        drawCenter.set(x+Global.shadowDeflectionX, y+Global.shadowDeflectionY);

        animationManager.setFrame(
                animeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                moveAnimeFrameIndex
        );
        animationManager.drawScaledFrame
                (drawCenter, Global.shadowScaleX, Global.shadowScaleY);

        InitGL.changeTexColor(null);
    }

    synchronized public void onDraw(GL10 gl){

        drawCenter.set(x, y);

        animationManager.setFrame(
                animeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                moveAnimeFrameIndex
        );
        animationManager.drawFrame(drawCenter);

        if(isNowCharging){
            drawCenter.set(x, y-32);

            animationManager.setFrame(
                    chargingBallAnimeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                    chargeBallAnimeFrameIndex
            );
            animationManager.drawFrame(drawCenter);
        }

        if(isAlreadyCharged){
            drawCenter.set(x, y-32);

            animationManager.setFrame(
                    chargedBallAnimeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                    chargeBallAnimeFrameIndex
            );
            animationManager.drawFrame(drawCenter);
        }

        if(isNowConversion){
            drawCenter.set(x, y);

            animationManager.setFrame(
                    conversionAnimeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                    conversionAnimeFrameIndex
            );
            animationManager.drawFrame(drawCenter);
        }

        if(isShielding){
            drawCenter.set(x, y);

            animationManager.setFrame(
                    shieldAnimeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                    shieldAnimeFrameIndex
            );
            animationManager.drawFrame(drawCenter);
        }

        if(isBurnerOn){
            drawCenter.set(x, y+60);

            animationManager.setFrame(
                    burnerAnimeSet.getData(AnimationManager.AnimeKind.NORMAL,0),
                    burnerAnimeFrameIndex
            );
            animationManager.drawFrame(drawCenter);
        }
    }

    public void changeAnimeFrame(MyPlane plane){

        double divideSpeed = plane.maxSpeed * 2 / 5d; // 全速の2倍を５フレームに割り当てindexを求めます

        moveAnimeFrameIndex = (int)((plane.velocity.x + plane.maxSpeed) / divideSpeed);
        moveAnimeFrameIndex = moveAnimeFrameIndex > 4 ? 4 : moveAnimeFrameIndex;

        MyPlane.PlaneState state = plane.state;

        if(state.isNowCharging){

            chargeBallAnimeFrameIndex = animationManager.checkAnimeLimit
                    (chargingBallAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL, 0), ++chargeAnimeFrame);

            if(chargeBallAnimeFrameIndex == -1) {

                state.setChargeFinish();
                chargeAnimeFrame = 0;
            }
        }

        if(isAlreadyCharged){

            chargeBallAnimeFrameIndex = animationManager.checkAnimeLimit
                    (chargedBallAnimeSet.getData(AnimeKind.NORMAL,0), ++chargeAnimeFrame);
        }

        if(isNowConversion){

            conversionAnimeFrameIndex = animationManager.checkAnimeLimit
                    (conversionAnimeSet.getData(AnimeKind.NORMAL,0),++conversionAnimeFrame);
        }

        if(isShielding){

            shieldAnimeFrameIndex = animationManager.checkAnimeLimit
                    (shieldAnimeSet.getData(AnimeKind.NORMAL,0),++shieldAnimeFrame);

            if(shieldAnimeFrameIndex == -1){

                isShielding = false;
            }
        }

        if(isBurnerOn){

            burnerAnimeFrameIndex = animationManager.checkAnimeLimit
                    (burnerAnimeSet.getData(AnimeKind.NORMAL,0), ++burnerAnimeFrame);
        }
    }
}
