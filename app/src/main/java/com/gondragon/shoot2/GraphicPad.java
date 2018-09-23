package com.gondragon.shoot2;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

public class GraphicPad {

    private final float padRadius = 50;

    private Point leftPadCenter = new Point();
    private Point rightPadCenter = new Point();

    public boolean isSetLeftPadCenter = false;
    public boolean isSetRightPadCenter = false;
    public Point leftPadDirVector = new Point();
    public Point rightPadDirVector = new Point();

    public int pointerCount;
    private int leftPointerID, rightPointerID;


    public GraphicPad(){

    }

    public double getLeftDirectionalAngle(){

        return Math.atan2(leftPadDirVector.y, leftPadDirVector.x);
    }

    public double getRightDirectionalAngle(){

        return Math.atan2(rightPadDirVector.y, rightPadDirVector.x);
    }

    private static PointF padCenter = new PointF();

    private void setPadCenter(Point realPos){

        float p = (realPos.x - MyRenderer.surfaceRect.left) / (float)MyRenderer.surfaceRect.width();
        float q = (realPos.y - MyRenderer.surfaceRect.top) / (float)MyRenderer.surfaceRect.height();

        padCenter.set(Global.virtualScreenSize.x * p, Global.virtualScreenSize.y * q);
    }

    public  PointF getPadCenter(){

        return padCenter;
    }

    public void onDraw(GL10 gl){

        UtilGL.setColor(gl, Color.WHITE);

        if(isSetLeftPadCenter) {
            setPadCenter(leftPadCenter);
            UtilGL.drawStrokeCircle(gl, padCenter, padRadius,20);
        }

        if(isSetRightPadCenter) {
            setPadCenter(rightPadCenter);
            UtilGL.drawStrokeCircle(gl, padCenter, padRadius, 20);
        }
    }

    public boolean onTouch(MotionEvent event){

        pointerCount = event.getPointerCount();

        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        int actionID = event.getPointerId(actionIndex);

        if(action==MotionEvent.ACTION_DOWN ||action==MotionEvent.ACTION_POINTER_DOWN){

            int touchX = (int)event.getX(actionIndex);
            int touchY = (int)event.getY(actionIndex);

            if(touchX *2 < MyRenderer.surfaceRect.width()){

                setLeftPadCenter(actionID, touchX, touchY);
            }
            else{

                setRightPadCenter(actionID, touchX, touchY);
            }
        }

        if(action==MotionEvent.ACTION_MOVE){

            if(isSetLeftPadCenter) setLeftPadMovement(event);
            if(isSetRightPadCenter) setRightPadMovement(event);
        }

        if(action==MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP){

            if(actionID == leftPointerID) clearLeftPadCenter();
            if(actionID == rightPointerID) clearRightPadCenter();
        }

        return (isSetLeftPadCenter||isSetRightPadCenter);
    }

    private void setLeftPadCenter(int actionID, int touchX, int touchY){

        isSetLeftPadCenter = true;
        leftPointerID = actionID;
        leftPadCenter.x = touchX;
        leftPadCenter.y = touchY;
    }

    private void setRightPadCenter(int actionID, int touchX, int touchY){

        isSetRightPadCenter = true;
        rightPointerID = actionID;
        rightPadCenter.x = touchX;
        rightPadCenter.y = touchY;
    }

    private void setLeftPadMovement(MotionEvent event){

        int index = event.findPointerIndex(leftPointerID);
        int x = (int)event.getX(index);
        int y = (int)event.getY(index);

        leftPadDirVector.x = x - leftPadCenter.x;
        leftPadDirVector.y = y - leftPadCenter.y;
    }

    private void setRightPadMovement(MotionEvent event){

        int index = event.findPointerIndex(rightPointerID);
        int x = (int)event.getX(index);
        int y = (int)event.getY(index);

        rightPadDirVector.x = x - rightPadCenter.x;
        rightPadDirVector.y = y - rightPadCenter.y;
    }

    private void clearLeftPadCenter(){

        isSetLeftPadCenter = false;
        leftPadDirVector.set(0, 0);
    }

    private void clearRightPadCenter(){

        isSetRightPadCenter = false;
        rightPadDirVector.set(0, 0);
    }
}
