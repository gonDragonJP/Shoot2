package com.gondragon.shoot2.texture;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.gondragon.shoot2.database.AccessOfTextureData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class TextureSheetTest {

    @Test
    public void useAppContext() {

        Context appContext = InstrumentationRegistry.getTargetContext();

        Resources resoure = appContext.getResources();

        AssetManager assetManager = appContext.getAssets();

        readImage(assetManager, "texImage/chr_sheet.png");

        Logger logger = Logger.getLogger("hoge");
        logger.info("hogehoge");
    }

    Bitmap texImage;

    private void readImage(AssetManager am, String pictureName){

        try {

            InputStream stream = am.open(pictureName);
            texImage = BitmapFactory.decodeStream(new BufferedInputStream(stream));



        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
