package com.gondragon.shoot2.animation;

public class AnimationInitializer {

    public static TextureSheet planeTex;
    public static TextureSheet effectTex0, effectTex1, effectTex2, effectTex3;
    public static TextureSheet bulletTex1, bulletTex2;
    public static TextureSheet itemTex;

    private static final int stageLimitedEnemyTexSheetNumber = 9;
    private static final int maxEnemyTexSheetNumber = 30;
    public static TextureSheet[] enemyTex = new TextureSheet[maxEnemyTexSheetNumber];

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
		planeTex = new TextureSheet(R.drawable.myplanesheet, 4, 4);
		effectTex0 = new TextureSheet(R.drawable.effect_sheet000, 8, 8);
		effectTex1 = new TextureSheet(R.drawable.effect_sheet001, 8, 8);
		effectTex2 = new TextureSheet(R.drawable.effect_sheet002, 4, 4);
		effectTex3 = new TextureSheet(R.drawable.effect_sheet003, 8, 8);
		bulletTex1 = new TextureSheet(R.drawable.bullet_sheet000, 8, 8);
		bulletTex2 = new TextureSheet(R.drawable.bullet_sheet001, 8, 8);
		itemTex = new TextureSheet(R.drawable.item_sheet, 8, 8);
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
			enemyTex[0]= new TextureSheet(R.drawable.enemy_sheet001, 8, 8);
			enemyTex[1]= new TextureSheet(R.drawable.enemy_sheet000, 8, 8);
			enemyTex[2]= new TextureSheet(R.drawable.enemy_sheet002, 8, 8);
			enemyTex[3]= new TextureSheet(R.drawable.enemy_sheet003, 8, 8);
			enemyTex[5]= new TextureSheet(R.drawable.midenemy_sheet000, 4, 2);
			enemyTex[8]= new TextureSheet(R.drawable.boss01_sheet, 1, 1);
		*/
                break;

            case 2:
		/*
			enemyTex[0]= new TextureSheet(R.drawable.enemy_sheet001, 8, 8);
			enemyTex[1]= new TextureSheet(R.drawable.enemy_sheet000, 8, 8);
			enemyTex[2]= new TextureSheet(R.drawable.enemy_sheet002, 8, 8);
			enemyTex[3]= new TextureSheet(R.drawable.enemy_sheet003, 8, 8);
			enemyTex[4]= new TextureSheet(R.drawable.enemy_sheet004, 8, 8);
			enemyTex[5]= new TextureSheet(R.drawable.midenemy_sheet000, 4, 2);
			enemyTex[6]= new TextureSheet(R.drawable.midenemy_sheet001, 4, 4);
			enemyTex[8]= new TextureSheet(R.drawable.boss01_sheet, 1, 1);
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
        myPlaneSet.normalAnime.frameOffset = 0;

        myShieldSet.normalAnime.drawSize.set(64, 64);
        //myShieldSet.normalAnime.textureSheet = effectTex0;
        myShieldSet.normalAnime.frameOffset = 48;
        myShieldSet.normalAnime.frameNumber = 12;
        myShieldSet.normalAnime.frameInterval = 3;
        myShieldSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myConversionSet.normalAnime.drawSize.set(64, 64);
        //myConversionSet.normalAnime.textureSheet = effectTex0;
        myConversionSet.normalAnime.frameOffset = 20;
        myConversionSet.normalAnime.frameNumber = 20;
        myConversionSet.normalAnime.frameInterval = 1;
        myConversionSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        myBurnerSet.normalAnime.drawSize.set(32, 64);
        //myBurnerSet.normalAnime.textureSheet = effectTex3;
        myBurnerSet.normalAnime.frameOffset = 0;
        myBurnerSet.normalAnime.frameNumber = 15;
        myBurnerSet.normalAnime.frameInterval = 1;
        myBurnerSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeMyBulletAnime(){

        myBulletSet.normalAnime.drawSize.set(16, 16);
        //myBulletSet.normalAnime.textureSheet = bulletTex1;
        myBulletSet.normalAnime.frameOffset = 16;
        myBulletSet.normalAnime.frameNumber = 4;
        myBulletSet.normalAnime.frameInterval = 1;
        myBulletSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        myBulletSet.explosionAnime.drawSize.set(16, 16);
        //myBulletSet.explosionAnime.textureSheet = bulletTex1;
        myBulletSet.explosionAnime.frameOffset = 8;
        myBulletSet.explosionAnime.frameNumber = 4;
        myBulletSet.explosionAnime.frameInterval = 3;
        myBulletSet.explosionAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myLaserSet.normalAnime.drawSize.set(32, 32);
        //myLaserSet.normalAnime.textureSheet = bulletTex2;
        myLaserSet.normalAnime.frameOffset = 0;
        myLaserSet.normalAnime.frameNumber = 1;
        myLaserSet.normalAnime.frameInterval = 1;
        myLaserSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.ONCE;

        myLaserSet.explosionAnime.drawSize.set(64, 64);
        //myLaserSet.explosionAnime.textureSheet = effectTex0;
        myLaserSet.explosionAnime.frameOffset = 40;
        myLaserSet.explosionAnime.frameNumber = 8;
        myLaserSet.explosionAnime.frameInterval = 2;
        myLaserSet.explosionAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeMyChargingBallAnime(){

        myChargingBallSet.normalAnime.drawSize.set(64, 64);
        //myChargingBallSet.normalAnime.textureSheet = effectTex0;
        myChargingBallSet.normalAnime.frameOffset = 0;
        myChargingBallSet.normalAnime.frameNumber = 10;
        myChargingBallSet.normalAnime.frameInterval = 6;
        myChargingBallSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.STOP;

        myChargedBallSet.normalAnime.drawSize.set(64, 64);
        //myChargedBallSet.normalAnime.textureSheet = effectTex0;
        myChargedBallSet.normalAnime.frameOffset = 10;
        myChargedBallSet.normalAnime.frameNumber = 10;
        myChargedBallSet.normalAnime.frameInterval = 3;
        myChargedBallSet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }

    private static void initializeItemAnime(){

        shieldEnergySet.normalAnime.drawSize.set(32, 32);
        //shieldEnergySet.normalAnime.textureSheet = itemTex;
        shieldEnergySet.normalAnime.frameOffset = 0;
        shieldEnergySet.normalAnime.frameNumber = 15;
        shieldEnergySet.normalAnime.frameInterval = 4;
        shieldEnergySet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;

        weaponEnergySet.normalAnime.drawSize.set(32, 32);
        //weaponEnergySet.normalAnime.textureSheet = itemTex;
        weaponEnergySet.normalAnime.frameOffset = 16;
        weaponEnergySet.normalAnime.frameNumber = 15;
        weaponEnergySet.normalAnime.frameInterval = 4;
        weaponEnergySet.normalAnime.repeatAttribute = AnimationData.RepeatAttribute.LOOP;
    }
}
