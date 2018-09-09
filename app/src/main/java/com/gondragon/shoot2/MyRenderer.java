package com.gondragon.shoot2;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer{

    public interface Renderable{

        public enum Timing {PREDRAW, ONDRAW, AFTERDRAW};

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

    public boolean isDrawableGraphicPad = false; //操作パッドの描画が必要かどうかのスイッチ
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

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        renderScreen(gl);
    }

    private void renderScreen(GL10 gl){

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        UtilGL.setColor(gl,Color.RED);
        UtilGL.drawLine(gl,new PointF(0,0),new PointF(Global.virtualScreenSize.x, Global.virtualScreenSize.y));
        UtilGL.drawLine(gl,new PointF(Global.virtualScreenSize.x,0),new PointF(0, Global.virtualScreenSize.y));

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

        UtilGL.enableDefaultBlend(gl);
        UtilGL.setTextureSTCoords(null);
        UtilGL.changeTexColor(gl, null);

        doAllRenderingTasks(gl, Renderable.Timing.PREDRAW);
        doAllRenderingTasks(gl, Renderable.Timing.ONDRAW);

        //画面定位置のオブジェクト描出
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(
                0, Global.virtualScreenSize.x,
                Global.virtualScreenSize.y, 0,
                0.5f, -0.5f
        );
        if(isDrawableGraphicPad) graphicPad.onDraw(gl);

        //doAllRenderingTasks(gl, Renderable.Timing.AFTERDRAW);

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

    synchronized private void doAllRenderingTasks(GL10 gl, Renderable.Timing timing) {

        for (MyRenderer.Renderable e : renderingTaskList) {

            if(e.getTiming() == timing) e.render(gl);
        }
    }

@Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        setSurfaceRect(UtilGL.setViewPortWithAspectRatio(gl, width, height, Global.aspectRatio));
    }
}
