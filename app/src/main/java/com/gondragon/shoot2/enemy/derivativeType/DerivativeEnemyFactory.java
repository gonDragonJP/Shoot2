package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.CallbackOfMyPlane;
import com.gondragon.shoot2.enemy.CallbackOfGeneratingChild;
import com.gondragon.shoot2.enemy.Enemy;
import com.gondragon.shoot2.enemy.EnemyData;

import java.util.Hashtable;
import java.util.Map;

public class DerivativeEnemyFactory {

    private Map<String, DerivativeEnemyKind> nameToDEKindMap
            = new Hashtable<String, DerivativeEnemyKind>();

    public DerivativeEnemyFactory(int stage){

        initialize(stage);
    }

    private void initialize(int stage){

        nameToDEKindMap.clear();
        setMap(stage); 				// データベース化する部分
    }

    private void setMap(int stage){

        switch(stage){

            case 1: setStage1DE(); break;
            case 2: setStage2DE(); break;
            default:
        }
    }

    public Enemy getDerivativeEnemy(

            String enemyName, EnemyData.EnemyCategory category,
            CallbackOfMyPlane cbOfMyPlanePos,
            CallbackOfGeneratingChild cbOfGeneratingChild
    ){

        DerivativeEnemy derivativeEnemy;

        if (category == EnemyData.EnemyCategory.PARTS)
            // partsだけは派生型として処理してる　パーツ毎に違う処理を考えるなら他と同様の処理を考えるべき

            derivativeEnemy = new DE_Parts();

        else{

            String className = nameToDEKindMap.get(enemyName).getClassName();
            className = "com.gondragon.shoot2.enemy.derivativeType." + className;
            derivativeEnemy = generateEnemyByReflection(className);

        }

        derivativeEnemy.initialize(cbOfMyPlanePos, cbOfGeneratingChild);
        // リフレクションで簡単にデフォルトインストラクタによる生成を行っているため後からイニシャライザで設定しています

        return (Enemy)derivativeEnemy;
    }

    private DerivativeEnemy generateEnemyByReflection(String enemyName){

        DerivativeEnemy derivativeEnemy = null;

        try {
            Class<?> clazz = Class.forName(enemyName);
            derivativeEnemy = (DerivativeEnemy)clazz.newInstance();

        } catch (ReflectiveOperationException e) {

            e.printStackTrace();
        }

        return derivativeEnemy;
    }

    private enum DerivativeEnemyKind {

        BLOCKER_TYPE0		("DE_blocker_Type0"),
        BOMB_GAS			("DE_bomb_gas"),
        LASER_TYPE0			("DE_laser_Type0"),
        MAKE_SCATTER_TYPE0	("DE_make_scatter_Type0"),
        CARRIER_BOSS		("DE_carrier_Boss"),
        HARRICANE			("DE_harricane"),
        MINE_TYPE0			("DE_mine_Type0");

        private String className;

        DerivativeEnemyKind(String className){

            this.className = className;
        }

        public String getClassName(){

            return className;
        }
    };

    private void setStage1DE(){

        nameToDEKindMap.put("blocker0", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("blocker1", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("blocker2", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("bomb_gas", DerivativeEnemyKind.BOMB_GAS);
        nameToDEKindMap.put("laser_head", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("laser_body", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("laser_tail", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("mak_scatter0", DerivativeEnemyKind.MAKE_SCATTER_TYPE0);
        nameToDEKindMap.put("bossCarrier", DerivativeEnemyKind.CARRIER_BOSS);
    }

    private void setStage2DE(){

        nameToDEKindMap.put("blocker0", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("blocker1", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("blocker2", DerivativeEnemyKind.BLOCKER_TYPE0);
        nameToDEKindMap.put("bomb_gas", DerivativeEnemyKind.BOMB_GAS);
        nameToDEKindMap.put("laser_head", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("laser_body", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("laser_tail", DerivativeEnemyKind.LASER_TYPE0);
        nameToDEKindMap.put("mak_scatter0", DerivativeEnemyKind.MAKE_SCATTER_TYPE0);
        nameToDEKindMap.put("bossCarrier", DerivativeEnemyKind.CARRIER_BOSS);

        nameToDEKindMap.put("harricane0", DerivativeEnemyKind.HARRICANE);
        nameToDEKindMap.put("harricane1", DerivativeEnemyKind.HARRICANE);
        nameToDEKindMap.put("mine0", DerivativeEnemyKind.MINE_TYPE0);
    }
}
