package com.gondragon.shoot2;

import android.content.Context;

import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.stage.StageManager;
import com.gondragon.shoot2.vector.Int2Vector;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

public class GameThreadModule {

    private Timer timer;
    private TimerTask timerTask;
    private boolean isTestMode;

    private StageManager stageManager;
    private MyRenderer renderer;

    private int scrollPoint =0;
    private boolean isEnableTex = true;

    private GameThreadModule(){}; //デフォルトコンストラクタ無効

    public GameThreadModule(Context context, MyRenderer renderer){

        stageManager = new StageManager(context,

                new MyPlane.CallbackOfMyPlane(){

                    @Override
                    public Int2Vector getMyPlanePos() {

                        return new Int2Vector(160,400);
                    }

                    @Override
                    public void setMyPlanePos(Int2Vector requestPos) {

                    }
                }
        );

        this.renderer = renderer;
    }

    public void setStage(int stageNumber){

        stageManager.setStage(stageNumber);

        MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
            @Override
            public void render(GL10 gl) {

                StageData.bindGLTextures(gl);
            }
        };
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

        stageManager.updateEventIndex(scrollPoint);
        stageManager.resetAllEnemies();
    }

    synchronized public void updateSlider(){

        //DrawModule drawModule = cbOfMainApp.getdrawModule();

        //drawModule.drawScreen();
        refreshQueueOfEvent();
    }

    synchronized public void pushResetButton(){

        scrollPoint =0;
        updateSlider();
    }

    synchronized public void startThread(){

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

            int scrollMax = StageData.stageEndPoint;

            @Override
            synchronized public void run() {

                if(isTestMode){

                   // drawModule.clearScreen();
                }
                else{

                    scrollPoint += Global.scrollSpeedPerFrame;

                    if(scrollPoint > scrollMax) {

                        scrollPoint = scrollMax;
                        this.cancel();
                    }

                    //drawModule.drawScreen();

                    /*MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
                        @Override
                        public void render(GL10 gl) {

                            float y0= (float)Math.random()*300;
                            float y1= (float)Math.random()*300;

                            PointF startPoint = new PointF(0,y0);
                            PointF endPoint = new PointF(500,y1);

                            UtilGL.drawLine(gl, startPoint, endPoint);
                        }
                    };
                    renderer.addRenderingTask(renderTask);*/
                }

                stageManager.periodicalProcess(scrollPoint, isTestMode);

                MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
                    @Override
                    public void render(GL10 gl) {

                        stageManager.drawEnemies(gl, isEnableTex);
                    }
                };
                renderer.addRenderingTask(renderTask);

                if(isTestMode){

                    if(stageManager.getEnemyCount()==0) pushStopButton();
                }
            }
        };
    }
}
