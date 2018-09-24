package com.gondragon.shoot2;

import android.content.Context;

import com.gondragon.shoot2.collision.CollisionDetection;
import com.gondragon.shoot2.database.AccessOfEnemyData;
import com.gondragon.shoot2.database.AccessOfEventData;
import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.effect.ScreenEffect;
import com.gondragon.shoot2.effect.StageEffect;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.stage.Background;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.stage.StageManager;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

public class GameThreadModule {

    private Timer timer;
    private TimerTask timerTask;
    private boolean isTestMode, isGameTaskActive;

    private MyPlane myPlane;
    private StageManager stageManager;
    private MyRenderer renderer;

    private boolean isEnableTex = true;

    private GameThreadModule(){}; //デフォルトコンストラクタ無効

    public GameThreadModule(Context context, MyRenderer renderer){

        this.renderer = renderer;

        //アセットにアクセスするクラスにはcontextが必要なのでセット
        AccessOfEventData.setContext(context);
        AccessOfEnemyData.setContext(context);
        AccessOfTextureData.setContext(context);

        ScreenEffect.setRenderer(renderer);


        myPlane = new MyPlane();

        stageManager = new StageManager(myPlane);
    }

    public void setStage(int stageNumber){

        CollisionDetection.initializeLists(); // staticリストをステージの最初にリセットします。

        stageManager.setStage(stageNumber);

        //テクスチャをGLインターフェイスにバインドします
        MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {

            @Override
            public Timing getTiming() {

                return Timing.ONCREATE;
            }

            @Override
            public void render(GL10 gl) {

                StageData.bindGLTextures(gl);

            }
        };

        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.ONCREATE);
        renderer.addRenderingTask(renderTask);
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

        stageManager.updateEventIndex();
        stageManager.resetAllEnemies();
    }

    synchronized public void updateSlider(){

        //DrawModule drawModule = cbOfMainApp.getdrawModule();

        //drawModule.drawScreen();
        refreshQueueOfEvent();
    }

    synchronized public void pushResetButton(){

        updateSlider();
    }

    synchronized public void startThread(){

        cancelTimer();
        timer = new Timer();
        makeTimerTask();

        isTestMode = false;
        isGameTaskActive = true;
        timer.schedule(timerTask, 0, Global.frameIntervalTime);

        StageEffect.startStageEffect();
    }

    public void flipGameTaskActivity(){

        isGameTaskActive = isGameTaskActive==true ? false : true ;
    }

    public void setGameTaskActivity(boolean sw){

        isGameTaskActive = sw;
    }

    public void requestReGLTexBind(){

        StageData.isGLTexBinded = false;
       // renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.ONCREATE);
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

            int scrollMax = StageData.stageEndPoint;

            @Override
            synchronized public void run() {

                if(!isGameTaskActive) return;

                if(isTestMode){

                   // drawModule.clearScreen();
                }

                stageManager.periodicalProcess(isTestMode);
                myPlane.periodicalProcess(renderer.graphicPad);
                ScreenEffect.periodicalProcess();
                CollisionDetection.doAllDetection();

                renderer.setScreenSlidingX(myPlane.x);

                MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
                    @Override
                    public Timing getTiming() {

                        return Timing.ONDRAW;
                    }

                    @Override
                    public void render(GL10 gl) {

                        stageManager.onDraw(gl, isEnableTex);
                        myPlane.drawer.onDraw(gl);
                        myPlane.shotGenerator.onDraw(gl);
                    }
                };

                renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.ONDRAW);
                renderer.addRenderingTask(renderTask);
                ScreenEffect.renderAllLists();

                if(isTestMode){

                    if(stageManager.getEnemyCount()==0) pushStopButton();
                }
            }
        };
    }
}
