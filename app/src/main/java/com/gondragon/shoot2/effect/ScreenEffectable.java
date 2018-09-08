package com.gondragon.shoot2.effect;

import javax.microedition.khronos.opengles.GL10;

public interface ScreenEffectable {

    boolean isActive();

    boolean isFinished();

    boolean draw(GL10 gl);

    void periodicalProcess();
}
