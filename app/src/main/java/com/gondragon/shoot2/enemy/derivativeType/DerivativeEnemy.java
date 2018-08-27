package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.CallbackOfMyPlane;
import com.gondragon.shoot2.enemy.CallbackOfGeneratingChild;
import com.gondragon.shoot2.enemy.Enemy;

public class DerivativeEnemy extends Enemy {

    public DerivativeEnemy(){

        super(null,null);
    }

    public void initialize(	// リフレクションで簡単にデフォルトインストラクタによる生成を行うために必要となったイニシャライザです

                               CallbackOfMyPlane cbOfMyPlanePos,
                               CallbackOfGeneratingChild cbOfGeneratingChild
    ){

        this.cbOfMyPlane = cbOfMyPlanePos;
        this.cbOfGeneratingChild = cbOfGeneratingChild;
    }
}

