package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.enemy.Enemy;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

public class DE_Parts extends DerivativeEnemy{
	
	Double2Vector partsProportion = new Double2Vector();
	Int2Vector tempChildPosition = new Int2Vector();

	@Override
	public void setData(	
			
				EnemyData enemyData,
				Int2Vector requestPos, 
				Enemy parentEnemy
			){
		
		super.setData(enemyData, requestPos, parentEnemy);
		
		partsProportion.x = enemyData.startPosition.x / 100f;
		partsProportion.y = enemyData.startPosition.y / 100f;
		
		setPartsPosition();
		
		fx = x;		fy = y;
	}

	@Override
	protected void movingProcess(){
		
		checkPartsParameter();
	}
		
	private void checkPartsParameter(){
		
		if(parentEnemy.isInScreen == false){
			
			isInScreen = false;
			return;
		}
		
		if(parentEnemy.isInExplosion == true){
			
			setExplosion();
			return;
		}
		
		setPartsPosition();
		
		velocity.copy(parentEnemy.velocity); // �����̈ړ��ł͎g��Ȃ��������I�u�W�F�N�g�ŕK�v�ɂȂ鎖����
	}
	
	private void setPartsPosition(){
		
		Double2Vector drawSize = parentEnemy.getDrawSizeOfNormalAnime();
	
		int relativeX = (int)(drawSize.x * partsProportion.x);
		int relativeY = (int)(drawSize.y * partsProportion.y);
	
		double c = Math.cos(parentEnemy.drawAngle * Global.radian);
		double s = Math.sin(parentEnemy.drawAngle * Global.radian);
	
		x = parentEnemy.x + (int)(relativeX * c - relativeY * s);
		y = parentEnemy.y + (int)(relativeX * s + relativeY * c);
		
	}
}
