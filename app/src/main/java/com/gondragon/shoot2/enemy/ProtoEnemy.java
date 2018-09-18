package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.vector.Int2Vector;
import com.gondragon.shoot2.vector.IntRect;

public class ProtoEnemy {

    protected EnemyData myData;
    protected Enemy parentEnemy;

    protected IntRect screenLimit;

    public boolean isInScreen, isNowDamaged, isInExplosion, hasShadow, isGrounder;

    public int x, y;

    protected int hitPoints, atackPoints;
    protected boolean neededCheckingWithBullet;

    protected CollisionChecker collisionChecker;

    public ProtoEnemy(){

        initialize();
    }

    private void initialize(){

        screenLimit = new IntRect();
        setScreenLimit();

        collisionChecker = new CollisionChecker((Enemy)this);

        isInScreen = true;
        isNowDamaged = false;
        isInExplosion = false;
    }

    protected  void getDamaged(int damage){

        hitPoints -= damage;

        if(hitPoints <= 0) setExplosion();
        else
            isNowDamaged = true;
    }

    protected void setExplosion(){

        isNowDamaged = false;
        collisionChecker.setActive(false); // 爆発したら衝突判定は無効にする
        isInExplosion = true;
        hasShadow = false;
    }

    protected  void setOutOfScreen(){

        isInScreen = false;
    }

    protected  void destroy(){ // setOutOfScreenされた後にmanagerの方から破壊されます

        if(collisionChecker !=null) collisionChecker.setActive(false);
    }

    private void setScreenLimit(){

        screenLimit.left  = Global.virtualScreenLimit.left;
        screenLimit.right = Global.virtualScreenLimit.right;
        screenLimit.top   = Global.virtualScreenLimit.top;
        screenLimit.bottom= Global.virtualScreenLimit.bottom;
    }

    protected void checkScreenLimit(){

        if(	x<screenLimit.left || y<screenLimit.top ||
                x>screenLimit.right || y>screenLimit.bottom
                ){
            isInScreen = false;
        }
    }

    public void setData(

            EnemyData enemyData,
            Int2Vector requestPos,
            Enemy parentEnemy
    ){

        this.myData = enemyData;
        this.parentEnemy = parentEnemy;

        EnemyData.EnemyCategory category = EnemyData.EnemyCategory.getFromID(myData.objectID/1000);
        this.hasShadow = (category== EnemyData.EnemyCategory.FLYING);
        this.isGrounder = (category== EnemyData.EnemyCategory.GROUND);

        this.hitPoints = myData.hitPoints;
        this.neededCheckingWithBullet = (hitPoints != 0);
    }

    public int getObjectID(){

        return myData.objectID;
    }

    public EnemyData.EnemyCategory getCategory(){

        return myData.getCategory();
    }

    public Enemy getParentEnemy(){

        return parentEnemy;
    }
}