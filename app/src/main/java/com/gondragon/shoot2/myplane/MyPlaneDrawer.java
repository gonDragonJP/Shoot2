package com.gondragon.shoot2.myplane;

import android.graphics.PointF;
import android.util.Log;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationManager;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.texture.TextureInitializer;
import com.gondragon.shoot2.texture.TextureSheet;

import javax.microedition.khronos.opengles.GL10;

public class MyPlaneDrawer {

    private int screenX, screenY, screenBottomLimit;


    private AnimationManager animationManager;
    private AnimationSet
            animeSet, chargingBallAnimeSet, chargedBallAnimeSet, conversionAnimeSet,
            shieldAnimeSet, burnerAnimeSet;

    private int moveAnimeFrameIndex;
    private int chargeBallAnimeFrameIndex, chargeAnimeFrame;
    private int conversionAnimeFrameIndex, conversionAnimeFrame;
    private int shieldAnimeFrameIndex, shieldAnimeFrame;
    private int burnerAnimeFrameIndex, burnerAnimeFrame;

    private MyPlane plane;

    public MyPlaneDrawer(MyPlane myPlane){

        plane = myPlane;

        initialize();
    }

    public void initialize(){

        screenX = (int)Global.virtualScreenSize.x;
        screenY = (int)Global.virtualScreenSize.y;
        screenBottomLimit = screenY - planeSize;

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

        resetAnimeState();
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

    public static final int planeSize =64;
    private static PointF drawCenter = new PointF();
    private static PointF drawSize = new PointF();
    private static TextureSheet drawSheet;

    private void setFrame(AnimationData animeData, int currentIndex){

        int frameIndex = animeData.frameOffset + currentIndex;
        int textureID = animeData.textureID;
        drawSize.set((float)animeData.drawSize.x, (float)animeData.drawSize.y);
        drawSheet = StageData.enumTexSheets[textureID];

        UtilGL.setTextureSTCoords(drawSheet.getSTRect(frameIndex));
    }

    private void drawFrame(GL10 gl){

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glTranslatef(drawCenter.x, drawCenter.y, 0);
            drawCenter.set(0, 0);
            UtilGL.drawTexture(gl, drawCenter, drawSize, drawSheet.GLtexID);
        }
        gl.glPopMatrix();
    }

    private static final float[] shadowColor = {0, 0, 0, 0};
    private static final int shadowDeflectionX = 16;
    private static final int shadowDeflectionY = -16;
    private static final float shadowScaleX = 0.75f;
    private static final float shadowScaleY = 0.75f;

    public void drawShadow(GL10 gl){

        UtilGL.changeTexColor(gl, shadowColor);

        drawCenter.set(plane.x+shadowDeflectionX, plane.y+shadowDeflectionY);

        setFrame(
                animeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                moveAnimeFrameIndex
        );

        drawSize.set(planeSize*shadowScaleX, planeSize*shadowScaleY);
        drawFrame(gl);

        drawSize.set(planeSize, planeSize);
        UtilGL.changeTexColor(gl,null);
    }

    public void onDraw(GL10 gl){

        drawCenter.set(plane.x, plane.y);

        setFrame(
                animeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                moveAnimeFrameIndex
        );
        drawFrame(gl);

        if(plane.state.isNowCharging||plane.state.isAlreadyCharged){
            drawCenter.set(plane.x, plane.y-32);

            setFrame(
                    chargingBallAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                    chargeBallAnimeFrameIndex
            );
            drawFrame(gl);
        }

        if(plane.state.isNowConversion){
            drawCenter.set(plane.x, plane.y);

            setFrame(
                    conversionAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                    conversionAnimeFrameIndex
            );
            drawFrame(gl);
        }

        if(plane.state.isShielding){
            drawCenter.set(plane.x, plane.y);

            setFrame(
                    shieldAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                    shieldAnimeFrameIndex
            );
            drawFrame(gl);
        }

        if(plane.state.isBurnerOn){
            drawCenter.set(plane.x, plane.y+60);

            setFrame(
                    burnerAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                    burnerAnimeFrameIndex
            );
            drawFrame(gl);
        }

        plane.shotGenerator.onDraw(gl);
    }

    public void changeAnimeFrame(){

        double divideSpeed = plane.maxSpeed * 2 / 5d; // 全速の2倍を５フレームに割り当てindexを求めます

        moveAnimeFrameIndex = (int)((plane.velocity.x + plane.maxSpeed) / divideSpeed);
        moveAnimeFrameIndex = moveAnimeFrameIndex > 4 ? 4 : moveAnimeFrameIndex;


        if(plane.state.isNowCharging){

            chargeBallAnimeFrameIndex = animationManager.checkAnimeLimit
                    (chargingBallAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL, 0), ++chargeAnimeFrame);

            if(chargeBallAnimeFrameIndex == -1) {

                plane.state.setChargeFinish();
                chargeAnimeFrame = 0;
            }
        }

        if(plane.state.isAlreadyCharged){

            chargeBallAnimeFrameIndex = animationManager.checkAnimeLimit
                    (chargedBallAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0), ++chargeAnimeFrame);
        }

        if(plane.state.isNowConversion){

            conversionAnimeFrameIndex = animationManager.checkAnimeLimit
                    (conversionAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),++conversionAnimeFrame);
        }

        if(plane.state.isShielding){

            shieldAnimeFrameIndex = animationManager.checkAnimeLimit
                    (shieldAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),++shieldAnimeFrame);

            if(shieldAnimeFrameIndex == -1){

                plane.state.isShielding = false;
            }
        }

        if(plane.state.isBurnerOn){

            burnerAnimeFrameIndex = animationManager.checkAnimeLimit
                    (burnerAnimeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0), ++burnerAnimeFrame);
        }
    }
}
