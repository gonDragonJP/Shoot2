package com.gondragon.shoot2.myplane;

import com.gondragon.shoot2.CollisionDetection;

public class CollisionChecker implements CollisionDetection.Collisionable {

    private MyPlane myPlane;

    final int collisionRadius = 16;

    public CollisionChecker(MyPlane myPlane){

        this.myPlane = myPlane;

        CollisionDetection.setCollisonListener(this);
    }

    @Override
    public boolean checkCollision(CollisionDetection.Collisionable object){

        return false;
    };

    @Override
    public boolean checkCollisionableYet(){

        return true;
    };

    @Override
    public void doCollisionProcess(CollisionDetection.Collisionable object){

    };

    @Override
    public CollisionableObject getObject(){

        return CollisionableObject.MyPlane;
    };
}
