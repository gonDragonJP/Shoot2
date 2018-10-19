package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.myplane.CallbackOfMyPlane;
import com.gondragon.shoot2.enemy.EnemyCommunicable;
import com.gondragon.shoot2.enemy.Enemy;

public class DerivativeEnemy extends Enemy {

    public DerivativeEnemy(){

        super(null,null);
    }

    public void initialize(	// リフレクションで簡単にデフォルトインストラクタによる生成を行うために必要となったイニシャライザです

                               CallbackOfMyPlane cbOfMyPlanePos,
                               EnemyCommunicable enemiesManager
    ){

        this.cbOfMyPlane = cbOfMyPlanePos;
        this.enemiesManager = enemiesManager;
    }
}

