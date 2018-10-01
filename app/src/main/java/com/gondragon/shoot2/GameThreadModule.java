package com.gondragon.shoot2;

import android.content.Context;

import com.gondragon.shoot2.animation.AnimationInitializer;
import com.gondragon.shoot2.collision.CollisionDetection;
import com.gondragon.shoot2.database.AccessOfEnemyData;
import com.gondragon.shoot2.database.AccessOfEventData;
import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.effect.ScreenEffect;
import com.gondragon.shoot2.effect.StageEffect;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.indicator.MyIndicator;
import com.gondragon.shoot2.myplane.MyPlane;
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
    private MyIndicator indicator;
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

        AnimationInitializer.initialize();

        myPlane = new MyPlane();

        stageManager = new StageManager(myPlane);

        indicator = new MyIndicator(myPlane);
    }

    public void initStageStarting(int stageNumber){

        CollisionDetection.initializeLists(); // staticリストをステージの最初にリセットします。

        stageManager.setStage(stageNumber);

        setRendereringTasks();
    }

    private void setRendereringTasks(){

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

        renderTask = new MyRenderer.Renderable() {
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

        renderTask = new MyRenderer.Renderable() {
            @Override
            public Timing getTiming() {

                return Timing.AFTERDRAW;
            }

            @Override
            public void render(GL10 gl) {

                indicator.onDraw(gl);
            }
        };

        renderer.deleteRenderingTask(MyRenderer.Renderable.Timing.AFTERDRAW);
        renderer.addRenderingTask(renderTask);
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

    synchronized public void startThread(){

        cancelTimer();
        timer = new Timer();
        makeTimerTask();

        isTestMode = false;
        isGameTaskActive = true;
        timer.schedule(timerTask, 0, Global.frameIntervalTime);

        StageEffect.startStageEffect();
    }

    public void setGameTaskActivity(boolean sw){

        isGameTaskActive = sw;
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



        timerTask = new TimerTask(){

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

                if(isTestMode){

                    if(stageManager.getEnemyCount()==0) pushStopButton();
                }
            }
        };
    }
}
