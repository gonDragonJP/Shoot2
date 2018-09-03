package com.gondragon.shoot2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MainGLSurface extends GLSurfaceView{

    GraphicPad graphicPad = new GraphicPad();
    MyRenderer myRenderer;

    public MainGLSurface(Context context){

        super(context);
    }

    public void setRenderer(GLSurfaceView.Renderer renderer){

        super.setRenderer(renderer);

        myRenderer = (MyRenderer)renderer;
        myRenderer.setGraphicPad(graphicPad);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

       myRenderer.isDrawableGraphicPad = graphicPad.onTouch(event);

       return  true;
    }
}
