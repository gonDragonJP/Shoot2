package com.gondragon.shoot2.effect;

import com.gondragon.shoot2.effect.effectable.Cutin;
import com.gondragon.shoot2.effect.effectable.TestEffect;
import com.gondragon.shoot2.effect.effectable.TurningColor;

import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class WorkThreadTest {

    @Test
    public void test(){

        WorkThread thread = new WorkThread();

        TestEffect tc = new TestEffect(0,1000,100);

        thread.startEffectablePreDraw(tc);

        try {
            thread.join(2000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        Logger.getLogger("TestEffect").warning("alive : " + String.valueOf(thread.isAlive()));

        thread = new WorkThread();

        tc = new TestEffect(0,1000,100);

        thread.startEffectablePreDraw(tc);



        try {
            thread.join(2000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

}