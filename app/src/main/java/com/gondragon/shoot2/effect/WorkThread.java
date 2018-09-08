package com.gondragon.shoot2.effect;

import android.util.Log;

import com.gondragon.shoot2.Global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class WorkThread extends Thread {

    private ScreenEffectable drawEffect;

    public void startEffectablePreDraw(ScreenEffectable effectable){

        drawEffect = effectable;
        this.start();
    }

    public void startEffectableAfterDraw(ScreenEffectable effectable){

        drawEffect = effectable;
        this.start();
    }

    @Override
    public void run(){

        boolean isActive = true;

        long lastUpdateTime = System.currentTimeMillis();

        while(isActive){

            long countTime = System.currentTimeMillis() - lastUpdateTime;

            if(countTime > Global.frameIntervalTime){

                drawEffect.periodicalProcess();
                lastUpdateTime = System.currentTimeMillis();

                if (drawEffect.isFinished()) isActive = false;
            }
        }
        Logger.getLogger("TestEffect").warning("finished");
    }
}
