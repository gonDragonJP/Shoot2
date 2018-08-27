package com.gondragon.shoot2.vector;

public class Int2Vector {

    public int x,y;

    public Int2Vector(){

        set(0,0);
    }

    public Int2Vector(int x, int y){

        set(x, y);
    }

    public void set(int x, int y){

        this.x = x; 	this.y = y;
    }

    public void copy(Int2Vector src){

        this.x = src.x;	this.y = src.y;
    }
}
