package com.gondragon.shoot2.effect.effectable;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.effect.ScreenEffect;

import javax.microedition.khronos.opengles.GL10;

public class WipeScreen extends BasicEffect {

    public enum WipeKind{ HOLEWIPE, REEDSCREENWIPE};

    RectF wipeRect = new RectF();
    PointF wipeCenter = new PointF();
    WipeKind wipeKind;
    int wipeAngle;
    float wipeHoleRadius, endHoleRadius;
    float reedSize, wipeRectHeight;
    boolean isWipeIn = true;

    public WipeScreen(int preWaitingMsec, int processMsec, int durationMsec) {
        super(preWaitingMsec, processMsec, durationMsec);
    }

    public void setParam
            (RectF wipeRect, WipeKind wipeKind, int wipeAngle, boolean isWipeIn){

        this.wipeRect.set(wipeRect);
        this.wipeCenter.set(wipeRect.centerX(), wipeRect.centerY());
        this.wipeKind = wipeKind;
        this.wipeAngle = wipeAngle;
        this.isWipeIn = isWipeIn;

        float w = wipeRect.width(), h = wipeRect.height();
        this.endHoleRadius = (float)(Math.sqrt(w * w + h * h) / 2);
        this.reedSize = h / processFrame;

    }

    @Override
    protected void _draw(GL10 gl) {

        gl.glEnable(GL10.GL_STENCIL_TEST);
        gl.glClear(GL10.GL_STENCIL_BUFFER_BIT);
        gl.glStencilFunc(GL10.GL_ALWAYS, 0x01, 0x01);
        gl.glStencilOp(GL10.GL_REPLACE, GL10.GL_REPLACE, GL10.GL_REPLACE);
        gl.glColorMask(false, false, false, false);

        switch(wipeKind){

            case HOLEWIPE:

                cutHole(gl);
                break;

            case REEDSCREENWIPE:

                cutReedScreen(gl);
                break;
        }

        gl.glStencilFunc(GL10.GL_EQUAL, 0x01, 0x01);
        gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);
        gl.glColorMask(true, true, true, true);

    }

    private void cutHole(GL10 gl){

        UtilGL.drawFillCircle(gl,wipeCenter, wipeHoleRadius, 32);
    }

    private void cutReedScreen(GL10 gl){

        RectF rect = new RectF();

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glTranslatef(wipeCenter.x, wipeCenter.y, 0);
            gl.glRotatef(wipeAngle, 0, 0, 1);
            gl.glTranslatef(-wipeCenter.x, -wipeCenter.y, 0);


            rect.set(wipeRect);
            rect.bottom = rect.top + reedSize;

            do{
                UtilGL.setColor(gl, Color.argb(255,255, 255, 255));

                UtilGL.drawRectAngle(gl, rect);

                rect.bottom += reedSize * 2;
                rect.top += reedSize * 2;

            }while(rect.top < (wipeRect.top + wipeRectHeight));

            rect.set(wipeRect);
            rect.top = rect.bottom - reedSize;

            do{
                UtilGL.setColor(gl, Color.argb(255,255, 255, 255));

                UtilGL.drawRectAngle(gl, rect);

                rect.bottom -= reedSize * 2;
                rect.top -= reedSize * 2;

            }while(rect.bottom > (wipeRect.bottom - wipeRectHeight));

        }
        gl.glPopMatrix();
    }

    @Override
    protected void _periodicalProcess() {

        float rate = (isWipeIn)?
                (float)(processFrameCount + 1) / processFrame
                :(float)(processFrame - processFrameCount) / processFrame;

        wipeHoleRadius = endHoleRadius * rate;

        wipeRectHeight = wipeRect.height() * rate;
    }

    @Override
    protected void finish(GL10 gl) {

        //gl.glDisable(GL10.GL_STENCIL_TEST);
    }
}
