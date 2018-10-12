package com.gondragon.shoot2.database;

import android.content.Context;
import android.database.Cursor;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.collision.CollisionRegion;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.enemy.GeneratingChild;
import com.gondragon.shoot2.enemy.MovingNode;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AccessOfEnemyData {

    //  注意！　使用の前にコンテキストのセット(setContext)が必要です

    private AccessOfEnemyData(){}

    private static Context context;
    private static String databaseName = Global.enemyAndEventDatabaseName;
    private static int databaseVersion = Global.enemyAndEventDB_Version;
    private static String stgTableSuffix;

    private static Logger logger = Logger.getLogger("AccessOfEnemyData");

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setEnemyList(ArrayList<EnemyData> enemyList, int stage){

        SQLiteManager.initDatabase(context, databaseName, databaseVersion);
        stgTableSuffix = "_Stage_" + String.valueOf(stage);

        ArrayList<Integer> stackList = new ArrayList<>();
        // cursorが単一オブジェクトの為、ネストでクエリ呼び出しすると正常に作動しない
        // ネストを避ける為、結果の一時退避用に使用しています

        Cursor cursor = SQLiteManager.getTheColumnValues
                ("BasicData"+stgTableSuffix, "objectID");
        // sql = "select objectID from BasicData ;";
        cursor.moveToFirst();

        do{
            int objectID = cursor.getInt(0);
            stackList.add(objectID);

        }while(cursor.moveToNext());

        for(int e: stackList){

            enemyList.add(generateEnemyData(e));
        }

        SQLiteManager.closeDatabase();
    }

    private static EnemyData generateEnemyData(int objectID){

        EnemyData enemyData = new EnemyData();

        enemyData.objectID = objectID;
        setEnemyData(enemyData);

        return enemyData;
    }

    private static void setEnemyData(EnemyData enemyData){

        String objectID =String.valueOf(enemyData.objectID);
        Cursor cursor;

        cursor = SQLiteManager.getRowValuesWithSelection
                ("BasicData"+stgTableSuffix, "objectID", objectID);
        //sql = "select * from BasicData where objectID="+objectID+";";
        setBasicData(enemyData, cursor);

        cursor = SQLiteManager.getRowValuesWithSelection
                ("MovingNode"+stgTableSuffix, "parentID", objectID);
        //sql = "select * from MovingNode where parentID="+objectID+";";
        setMovingNode(enemyData, cursor);

        cursor = SQLiteManager.getRowValuesWithSelection
                ("GeneratorNode"+stgTableSuffix, "parentID", objectID);
        //sql = "select * from GeneratorNode where parentID="+objectID+";";
        setGeneratorNode(enemyData, cursor);

        cursor = SQLiteManager.getRowValuesWithSelection
                ("CollisionNode"+stgTableSuffix, "parentID", objectID);
        //sql = "select * from CollisionNode where parentID="+objectID+";";
        setCollisionNode(enemyData, cursor);

        cursor = SQLiteManager.getRowValuesWithSelection
                ("AnimationData"+stgTableSuffix, "parentID", objectID);
        //sql = "select * from AnimationData where parentID="+objectID+";";
        setAnimationData(enemyData, cursor);
    }

    private static void setBasicData(EnemyData enemyData, Cursor cursor){

        if(!cursor.moveToFirst()) return;

            enemyData.name = cursor.getString(cursor.getColumnIndex("name"));
            enemyData.isDerivativeType = cursor.getInt(cursor.getColumnIndex("isDerivativeType")) >0;
            enemyData.textureID = cursor.getInt(cursor.getColumnIndex("textureID"));
            enemyData.hitPoints = cursor.getInt(cursor.getColumnIndex("hitPoint"));
            enemyData.atackPoints = cursor.getInt(cursor.getColumnIndex("atackPoint"));
            enemyData.startPosition.x = cursor.getInt(cursor.getColumnIndex("startPosition_X"));
            enemyData.startPosition.y = cursor.getInt(cursor.getColumnIndex("startPosition_Y"));
            enemyData.startPosAttrib.x = cursor.getInt(cursor.getColumnIndex("startPosAttrib_X"));
            enemyData.startPosAttrib.y = cursor.getInt(cursor.getColumnIndex("startPosAttrib_Y"));
    }

    private static void setMovingNode(EnemyData enemyData, Cursor cursor){

        if(!cursor.moveToFirst()) return;

        do{
            MovingNode node = new MovingNode();
            enemyData.node.add(cursor.getInt(cursor.getColumnIndex("nodeIndex")), node);

            node.startVelocity.x = cursor.getDouble(cursor.getColumnIndex("startVelocity_X"));
            node.startVelocity.y = cursor.getDouble(cursor.getColumnIndex("startVelocity_Y"));
            node.startAcceleration.x = cursor.getDouble(cursor.getColumnIndex("startAcceleration_X"));
            node.startAcceleration.y = cursor.getDouble(cursor.getColumnIndex("startAcceleration_Y"));
            node.homingAcceleration.x = cursor.getDouble(cursor.getColumnIndex("homingAcceleration_X"));
            node.homingAcceleration.y = cursor.getDouble(cursor.getColumnIndex("homingAcceleration_Y"));
            node.nodeDurationFrame = cursor.getInt(cursor.getColumnIndex("nodeDurationFrame"));
            node.startVelAttrib.x = cursor.getInt(cursor.getColumnIndex("startVelAttrib_X"));
            node.startVelAttrib.y = cursor.getInt(cursor.getColumnIndex("startVelAttrib_Y"));
            node.startAccAttrib.x = cursor.getInt(cursor.getColumnIndex("startAccAttrib_X"));
            node.startAccAttrib.y = cursor.getInt(cursor.getColumnIndex("startAccAttrib_Y"));

        }while(cursor.moveToNext());
    }

    private static void setGeneratorNode(EnemyData enemyData, Cursor cursor){

        if(!cursor.moveToFirst()) return;

        do{

            GeneratingChild node = new GeneratingChild();
            enemyData.generator.add(cursor.getInt(cursor.getColumnIndex("nodeIndex")), node);

            node.objectID = cursor.getInt(cursor.getColumnIndex("objectID"));
            node.repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
            node.startFrame = cursor.getInt(cursor.getColumnIndex("startFrame"));
            node.intervalFrame = cursor.getInt(cursor.getColumnIndex("intervalFrame"));
            node.centerX = cursor.getInt(cursor.getColumnIndex("centerX"));
            node.centerY = cursor.getInt(cursor.getColumnIndex("centerY"));

        } while(cursor.moveToNext());
    }

    private static void setCollisionNode(EnemyData enemyData, Cursor cursor){

        if(!cursor.moveToFirst()) return;

        do{

            CollisionRegion node = new CollisionRegion();
            enemyData.collision.add(cursor.getInt(cursor.getColumnIndex("nodeIndex")), node);

            node.centerX = cursor.getInt(cursor.getColumnIndex("centerX"));
            node.centerY = cursor.getInt(cursor.getColumnIndex("centerY"));
            node.size = cursor.getInt(cursor.getColumnIndex("size"));
            int shapeID = cursor.getInt(cursor.getColumnIndex("collisionShape"));
            node.collisionShape = CollisionRegion.CollisionShape.getFromID(shapeID);

        }while(cursor.moveToNext());

    }

    private static void setAnimationData(EnemyData enemyData, Cursor cursor){

        if(!cursor.moveToFirst()) return;

        do{

            AnimationData data = new AnimationData();
            AnimationSet.AnimeKind animeKind = AnimationSet.AnimeKind.getFromID(cursor.getInt(cursor.getColumnIndex("AnimationKind")));
            int keyNode = cursor.getInt(cursor.getColumnIndex("keyNode"));

            switch(animeKind){

                case NORMAL:
                    enemyData.animationSet.normalAnime = data;
                    break;
                case EXPLOSION:
                    enemyData.animationSet.explosionAnime = data;
                    break;
                case NODEACTION:
                    enemyData.animationSet.nodeActionAnime.put(keyNode, data);
                default:
            }

            data.textureID = cursor.getInt(cursor.getColumnIndex("textureID"));
            data.drawSize.x = cursor.getDouble(cursor.getColumnIndex("drawSize_X"));
            data.drawSize.y = cursor.getDouble(cursor.getColumnIndex("drawSize_Y"));
            data.repeatAttribute = AnimationData.RepeatAttribute.getFromID(cursor.getInt(cursor.getColumnIndex("RepeatAttribute")));
            data.frameOffset = cursor.getInt(cursor.getColumnIndex("frameOffset"));
            data.frameNumber= cursor.getInt(cursor.getColumnIndex("frameNumber"));
            data.frameInterval= cursor.getInt(cursor.getColumnIndex("frameInterval"));
            data.rotateAction = AnimationData.RotateAttribute.getFromID(cursor.getInt(cursor.getColumnIndex("RotateAttribute")));
            data.rotateOffset= cursor.getInt(cursor.getColumnIndex("rotateOffset"));
            data.angularVelocity= cursor.getDouble(cursor.getColumnIndex("angularVelocity"));

        }while (cursor.moveToNext());
    }
}
