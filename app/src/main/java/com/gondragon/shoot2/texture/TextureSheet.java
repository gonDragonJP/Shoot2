package com.gondragon.shoot2.texture;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.vector.IntRect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TextureSheet{

    public String pictureName;
    public int textureID, gridSizeX, gridSizeY;
    public int frameNumberX, frameNumberY;

    public Bitmap texImage;

    public TextureSheet(){

    }

    public TextureSheet(TextureSheet src){

        copy(src);
    }

    public void initialize(){

        readImage();

        frameNumberX = texImage.getWidth() / gridSizeX;
        frameNumberY = texImage.getHeight() / gridSizeY;
    }

    private void readImage(){

        String filePath = AccessOfTextureData.getTexImageDir() + pictureName;
        File imageFile = new File(filePath);
        try {
            InputStream stream = new FileInputStream(imageFile);
            texImage = BitmapFactory.decodeStream(new BufferedInputStream(stream));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            texImage = null;
        }
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
}
