package com.gondragon.shoot2.effect;

import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.effect.effectable.Cutin;
import com.gondragon.shoot2.effect.effectable.TurningColor;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class ScreenEffect {/*

    enum EffectableKind{ CUTIN, CHANGINGCOLOR, TYPEOUT, WIPESCREEN};
    public enum WipeKind{ HOLEWIPE, REEDSCREENWIPE};


    static final int frameIntervalTime = Global.frameIntervalTime;

    static WorkThread workThread = new WorkThread();
    private static RectF screenRect = new RectF();

    public static void setScreenRect(RectF rect){

        screenRect.set(rect);
    }

    public static void preDraw(GL10 gl){

        for(int i=0; i<workThread.Effectable List0.size(); i++){

            Effectable Effectable = workThread.Effectable List0.get(i);

            Effectable.draw(gl);
        }
    }

    public static void draw(GL10 gl){

        for(int i=0; i<workThread.Effectable List1.size(); i++){

            Effectable Effectable = workThread.Effectable List1.get(i);

            Effectable.draw(gl);
        }

        for(int i=0; i<workThread.Effectable List2.size(); i++){

            Effectable Effectable = workThread.Effectable List2.get(i);

            Effectable.draw(gl);
        }
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

        workThread.addEffectable(cutin);
    }

    static public turningColor getturningColor(
            int startColor, int endColor, int intervalSec, boolean isPendulum,
            int preWaitingSec, int processSec, int durationSec){

        turningColor cColor =
                new ScreenEffect().new turningColor
                        (Effectable Kind.turningColor, preWaitingSec, processSec, durationSec);

        cColor.setParam(startColor, endColor, intervalSec, isPendulum);

        workThread.addEffectable(cColor);

        return cColor;
    }
    public static void typeOutText(
            RectF drawRect, String string, int typeIntervalSec,
            int preWaitingSec, int processSec, int durationSec,
            turningColor turningColor){

        TypeOut typeOut = new ScreenEffect().new TypeOut
                (Effectable Kind.TYPEOUT, preWaitingSec, processSec, durationSec);

        typeOut.setParam(drawRect, string, typeIntervalSec, turningColor);

        workThread.addEffectable(typeOut);
    }
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
