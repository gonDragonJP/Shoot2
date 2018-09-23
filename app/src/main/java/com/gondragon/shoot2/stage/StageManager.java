package com.gondragon.shoot2.stage;

import android.content.Context;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.myplane.CallbackOfMyPlane;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.animation.AnimationManager;
import com.gondragon.shoot2.enemy.EnemiesManager;
import com.gondragon.shoot2.enemy.EnemyData;

import javax.microedition.khronos.opengles.GL10;

public class StageManager {

    private class CurrentStageState{

        public boolean isPreparedScroll;
        public boolean isStarted;

        public int scrollPoint;
        public int eventIndex;

        public CurrentStageState(){

            initialize();
        }

        public void initialize(){

            this.isStarted = false;
            this.isPreparedScroll = false;
            this.scrollPoint = 0;
            this.eventIndex = 0;
        }
    }

    private CallbackOfMyPlane cbOfMyPlane;

    private EnemiesManager enemiesManager;

    private CurrentStageState stageState;

    public StageManager(CallbackOfMyPlane cbOfMyPlane){

        this.cbOfMyPlane = cbOfMyPlane;
        this.stageState = new CurrentStageState();
    }

    public void setStage(int stageNumber){

        StageData.setStage(stageNumber);
        stageState.initialize();

        enemiesManager = new EnemiesManager
                (cbOfMyPlane, StageData.enemyList, StageData.derivativeEnemyFactory);
    }

    public void refreshEventList(){

        StageData.refreshEventListFromDB();
    }

    public void refreshEnemyList(){

        StageData.refreshEnemyListFromDB();
    }

    public void resetAllEnemies(){

        enemiesManager.resetAllEnemies();
    }

    public void addRootEnemy(int objectID){

        enemiesManager.addRootEnemy(objectID);
    }

    public void addRootEnemy(EnemyData srcData){ // Editorにおけるデータテスト用のメソッドです

        enemiesManager.addRootEnemy(srcData);
    }

    public int getEnemyCount(){

        return enemiesManager.getEnemyCount();
    }

    public void updateEventIndex(){

        int i =0;

        while(i < StageData.eventList.size()){

            EventData eventData = StageData.eventList.get(i);

            if(eventData.scrollPoint >= stageState.scrollPoint) break;

            i++;
        }

        stageState.eventIndex = i;
    }

    synchronized public void onDraw(GL10 gl, boolean isEnableTex){

        Background.onDraw(gl, stageState.scrollPoint);
        drawEnemies(gl, isEnableTex);
    }

    private void drawEnemies(GL10 gl, boolean isEnableTex){

        enemiesManager.onDrawEnemies(gl, isEnableTex);
    }

    synchronized public void periodicalProcess(){
        //game実行用のプロセスですが仮のものです

        stageState.scrollPoint += Global.scrollSpeedPerFrame;
        checkStageFinish();
        checkEvent();

        enemiesManager.periodicalProcess();
    }

    synchronized public void periodicalProcess(boolean isTestMode){

        if(!isTestMode){

            stageState.scrollPoint += Global.scrollSpeedPerFrame;

            if(stageState.scrollPoint > StageData.stageEndPoint) {

                stageState.scrollPoint = StageData.stageEndPoint;
            }

            checkEvent();
        }

        enemiesManager.periodicalProcess();
    }

    private void checkEvent(){

        while(stageState.eventIndex < StageData.eventList.size()){

            EventData eventData = StageData.eventList.get(stageState.eventIndex);

            if (eventData.scrollPoint <= stageState.scrollPoint){

                switch(eventData.eventCategory){

                    case ENEMYAPPEARANCE:
                        addRootEnemy(eventData.eventObjectID);
                        break;

                    case BRIEFING:
                        //stageEffect.briefing(eventData.eventObjectID);
                        //stageEffect.stageEndPlaneCruising();
                        break;

                    case BOSSAPPEARANCE:
                        addRootEnemy(eventData.eventObjectID);
                        break;

                    case PLAYBGM:
                        //BGMManager.playBGM(eventData.eventObjectID);
                        break;
                }

                stageState.eventIndex++;
            }
            else break;
        };
    }

    private void checkStageFinish(){

        if(stageState.scrollPoint > StageData.stageEndPoint){

            clearStage();
        }
    }

    private void clearStage(){

        if(StageData.isLastStage()){

            setStage(++StageData.stage);

        }else{

            clearGame();
        }
    }

    private void clearGame(){
        //巻き戻し

        setStage(1);
    }

    public void prepareStarting(){
		/*
		if(!place.isPreparedScroll){
			ScrollGraphicManager.setupScroll(currentPlace);
		}

		if(!place.isPreparedStageData){
			setStageData();
		}

		stageEffect.startStageEffect();*/
    }

	/*synchronized public void onDraw(GL10 gl){

		ScrollGraphicManager.onDraw(place);

		tempRect.set(
				0, Global.virtualScreenSize.y,
				200, Global.virtualScreenSize.y - 40);
		InitGL.drawText(
				tempRect,
				"ListSize:"+enemyGenerator.enemyList.size()
				//"Stage:"+currentPlace.stage+" Scroll:"+currentPlace.scrollPoint
		);
		tempRect.set(0, screenY - 50, 200, screenY - 90);
		InitGL.drawText(
				tempRect,
				"ScreenX:"+screenX+" Y:"+screenY
		);
	}*/
}
