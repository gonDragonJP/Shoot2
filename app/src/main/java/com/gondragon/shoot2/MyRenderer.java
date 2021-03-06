package com.gondragon.shoot2;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        setSurfaceRect(UtilGL.setViewPortWithAspectRatio(gl, width, height, Global.aspectRatio));
    }

    public interface Renderable{

        public enum Timing {ONCREATE, PREDRAW, ONDRAW, AFTERDRAW, AFTEREFFECT, MENU};

        Timing getTiming();

        void render(GL10 gl);
    }

    public static Rect surfaceRect = new Rect();
    //　surfaceRect : 設定アスペクト比で確保した実際画面におけるゲーム画面座標です

    public static void setSurfaceRect(Rect rect){

        surfaceRect.set(rect);
    }

    private static final float screenSlidingFactor = 4.0f;
    //  screenSlidingFactor : 自機が画面を動いた際に画面が横にずれるようにする為の係数です
    private static float screenSlidingX =0;

    public void setScreenSlidingX(int planeX){

        float virturalCenterX = Global.virtualScreenSize.x /2;
        screenSlidingX = (planeX - virturalCenterX) / screenSlidingFactor;
    }

    public boolean isNowOnTouchPad = false; //現在パッド入力があるかどうかのフラグ
    public GraphicPad graphicPad;

    public void setGraphicPad(GraphicPad pad){

        graphicPad = pad;
    }

    ArrayList<MyRenderer.Renderable> renderingTaskList = new ArrayList<>();

    synchronized public void addRenderingTask(MyRenderer.Renderable task){

        renderingTaskList.add(task);
    }

    synchronized public void deleteRenderingTask(Renderable.Timing timing){

        Iterator<Renderable> it = renderingTaskList.iterator();

        while(it.hasNext()){

            Renderable e = it.next();

            if(e.getTiming() == timing) it.remove();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        UtilGL.setupFont(gl,0);
        UtilGL.enableDefaultBlend(gl);
        UtilGL.setTextureSTCoords(null);
        UtilGL.changeTexColor(gl, null); //テクスチャモードはreplace

        doAllRenderingTasks(gl, Renderable.Timing.ONCREATE);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        renderScreen(gl);
    }

    private void renderScreen(GL10 gl){

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

        doAllRenderingTasks(gl, Renderable.Timing.PREDRAW);

        //自機に伴う視点の移動を考慮した投影範囲の設定
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(
                screenSlidingX, screenSlidingX + Global.virtualScreenSize.x,
                Global.virtualScreenSize.y, 0,
                0.5f, -0.5f
        );

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        //testDraw(gl);
        doAllRenderingTasks(gl, Renderable.Timing.ONDRAW);

        //画面定位置のオブジェクト描出
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(
                0, Global.virtualScreenSize.x,
                Global.virtualScreenSize.y, 0,
                0.5f, -0.5f
        );

        doAllRenderingTasks(gl, Renderable.Timing.AFTERDRAW);
        doAllRenderingTasks(gl, Renderable.Timing.AFTEREFFECT);
        doAllRenderingTasks(gl, Renderable.Timing.MENU);

        if(isNowOnTouchPad) graphicPad.onDraw(gl);

        //testDraw(gl);
        drawFPS(gl);
    }

    synchronized private void doAllRenderingTasks(GL10 gl, Renderable.Timing timing) {

        for (MyRenderer.Renderable e : renderingTaskList) {

            if(e.getTiming() == timing) e.render(gl);
        }
    }

    synchronized private void testDraw(GL10 gl){

        UtilGL.setColor(gl,Color.RED);
        UtilGL.drawLine(gl,new PointF(0,0),new PointF(Global.virtualScreenSize.x, Global.virtualScreenSize.y));
        UtilGL.drawLine(gl,new PointF(Global.virtualScreenSize.x,0),new PointF(0, Global.virtualScreenSize.y));

        int c=0;
        for (MyRenderer.Renderable e : renderingTaskList) {

            if(e.getTiming() == Renderable.Timing.AFTERDRAW) c++;
        }
        UtilGL.drawText(gl,new RectF(0,0,80,20),"list:" + String.valueOf(c));
    }

    private long lastUpdateTime;
    private int fps, frameCount =0;

    synchronized private void drawFPS(GL10 gl){

        ++frameCount;
        long currentTime = System.currentTimeMillis();

        if(currentTime - lastUpdateTime >1000){
            fps = frameCount;
            frameCount =0;
            lastUpdateTime = currentTime;
        }
        UtilGL.drawText(gl,new RectF(0,0,80,20),"FPS:" + String.valueOf(fps));
    }
}
