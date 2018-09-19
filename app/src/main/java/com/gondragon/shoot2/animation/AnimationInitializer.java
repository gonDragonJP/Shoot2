package com.gondragon.shoot2.animation;

public class AnimationInitializer {

    public static GLTexSheet planeTex;
    public static GLTexSheet effectTex0, effectTex1, effectTex2, effectTex3;
    public static GLTexSheet bulletTex1, bulletTex2;
    public static GLTexSheet itemTex;

    private static final int stageLimitedEnemyTexSheetNumber = 9;
    private static final int maxEnemyTexSheetNumber = 30;
    public static GLTexSheet[] enemyTex = new GLTexSheet[maxEnemyTexSheetNumber];

    public static AnimationSet myPlaneSet = new AnimationSet();
    public static AnimationSet myConversionSet = new AnimationSet();
    public static AnimationSet myShieldSet = new AnimationSet();
    public static AnimationSet myBurnerSet = new AnimationSet();

    public static AnimationSet myBulletSet = new AnimationSet();
    public static AnimationSet myLaserSet = new AnimationSet();

    public static AnimationSet myChargingBallSet = new AnimationSet();
    public static AnimationSet myChargedBallSet = new AnimationSet();

    public static AnimationSet shieldEnergySet = new AnimationSet();
    public static AnimationSet weaponEnergySet = new AnimationSet();

    static {
		/*
		planeTex = new GLTexSheet(R.drawable.myplanesheet, 4, 4);
		effectTex0 = new GLTexSheet(R.drawable.effect_sheet000, 8, 8);
		effectTex1 = new GLTexSheet(R.drawable.effect_sheet001, 8, 8);
		effectTex2 = new GLTexSheet(R.drawable.effect_sheet002, 4, 4);
		effectTex3 = new GLTexSheet(R.drawable.effect_sheet003, 8, 8);
		bulletTex1 = new GLTexSheet(R.drawable.bullet_sheet000, 8, 8);
		bulletTex2 = new GLTexSheet(R.drawable.bullet_sheet001, 8, 8);
		itemTex = new GLTexSheet(R.drawable.item_sheet, 8, 8);
		*/
        initializeMyPlaneAnime();
        initializeMyBulletAnime();
        initializeMyChargingBallAnime();
        initializeItemAnime();

        enemyTex[10] = bulletTex1;
        enemyTex[11] = bulletTex2;
        enemyTex[20] = effectTex1;
        enemyTex[21] = effectTex2;
    }

    public static void setStageEnemyTexSheet(int stage){

        clearStageLimitedEnemyTexSheets();

        switch(stage){

            case 1:
		/*
			enemyTex[0]= new GLTexSheet(R.drawable.enemy_sheet001, 8, 8);
			enemyTex[1]= new GLTexSheet(R.drawable.enemy_sheet000, 8, 8);
			enemyTex[2]= new GLTexSheet(R.drawable.enemy_sheet002, 8, 8);
			enemyTex[3]= new GLTexSheet(R.drawable.enemy_sheet003, 8, 8);
			enemyTex[5]= new GLTexSheet(R.drawable.midenemy_sheet000, 4, 2);
			enemyTex[8]= new GLTexSheet(R.drawable.boss01_sheet, 1, 1);
		*/
                break;

            case 2:
		/*
			enemyTex[0]= new GLTexSheet(R.drawable.enemy_sheet001, 8, 8);
			enemyTex[1]= new GLTexSheet(R.drawable.enemy_sheet000, 8, 8);
			enemyTex[2]= new GLTexSheet(R.drawable.enemy_sheet002, 8, 8);
			enemyTex[3]= new GLTexSheet(R.drawable.enemy_sheet003, 8, 8);
			enemyTex[4]= new GLTexSheet(R.drawable.enemy_sheet004, 8, 8);
			enemyTex[5]= new GLTexSheet(R.drawable.midenemy_sheet000, 4, 2);
			enemyTex[6]= new GLTexSheet(R.drawable.midenemy_sheet001, 4, 4);
			enemyTex[8]= new GLTexSheet(R.drawable.boss01_sheet, 1, 1);
		*/
                break;
        }
    }

    private static void clearStageLimitedEnemyTexSheets(){

        for(int i=0; i<stageLimitedEnemyTexSheetNumber; i++){

            if(enemyTex[i]!=null){

                enemyTex[i].release();
                enemyTex[i]=null;
            }
        }
    }

    private static void initializeMyPlaneAnime(){

        myPlaneSet.normalAnime.drawSize.set(64, 64);
        //myPlaneSet.normalAnime.textureSheet = planeTex;
        myPlaneSet.normalAnime.textureID = 0;
        myPlaneSet.normalAnime.frameOffset = 0;

        myShieldSet.normalAnime.drawSize.set(64, 64);
        //myShieldSet.normalAnime.textureSheet = effectTex0;
        myShieldSet.normalAnime.textureID = 1;
        myShieldSet.normalAnime.frameOffset = 48;
        myShieldSet.normalAnime.frameNumber = 12;
        myShieldSet.normalAnime.frameInterval = 3;
        myShieldSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myConversionSet.normalAnime.drawSize.set(64, 64);
        //myConversionSet.normalAnime.textureSheet = effectTex0;
        myConversionSet.normalAnime.textureID = 1;
        myConversionSet.normalAnime.frameOffset = 20;
        myConversionSet.normalAnime.frameNumber = 20;
        myConversionSet.normalAnime.frameInterval = 1;
        myConversionSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        myBurnerSet.normalAnime.drawSize.set(32, 64);
        //myBurnerSet.normalAnime.textureSheet = effectTex3;
        myBurnerSet.normalAnime.textureID = 4;
        myBurnerSet.normalAnime.frameOffset = 0;
        myBurnerSet.normalAnime.frameNumber = 15;
        myBurnerSet.normalAnime.frameInterval = 1;
        myBurnerSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeMyBulletAnime(){

        myBulletSet.normalAnime.drawSize.set(16, 16);
        //myBulletSet.normalAnime.textureSheet = bulletTex1;
        myBulletSet.normalAnime.textureID = 5;
        myBulletSet.normalAnime.frameOffset = 16;
        myBulletSet.normalAnime.frameNumber = 4;
        myBulletSet.normalAnime.frameInterval = 1;
        myBulletSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        myBulletSet.explosionAnime.drawSize.set(16, 16);
        //myBulletSet.explosionAnime.textureSheet = bulletTex1;
        myBulletSet.explosionAnime.textureID = 5;
        myBulletSet.explosionAnime.frameOffset = 8;
        myBulletSet.explosionAnime.frameNumber = 4;
        myBulletSet.explosionAnime.frameInterval = 3;
        myBulletSet.explosionAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myLaserSet.normalAnime.drawSize.set(32, 32);
        //myLaserSet.normalAnime.textureSheet = bulletTex2;
        myLaserSet.normalAnime.textureID = 6;
        myLaserSet.normalAnime.frameOffset = 0;
        myLaserSet.normalAnime.frameNumber = 1;
        myLaserSet.normalAnime.frameInterval = 1;
        myLaserSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.ONCE;

        myLaserSet.explosionAnime.drawSize.set(48, 48);
        //myLaserSet.explosionAnime.textureSheet = effectTex0;
        myLaserSet.explosionAnime.textureID = 1;
        myLaserSet.explosionAnime.frameOffset = 40;
        myLaserSet.explosionAnime.frameNumber = 8;
        myLaserSet.explosionAnime.frameInterval = 2;
        myLaserSet.explosionAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeMyChargingBallAnime(){

        myChargingBallSet.normalAnime.drawSize.set(64, 64);
        //myChargingBallSet.normalAnime.textureSheet = effectTex0;
        myChargingBallSet.normalAnime.textureID = 1;
        myChargingBallSet.normalAnime.frameOffset = 0;
        myChargingBallSet.normalAnime.frameNumber = 10;
        myChargingBallSet.normalAnime.frameInterval = 6;
        myChargingBallSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myChargedBallSet.normalAnime.drawSize.set(64, 64);
        //myChargedBallSet.normalAnime.textureSheet = effectTex0;
        myChargedBallSet.normalAnime.textureID = 1;
        myChargedBallSet.normalAnime.frameOffset = 10;
        myChargedBallSet.normalAnime.frameNumber = 10;
        myChargedBallSet.normalAnime.frameInterval = 3;
        myChargedBallSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeItemAnime(){

        shieldEnergySet.normalAnime.drawSize.set(32, 32);
        //shieldEnergySet.normalAnime.textureSheet = itemTex;
        shieldEnergySet.normalAnime.textureID = 7;
        shieldEnergySet.normalAnime.frameOffset = 0;
        shieldEnergySet.normalAnime.frameNumber = 15;
        shieldEnergySet.normalAnime.frameInterval = 4;
        shieldEnergySet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        weaponEnergySet.normalAnime.drawSize.set(32, 32);
        //weaponEnergySet.normalAnime.textureSheet = itemTex;
        weaponEnergySet.normalAnime.textureID = 7;
        weaponEnergySet.normalAnime.frameOffset = 16;
        weaponEnergySet.normalAnime.frameNumber = 15;
        weaponEnergySet.normalAnime.frameInterval = 4;
        weaponEnergySet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }
}
