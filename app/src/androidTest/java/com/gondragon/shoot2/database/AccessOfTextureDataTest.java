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
    private static final String databasePath = databaseAssetsDir + "texDB.db";

    @Test
    public void test() {

        setContext();

        SQLiteManager.initDatabase(databasePath);
    }

    private void setContext() {

        context = InstrumentationRegistry.getTargetContext();
    }

    private static Connection connection;
    private static Statement statement;

    public static void initDatabase(String databasePath){

        try {

            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
    }



}