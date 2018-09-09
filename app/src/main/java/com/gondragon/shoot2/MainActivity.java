package com.gondragon.shoot2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gondragon.shoot2.effect.ScreenEffect;

public class MainActivity extends Activity {

    private MyRenderer renderer;
    private GameThreadModule gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGame();

        gameThread.setStage(1);
        gameThread.startThread();

        //実際にレンダリングが始まるのはこれの後だと思われる
    }

    private void initializeGame(){

        initializeScreen();
        gameThread = new GameThreadModule(this, renderer);

        ScreenEffect.setRenderer(renderer);
    }

    private void initializeScreen(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        MainGLSurface glSurface = new MainGLSurface(this);
        renderer = new MyRenderer();
        glSurface.setRenderer(renderer);
        setLayout(glSurface);
    }

    private final int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
    private final int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    private void setLayout(MainGLSurface mainSurface){

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(getParams(matchParent,matchParent));

        mainLayout.addView(mainSurface);
        setContentView(mainLayout);
    }

    private LinearLayout.LayoutParams getParams(int arg0, int arg1){

        return new LinearLayout.LayoutParams(arg0, arg1);
    }


}
