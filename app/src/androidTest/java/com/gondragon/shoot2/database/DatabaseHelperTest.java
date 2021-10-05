package com.gondragon.shoot2.database;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DatabaseHelperTest {

    Context context;
    DatabaseHelper helper;

    @Before
    public void setContext() {

        context = InstrumentationRegistry.getTargetContext();
        helper = new DatabaseHelper(context, "test", null , 1);
    }


    @Test
    public void createDatabaseFromAsset() {

        try {
            helper.createDatabaseFromAsset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}