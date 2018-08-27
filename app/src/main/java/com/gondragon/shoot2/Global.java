package com.gondragon.shoot2;

import com.gondragon.shoot2.vector.Int2Vector;
import com.gondragon.shoot2.vector.IntRect;

public class Global {

    public static final int frameIntervalTime = 1000 /40;

    public static final float scrollSpeedPerFrame = 1.0f;

    public static final double radian = 3.14159/180;

    public static Int2Vector screenSize = new Int2Vector();
    public static Int2Vector screenCenter = new Int2Vector();

    static final float virtualScreenWideRate = 1.25f;
    static final Int2Vector virtualScreenSize = new Int2Vector(320, 480);

    static public float screenProjectionLeft;

    public static final int shadowDeflectionX = 16;
    public static final int shadowDeflectionY = -16;
    public static final float shadowScaleX = 0.75f;
    public static final float shadowScaleY = 0.75f;

    static public IntRect virtualScreenLimit = new IntRect();

    static {

        int screenSideSpace
                = (int)(virtualScreenSize.x * (virtualScreenWideRate-1) / 2);
        int screenEndSpace
                = (int)(virtualScreenSize.y * (virtualScreenWideRate-1) / 2);

        virtualScreenLimit.left = -screenSideSpace;
        virtualScreenLimit.top  = -screenEndSpace;
        virtualScreenLimit.right  = virtualScreenSize.x + screenSideSpace;
        virtualScreenLimit.bottom = virtualScreenSize.y + screenEndSpace;
    }

    private Global(){

        //screenSize は　本来は実際のデバイスの解像度がシステムから入ります
        //virtualScreenSize　が　プログラム上の仮想座標です
        //virtualScreenWideRate　は　画面外判定を緩やかにする為、上下左右に作る余白を含む範囲の大きさです
    }

    static public void setScreenVal(Int2Vector size){

        screenSize.x = size.x;
        screenSize.y = size.y;

        screenCenter.x = size.x / 2;
        screenCenter.y = size.y / 2;
    }

    public class ShotShape{
        int color, length, width;
        public void set(int color, int length, int width){
            this.color = color;
            this.length = length;
            this.width = width;
        }
    }
}
