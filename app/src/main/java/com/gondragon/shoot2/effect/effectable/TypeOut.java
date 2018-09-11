package com.gondragon.shoot2.effect.effectable;

import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.effect.ScreenEffect;

import javax.microedition.khronos.opengles.GL10;

public class TypeOut extends BasicEffect {

    String string;
    int typeIntervalFrame;
    int typeIntervalFrameCount;
    int typePosition;

    final int promptIntervalFrame = 5;
    int promptIntervalFrameCount;
    boolean isPromptBlinkOn;
    float chrWidth;

    RectF drawRect = new RectF();
    RectF typeRect = new RectF();

    TurningColor turningColor;

    public TypeOut(int preWaitingMsec, int processMsec, int durationMsec) {

        super(preWaitingMsec, processMsec, durationMsec);
    }

    public void setParam(
            RectF drawRect, String string, int typeIntervalSec,
            TurningColor turningColor){

        this.drawRect.set(drawRect);
        this.string = string;
        this.typeIntervalFrame = typeIntervalSec / Global.frameIntervalTime;
        this.turningColor = turningColor;

        chrWidth = drawRect.width() / string.length();
        typeRect.top = drawRect.top;
        typeRect.bottom = drawRect.bottom;
    }

    @Override
    public void render(GL10 gl) {

        super.render(gl);

        if(turningColor !=null)
            UtilGL.changeTexColor(gl, turningColor.matrix);

        for(int i=0; i<typePosition; i++){

            String typeStr = ""+string.charAt(i);

            typeRect.left = drawRect.left + chrWidth * i;
            typeRect.right = typeRect.left + chrWidth;

            UtilGL.drawText(gl, typeRect, typeStr);
        }

        if(isPromptBlinkOn){

            typeRect.left = typeRect.right;
            typeRect.right = typeRect.left + chrWidth;

            UtilGL.drawText(gl, typeRect, "_");
        }

        UtilGL.changeTexColor(gl,null);
    }

    @Override
    public void effectProcess() {

        if(promptIntervalFrameCount++ > promptIntervalFrame){

            promptIntervalFrameCount = 0;
            isPromptBlinkOn = (isPromptBlinkOn)? false : true;
        }

        typePosition = typeIntervalFrameCount++ / typeIntervalFrame;

        if(typePosition > string.length()){

            typePosition = string.length();
            isPromptBlinkOn = false;
        }
    }

    @Override
    protected void finish(GL10 gl) {

    }
}
