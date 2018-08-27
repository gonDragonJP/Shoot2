package com.gondragon.shoot2.animation;

import com.gondragon.shoot2.vector.DoubleRect;

public class TextureSheet{

    int textureID, resourceID;
    int frameNumberX, frameNumberY;
    DoubleRect texRect = new DoubleRect();

    public TextureSheet(int resourceID, int nx, int ny){

        //textureID = InitGL.loadTexture(context.getResources(), resourceID);
        this.resourceID = resourceID;
        this.frameNumberX = nx;
        this.frameNumberY = ny;
    }

    public DoubleRect getTexPositionRect(int frameIndex){

        final float texFrameSizeX = 1.0f / frameNumberX;
        final float texFrameSizeY = 1.0f / frameNumberY;

        float left  = (frameIndex % frameNumberX) * texFrameSizeX;
        float right = left + texFrameSizeX;
        float top   = (frameIndex / frameNumberX) * texFrameSizeY;;
        float bottom= top + texFrameSizeY;

        texRect.set(left, right, top, bottom);

        return texRect;
    }

    public void release(){

        //InitGL.TextureManager.deleteTexture(resourceID);
    }
}
