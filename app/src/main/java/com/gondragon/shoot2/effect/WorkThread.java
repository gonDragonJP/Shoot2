package com.gondragon.shoot2.effect;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;

import javax.microedition.khronos.opengles.GL10;

public class WorkThread extends Thread {

    private MyRenderer renderer;
    private ScreenEffectable effectTask;
    private MyRenderer.Renderable.Timing renderingTiming;

    public WorkThread(MyRenderer renderer){

        this.renderer = renderer;
    }

    public void startEffectablePreDraw(ScreenEffectable effectable){

        effectTask = effectable;
        renderingTiming = MyRenderer.Renderable.Timing.PREDRAW;
        this.start();
    }

    public void startEffectableAfterDraw(ScreenEffectable effectable){

        effectTask = effectable;
        renderingTiming = MyRenderer.Renderable.Timing.AFTERDRAW;
        this.start();
    }

    @Override
    public void run(){

        boolean isActive = true;

        long lastUpdateTime = System.currentTimeMillis();

        while(isActive){

            long countTime = System.currentTimeMillis() - lastUpdateTime;

            if(countTime > Global.frameIntervalTime){

                effectTask.periodicalProcess();
                lastUpdateTime = System.currentTimeMillis();

                MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {

                    @Override
                    public Timing getTiming() {

                        return renderingTiming;
                    }

                    @Override
                    public void render(GL10 gl) {

                        effectTask.draw(gl);
                    }
                };
                renderer.deleteRenderingTask(renderingTiming);
                renderer.addRenderingTask(renderTask);

                if (effectTask.isFinished()) isActive = false;
            }
        }
    }
}
