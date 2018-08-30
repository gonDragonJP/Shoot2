package com.gondragon.shoot2.database;

import android.content.Context;
import android.database.Cursor;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.enemy.CollisionRegion;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.enemy.GeneratingChild;
import com.gondragon.shoot2.enemy.MovingNode;
import com.gondragon.shoot2.stage.StageData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public class AccessOfEnemyData {

    //  注意！　使用の前にコンテキストのセット(setContext)が必要です

    private AccessOfEnemyData(){}

    private static Context context;
    private static String databaseName = Global.enemyAndEventDatabaseName;
    private static int databaseVersion = Global.enemyAndEventDB_Version;

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setEnemyList(ArrayList<EnemyData> enemyList){

        SQLiteManager.initDatabase(context, databaseName, databaseVersion);

        String sql;
        ResultSet resultSet;
        ArrayList<Integer> stackList = new ArrayList<>();
        // ResultSetが単一オブジェクトの為、ネストでクエリ呼び出しすると正常に作動しない
        // ネストを避ける為、結果の一時退避用に使用しています

       // sql = "select objectID from BasicData ;";

        Cursor cursor = SQLiteManager.getColumnValuesFromTable("BasicData", "objectID");
        cursor.moveToFirst();

        do{
            int objectID = cursor.getInt(1);
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
        Cursor cursor = SQLiteManager.getRowValues("BasicData", "objectID", objectID);

        //sql = "select * from BasicData where objectID="+objectID+";";
        //resultSet = SQLiteManager.getResultSet(sql);

        setBasicData(enemyData, cursor);

        //sql = "select * from MovingNode where parentID="+objectID+";";
        //resultSet = SQLiteManager.getResultSet(sql);

        //setMovingNode(enemyData, resultSet);

        //sql = "select * from GeneratorNode where parentID="+objectID+";";
        //resultSet = SQLiteManager.getResultSet(sql);

        //setGeneratorNode(enemyData, resultSet);

        //sql = "select * from CollisionNode where parentID="+objectID+";";
        //resultSet = SQLiteManager.getResultSet(sql);

        //setCollisionNode(enemyData, resultSet);

        //sql = "select * from AnimationData where parentID="+objectID+";";
        //resultSet = SQLiteManager.getResultSet(sql);

        //setAnimationData(enemyData, resultSet);
    }

    private static void setBasicData(EnemyData enemyData, Cursor cursor){

        cursor.moveToFirst();

        do{

        }while(cursor.moveToNext());

        /*
            enemyData.name = cursor.getString("name");
            enemyData.isDerivativeType = resultSet.getBoolean("isDerivativeType");
            enemyData.textureID = resultSet.getInt("textureID");
            enemyData.hitPoints = resultSet.getInt("hitPoint");
            enemyData.atackPoints = resultSet.getInt("atackPoint");
            enemyData.startPosition.x = resultSet.getInt("startPosition_X");
            enemyData.startPosition.y = resultSet.getInt("startPosition_Y");
            enemyData.startPosAttrib.x = resultSet.getInt("startPosAttrib_X");
            enemyData.startPosAttrib.y = resultSet.getInt("startPosAttrib_Y");

        */
    }

    private static void setMovingNode(EnemyData enemyData, ResultSet resultSet){

        try {

            while(resultSet.next()){

                MovingNode node = new MovingNode();
                enemyData.node.add(resultSet.getInt("nodeIndex"), node);

                node.startVelocity.x = resultSet.getDouble("startVelocity_X");
                node.startVelocity.y = resultSet.getDouble("startVelocity_Y");
                node.startAcceleration.x = resultSet.getDouble("startAcceleration_X");
                node.startAcceleration.y = resultSet.getDouble("startAcceleration_Y");
                node.homingAcceleration.x = resultSet.getDouble("homingAcceleration_X");
                node.homingAcceleration.y = resultSet.getDouble("homingAcceleration_Y");
                node.nodeDurationFrame = resultSet.getInt("nodeDurationFrame");
                node.startVelAttrib.x = resultSet.getInt("startVelAttrib_X");
                node.startVelAttrib.y = resultSet.getInt("startVelAttrib_Y");
                node.startAccAttrib.x = resultSet.getInt("startAccAttrib_X");
                node.startAccAttrib.y = resultSet.getInt("startAccAttrib_Y");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void setGeneratorNode(EnemyData enemyData, ResultSet resultSet){

        try {

            while(resultSet.next()){

                GeneratingChild node = new GeneratingChild();
                enemyData.generator.add(resultSet.getInt("nodeIndex"), node);

                node.objectID = resultSet.getInt("objectID");
                node.repeat = resultSet.getInt("repeat");
                node.startFrame = resultSet.getInt("startFrame");
                node.intervalFrame = resultSet.getInt("intervalFrame");
                node.centerX = resultSet.getInt("centerX");
                node.centerY = resultSet.getInt("centerY");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void setCollisionNode(EnemyData enemyData, ResultSet resultSet){

        try {

            while(resultSet.next()){

                CollisionRegion node = new CollisionRegion();
                enemyData.collision.add(resultSet.getInt("nodeIndex"), node);

                node.centerX = resultSet.getInt("centerX");
                node.centerY = resultSet.getInt("centerY");
                node.size = resultSet.getInt("size");
                int shapeID = resultSet.getInt("collisionShape");
                node.collisionShape = CollisionRegion.CollisionShape.getFromID(shapeID);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void setAnimationData(EnemyData enemyData, ResultSet resultSet){

        try {

            while(resultSet.next()){

                AnimationData data = new AnimationData();
                AnimationSet.AnimeKind animeKind = AnimationSet.AnimeKind.getFromID(resultSet.getInt("AnimationKind"));
                int keyNode = resultSet.getInt("keyNode");

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

                data.textureID = resultSet.getInt("textureID");
                data.drawSize.x = resultSet.getDouble("drawSize_X");
                data.drawSize.y = resultSet.getDouble("drawSize_Y");
                data.repeatAttribute = AnimationData.RepeatAttribute.getFromID(resultSet.getInt("RepeatAttribute"));
                data.frameOffset = resultSet.getInt("frameOffset");
                data.frameNumber= resultSet.getInt("frameNumber");
                data.frameInterval= resultSet.getInt("frameInterval");
                data.rotateAction = AnimationData.RotateAttribute.getFromID(resultSet.getInt("RotateAttribute"));
                data.rotateOffset= resultSet.getInt("rotateOffset");
                data.angularVelocity= resultSet.getDouble("angularVelocity");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
