package com.gondragon.shoot2.enemy.derivativeType;

public class DE_bomb_gas extends DerivativeEnemy{

	boolean isFinishedInitialSound = false;

	@Override
	public void setExplosion(){
		
	}
	
	@Override
	public void periodicalProcess(){
	 super.periodicalProcess();
	 
	 if(!isFinishedInitialSound){
		// SoundEffect.play(SoundKind.EXPLOSION1);
		 isFinishedInitialSound = true;
	 }
	}

}
