package com.gondragon.shoot2.enemy.derivativeType;

public class DE_blocker_Type0 extends DerivativeEnemy{
	
	private double evaAngle1, evaAngle2;

	@Override
	protected void checkGeneratingCount(){
		
		if(++genFrameCount<genStartFrame) return;

		if(checkSightOnPlane())	requestGenerating();
		else{
			
			--genFrameCount;
			return;
		}

		if (--genRepeat<1) {
			
			if (++genIndex < myData.generator.size()) setGenerating(genIndex);
		}
		else 
			genStartFrame +=genIntervalFrame;	
	
	}
	
	private boolean checkSightOnPlane(){
		
		evaAngle1 = getAngleOfTendToPlane();
		evaAngle2 = drawAngle;
		
		if(Math.abs(evaAngle1 - evaAngle2)<25) return true;
		
		return false;
	}
}
