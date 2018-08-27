package com.gondragon.shoot2.vector;

public class DoubleRect {

    public double left,right,top,bottom;

    public DoubleRect(){

        initialize();
    }

    public DoubleRect(double left, double right, double top, double bottom){

        set(left, right, top, bottom);
    }

    public void initialize(){

        left = right = top = bottom =0;
    }

    public void set(double left, double right, double top, double bottom){

        this.left = left;	this.right  = right;
        this.top  = top;	this.bottom = bottom;

    }

    public double width(){

        return right - left;
    }

    public double height(){

        return bottom - top;
    }

    public double centerX(){

        return (left + right)/2;
    }

    public double centerY(){

        return (top + bottom)/2;
    }
}
