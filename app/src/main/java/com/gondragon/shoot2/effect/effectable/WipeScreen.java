package com.gondragon.shoot2.effect.effectable;

import javax.microedition.khronos.opengles.GL10;

public class WipeScreen extends BasicEffect {
    public WipeScreen(int preWaitingMsec, int processMsec, int durationMsec) {
        super(preWaitingMsec, processMsec, durationMsec);
    }

    @Override
    protected void _draw(GL10 gl) {

    }

    @Override
    protected void _periodicalProcess() {

    }

    @Override
    protected void finish(GL10 gl) {

    }
}
