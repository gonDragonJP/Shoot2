package com.gondragon.shoot2.stage;

import com.gondragon.shoot2.database.AccessOfEnemyData;
import com.gondragon.shoot2.database.AccessOfEventData;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.enemy.derivativeType.DerivativeEnemyFactory;
import com.gondragon.shoot2.texture.TextureInitializer;
import com.gondragon.shoot2.texture.TextureSheet;

import java.util.ArrayList;

public class StageData {

    private static final int stageLength[]
            = {8000,8000,8000,8000,8000};

    private static final boolean isStageShadowOn[]
            = {false,true,false,false,false};

    private static FileAccess fileAccess = new FileAccess();

    public static int stage;

    public static boolean isShadowOn;
    public static int stageEndPoint;

    public static ArrayList<EnemyData> enemyList = new ArrayList<EnemyData>();
    public static ArrayList<EventData> eventList = new ArrayList<EventData>();

    public static DerivativeEnemyFactory derivativeEnemyFactory;

    public static TextureSheet[] textureSheets;

    private StageData(){

    }

    public static void initialize(int stageNumber){

        stage = stageNumber;
        isShadowOn = isStageShadowOn[stageNumber -1];
        stageEndPoint = stageLength [stageNumber -1];

        //AnimationInitializer.setStageEnemyTexSheet(stageNumber);
        //ToDo) dbにテクスチャマッピングテーブルを作りイニシャライザからは切り離して自身でマップすること！ →　(Done

        textureSheets
                = TextureInitializer.getStageEnemyTexSheets(stageNumber);

        //fileAccess.setEventList(eventList, stageNumber);
        //fileAccess.setEnemyList(enemyList, stageNumber); //独自ファイルフォーマット読み込み用

        refreshEventListFromDB();
        refreshEnemyListFromDB();

        derivativeEnemyFactory = new DerivativeEnemyFactory(stageNumber);
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