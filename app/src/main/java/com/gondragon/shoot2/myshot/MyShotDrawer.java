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
import com.gondragon.shoot2.texture.TextureSheet;

import javax.microedition.khronos.opengles.GL10;

public class MyShotDrawer {

    protected MyPlane plane;
    private MyShotGenerator shotGenerator;

    protected int totalAnimeFrame;
    protected int animeFrame;
    protected AnimationSet animeSet;
    protected AnimationSet.AnimeKind animeKind = AnimationSet.AnimeKind.NORMAL;

    public MyShotDrawer(){

        initialize();
    }

    private static Rect screenLimit = new Rect();

    private void initialize(){

        screenLimit.left  = (int)Global.virtualScreenLimit.left;
        screenLimit.right = (int)Global.virtualScreenLimit.right;
        screenLimit.top   = (int)Global.virtualScreenLimit.top;
        screenLimit.bottom= (int)Global.virtualScreenLimit.bottom;
    }

    public void setShape(boolean isLaser){

        if(isLaser){
            animeSet = AnimationManager.AnimeObject.getAnimeSet
                    (AnimationManager.AnimeObject.MYLASER, 0);
        }
        else{
            animeSet = AnimationManager.AnimeObject.getAnimeSet
                    (AnimationManager.AnimeObject.MYBULLET, 0);
        }
    }

    public  void setExplosion(){

        animeKind = AnimationSet.AnimeKind.EXPLOSION;
        totalAnimeFrame = 0;
    }

    public boolean checkScreenLimit(int x, int y){

         return !(x<screenLimit.left || y<screenLimit.top || x>screenLimit.right || y>screenLimit.bottom);
    }

    protected boolean animate(){

        animeFrame = AnimationManager.checkAnimeLimit
                (animeSet.getAnime(animeKind, 0), ++totalAnimeFrame);

        if (animeFrame == -1) return false;

        return true;
    }

    protected PointF drawCenter = new PointF();
    private float drawAngle;
    private static PointF drawSize = new PointF(planeSize, planeSize);
    private static TextureSheet drawSheet;



    private void setFrame(AnimationData animeData, int currentIndex){

        int frameIndex = animeData.frameOffset + currentIndex;
        int textureID = animeData.textureID;
        drawSheet = textureSheets[textureID];

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

    public void onDraw(GL10 gl, MyShot myShot){

        drawCenter.set(myShot.x, myShot.y);

        if(!myShot.isInExplosion){

            setFrame(
                    animeSet.getAnime(AnimationSet.AnimeKind.NORMAL,0),
                    animeFrame
            );
        }

        else{

            setFrame(
                    animeSet.getAnime(AnimationSet.AnimeKind.EXPLOSION,0),
                    animeFrame
            );
        }

        drawFrame(gl);
    }
}
