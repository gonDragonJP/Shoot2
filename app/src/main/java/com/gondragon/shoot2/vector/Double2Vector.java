package com.gondragon.shoot2.vector;

public class Double2Vector{

    public double x,y;

    public Double2Vector(){

    }

    public Double2Vector(double x, double y){

        this.x = x;	this.y = y;
    }

    public void set(double x, double y){

        this.x = x; this.y = y;
    }

    public void copy(Double2Vector src){

        this.x = src.x;	this.y = src.y;
    }

    public double length(){

        double x = (double)this.x;
        double y = (double)this.y;

        return Math.sqrt(x * x + y * y);
    }

    public double getUnitX(){

        return (double)x/length();
    }

    public double getUnitY(){

        return (double)y/length();
    }

    public void normalize(double std){

        double stdOverLength = std / length();

        x = x * stdOverLength;
        y = y * stdOverLength;
    }

    public void limit(double limitLength){

        double length = length();

        if(length > limitLength) {

            double a = limitLength / length;
            x *= a;
            y *= a;
        }
    }

    public void plus(Double2Vector src){

        x += src.x;
        y += src.y;
    }

    public void plus(double xa, double ya){

        x += xa;
        y += ya;
    }

    public void minus(Double2Vector src){

        x -= src.x;
        y -= src.y;
    }

    public void minus(double xa, double ya){

        x -= xa;
        y -= ya;
    }


    public void product(double k){

        x *= k;
        y *= k;
    }
}
