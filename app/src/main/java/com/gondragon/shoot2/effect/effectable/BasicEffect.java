package com.gondragon.shoot2.effect.effectable;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.effect.ScreenEffectable;

import javax.microedition.khronos.opengles.GL10;

public abstract class BasicEffect implements ScreenEffectable{

    boolean isActive = true;
    boolean isStarted = false;
    boolean isFinished = false;

    public int preWaitingFrame, processFrame, durationFrame;
    public int preWaitingFrameCount, processFrameCount, durationFrameCount;

    public BasicEffect(int preWaitingMsec, int processMsec, int durationMsec){

        int frameMsec = Global.frameIntervalTime;

        preWaitingFrame = preWaitingMsec / frameMsec;
        processFrame = processMsec / frameMsec;
        durationFrame = durationMsec / frameMsec;
    }

    @Override
    public boolean draw(GL10 gl){

        if(!isStarted || !isActive) return false;
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
