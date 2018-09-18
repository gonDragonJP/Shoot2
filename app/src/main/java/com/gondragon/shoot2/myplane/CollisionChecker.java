package com.gondragon.shoot2.myplane;

import com.gondragon.shoot2.collision.CollisionDetection;
import com.gondragon.shoot2.collision.CollisionRegion;

public class CollisionChecker implements CollisionDetection.Collisionable {

    private MyPlane myPlane;

    private CollisionRegion collisionRegion;

    public CollisionChecker(MyPlane myPlane){

        this.myPlane = myPlane;
        collisionRegion = new CollisionRegion();

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
    }

    @Override
    public CollisionRegion getCollisionRegion() {

        collisionRegion.centerX = myPlane.x;
        collisionRegion.centerY = myPlane.y;
        collisionRegion.size = 16;

        return collisionRegion;
    }

    @Override
    public int getAttackPower() {

        return 100;
    }
}
