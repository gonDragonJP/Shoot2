package com.gondragon.shoot2.stage;

import android.content.Context;
import android.util.Log;

import com.gondragon.shoot2.database.AccessOfEnemyData;
import com.gondragon.shoot2.database.AccessOfEventData;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.enemy.derivativeType.DerivativeEnemyFactory;
import com.gondragon.shoot2.texture.TextureInitializer;
import com.gondragon.shoot2.texture.TextureSheet;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class StageData {

    private static Context context;

    private static final int stageLength[]
            = {8000,8000,8000,8000,8000};

    private static final boolean isStageShadowOn[]
            = {false,true,false,false,false};

    public static int stage;

    public static boolean isShadowOn;
    public static int stageEndPoint;

    public static ArrayList<EnemyData> enemyList = new ArrayList<EnemyData>();
    public static ArrayList<EventData> eventList = new ArrayList<EventData>();

    public static DerivativeEnemyFactory derivativeEnemyFactory;

    public static TextureSheet[] enemyTexSheets;
    public static TextureSheet[] enumTexSheets;

    public static boolean isGLTexBinded;

    private StageData(){

    }

    public static void setStage(int stageNumber){

        stage = stageNumber;
        isShadowOn = isStageShadowOn[stageNumber -1];
        stageEndPoint = stageLength [stageNumber -1];

        enemyTexSheets
                = TextureInitializer.getStageEnemyTexSheets(stageNumber);
        enumTexSheets
                = TextureInitializer.getEnumTexSheets();

        refreshEventListFromDB();
        refreshEnemyListFromDB();

        derivativeEnemyFactory = new DerivativeEnemyFactory(stageNumber);

        isGLTexBinded = false; // glインターフェイスへのテクスチャバインドはまだ行われていません
    }

    public static void bindGLTextures(GL10 gl){
        // glインターフェイスが必要なのでDB読み込みの直後、ゲームスレッドから呼ばれます

        if(isGLTexBinded) return;
            //スレッドから何度も呼ばれるとバインドが上手くいかないので既に呼ばれていたら何もしません

        for(TextureSheet sheet : enemyTexSheets){

            if(sheet!=null) {
                sheet.bindGLTexture(gl);
                //Log.e("**********", sheet.pictureName);
                //Log.e("---------", String.valueOf(sheet.GLtexID));
            }
        }

        for(TextureSheet sheet : enumTexSheets){

            if(sheet!=null) {
                sheet.bindGLTexture(gl);
                //Log.e("**********", sheet.pictureName);
                //Log.e("---------", String.valueOf(sheet.GLtexID));
            }
        }

        isGLTexBinded = true;
    }

    public static void refreshEventListFromDB(){

        eventList.clear();
        AccessOfEventData.setEventList(eventList);
    }

    public static void refreshEnemyListFromDB(){

        enemyList.clear();
        AccessOfEnemyData.setEnemyList(enemyList);
    }

    public static boolean isLastStage(){

        return (stage == stageLength.length);
    }

    public static int getIndexOfEnemyList(int objectID){

        int result = -1;

        for(int i=0; i<enemyList.size(); i++){

            EnemyData enemyData = enemyList.get(i);
            if(enemyData.objectID == objectID){
                result = i;
                break;
            }
        }
        return result;
    }

    public static int getLastIDinEnemyList(EnemyData.EnemyCategory category){

        int lastID =-1;

        for(EnemyData e: enemyList){

            if(e.objectID >= (category.getID() +1)*1000) break;
            lastID = e.objectID;
        }
        return lastID;
    }
}