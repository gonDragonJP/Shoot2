package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

public class DE_harricane extends DerivativeEnemy{
	
	static final int thresholdDistance = 110;
	
	private static Double2Vector tempVec = new Double2Vector();
	private static Int2Vector requestMyPlanePos = new Int2Vector();
	private static int frameCountOfSE;
	private static int pullPower;
	private static double distanceToPlane;
	
	@Override
	public void periodicalProcess(){
	 super.periodicalProcess();
	 
	 /*if(!isFinishedInitialSound){
		 SoundEffect.play(SoundKind.EXPLOSION1);
		 isFinishedInitialSound = true;
	 }*/
	 
	 if (checkApproachingPlane()){
		 
		 pullPlane();
		 
		 if(frameCountOfSE==0){
			 float volumeRate 
			 	= 0.5f +  0.5f- (float)distanceToPlane/thresholdDistance/2;
			 //SoundEffect.play(SoundKind.HARRICANE, volumeRate);
		 	frameCountOfSE=40;
		 }
	 }
	 
	 if(frameCountOfSE>0) frameCountOfSE--;
	}
	
	private boolean checkApproachingPlane(){
		
		Int2Vector myPlanePos = cbOfMyPlane.getMyPlanePos();
	
		tempVec.set(myPlanePos.x - x, myPlanePos.y - y);	
		distanceToPlane = tempVec.length();
		
		if (distanceToPlane ==0) return false;
		else return (distanceToPlane < thresholdDistance);
	}
	
	private void pullPlane(){
		
		Int2Vector myPlanePos = cbOfMyPlane.getMyPlanePos();
		
		pullPower = (distanceToPlane * 2 < thresholdDistance) ? 3 : 2; 
		tempVec.normalize(pullPower);
		
		requestMyPlanePos.copy(myPlanePos);
		
		requestMyPlanePos.x -= tempVec.x;
		requestMyPlanePos.y -= tempVec.y;
		
		cbOfMyPlane.setMyPlanePos(requestMyPlanePos);
	}

}
