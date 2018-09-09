package com.gondragon.shoot2.effect.effectable;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;
import com.gondragon.shoot2.effect.ScreenEffectable;

import javax.microedition.khronos.opengles.GL10;

public abstract class BasicEffect implements MyRenderer.Renderable {

    public boolean isActive = true;
    // レンダリングスレッドが終了を認識すると非アクティブになります

    public boolean isStarted = false;
    // エフェクトの開始

    public boolean isFinished = false;
    // エフェクトプロセスが終了すると非アクティブになります
    // レンダリングはまだ終了していません

    public int preWaitingFrame, processFrame, durationFrame;
    public int preWaitingFrameCount, processFrameCount, durationFrameCount;


    public BasicEffect(int preWaitingMsec, int processMsec, int durationMsec){

        int frameMsec = Global.frameIntervalTime;

        preWaitingFrame = preWaitingMsec / frameMsec;
        processFrame = processMsec / frameMsec;
        durationFrame = durationMsec / frameMsec;
    }

    public Timing renderingTiming;

    @Override
    public Timing getTiming() {

        return renderingTiming;
    }

    @Override
    public void render(GL10 gl) {

        if(!isActive || !isStarted) return;
        if(isFinished){

            finish(gl);
            isActive = false;
            return;
        }

        //ここに固有の描画処理
    }

    public void periodicalProcess(){

        if(preWaitingFrameCount < preWaitingFrame){

            preWaitingFrameCount++;
            return;
        }

        isStarted = true;

        if(processFrameCount < processFrame){

            effectProcess(); //固有のプロセス処理
            processFrameCount++;
        }
        else{

            if(durationFrameCount++ > durationFrame){

                isFinished = true;
            }
        }
    }

    protected abstract void effectProcess();
    protected abstract void finish(GL10 gl);
}
