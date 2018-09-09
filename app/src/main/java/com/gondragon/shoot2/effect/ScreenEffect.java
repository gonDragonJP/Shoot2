package com.gondragon.shoot2.effect;

import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;
import com.gondragon.shoot2.effect.effectable.Cutin;
import com.gondragon.shoot2.effect.effectable.TurningColor;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class ScreenEffect {

    private static MyRenderer renderer;
    static final int frameIntervalTime = Global.frameIntervalTime;

    public static void setRenderer(MyRenderer arg){

        renderer = arg;
    }

    public static void cutinText(
            RectF startRect, RectF endRect, String string,
            int preWaitingSec, int processSec, int durationSec,
            float startAngle, float endAngle,
            TurningColor turningColor){

        Cutin cutin = new Cutin(preWaitingSec, processSec, durationSec);

        cutin.setParam(
                startRect, endRect, string,
                startAngle, endAngle, turningColor
        );

        new WorkThread(renderer).startEffectableAfterDraw(cutin);
    }

    static public TurningColor getTurningColor(
            int startColor, int endColor, int intervalSec, boolean isPendulum,
            int preWaitingSec, int processSec, int durationSec){

        TurningColor tColor = new TurningColor (preWaitingSec, processSec, durationSec);

        tColor.setParam(startColor, endColor, intervalSec, isPendulum);

        new WorkThread(renderer).startEffectablePreDraw(tColor);

        return tColor;
    }
    /*
    public static void typeOutText(
            RectF drawRect, String string, int typeIntervalSec,
            int preWaitingSec, int processSec, int durationSec,
            turningColor turningColor){

        TypeOut typeOut = new ScreenEffect().new TypeOut
                (Effectable Kind.TYPEOUT, preWaitingSec, processSec, durationSec);

        typeOut.setParam(drawRect, string, typeIntervalSec, turningColor);

        workThread.addEffectable(typeOut);
    }

    public enum WipeKind{ HOLEWIPE, REEDSCREENWIPE};

    public static void wipeScreen(
            RectF wipeRect, WipeKind wipeKind, int wipeAngle,  boolean isWipeIn,
            int preWaitingSec, int processSec, int durationSec){

        WipeScreen wScreen = new ScreenEffect().new WipeScreen
                (Effectable Kind.WIPESCREEN, preWaitingSec, processSec, durationSec);

        wScreen.setParam(wipeRect, wipeKind, wipeAngle, isWipeIn);

        workThread.addEffectable PreDraw(wScreen);
        //workThread.addEffectable
        //(wScreen);
    }*/
}
