package com.gondragon.shoot2.vector;

public class IntRect {

    public int left,right,top,bottom;

    public IntRect(){

        initialize();
    }

    public IntRect(int left, int right, int top, int bottom){

        set(left, right, top, bottom);
    }

    public void initialize(){

        left = right = top = bottom =0;
    }

    public void set(int left, int right, int top, int bottom){

        this.left = left;	this.right  = right;
        this.top  = top;	this.bottom = bottom;
    }

    public int width(){

        return right - left;
    }

    public int height(){

        return bottom - top;
    }

    public int centerX(){

        return (left + right)/2;
    }

    public int centerY(){

        return (top + bottom)/2;
    }
}
