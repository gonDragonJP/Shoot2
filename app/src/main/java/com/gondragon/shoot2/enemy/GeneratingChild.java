package com.gondragon.shoot2.enemy;

public class GeneratingChild {

    public int objectID = -1;
    public int repeat;
    public int startFrame;
    public int intervalFrame;
    public int centerX, centerY;

    public GeneratingChild() {

    }

    public GeneratingChild(GeneratingChild src){

        this();
        this.copy(src);
    }

    public void copy(GeneratingChild srcGen){

        objectID = srcGen.objectID;
        repeat = srcGen.repeat;
        startFrame = srcGen.startFrame;
        intervalFrame = srcGen.intervalFrame;
        centerX = srcGen.centerX;
        centerY = srcGen.centerY;
    }
}
