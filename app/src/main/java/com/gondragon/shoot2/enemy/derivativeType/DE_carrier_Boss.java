package com.gondragon.shoot2.enemy.derivativeType;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.enemy.Enemy;

public class DE_carrier_Boss extends DerivativeEnemy{
	
	static final int partsNumber = 6;
	Enemy[] parts = new Enemy[partsNumber];
	private int partsIndex;

	
	@Override
	public void setExplosion(){
		super.setExplosion();
		
		//BGMManager.fadeOutVolume(4);
	}
	
	@Override
	protected boolean checkNodeDuration(){

		if(nodeDurationFrame < ++nodeFrameCount){
			
			if(nodeIndex == 999) loopNodeAndGenerating();
			
			if(myData.node.size() == (++nodeIndex)){
				
				setResetPositionNode();
			}
			else{
				
				setMovingNode(nodeIndex);
				setNodeActionAnime(nodeIndex);
			}
		}
		return true;
	}
	
	private void setResetPositionNode(){
		
		int movingBackFrame = 50;
		
		double dstX = Global.virtualScreenSize.x/2;
		double dstY = Global.virtualScreenLimit.top + 150;
		
		double dx = (dstX - x) / movingBackFrame;
		double dy = (dstY - y) / movingBackFrame;
		
		velocity.set(dx, dy);
		
		nodeDurationFrame = movingBackFrame;
		nodeFrameCount = 0;
		nodeIndex = 999;
	}
	
	@Override
	protected void checkGeneratingCount(){
		
		super.checkGeneratingCount();
		
		if((tempGenChild != null) && (partsIndex < partsNumber)){
			
			parts[partsIndex++] = tempGenChild;
		}
	}

	private void loopNodeAndGenerating(){
		
		int skipGeneratingCount = 200;  //node0�I�����genFrame
		int skipGeneratingCount2= 2000; //hard mode�ڍs��genFrame
		
		nodeIndex = 0;
		
		for(int i=0; i<partsNumber; i++){
			
			if(parts[i].isInScreen == true){
				
				switch(parts[i].getObjectID()){
				
				case 4003: case 4004: case 4005: case 4006:
				
					parts[i].cueingGenerating(1 , skipGeneratingCount2);
					break;
					
				default:
					
					parts[i].cueingGenerating(0 , skipGeneratingCount);
					break;
				}
			}
		}
	}
}
