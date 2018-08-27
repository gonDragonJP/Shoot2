package com.gondragon.shoot2.stage;

public class EventData {

    public enum EventCategory{

        ENEMYAPPEARANCE(0), BRIEFING(1), BOSSAPPEARANCE(2), PLAYBGM(3);

        int id;

        EventCategory(int id){

            this.id = id;
        }

        public static EventCategory getFromID(int id){

            for(EventCategory category : EventCategory.values()){

                if(category.id == id) return category;
            }

            return EventCategory.ENEMYAPPEARANCE;
        }

        public int getID(){

            return id;
        }
    };

    private int databaseID; //データベース利用時のみアクセスするようにします
    public int scrollPoint;
    public EventCategory eventCategory;
    public int eventObjectID;

    public void initialize(){

        scrollPoint = 0;
        eventCategory = EventCategory.ENEMYAPPEARANCE;
        eventObjectID = -1;
    }

    public void setDatabaseID(int id){

        this.databaseID = id;
    }

    public final int getDatabaseID(){

        return databaseID;
    }

    public void copy(EventData src){

        this.databaseID = src.databaseID;

        this.scrollPoint = src.scrollPoint;
        this.eventCategory = src.eventCategory;
        this.eventObjectID = src.eventObjectID;
    }
}