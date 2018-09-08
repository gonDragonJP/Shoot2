package com.gondragon.shoot2.effect.effectable;

import android.graphics.Color;

import com.gondragon.shoot2.Global;

import javax.microedition.khronos.opengles.GL10;

public class TurningColor extends BasicEffect {

    float[] startColor = new float[4];
    float[] endColor = new float[4];
    int intervalFrame;
    boolean isPendulum = false;

    public float[] matrix = new float[4];

    public TurningColor(int preWaitingMsec, int processMsec, int durationMsec) {
        super(preWaitingMsec, processMsec, durationMsec);
    }

    public void setParam
            (int startColor, int endColor, int intervalSec, boolean isPendulum){

        this.startColor[0] = Color.red(startColor) / 255f;
        this.startColor[1] = Color.green(startColor) / 255f;
        this.startColor[2] = Color.blue(startColor) / 255f;
        this.startColor[3] = Color.alpha(startColor) / 255f;

        this.endColor[0] = Color.red(endColor) / 255f;
        this.endColor[1] = Color.green(endColor) / 255f;
        this.endColor[2] = Color.blue(endColor) / 255f;
        this.endColor[3] = Color.alpha(endColor) / 255f;

        intervalFrame = intervalSec / Global.frameIntervalTime;
        this.isPendulum = isPendulum;
    }

    @Override
    protected void _draw(GL10 gl) {

    }

    @Override
    protected void _periodicalProcess() {

        if(matrix == null) return;

        float rate = (float)(processFrameCount % intervalFrame) / intervalFrame;

        if(isPendulum) rate = 2 * (0.5f - Math.abs(rate - 0.5f));

        for(int i=0; i<4; i++)
            matrix[i] = startColor[i] + (endColor[i]-startColor[i]) * rate;

    }

    @Override
    protected void finish(GL10 gl) {

    }
}
