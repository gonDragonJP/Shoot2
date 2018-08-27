package com.gondragon.shoot2.texture;


import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.vector.IntRect;

import java.nio.file.Paths;

public class TextureSheet{

    public String pictureName;
    public int textureID, gridSizeX, gridSizeY;
    public int frameNumberX, frameNumberY;

   // public Image texImage;

    public TextureSheet(){

    }

    public TextureSheet(TextureSheet src){

        copy(src);
    }

    public void initialize(){

        readImage();

       // frameNumberX = (int)texImage.getWidth() / gridSizeX;
        //frameNumberY = (int)texImage.getHeight() / gridSizeY;
    }

    private void readImage(){

        String filePath = AccessOfTextureData.getTexImageDir() + pictureName;
        //texImage = new Image(Paths.get(filePath).toUri().toString());
    }

    public void copy(TextureSheet src){

        this.pictureName = src.pictureName;
        this.textureID = src.textureID;
        this.gridSizeX = src.gridSizeX;
        this.gridSizeY = src.gridSizeY;
        this.frameNumberX = src.frameNumberX;
        this.frameNumberY = src.frameNumberY;

        //this.texImage = src.texImage;
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
