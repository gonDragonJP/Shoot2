package com.gondragon.shoot2;

import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        renderScreen(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


    }

    private void renderScreen(GL10 gl){

        float planeX=250, screenSizeX=500,screenSizeY=1000,screenProjectionLeft;

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float sx = (planeX - screenSizeX / 2)/4;
        screenProjectionLeft = sx;
        gl.glOrthof(
                (int)sx, (int)(sx + screenSizeX),
                (int)screenSizeY, 0,
                0.5f, -0.5f
        );

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        UtilGL.setColor(gl,Color.RED);
        UtilGL.drawLine(gl,new PointF(0,0),new PointF(200,200));
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
}
