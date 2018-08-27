package com.gondragon.shoot2.animation;

import com.gondragon.shoot2.enemy.Enemy;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.vector.Double2Vector;

public class AnimationManager {

    public enum AnimeObject {

        MYPLANE(AnimationInitializer.myPlaneSet),
        MYBULLET(AnimationInitializer.myBulletSet),
        MYCHARGINGBALL(AnimationInitializer.myChargingBallSet),
        MYCHARGEDBALL(AnimationInitializer.myChargedBallSet),
        MYCONVERSION(AnimationInitializer.myConversionSet),
        MYLASER(AnimationInitializer.myLaserSet),
        MYSHIELD(AnimationInitializer.myShieldSet),
        MYBURNER(AnimationInitializer.myBurnerSet),
        SHIELDENERGY(AnimationInitializer.shieldEnergySet),
        WEAPONENERGY(AnimationInitializer.weaponEnergySet),
        ENEMY(null);

        AnimationSet animeSet;

        AnimeObject(AnimationSet set){

            animeSet = set;
        }

        public static final AnimationSet getAnimeSet(AnimeObject object){

            return object.animeSet;
        }

        public static final AnimationSet getAnimeSet(AnimeObject object, int objectID){

            if(object == ENEMY){

                return StageData.enemyList.get(objectID).animationSet;
                //enemyのアニメはEnemyData管理なのでここからグローバルにアクセスするのはあまり推奨されません
            }else{

                return null;
            }
        }
    };

    public static double getEnemyRotateAngle
            (AnimationData animeData, Enemy enemy, boolean isInitialAngle){

        double resultAngle = animeData.rotateOffset;

        switch(animeData.rotateAction){

            case DEFAULT:
                break;

            case TENDTOPLANE:
                resultAngle += enemy.getAngleOfTendToPlane();
                break;

            case CLOCKWISE:
                if(isInitialAngle) return resultAngle;
                resultAngle = enemy.drawAngle + animeData.angularVelocity;
                break;

            case TENDAHEAD:

                resultAngle += enemy.getAngleOfTendAhead();
                break;

            case TENDPARENTFACEON:

                Enemy parentEnemy = enemy.getParentEnemy();

                if(parentEnemy != null)
                    resultAngle += parentEnemy.drawAngle;
                break;

            case SETPARENTFACEONANDCW:

                parentEnemy = enemy.getParentEnemy();

                if(isInitialAngle){
                    if(parentEnemy != null)
                        resultAngle += parentEnemy.drawAngle;
                    break;
                }
                resultAngle = enemy.drawAngle + animeData.angularVelocity;
                break;

            default:
                resultAngle = 0;
        }

        return resultAngle;
    }

    public static int checkAnimeLimit(AnimationData drawAnimeData, int totalFrame){

        int frameNumber, frameInterval, animeFrame;
        AnimationData.RepeatAttribute repeatAttrib;

        frameNumber = drawAnimeData.frameNumber;
        frameInterval = drawAnimeData.frameInterval;
        repeatAttrib = drawAnimeData.repeatAttribute;

        if(frameInterval==0) return 0;
        animeFrame = totalFrame / frameInterval;

        if(animeFrame>=frameNumber){

            switch(repeatAttrib){

                case LOOP:
                    break;

                case ONCE:
                    return frameNumber-1;

                case STOP:
                    return -1;
            }
        }

        return animeFrame % frameNumber;
    }

    public void setFrame (AnimationData data, int animationFrame){

        int textureID = data.textureID;
        int drawSheetOffset = data.frameOffset;

        //InitGL.setTextureSTCoords(
        //		drawSheet.getTexPositionRect(animationFrame + drawSheetOffset)
        //		);
    }

    public void drawFrame(AnimationData data, Double2Vector center){

        int textureID = data.textureID;
        Double2Vector drawSize = data.drawSize;

        //InitGL.drawTexture(center, drawSize, drawSheet.textureID);
    }

    public void drawScaledFrame
            (AnimationData data, Double2Vector center, float scaleX, float scaleY){

        int textureID = data.textureID;
        Double2Vector drawSize = data.drawSize;

        Double2Vector tempSize = new Double2Vector();

        tempSize.x = drawSize.x * scaleX;
        tempSize.y = drawSize.y * scaleY;

        //InitGL.drawTexture(center, tempSize, drawSheet.textureID);
    }

    public void drawFlexibleFrame(Double2Vector center, Double2Vector tempSize){

        //InitGL.drawTexture(center, tempSize, drawSheet.textureID);
    }
}
