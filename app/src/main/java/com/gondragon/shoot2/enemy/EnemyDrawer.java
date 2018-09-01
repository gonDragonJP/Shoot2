package com.gondragon.shoot2.enemy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.texture.TextureSheet;
import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.IntRect;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class EnemyDrawer {

    static private final int drawRadius = 5;
    static private final int drawAngleLineLength = 20;

    static private GL10 gl;
    static private boolean isEnableTex = false;

    public static void setGl(GL10 glArg){

        gl = glArg;
    }

    public static void setEnableTex(boolean isEnable){

        isEnableTex = isEnable;
    }

    public static void onDrawIfGrounder(Enemy enemy){

        int color = getEnemyColor(Color.MAGENTA, enemy.getCategory());

        if(enemy.isGrounder)
            if(!isEnableTex || !onDrawWithTex(enemy)) onDraw(enemy,color);
    }

    public static void onDrawIfAir(Enemy enemy){

        int color = getEnemyColor(Color.BLUE, enemy.getCategory());

        if(!enemy.isGrounder)
            if(!isEnableTex || !onDrawWithTex(enemy)) onDraw(enemy,color);
    }

    private static int getEnemyColor(int defaultColor, EnemyData.EnemyCategory category){

        int resultColor = defaultColor;

        if (category == EnemyData.EnemyCategory.PARTS) resultColor = Color.CYAN;

        return resultColor;
    }

    static void onDraw(Enemy enemy, int color){

        UtilGL.setColor(gl, color);
        PointF center = new PointF(enemy.x, enemy.y);

        UtilGL.drawStrokeCircle(gl, center, drawRadius, 10);

        UtilGL.setColor(gl,Color.WHITE);
        drawCollisionRegions(enemy);
        drawFaceOnLine(enemy);
    }

    static void drawCollisionRegions(Enemy enemy){

        ArrayList<CollisionRegion> list = enemy.collisionRotated;

        PointF center = new PointF();

        for(CollisionRegion e: list){

            int colX = enemy.x + e.centerX;
            int colY = enemy.y + e.centerY;
            int colRadius = e.size;

            center.set(colX, colY);

            UtilGL.drawStrokeCircle
                    (gl, center, colRadius, 20);
        }
    }

    static void drawFaceOnLine(Enemy enemy){

        Double2Vector vec = enemy.getUnitVectorOfFaceOn();

        int px = enemy.x + (int)(drawAngleLineLength *vec.x);
        int py = enemy.y + (int)(drawAngleLineLength *vec.y);

        PointF start = new PointF(enemy.x, enemy.y);
        PointF end = new PointF(px, py);

        UtilGL.drawLine(gl, start, end);
    }

    static boolean onDrawWithTex(Enemy enemy){

        AnimationData anime  = enemy.getCurrentAnimeData();
        int textureID = anime.textureID;
        int frameIndex = anime.frameOffset + enemy.animeFrame;
        double drawSizeX = anime.drawSize.x;
        double drawSizeY = anime.drawSize.y;

        TextureSheet sheet = StageData.textureSheets[textureID];
        if(sheet == null) return false;
        Bitmap img = sheet.texImage;

        IntRect texRect = sheet.getTexRect(frameIndex); // テクスチャ座標取得

        setAffine(enemy.x, enemy.y, drawSizeX, drawSizeY, enemy.drawAngle);

       /* gl.drawImage(
                img, texRect.left, texRect.top, sheet.gridSizeX, sheet.gridSizeY,
                0, 0, drawSizeX, drawSizeY
        );

        toIdentityAffine();*/
        return true;
    }

    //private static Affine affine = new Affine();

    static void setAffine(int x, int y, double sizeX, double sizeY, double angle){

        //affineのappendは数学の式のように左から行列を書き並べる感じで記述します
     /*   affine.appendTranslation(x, y);
        affine.appendRotation(angle);
        affine.appendTranslation(-sizeX/2, -sizeY/2);

        gl.setTransform(affine);*/
    }

    static void toIdentityAffine(){

        //affine.setToIdentity();
        //gl.setTransform(affine);
    }
}
