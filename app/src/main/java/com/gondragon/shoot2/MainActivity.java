package com.gondragon.shoot2;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gondragon.shoot2.UI.MenuView;
import com.gondragon.shoot2.UI.PauseMenu;

public class MainActivity extends Activity {

    private MainGLSurface glSurface;
    private LinearLayout gameLayout;
    private MenuView menuView;

    private MyRenderer renderer;
    private GameThreadModule gameThread;
    private PauseMenu pauseMenu;

    enum Sequence{
        GameScreen, Menu
    }
    private Sequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sequence = Sequence.GameScreen;

        initializeScreenCompornent();
        initializeGameCompornent();
    }

    private void initializeScreenCompornent(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        glSurface = new MainGLSurface(this);
        renderer = new MyRenderer();
        glSurface.setRenderer(renderer);
        setGameLayout(glSurface);

        pauseMenu = new PauseMenu(renderer, getMenuCallback());
        menuView = new MenuView(this);
    }

    private void initializeGameCompornent(){

        gameThread = new GameThreadModule(this, renderer);
    }

    private final int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    private LinearLayout setGameLayout(MainGLSurface glSurface){

        gameLayout = new LinearLayout(this);
        gameLayout.setLayoutParams(getParams(matchParent,matchParent));

        gameLayout.addView(glSurface);

        return gameLayout;
    }

    private LinearLayout.LayoutParams getParams(int arg0, int arg1){

        return new LinearLayout.LayoutParams(arg0, arg1);
    }

    @Override
    protected void onStart() {

        super.onStart();

        gameThread.setStage(1);
        gameThread.startThread();
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (sequence){

            case Menu:
                setContentView(menuView); break;

            case GameScreen:
                setContentView(gameLayout); break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{

            gameThread.setGameTaskActivity(false);

            switch (sequence){

                case GameScreen:
                    sequence = Sequence.Menu;
                    gameThread.setGameTaskActivity(false);
                    pauseMenu.show();
                    break;

                case Menu:
                    sequence = Sequence.GameScreen;
                    gameThread.setGameTaskActivity(true);
                    pauseMenu.hide();

                    // contentviewを切り替えた後はゲーム画面に戻すのに以下が必要
                    //　たぶんGLインターフェイスが更新される為？　テクスチャの再登録が必要
                    //setContentView(gameLayout);
                    //gameThread.requestReGLTexBind();
            }


        }
        return false;
    }

    public interface  MenuCallBack{

        void resumeGameAtMenu();
        void exitGameAtMenu();
    }

    private MenuCallBack getMenuCallback(){

        return new MenuCallBack() {
            @Override
            public void resumeGameAtMenu() {  resumeGame(); }
            public void exitGameAtMenu() { exitGame();}
        };
    }

    private void resumeGame(){

        sequence = Sequence.GameScreen;
        gameThread.setGameTaskActivity(true);
        pauseMenu.hide();
    }

    private  void exitGame(){

        pauseMenu.hide();
        this.finishAndRemoveTask();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameThread.cancelTimer();
    }
}
