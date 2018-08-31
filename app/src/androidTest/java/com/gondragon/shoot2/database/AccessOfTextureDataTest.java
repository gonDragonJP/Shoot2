package com.gondragon.shoot2.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.gondragon.shoot2.texture.TextureSheet;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
    }
}