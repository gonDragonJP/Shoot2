package com.gondragon.shoot2.database;

import android.content.Context;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.stage.EventData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccessOfEventData {

    //  注意！　使用の前にコンテキストのセット(setContext)が必要です

    private AccessOfEventData(){}

    private static Context context;
    private static String databaseName = Global.enemyAndEventDatabaseName;
    private static int databaseVersion = Global.enemyAndEventDB_Version;

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setEventList(ArrayList<EventData> eventList){

        SQLiteManager.initDatabase(context, databaseName, databaseVersion);

        String sql;
        ResultSet resultSet;

        sql = "select * from EventData order by scrollPoint;";
        resultSet = SQLiteManager.getResultSet(sql);

        try {
            while(resultSet.next()){

                eventList.add(generateEventData(resultSet));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        SQLiteManager.closeDatabase();
    }

    private static EventData generateEventData(ResultSet resultSet){

        EventData eventData = new EventData();

        setEventData(eventData, resultSet);

        return eventData;
    }

    private static void setEventData(EventData eventData, ResultSet resultSet){

        try {
            eventData.setDatabaseID(resultSet.getInt("ID"));

            eventData.scrollPoint = resultSet.getInt("scrollPoint");
            eventData.eventCategory = EventData.EventCategory.getFromID
                    (resultSet.getInt("EventCategory"));
            eventData.eventObjectID = resultSet.getInt("eventObjectID");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
