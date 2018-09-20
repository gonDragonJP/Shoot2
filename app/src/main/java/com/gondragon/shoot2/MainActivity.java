package com.gondragon.shoot2;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gondragon.shoot2.UI.MenuView;
import com.gondragon.shoot2.effect.ScreenEffect;

public class MainActivity extends Activity {

    private MainGLSurface glSurface;
    private LinearLayout gameLayout;
    private MenuView menuView;

    private MyRenderer renderer;
    private GameThreadModule gameThread;

    enum Sequence{
        GameScreen, Menu
    }
    private Sequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sequence = Sequence.GameScreen;

        initializeScreen();
        initializeGameCompornent();
    }

    private void initializeScreen(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        glSurface = new MainGLSurface(this);
        renderer = new MyRenderer();
        glSurface.setRenderer(renderer);

        menuView = new MenuView(this);

        switch (sequence){

            case Menu:
                setContentView(menuView); break;

            case GameScreen:
                setContentView(getGameLayout(glSurface)); break;
        }
    }

    private void initializeGameCompornent(){

        gameThread = new GameThreadModule(this, renderer);
        ScreenEffect.setRenderer(renderer);
    }

    private final int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    private LinearLayout getGameLayout(MainGLSurface glSurface){

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{

            gameThread.setGameTaskActivity(false);

            switch (sequence){

                case GameScreen:
                    sequence = Sequence.Menu;
                    gameThread.setGameTaskActivity(false);
                    setContentView(menuView);
                    break;

                case Menu:
                    sequence = Sequence.GameScreen;
                    //setContentView(gameLayout);
                    gameThread.setGameTaskActivity(true);
                    setContentView(gameLayout);
                    gameThread.requestReGLTexBind();
            }


        }
        return false;
    }

    private void openMenu(){

        //sequence = Sequence.Menu;
        setContentView(menuView);
        setContentView(gameLayout);
        //glSurface.invalidate();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
