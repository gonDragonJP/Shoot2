package com.gondragon.shoot2.effect;

import android.util.Log;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.microedition.khronos.opengles.GL10;

public class WorkThread extends Thread {

    private MyRenderer renderer;
    private ScreenEffectable drawEffect;

    public WorkThread(MyRenderer renderer){

        this.renderer = renderer;
    }
    public void startEffectablePreDraw(ScreenEffectable effectable){

        drawEffect = effectable;
        this.start();
    }

    public void startEffectableAfterDraw(ScreenEffectable effectable){

        drawEffect = effectable;
        this.start();
    }

    @Override
    public void run(){

        boolean isActive = true;

        long lastUpdateTime = System.currentTimeMillis();

        while(isActive){

            long countTime = System.currentTimeMillis() - lastUpdateTime;

            if(countTime > Global.frameIntervalTime){

                drawEffect.periodicalProcess();
                lastUpdateTime = System.currentTimeMillis();

                MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
                    @Override
                    public void render(GL10 gl) {

                        drawEffect.draw(gl);
                    }
                };

                renderer.addRenderingTask(renderTask);

                if (drawEffect.isFinished()) isActive = false;
            }
        }
    }
}
