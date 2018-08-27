package com.gondragon.shoot2.enemy.derivativeType;

public class DE_mine_Type0 extends DerivativeEnemy{

	@Override
	public void setExplosion(){
		
		super.setExplosion();
		
		cueingGenerating(0 , 999);
	}
	
	@Override
	public void periodicalProcess(){
	 super.periodicalProcess();
	 
	 	if((isInExplosion) && (myData.generator.size() > genIndex)){
	 		
	 		checkGeneratingCount();
	 	}
	}
}
