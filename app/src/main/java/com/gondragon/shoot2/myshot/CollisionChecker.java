package com.gondragon.shoot2.myshot;

import com.gondragon.shoot2.collision.CollisionDetection;
import com.gondragon.shoot2.collision.CollisionRegion;

public class CollisionChecker implements CollisionDetection.Collisionable {

    private boolean isActive;

    private MyShot myShot;

    private CollisionRegion collisionRegion;

    public CollisionChecker(MyShot myShot){

        this.isActive = false;
        this.myShot = myShot;
        collisionRegion = new CollisionRegion();
    }

    public void setActive(boolean sw){

        if(sw) {
            this.isActive = true;
            CollisionDetection.setCollisonListener(this);
        }
        else{
            this.isActive = false;
        }
    }

    @Override
    public boolean checkCollision(CollisionDetection.Collisionable object){

        return false;
    };

    @Override
    public boolean checkCollisionableYet(){

        return isActive;
    };

    @Override
    public void doCollisionProcess(CollisionDetection.Collisionable object){

        myShot.setExplosion();
    };

    @Override
    public CollisionableObject getObject(){

        return CollisionableObject.Shot;
    }

    @Override
    public CollisionRegion getCollisionRegion() {

        collisionRegion.centerX = myShot.x;
        collisionRegion.centerY = myShot.y;
        collisionRegion.size =myShot.shotRadius;

        return collisionRegion;
    }

    @Override
    public int getAttackPower() {

        return myShot.shotPower;
    }
}

