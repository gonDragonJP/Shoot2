package com.gondragon.shoot2.effect;

import android.graphics.RectF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.MyRenderer;
import com.gondragon.shoot2.effect.effectable.BasicEffect;
import com.gondragon.shoot2.effect.effectable.Cutin;
import com.gondragon.shoot2.effect.effectable.TurningColor;
import com.gondragon.shoot2.effect.effectable.TypeOut;
import com.gondragon.shoot2.effect.effectable.WipeScreen;

import java.util.ArrayList;
import java.util.Iterator;
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

        addPreDrawEffect(cutin);
    }

    static public TurningColor getTurningColor(
            int startColor, int endColor, int intervalSec, boolean isPendulum,
            int preWaitingSec, int processSec, int durationSec){

        TurningColor tColor = new TurningColor (preWaitingSec, processSec, durationSec);

        tColor.setParam(startColor, endColor, intervalSec, isPendulum);

        addPreDrawEffect(tColor);

        return tColor;
    }

    public static void typeOutText(
            RectF drawRect, String string, int typeIntervalSec,
            int preWaitingSec, int processSec, int durationSec,
            TurningColor turningColor){

        TypeOut typeOut = new TypeOut(preWaitingSec, processSec, durationSec);

        typeOut.setParam(drawRect, string, typeIntervalSec, turningColor);

        addPreDrawEffect(typeOut);
    }

    public static void wipeScreen(
            RectF wipeRect, WipeScreen.WipeKind wipeKind, int wipeAngle, boolean isWipeIn,
            int preWaitingSec, int processSec, int durationSec){

        WipeScreen wScreen = new WipeScreen(preWaitingSec, processSec, durationSec);

        wScreen.setParam(wipeRect, wipeKind , wipeAngle, isWipeIn);

        addPreDrawEffect(wScreen);
    }

    private static ArrayList<MyRenderer.Renderable> preDrawEffectList = new ArrayList<>();
    private static ArrayList<MyRenderer.Renderable> afterDrawEffectList = new ArrayList<>();

    public static void addPreDrawEffect(BasicEffect effect) {

        effect.renderingTiming = MyRenderer.Renderable.Timing.PREDRAW;
        preDrawEffectList.add(effect);
    }

    public static void afterPreDrawEffect(BasicEffect effect) {

        effect.renderingTiming = MyRenderer.Renderable.Timing.AFTERDRAW;
        afterDrawEffectList.add(effect);
    }

    public static void renderAllLists(){

        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.PREDRAW);
        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.AFTERDRAW);

        for(MyRenderer.Renderable e: preDrawEffectList){

            renderer.addRenderingTask(e);
        }

        for(MyRenderer.Renderable e: afterDrawEffectList){

            renderer.addRenderingTask(e);
        }
    }

    public static void periodicalProcess(){

        Iterator<MyRenderer.Renderable> it;

        it = preDrawEffectList.iterator();
        while(it.hasNext()){

            BasicEffect effect = (BasicEffect) it.next();
            effect.periodicalProcess();
            if(!effect.isActive) it.remove();
        }

        it = afterDrawEffectList.iterator();
        while(it.hasNext()){

            BasicEffect effect = (BasicEffect) it.next();
            effect.periodicalProcess();
            if(!effect.isActive) it.remove();
        }
    }
}
