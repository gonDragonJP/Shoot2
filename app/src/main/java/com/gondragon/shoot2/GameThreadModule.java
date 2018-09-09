package com.gondragon.shoot2;

import android.content.Context;

import com.gondragon.shoot2.database.AccessOfEnemyData;
import com.gondragon.shoot2.database.AccessOfEventData;
import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.myplane.CallbackOfMyPlane;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.myshot.ShotGenerator;
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

    private MyPlane myPlane;
    private StageManager stageManager;
    private MyRenderer renderer;

    private int scrollPoint =0;
    private boolean isEnableTex = true;

    private GameThreadModule(){}; //デフォルトコンストラクタ無効

    public GameThreadModule(Context context, MyRenderer renderer){

        //アセットにアクセスするクラスにはcontextが必要なのでセット
        AccessOfEventData.setContext(context);
        AccessOfEnemyData.setContext(context);
        AccessOfTextureData.setContext(context);

        myPlane = new MyPlane();

        stageManager = new StageManager(context, myPlane);

        this.renderer = renderer;
    }

    public void setStage(int stageNumber){

        stageManager.setStage(stageNumber);

        //テクスチャをGLインターフェイスにバインドします

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
        timer.schedule(timerTask, 500, Global.frameIntervalTime);
        // ステージのセット時にレンダリングフレームからテクスチャのバインドを行う為、
        // レンダリングスレッドを少し待つ必要のでdelayを置いています
        // ※ここで作ったゲームスレッドはステージセット後すぐに呼び出されると
        //　 ステージセット時のバインド操作を実行前に消去してしまいます！
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
                }

                stageManager.periodicalProcess(scrollPoint, isTestMode);
                myPlane.periodicalProcess(renderer.graphicPad);

                renderer.setScreenSlidingX(myPlane.x);

                MyRenderer.Renderable renderTask = new MyRenderer.Renderable() {
                    @Override
                    public void render(GL10 gl) {

                        stageManager.drawEnemies(gl, isEnableTex);
                        myPlane.drawer.onDraw(gl);
                        myPlane.shotGenerator.onDraw(gl);
                    }
                };

                renderer.resetRenderingTask();
                renderer.addRenderingTask(renderTask);

                if(isTestMode){

                    if(stageManager.getEnemyCount()==0) pushStopButton();
                }
            }
        };
    }
}
