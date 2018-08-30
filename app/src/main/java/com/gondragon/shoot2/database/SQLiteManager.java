package com.gondragon.shoot2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {

    private static SQLiteDatabase database;

    public static void initDatabase(Context context, String databaseName, int databaseVersion){

        DatabaseHelper helper = new DatabaseHelper(context, databaseName, null, databaseVersion);

        try {

            helper.createDatabaseFromAsset();
            database = helper.getReadableDatabase();

        } catch (IOException ioe) {

            ioe.printStackTrace();
        }
    }

    public static Cursor getColumnValuesFromTable(String table, String column){

        String[] columnArgs = new String[1];
        columnArgs[0] = column;

        return database.query(table, columnArgs, null,null,null,null,null);
    }

    public static Cursor getRowValues(String table, String selection, String arg){

        String[] selectionArgs = new String[1];
        selectionArgs[0] = arg;

        return database.query(table, null, selection+"=?", selectionArgs,null,null,null);
    }

    public static void closeDatabase(){

        database.close();
    }
}
