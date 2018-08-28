package com.gondragon.shoot2.texture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gondragon.shoot2.R;
import com.gondragon.shoot2.database.AccessOfTextureData;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class TextureSheetTest {

    TextureSheet textureSheet;

    @Before
    public void setup(){

        textureSheet = new TextureSheet();
    }

    @Test
    public void initialize() {


    }

    Bitmap texImage;

    private void readImage(String pictureName){

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
}