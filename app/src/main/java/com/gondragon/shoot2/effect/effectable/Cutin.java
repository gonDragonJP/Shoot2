package com.gondragon.shoot2.effect.effectable;

import android.graphics.RectF;

import com.gondragon.shoot2.UtilGL;

import javax.microedition.khronos.opengles.GL10;

public class Cutin extends BasicEffect {

    RectF startRect, endRect;
    String string;

    float startAngle, endAngle;

    RectF drawRect = new RectF();
    RectF convRect = new RectF();
    float drawAngle;

    TurningColor turningColor;

    public Cutin(int preWaitingMsec, int processMsec, int durationMsec) {

        super(preWaitingMsec, processMsec, durationMsec);
    }

    public void setParam(
            RectF startRect, RectF endRect, String string,
            float startAngle, float endAngle,
            TurningColor turningColor){

        this.startRect = startRect;
        this.endRect = endRect;
        this.string = string;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.turningColor = turningColor;
    }

    @Override
    public void effectRender(GL10 gl) {

        if(turningColor!=null)
            UtilGL.changeTexColor(gl, turningColor.matrix);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glTranslatef(drawRect.centerX(), drawRect.centerY(), 0);
            gl.glRotatef(drawAngle, 0, 0, 1);

            UtilGL.drawText(gl, convRect, string);
        }
        gl.glPopMatrix();

        UtilGL.changeTexColor(gl,null);
    }

    @Override
    protected void effectProcess() {

        float rate = (float)(processFrameCount + 1) / processFrame;

        float left = startRect.left + (endRect.left - startRect.left) * rate;
        float right = startRect.right + (endRect.right - startRect.right) * rate;
        float top = startRect.top + (endRect.top - startRect.top) * rate;
        float bottom = startRect.bottom + (endRect.bottom - startRect.bottom) * rate;

        drawRect.set(left, top, right, bottom);

        float a = drawRect.width() / 2;
        float b = drawRect.height() / 2;

        convRect.set(-a, -b, a, b);

        drawAngle = startAngle + (endAngle - startAngle) * rate;
    }

    @Override
    protected void finish(GL10 gl) {

    }
}
