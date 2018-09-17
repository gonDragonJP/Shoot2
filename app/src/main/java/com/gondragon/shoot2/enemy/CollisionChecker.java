package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.CollisionDetection;
import com.gondragon.shoot2.myshot.MyShot;

public class CollisionChecker implements CollisionDetection.Collisionable {

    private boolean isActive;

    private Enemy enemy;

    public CollisionChecker(Enemy enemy){

        this.enemy = enemy;
        this.isActive = false;
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

    };

    @Override
    public CollisionableObject getObject(){

        return CollisionableObject.Enemy;
    };
}
