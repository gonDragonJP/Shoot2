package com.gondragon.shoot2.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

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