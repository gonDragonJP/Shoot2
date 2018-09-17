package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.myplane.CallbackOfMyPlane;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.enemy.derivativeType.DerivativeEnemyFactory;
import com.gondragon.shoot2.vector.Int2Vector;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class EnemiesManager {

    private DerivativeEnemyFactory derivativeEnemyFactory;
    private CallbackOfMyPlane cbOfMyPlanePos;

    ArrayList<EnemyData> enemyDataList;
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();

    public EnemiesManager(

            CallbackOfMyPlane cbOfMyPlanePos,
            ArrayList<EnemyData> enemyDataList,
            DerivativeEnemyFactory derivativeEnemyFactory
    ){

        this.cbOfMyPlanePos = cbOfMyPlanePos;

        this.enemyDataList = enemyDataList;
        this.derivativeEnemyFactory = derivativeEnemyFactory;
    }

    public void initialize(){

    }

    public void resetAllEnemies(){

        enemyList.clear();
    }

    public int getEnemyCount(){

        return enemyList.size();
    }

	/*synchronized public void onDrawShadow(GL10 gl){

		int j = list.size();
		for(int i=0; i<j; i++){

			list.get(i).onDrawShadow(gl);
		}
	}*/

    synchronized public void onDrawEnemies(GL10 gl, boolean isEnableTex){

        //System.out.println("List Size : " + String.valueOf(enemyList.size()));

        EnemyDrawer.setGl(gl);
        EnemyDrawer.setEnableTex(isEnableTex);

        for(Enemy e : enemyList){

            EnemyDrawer.onDrawIfGrounder(e);
        }

        for(Enemy e : enemyList){

            EnemyDrawer.onDrawIfAir(e);
        }
    }

    synchronized public void periodicalProcess(){

        enemiesPeriodicalProcess();
        checkPositionLimit();
    }

    private void enemiesPeriodicalProcess()
    {	//ループの途中でリストサイズが変わるためコレクションループは使えません(生成で増えます)

        int size = enemyList.size();

        for(int i=0; i<size; i++){

            enemyList.get(i).periodicalProcess();
        }
    }

    private void checkPositionLimit(){

        int j=enemyList.size()-1;
        for(int i=j; i>=0; i--){

            Enemy enemy = enemyList.get(i);
            if(enemy.isInScreen == false){

                enemy.destroy();
                enemyList.remove(i);
            }
        }
    }

    public void addRootEnemy(int objectID){

        EnemyData srcData = getEnemyDataFromObjectID(objectID);

        try {

            if (srcData == null) throw new Exception();

        } catch (Exception e) {

            System.out.println("Listに該当objectIDが見つかりません");
            return;
        }
        generateEnemy(srcData, null, null);
    }

    public void addRootEnemy(EnemyData srcData){ // Editorにおけるデータテスト用のメソッドです

        generateEnemy(srcData, null, null);
    }

    private Enemy addChildEnemy(Enemy parent){

        GeneratingChild gen = parent.myData.generator.get(parent.genIndex);
        int objectID = gen.objectID;

        EnemyData srcData = getEnemyDataFromObjectID(objectID);

        Int2Vector startPos = new Int2Vector(gen.centerX, gen.centerY);

        return generateEnemy(srcData, startPos, parent);
    }

    private Enemy generateEnemy(

            EnemyData enemyData,
            Int2Vector requestStartPos,
            Enemy parentEnemy
    ){

        Enemy enemy;

        if(enemyData.isDerivativeType){

            EnemyData.EnemyCategory category = enemyData.getCategory();

            enemy = derivativeEnemyFactory.getDerivativeEnemy(enemyData.name, category, cbOfMyPlanePos,
                        new CallbackOfGeneratingChild() {
                            @Override
                            public Enemy getGeneratingChild(Enemy parent) {

                                return addChildEnemy(parent);
                            }
                        });
        }
        else{

            enemy = new Enemy(cbOfMyPlanePos,
                        new CallbackOfGeneratingChild() {
                            @Override
                            public Enemy getGeneratingChild(Enemy parent) {

                                return addChildEnemy(parent);
                            }
                        });
        }

        enemy.setData(enemyData, requestStartPos, parentEnemy);
        enemyList.add(enemy);

        return enemy;
    }

    private EnemyData getEnemyDataFromObjectID(int objectID){

        EnemyData result = null;

        for(EnemyData e : enemyDataList){

            if(e.objectID == objectID){
                result = e;
                break;
            }
        }
        return result;
    }
}
