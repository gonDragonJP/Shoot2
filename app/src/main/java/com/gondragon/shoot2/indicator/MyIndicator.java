package com.gondragon.shoot2.indicator;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.texture.TextureInitializer;

import javax.microedition.khronos.opengles.GL10;

public class MyIndicator {

    static final int barRelativeLeft = -40;
    static final int barMaxWidth = 95;
    static final int barHeight = 5;
    static final int shieldBarRelativeTop = -14;
    static final int weaponBarRelativeTop = 10;

    private static int meterCenterX, meterCenterY;
    private static PointF drawCenter, drawSize;
    private static RectF texSTRect, drawRect;

    MyPlane plane;

    public MyIndicator(MyPlane myPlane){

        this.plane = myPlane;

        initialize();
    }

    private void initialize(){

        meterCenterX = Global.virtualScreenSize.x /2;
        meterCenterY = Global.virtualScreenSize.y - 32;

        drawCenter = new PointF();
        drawSize = new PointF();
        texSTRect = new RectF();
        drawRect = new RectF();
    }

    public void onDraw(GL10 gl){

        drawBars(gl);
        drawMeter(gl);
    }

    private void drawBars(GL10 gl){

        float barLeft   = meterCenterX + barRelativeLeft;
        float barTop    = meterCenterY + shieldBarRelativeTop;
        float barRight  = barLeft + plane.hitPoints / plane.maxHP * barMaxWidth;
        float barBottom = barTop + barHeight;

        gl.glColor4f(0.8f, 0.1f, 0, 1);
        drawRect.set(barLeft, barTop, barRight, barBottom);
        UtilGL.drawRectAngle(gl, drawRect);

        barTop    = meterCenterY + weaponBarRelativeTop;
        barRight  = barLeft +
                plane.shotGenerator.weaponEnergy / plane.shotGenerator.maxWeaponEnergy * barMaxWidth;
        barBottom = barTop + barHeight;

        float addColor = plane.shotGenerator.weaponLevel * 0.3f;

        gl.glColor4f(0, 0.5f + addColor, addColor, 1);
        drawRect.set(barLeft, barTop, barRight, barBottom);
        UtilGL.drawRectAngle(gl,drawRect);
    }

    private void drawMeter(GL10 gl){

        drawCenter.set(meterCenterX, meterCenterY);
        drawSize.set(128, 64);
        texSTRect.set(0, 0, 1, 1);

        int meterTexID =
                StageData.enumTexSheets[TextureInitializer.EnumTexture.Meter.textureId].GLtexID;

        UtilGL.setTextureSTCoords(texSTRect);
        UtilGL.drawTexture(gl, drawCenter, drawSize, meterTexID);
    }
}
