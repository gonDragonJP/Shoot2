package com.gondragon.shoot2.texture;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.vector.DoubleRect;
import com.gondragon.shoot2.vector.IntRect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public class TextureSheet{

    public String pictureName;
    public int textureID, gridSizeX, gridSizeY;
    public int frameNumberX, frameNumberY;

    public Bitmap texImage;

    public int GLtexID=-1; // ダミーインデックスの為に生成される事があるので-1のままの時はGLtexture解放をスキップします

    public TextureSheet(){

    }

    public TextureSheet(TextureSheet src){

        copy(src);
    }

    public void initialize(){

    }

    public void copy(TextureSheet src){

        this.pictureName = src.pictureName;
        this.textureID = src.textureID;
        this.gridSizeX = src.gridSizeX;
        this.gridSizeY = src.gridSizeY;
        this.frameNumberX = src.frameNumberX;
        this.frameNumberY = src.frameNumberY;

        this.texImage = src.texImage;
    }

    private IntRect texRect = new IntRect();

    public IntRect getTexRect(int frameIndex){

        int left  = (frameIndex % frameNumberX) * gridSizeX;
        int right = left + gridSizeX;
        int top   = (frameIndex / frameNumberX) * gridSizeY;;
        int bottom= top + gridSizeY;

        texRect.set(left, right, top, bottom);

        return texRect;
    }

    private RectF texSTRect = new RectF();

    public RectF getSTRect(int frameIndex){

        final float texFrameSizeX = 1.0f / frameNumberX;
        final float texFrameSizeY = 1.0f / frameNumberY;

        float left  = (frameIndex % frameNumberX) * texFrameSizeX;
        float right = left + texFrameSizeX;
        float top  = (frameIndex / frameNumberX) * texFrameSizeY;;
        float bottom = top + texFrameSizeY;

        //BMPは画像が逆さまに入っている?

        texSTRect.set(left, top, right, bottom);

        return texSTRect;
    }

    private GL10 gl;

    public void bindGLTexture(GL10 gl){

        this.gl = gl;
        GLtexID = UtilGL.setTexture(gl, texImage);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            releaseTex();
        }
    }

    private void releaseTex() {

        int[] tex = new int[1];
        tex[0] = GLtexID;
        if(GLtexID !=-1) gl.glDeleteTextures(1, tex, 0);
    }
}

