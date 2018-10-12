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

    private enum DataByStage {

        Stage_01(1,1,8000,false),
        Stage_02(2,3,8000,true),
        Stage_03(3,1,8000,false),
        Stage_04(4,1,8000,false),
        Stage_05(5,1,8000,false);

        public int stageNo, bgPicNumber, stageLength;
        public boolean isStageShadowOn;

        DataByStage(int stageNo, int bgPicNumber, int stageLength, boolean isStageShadowOn){
            this.stageNo = stageNo;
            this.bgPicNumber = bgPicNumber;
            this.stageLength = stageLength;
            this.isStageShadowOn = isStageShadowOn;
        };

        public static DataByStage getData(int stage){

            for(DataByStage e: DataByStage.values()){

                if(e.stageNo == stage) return e;
            }
            return null;
        }
    }

    private static Context context;

    public static int stage;

    public static boolean isShadowOn;
    public static int stageEndPoint;

    public static ArrayList<EnemyData> enemyList = new ArrayList<EnemyData>();
    public static ArrayList<EventData> eventList = new ArrayList<EventData>();

    public static DerivativeEnemyFactory derivativeEnemyFactory;

    public static TextureSheet[] enemyTexSheets;
    public static TextureSheet[] enumTexSheets;
    public static TextureSheet[] backgroundSheets;

    public static boolean isGLTexBinded;

    private StageData(){

    }

    public static void setStage(int stage){

        StageData.stage = stage;

        DataByStage data = DataByStage.getData(stage);
        isShadowOn = data.isStageShadowOn;
        stageEndPoint = data.stageLength;

        Background.initialize(data.bgPicNumber);

        enemyTexSheets
                = TextureInitializer.getStageEnemyTexSheets(stage);
        enumTexSheets
                = TextureInitializer.getEnumTexSheets();
        backgroundSheets
                //= Background.getBackgroundSheets(stage);
                = TextureInitializer.getBackgroundTexSheets(stage);

        refreshEventListFromDB();
        refreshEnemyListFromDB();

        derivativeEnemyFactory = new DerivativeEnemyFactory(stage);

        isGLTexBinded = false; // glインターフェイスへのテクスチャバインドはまだ行われていません
    }

    public static void bindGLTextures(GL10 gl){
        // glインターフェイスが必要なのでDB読み込みの直後、ゲームスレッドから呼ばれます

        if(isGLTexBinded) return;
            //スレッドから何度も呼ばれるとバインドが上手くいかないので既に呼ばれていたら何もしません

        for(TextureSheet sheet : enemyTexSheets){

            if(sheet!=null) {
                sheet.bindGLTexture(gl);
            }
        }

        for(TextureSheet sheet : enumTexSheets){

            if(sheet!=null) {
                sheet.bindGLTexture(gl);
            }
        }

        for(TextureSheet sheet : backgroundSheets){

            if(sheet!=null) {
                sheet.bindGLTexture(gl);
            }
        }

        isGLTexBinded = true;
    }

    public static void refreshEventListFromDB(){

        eventList.clear();
        AccessOfEventData.setEventList(eventList, stage);
    }

    public static void refreshEnemyListFromDB(){

        enemyList.clear();
        AccessOfEnemyData.setEnemyList(enemyList, stage);
    }

    public static boolean isLastStage(){

        return (stage == DataByStage.values().length);
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