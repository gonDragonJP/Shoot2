package com.gondragon.shoot2.myplane;

import android.graphics.PointF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.animation.AnimationManager;

import javax.microedition.khronos.opengles.GL10;

public class MyPlaneDrawer {

    private int screenX, screenY, screenBottomLimit;
    final int drawSize = 64;
    final PointF size = new PointF(drawSize, drawSize)

    private AnimationManager animationManager;
    private AnimationManager.AnimationSet
            animeSet, chargingBallAnimeSet, chargedBallAnimeSet, conversionAnimeSet,
            shieldAnimeSet, burnerAnimeSet;

    private int moveAnimeFrameIndex;
    private int chargeBallAnimeFrameIndex, chargeAnimeFrame;
    private int conversionAnimeFrameIndex, conversionAnimeFrame;
    private int shieldAnimeFrameIndex, shieldAnimeFrame;
    private int burnerAnimeFrameIndex, burnerAnimeFrame;
    private PointF drawCenter = new PointF();

    public MyPlaneDrawer(){

        screenX = (int)Global.virtualScreenSize.x;
        screenY = (int)Global.virtualScreenSize.y;

        screenBottomLimit = screenY - drawSize;
    }

    public void initialize(){

        animeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYPLANE, 0);

        chargingBallAnimeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYCHARGINGBALL, 0);

        chargedBallAnimeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYCHARGEDBALL, 0);

        conversionAnimeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYCONVERSION, 0);

        shieldAnimeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYSHIELD, 0);

        burnerAnimeSet = animationManager.getAnimationSet
                (AnimationManager.AnimeObject.MYBURNER, 0);
    }

    public void resetAnimeState(){

        isNowCharging = isAlreadyCharged = isShielding = isNowConversion = false;

        moveAnimeFrameIndex = 2;
        chargeBallAnimeFrameIndex = chargeAnimeFrame = 0;
        conversionAnimeFrameIndex = conversionAnimeFrame = 0;
        shieldAnimeFrameIndex = shieldAnimeFrame = 0;
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

    private void changeAnimeFrame(){

        moveAnimeFrameIndex = (int)((velocity.x + maxSpeed) / animeDivSpeed);
        moveAnimeFrameIndex = moveAnimeFrameIndex > 4 ? 4 : moveAnimeFrameIndex;

        if(isNowCharging){

            chargeBallAnimeFrameIndex = animationManager.checkAnimeLimit
                    (chargingBallAnimeSet.getData(AnimeKind.NORMAL, 0), ++chargeAnimeFrame);
            if(chargeBallAnimeFrameIndex == -1) {
                isNowCharging = false;
                isAlreadyCharged = true;
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
