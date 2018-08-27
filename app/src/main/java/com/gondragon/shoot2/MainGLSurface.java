package com.gondragon.shoot2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MainGLSurface extends GLSurfaceView{

    public MainGLSurface(Context context){

        super(context);
    }

    public void setRenderer(GLSurfaceView.Renderer render){

        super.setRenderer(render);
    }
}
