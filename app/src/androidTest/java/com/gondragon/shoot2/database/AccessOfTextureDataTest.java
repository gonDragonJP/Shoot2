package com.gondragon.shoot2.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class AccessOfTextureDataTest {

    private static Context context;

    private static final String databaseAssetsDir = "database/";
    private static final String databaseUrl = "file:///android_asset/" + databaseAssetsDir + "texDB.db";

    @Test
    public void test() {

        setContext();

        initDatabase(databaseUrl);
    }

    private void setContext() {

        context = InstrumentationRegistry.getTargetContext();
    }

    private static Connection connection;
    private static Statement statement;

    public static void initDatabase(String databaseUrl){

        try {

            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseUrl);
            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
    }



}