package com.gondragon.shoot2.myshot;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationManager;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.myplane.MyPlaneDrawer;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.texture.TextureInitializer;
import com.gondragon.shoot2.texture.TextureSheet;

import javax.microedition.khronos.opengles.GL10;

public class MyShotDrawer {

    public enum Shape{BULLET, LASER};

    private int totalAnimeFrame;
    private int animeFrame;
    private AnimationSet animeSet;
    private AnimationSet.AnimeKind animeKind;

    private static Rect screenLimit = new Rect();
    private static boolean isInitialized = false;

    private MyShot myShot;

    public MyShotDrawer(MyShot myShot){

        this.myShot = myShot;

        if(!isInitialized) initialize(); // static field を最初の一回だけ初期化します
    }

    private void initialize(){

        screenLimit.left  = (int)Global.virtualScreenLimit.left;
        screenLimit.right = (int)Global.virtualScreenLimit.right;
        screenLimit.top   = (int)Global.virtualScreenLimit.top;
        screenLimit.bottom= (int)Global.virtualScreenLimit.bottom;

        isInitialized = true;
    }

    public static boolean checkScreenLimit(int x, int y){

        return !(x<screenLimit.left || y<screenLimit.top || x>screenLimit.right || y>screenLimit.bottom);
    }

    public void setShape(Shape shape){

        switch(shape){
            case BULLET:
                animeSet = AnimationManager.AnimeObject.getAnimeSet
                        (AnimationManager.AnimeObject.MYBULLET);
                break;

            case LASER:
                animeSet = AnimationManager.AnimeObject.getAnimeSet
                    (AnimationManager.AnimeObject.MYLASER);
                break;
        }

        animeKind = AnimationSet.AnimeKind.NORMAL;
    }

    public void setExplosion(int startAnimeFrame){

        animeKind = AnimationSet.AnimeKind.EXPLOSION;
        totalAnimeFrame = startAnimeFrame;
    }

    public boolean animate(){

        animeFrame = AnimationManager.checkAnimeLimit
                (animeSet.getAnime(animeKind, 0), ++totalAnimeFrame);

        if (animeFrame == -1) return false;

        return true;
    }

    public void setAnimeFrame(int frame){
        //強制的にアニメフレームを指定します。レーザーの頭尾フレーム指定の為にしか使っていません

        animeFrame = frame;
    }

    private static PointF drawCenter = new PointF();
    private static PointF drawSize = new PointF();
    private static float drawAngle;
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
            gl.glRotatef(drawAngle, 0, 0, 1);
            drawCenter.set(0, 0);
            UtilGL.drawTexture(gl, drawCenter, drawSize, drawSheet.GLtexID);
        }
        gl.glPopMatrix();
    }

    public void onDraw(GL10 gl){
/*
        if(!myShot.isInExplosion){

            setFrame(
                    animeSet.getAnime(animeKind,0),
                    animeFrame
            );
        }

        else{

            setFrame(
                    animeSet.getAnime(AnimationSet.AnimeKind.EXPLOSION,0),
                    animeFrame
            );
        }*/

        try {

            AnimationData anime = animeSet.getAnime(animeKind,0);

            setFrame(
                    anime,
                    animeFrame
            );
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        drawCenter.set(myShot.x, myShot.y);
        drawAngle = 90+(float)(Math.atan2(myShot.velocity.y, myShot.velocity.x) / Global.radian);
        drawFrame(gl);
    }
}
