package com.gondragon.shoot2;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gondragon.shoot2.vector.Int2Vector;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{

    public static Int2Vector screenSize = new Int2Vector();
    public static final float screenSlidingFactor = 4.0f;
    //　screenSize : 設定アスペクト比で確保した実際画面の縦横サイズです
    //  screenSlidingFactor : 自機が画面を動いた際に画面が横にずれるようにする為の係数です

    public static void setScreenVal(int realWidth, int realHeight){

        screenSize.x = realWidth;
        screenSize.y = realHeight;
    }

    ArrayList<MyRenderable> renderingTaskList = new ArrayList<>();

    synchronized public void addRenderingTask(MyRenderable task){

        renderingTaskList.add(task);
    }

    synchronized private void doAllRenderingTasks(GL10 gl){

        for(MyRenderable e : renderingTaskList){

            e.render(gl);
        }

        renderingTaskList.clear();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        renderScreen(gl);
    }

    private void renderScreen(GL10 gl){

        float virturalCenterX = Global.virtualScreenSize.x /2;
        float planeX = virturalCenterX;
        float sx = (planeX - virturalCenterX) / screenSlidingFactor;

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(
                (int)sx, (int)(sx + Global.virtualScreenSize.x),
                (int)Global.virtualScreenSize.y, 0,
                0.5f, -0.5f
        );

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        UtilGL.setColor(gl,Color.RED);
        UtilGL.drawLine(gl,new PointF(0,0),new PointF(Global.virtualScreenSize.x, Global.virtualScreenSize.y));
        UtilGL.drawLine(gl,new PointF(Global.virtualScreenSize.x,0),new PointF(0, Global.virtualScreenSize.y));


        doAllRenderingTasks(gl);

        //UtilGL.enableDefaultBlend();
        //UtilGL.setTextureSTCoords(null);
        //UtilGL.changeTexColor(null);

        //ScreenEffect.preDraw(gl);

        /*objects.stageManager.onDraw(gl);
        if(objects.stageManager.currentPlace.isShadowOn){

            objects.enemyGenerator.onDrawShadow(gl);
            objects.plane.onDrawShadow(gl);
        }
        objects.enemyGenerator.onDrawGrounders(gl);
        objects.enemyGenerator.onDrawAirs(gl);
        objects.plane.onDraw(gl);
        objects.shotGenerator.onDraw(gl);
        objects.itemGenerator.onDraw(gl);
        objects.indicator.onDraw(gl);
        objects.pad.onDraw(gl);*/

        //ScreenEffect.draw(gl);
        //drawFPS();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        Point size = UtilGL.setViewPortWithAspectRatio(gl, width, height, Global.aspectRatio);

        setScreenVal(size.x, size.y);
    }
}
