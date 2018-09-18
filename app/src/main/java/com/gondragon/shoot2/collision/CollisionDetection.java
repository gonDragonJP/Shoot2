package com.gondragon.shoot2.collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class CollisionDetection {

    public interface Collisionable{

        enum CollisionableObject{
            MyPlane,
            Enemy,
            Shot
        }

        boolean checkCollision(Collisionable object);
        boolean checkCollisionableYet();
        void doCollisionProcess(Collisionable object);
        CollisionableObject getObject();
        CollisionRegion getCollisionRegion();
        int getAttackPower();
    }

    private static ArrayList<Collisionable> enemyList = new ArrayList<>();
    private static ArrayList<Collisionable> shotList = new ArrayList<>();
    private static Collisionable myPlane;

    public static void setCollisonListener(Collisionable c){

        switch (c.getObject()){

            case MyPlane: myPlane = c; break;

            case Enemy: enemyList.add(c); break;

            case Shot: shotList.add(c); break;
        }
    }

    public static void doAllDetection(){

        //Logger.getLogger("enemyCollList:").warning(String.valueOf(enemyList.size()));

        shotVsEnemy();
        planeVsEnemy();
        //planeVsItem();

        checkCollisonableYet();
    }

    private static void shotVsEnemy() {

        for (Collisionable s : shotList) {

            for (Collisionable e : enemyList) {

                if (e.checkCollision(s)) {

                    e.doCollisionProcess(s);
                    s.doCollisionProcess(e);
                }
            }
        }
    }

    private static void planeVsEnemy() {

        for (Collisionable e : enemyList) {

           if (e.checkCollision(myPlane)) {

               e.doCollisionProcess(myPlane);
               myPlane.doCollisionProcess(e);
           }
        }
    }

    private static void checkCollisonableYet(){

        Iterator<Collisionable> it;

        it = enemyList.iterator();
        while (it.hasNext()){

            if(it.next().checkCollisionableYet()) continue;

            it.remove();
        }

        it = shotList.iterator();
        while (it.hasNext()){

            if(it.next().checkCollisionableYet()) continue;

            it.remove();
        }
    }

        /*for(int i=0; i<shotCount; i++){

            MyShot shot = shotGenerator.list.get(i);
            if(shot.isInExplosion) break;

            int shotRadius = shot.shotRadius;

            for(int j=0; j<enemyCount; j++){

                Enemy enemy = enemyGenerator.list.get(j);

                if(enemy.hitPoints<=0) continue;

                for(int k=0; k<enemy.colNumber; k++){

                    CollisionRegion col = enemy.collisionRotated.get(k);
                    int x = enemy.x + col.
                    centerX;
                    int y = enemy.y + col.centerY;
                    int radius = col.size + shotRadius;

                    vecA.set(shot.x, shot.y);
                    vecB.set(x, y);
                    vecC.copy(vecA);
                    vecC.minus(vecB);

                    if(vecC.length()<radius) shootEnemy(shot, enemy);
                }
            }
        }
    }

    private void shootEnemy(MyShot shot, Enemy enemy){

        shot.setExplosion();

        enemy.hitPoints -= shot.shotPower;
        if(enemy.hitPoints <= 0){

            enemy.setExplosion();
            enemy.dropItem(shot.isLaser);
        }
    }

    private void planeVsEnemy(){

        int enemyCount = enemyGenerator.list.size();
        int planeRadius = plane.collisionRadius;

        for(int j=0; j<enemyCount; j++){

            Enemy enemy = enemyGenerator.list.get(j);
            if(enemy.isInExplosion|enemy.isGrounder) continue;

            for(int k=0; k<enemy.colNumber; k++){

                CollisionRegion col = enemy.collisionRotated.get(k);
                int x = enemy.x + col.centerX;
                int y = enemy.y + col.centerY;
                int radius = col.size + planeRadius;

                vecA.set(plane.x, plane.y);
                vecB.set(x, y);
                vecC.copy(vecA);
                vecC.minus(vecB);

                if(vecC.length()<radius) shootPlane(enemy);
            }
        }
    }

    private void shootPlane(Enemy enemy){

        enemy.setExplosion();

        plane.setDamaged(enemy.atackPoints);
    }

    private void planeVsItem(){

        int itemCount = itemGenerator.list.size();
        int planeRadius = plane.collisionRadius;
        int radius;

        for(int i=0; i<itemCount; i++){

            Item item = itemGenerator.list.get(i);
            radius = planeRadius + item.radius;

            vecA.set(plane.x, plane.y);
            vecB.set(item.x, item.y);
            vecC.copy(vecA);
            vecC.minus(vecB);

            if(vecC.length()<radius) getItem(item);
        }
    }

    private void getItem(Item item){

        item.gotByPlane();

    }*/
}
