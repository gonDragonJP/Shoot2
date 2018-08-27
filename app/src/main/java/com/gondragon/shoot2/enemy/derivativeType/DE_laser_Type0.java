package com.gondragon.shoot2.enemy.derivativeType;


import com.gondragon.shoot2.vector.Double2Vector;

public class DE_laser_Type0 extends DerivativeEnemy{

	Double2Vector relativePos = new Double2Vector();
	Double2Vector parentFaceOnUnitVec = new Double2Vector();
	
	@Override 
	public void setExplosion(){
		
	}

	@Override
	public void periodicalProcess(){
	
		super.periodicalProcess();
		
		if(parentEnemy.isInExplosion == true){
			isInScreen = false;
		}
	}
	
	@Override
	protected void flyAhead(){
	
		relativePos.x += velocity.x;
		relativePos.y += velocity.y;
		
		double distance = relativePos.length();
	
		velocity.plus(acceleration);
		
		parentFaceOnUnitVec = parentEnemy.getUnitVectorOfFaceOn();
	
		x = (int)(parentEnemy.x + parentFaceOnUnitVec.x * distance);
		y = (int)(parentEnemy.y + parentFaceOnUnitVec.y * distance);
	}
}