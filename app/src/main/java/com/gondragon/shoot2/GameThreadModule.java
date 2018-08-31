package com.gondragon.shoot2;

import android.content.Context;

import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.stage.StageManager;
import com.gondragon.shoot2.vector.Int2Vector;

import java.util.Timer;
import java.util.TimerTask;

public class GameThreadModule {

    private Timer timer;
    private TimerTask timerTask;
    private boolean isTestMode;

    private StageManager stageManager;

    private GameThreadModule(){}; //デフォルトコンストラクタ無効

    public GameThreadModule(Context context){

        stageManager = new StageManager(context,

                new CallbackOfMyPlane(){

                    @Override
                    public Int2Vector getMyPlanePos() {

                        return new Int2Vector(160,400);
                    }

                    @Override
                    public void setMyPlanePos(Int2Vector requestPos) {

                    }
                }
        );
    }

    public void setGameStage(int stageNumber){

        stageManager.setStage(stageNumber);

        //AccessOfEventData.addEventList(StageData.eventList);
        //AccessOfEnemyData.addEnemyList(StageData.enemyList); データベース製作用
    }

    public void refreshEventList(){

        stageManager.refreshEventList();
    }

    public void refreshEnemyList(){

        stageManager.refreshEnemyList();
    }

    synchronized public void testEnemy(EnemyData enemyData){

        stageManager.resetAllEnemies();
        stageManager.addRootEnemy(enemyData);

        cancelTimer();
        timer = new Timer();
        makeTimerTask();

        isTestMode = true;
        timer.schedule(timerTask, 0, Global.frameIntervalTime);
    }

    public void refreshQueueOfEvent(){
        //slider操作時やtableによるpoint変更に伴い呼び出されイベント位置を再設定します

       // double sliderValue = slider.getValue();

       // stageManager.updateEventIndex(sliderValue);
        stageManager.resetAllEnemies();
    }

    synchronized public void updateSlider(){

        //DrawModule drawModule = cbOfMainApp.getdrawModule();

        //drawModule.drawScreen();
        refreshQueueOfEvent();
    }

    synchronized public void pushResetButton(){

       // slider.setValue(0);
        updateSlider();
    }

    synchronized public void pushStartButton(){

        cancelTimer();
        timer = new Timer();
        makeTimerTask();

        isTestMode = false;
        timer.schedule(timerTask, 0, Global.frameIntervalTime);
    }

    synchronized public void pushStopButton(){

        cancelTimer();
        if(isTestMode){

            stageManager.resetAllEnemies();
        }
    }

    public void cancelTimer(){

        if(timer != null) timer.cancel();
    }

    private void makeTimerTask(){

        //DrawModule drawModule = cbOfMainApp.getdrawModule();

        timerTask = new TimerTask(){

            //double sliderValue = slider.getValue();
            int scrollMax = StageData.stageEndPoint;

            @Override
            synchronized public void run() {

                if(isTestMode){

                   // drawModule.clearScreen();
                }
                else{

                    //sliderValue += Global.scrollSpeedPerFrame;

                    /*if(sliderValue > scrollMax) {

                        sliderValue = scrollMax;
                        this.cancel();
                    }
                    slider.setValue(sliderValue);
                    drawModule.drawScreen();*/
                }

                //stageManager.periodicalProcess(sliderValue, isTestMode);
                //stageManager.drawEnemies(canvas, checkEnableTex.isSelected());

                if(isTestMode){

                    if(stageManager.getEnemyCount()==0) pushStopButton();
                }
            }
        };
    }
}
