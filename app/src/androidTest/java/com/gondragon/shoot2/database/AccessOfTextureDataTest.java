package com.gondragon.shoot2.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import android.util.Log;

import com.gondragon.shoot2.texture.TextureSheet;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AccessOfTextureDataTest {

    @Before
    public void setContext() {

        Context context = InstrumentationRegistry.getTargetContext();
        AccessOfTextureData.setContext(context);
    }

    @Test
    public void test() {

        List<TextureSheet>  textureSheetList = new ArrayList<TextureSheet>();
        AccessOfTextureData.setTexDataList(textureSheetList,1);

        TextureSheet testSheet = textureSheetList.get(0);
        Log.e("width----------------",String.valueOf(testSheet.texImage.getWidth()));
        Log.e("height----------------",String.valueOf(testSheet.texImage.getHeight()));
    }
}