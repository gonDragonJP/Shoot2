package com.gondragon.shoot2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {

    private static Connection connection;
    private static Statement statement;

    static String databasePath ="C:/Users/Takahiro/workspace/MySQLite/test.db";

    public static void initDatabase(String databasePath){

        try {

            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
    }

    public static void closeDatabase(){

        try {

            statement.close();
            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void update(String sql){

        try {
            statement.executeUpdate(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static ResultSet getResultSet(String sql){

        ResultSet result = null;

        try {
            result = statement.executeQuery(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }
}
