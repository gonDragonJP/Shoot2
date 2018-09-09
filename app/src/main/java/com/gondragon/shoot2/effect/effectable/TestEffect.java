package com.gondragon.shoot2.effect.effectable;

import java.util.logging.Logger;

import javax.microedition.khronos.opengles.GL10;

public class TestEffect extends BasicEffect {
    public TestEffect(int preWaitingMsec, int processMsec, int durationMsec) {
        super(preWaitingMsec, processMsec, durationMsec);
    }

    int count = 0;

    @Override
    protected void _draw(GL10 gl) {

    }

    @Override
    protected void _periodicalProcess() {

        Logger.getLogger("TestEffect").warning("loop count:" + String.valueOf(count++));
    }

    @Override
    protected void finish(GL10 gl) {

    }
}
