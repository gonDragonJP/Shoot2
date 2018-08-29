package com.gondragon.shoot2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gondragon.shoot2.database.AccessOfTextureData;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        MainGLSurface glSurface = new MainGLSurface(this);

        glSurface.setRenderer(new MyRenderer());
        setLayout(glSurface);

        //リソース使用クラスにはコンテキストのセットが必要です
        AccessOfTextureData.setContext(this);
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
