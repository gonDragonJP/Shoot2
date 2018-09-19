package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.collision.CollisionDetection;
import com.gondragon.shoot2.collision.CollisionRegion;

import java.util.logging.Logger;

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
            this.isActive = false; //CollisionDetectorから検出され衝突物リストから削除されます
        }
    }

    @Override
    public boolean checkCollision(CollisionDetection.Collisionable object){

        if(enemy.hitPoints == 0) return false; // 敵弾オブジェクトなどは最初からHP0です

        CollisionRegion colObject = object.getCollisionRegion();

        for(int i=0; i<enemy.collisionRotated.size(); i++){

            CollisionRegion col = enemy.collisionRotated.get(i);

            if(col.checkCollision(colObject)) return true;
        }

        return false;
    };

    @Override
    public boolean checkCollisionableYet(){

        return isActive;
    };

    @Override
    public void doCollisionProcess(CollisionDetection.Collisionable object){

        enemy.getDamaged(object.getAttackPower());

        //Logger.getLogger("enemyCollisonChecker").warning("Collision!");
    };

    @Override
    public CollisionableObject getObject(){

        return CollisionableObject.Enemy;
    }

    @Override
    public CollisionRegion getCollisionRegion() {

        return null;
    }

    @Override
    public int getAttackPower() {

        return enemy.atackPoints;
    }
}
