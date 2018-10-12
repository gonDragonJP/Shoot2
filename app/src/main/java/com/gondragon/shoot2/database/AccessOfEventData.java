package com.gondragon.shoot2.database;

import android.content.Context;
import android.database.Cursor;

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
    private static String stgTableSuffix;

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setEventList(ArrayList<EventData> eventList, int stage){

        SQLiteManager.initDatabase(context, databaseName, databaseVersion);
        stgTableSuffix = "_Stage_" + String.valueOf(stage);

        Cursor cursor = SQLiteManager.getRowValuesWithOrder
                ("EventData"+stgTableSuffix,"scrollPoint");
        //sql = "select * from EventData order by scrollPoint;";
        cursor.moveToFirst();

        do{
                eventList.add(generateEventData(cursor));

        }while(cursor.moveToNext());

        SQLiteManager.closeDatabase();
    }

    private static EventData generateEventData(Cursor cursor){

        EventData eventData = new EventData();

        setEventData(eventData, cursor);

        return eventData;
    }

    private static void setEventData(EventData eventData, Cursor cursor){

        eventData.setDatabaseID(cursor.getInt(cursor.getColumnIndex("ID")));

        eventData.scrollPoint = cursor.getInt(cursor.getColumnIndex("scrollPoint"));
        eventData.eventCategory = EventData.EventCategory.getFromID
                (cursor.getInt(cursor.getColumnIndex("EventCategory")));
        eventData.eventObjectID = cursor.getInt(cursor.getColumnIndex("eventObjectID"));
    }
}
