package com.gondragon.shoot2.effect.effectable;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;
import com.gondragon.shoot2.effect.ScreenEffectable;

import javax.microedition.khronos.opengles.GL10;

public abstract class BasicEffect implements ScreenEffectable{

    public boolean isActive = true;
    public boolean isStarted = false;
    public boolean isFinished = false;

    public int preWaitingFrame, processFrame, durationFrame;
    public int preWaitingFrameCount, processFrameCount, durationFrameCount;


    public BasicEffect(int preWaitingMsec, int processMsec, int durationMsec){

        int frameMsec = Global.frameIntervalTime;

        preWaitingFrame = preWaitingMsec / frameMsec;
        processFrame = processMsec / frameMsec;
        durationFrame = durationMsec / frameMsec;
    }

    @Override
    public boolean isActive(){
            // レンダリングスレッドが終了を認識すると非アクティブになります

        return isActive;
    }

    @Override
    public boolean isFinished(){
            // エフェクト個別のスレッドでエフェクトプロセスが終了すると非アクティブになります
            // レンダリングはまだ終了していません
        return isFinished;
    }


    @Override
    public boolean draw(GL10 gl){

        if(!isActive || !isStarted) return false;
        if(isFinished){

            finish(gl);
            isActive = false;
            return false;
        }
        _draw(gl);

        return true;
    };

    @Override
    public void periodicalProcess(){

        if(preWaitingFrameCount < preWaitingFrame){

            preWaitingFrameCount++;
            return;
        }

        isStarted = true;

        if(processFrameCount < processFrame){

            _periodicalProcess();
            processFrameCount++;
        }
        else{

            if(durationFrameCount++ > durationFrame){

                isFinished = true;
            }
        }
    }

    protected abstract void _draw(GL10 gl);
    protected abstract void _periodicalProcess();
    protected abstract void finish(GL10 gl);
}
