package com.gondragon.shoot2.enemy.derivativeType;

public class DE_make_scatter_Type0 extends DerivativeEnemy{

	@Override
	protected void checkGeneratingCount(){
		super.checkGeneratingCount();
		
		if(tempGenChild!=null){
			
			//tempGenChild.addNodeDuration(0, -20);
			tempGenChild.addNodeDuration(0, -(3 - genRepeat)*15);
			tempGenChild.addNodeDuration(1, genRepeat*5);
			tempGenChild.slideStartFrame(0, -(3 - genRepeat)*15);
			
		}
	}
}
