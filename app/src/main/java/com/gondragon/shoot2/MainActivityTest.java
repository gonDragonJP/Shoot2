package com.gondragon.shoot2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gondragon.shoot2.stage.StageData;

public class MainActivityTest extends AppCompatActivity {

    private class testView extends View {

        public testView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas){

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawCircle(200,200,200, paint);

            Bitmap bitmap = StageData.enemyTexSheets[0].texImage;
            canvas.drawBitmap(bitmap,0,0,null);
        }
    }

    private MyRenderer renderer;
    private GameThreadModule gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initializeGame();

        gameThread.setStage(1);
        gameThread.startThread();

        setContentView(new testView(this));
    }

    private void initializeGame(){

        initializeScreen();
        gameThread = new GameThreadModule(this, renderer);
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
