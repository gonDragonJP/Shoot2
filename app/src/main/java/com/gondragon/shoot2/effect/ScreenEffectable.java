package com.gondragon.shoot2.effect;

import javax.microedition.khronos.opengles.GL10;

public interface ScreenEffectable {

    boolean draw(GL10 gl);

    void periodicalProcess();
}
