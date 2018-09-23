package com.gondragon.shoot2.UI;

import android.graphics.Color;
import android.graphics.RectF;
import android.view.Menu;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.GraphicPad;
import com.gondragon.shoot2.MainActivity;
import com.gondragon.shoot2.MyRenderer;
import com.gondragon.shoot2.UtilGL;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

public class PauseMenu {

    private final float menuWidth =200, menuHeight=300;
    private final float menuCenterX =Global.virtualScreenSize.x /2;
    private final float menuCenterY =Global.virtualScreenSize.y /2;

    MyRenderer renderer;
    Timer timer;
    MainActivity.MenuCallBack callBack;

    private MenuItem selectedItem;
    private RectF[] menuRect;

    private enum MenuItem{

        Pause("Pause Menu",1.2f,1.2f, Color.WHITE,3,false),
        ResumeGame("Resume Game",1.0f,1.0f,Color.WHITE,2,true),
        ExitGame("Exit Game",1.0f,1.0f,Color.RED,2,true);

        String text;
        float widthRate, heightRate;
        int color, lineBreak;
        boolean selectable;

        MenuItem(
                String text, float widthRate, float heightWeight,
                int color, int lineBreak, boolean selectable
        ){

            this.text = text;   this.widthRate = widthRate; this.heightRate = heightWeight;
            this.color = color; this.lineBreak = lineBreak; this.selectable = selectable;
        }
    }

    public PauseMenu(MyRenderer renderer, MainActivity.MenuCallBack callBack){

        this.renderer = renderer;
        this.callBack = callBack;
    }

    public void show(){

        selectedItem = null;

        setMenuItemsPos();
        requestRender();
        startMenuThread();
    }

    public void hide(){

        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.AFTERDRAW);
        timer.cancel();
    }

    private void requestRender(){

        MyRenderer.Renderable renderable = new MyRenderer.Renderable() {
            @Override
            public Timing getTiming() {
                return Timing.AFTERDRAW;
            }

            @Override
            public void render(GL10 gl) {

                endarkWholeScreen(gl);
                drawMenuBox(gl);
                drawMenuItems(gl);
            }
        };

        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.AFTERDRAW);
        renderer.addRenderingTask(renderable);
    }

    private void endarkWholeScreen(GL10 gl){

        RectF rect = new RectF(0,0, Global.virtualScreenSize.x,Global.virtualScreenSize.y);

        gl.glColor4f(0,0,0,0.5f);
        UtilGL.drawRectAngle(gl, rect);
    }

    private void drawMenuBox(GL10 gl){

        float left = menuCenterX - menuWidth /2;
        float top = menuCenterY - menuHeight /2;
        float right = left + menuWidth;
        float bottom = top + menuHeight;

        gl.glColor4f(0.3f,0.3f,0.3f,0.9f);
        UtilGL.drawRectAngle(gl, new RectF(left,top,right,bottom));
    }

    public int frameCount;

    private void drawMenuItems(GL10 gl){

        frameCount = (++frameCount) % 8;
        boolean signalFrame = frameCount <4;

        for(MenuItem e: MenuItem.values()){

            int color = (selectedItem == e && signalFrame) ? Color.GREEN : e.color;

            UtilGL.changeTexColor(gl, UtilGL.getColorArray(color));
            UtilGL.drawText(gl, menuRect[e.ordinal()],e.text);
        }

        UtilGL.changeTexColor(gl,null);
    }

    private void setMenuItemsPos(){

        menuRect = new RectF[MenuItem.values().length];

        float basicCharXSize = 15;
        float basicCharYSize = 15;

        float textLine =menuCenterY - menuHeight/2 + basicCharYSize;

        for(MenuItem e: MenuItem.values()){

            float textWidth = e.text.length() * basicCharXSize;

            float left = menuCenterX - textWidth * e.widthRate /2;
            float top = textLine;
            float right = left + textWidth * e.widthRate;
            float bottom = top + basicCharYSize * e.heightRate;

           menuRect[e.ordinal()] = new RectF(left, top, right, bottom);

            textLine += basicCharYSize * e.heightRate * e.lineBreak;
        }
    }

    private void startMenuThread(){

        timer = new Timer();

        final TimerTask timerTask = new TimerTask(){

            @Override
            synchronized public void run() {

                selectedItem = getSelection();

                if(selectedItem != null) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doMenuItems();
                }
            }
        };

        timer.schedule(timerTask, 0, Global.frameIntervalTime);
    }

    private MenuItem getSelection(){

        GraphicPad pad = renderer.graphicPad;
        float touchX = pad.getPadCenter().x;
        float touchY = pad.getPadCenter().y;

        for(MenuItem e: MenuItem.values()){

            float left = menuRect[e.ordinal()].left;
            float top = menuRect[e.ordinal()].top;
            float right = menuRect[e.ordinal()].right;
            float bottom = menuRect[e.ordinal()].bottom;

            if ((touchX > left && touchX < right) && (touchY > top && touchY < bottom)){

                return e;
            }
        }
        return null;
    }

    private void doMenuItems(){

        switch (selectedItem){

            case ResumeGame:
                callBack.resumeGameAtMenu();
                break;
            case ExitGame:
                callBack.exitGameAtMenu();
                break;
        }
    }
}
