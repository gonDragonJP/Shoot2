package com.gondragon.shoot2.enemy;

import android.graphics.Color;
import android.graphics.PointF;

import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.collision.CollisionRegion;
import com.gondragon.shoot2.stage.StageData;
import com.gondragon.shoot2.texture.TextureSheet;
import com.gondragon.shoot2.vector.Double2Vector;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class EnemyDrawer {

    static private final int drawRadius = 5;
    static private final int drawAngleLineLength = 20;

    static private GL10 gl;
    static private boolean isEnableTex;

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

            center.set(e.centerX, e.centerY);

            UtilGL.drawStrokeCircle
                    (gl, center, e.size, 20);
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

    private static PointF drawCenter = new PointF();
    private static PointF drawSize = new PointF();

    static boolean onDrawWithTex(Enemy enemy){

        AnimationData anime  = enemy.getCurrentAnimeData();
        int textureID = anime.textureID;
        int frameIndex = anime.frameOffset + enemy.animeFrame;
        drawSize.set((float)anime.drawSize.x, (float)anime.drawSize.y);

        TextureSheet sheet = StageData.enemyTexSheets[textureID];
        if(sheet == null) return false;

        drawCenter.set(enemy.x, enemy.y);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glTranslatef(drawCenter.x, drawCenter.y, 0);
            gl.glRotatef((float)enemy.drawAngle, 0, 0, 1);

            drawCenter.set(0, 0);
            UtilGL.setTextureSTCoords(sheet.getSTRect(frameIndex));

            if(enemy.checkNowDamaged()) {

                float[] cc = {(float)Math.random(),(float)Math.random(),(float)Math.random(),0};
                UtilGL.changeTexColor(gl, cc);
                UtilGL.drawTexture(gl, drawCenter, drawSize, sheet.GLtexID);
                UtilGL.changeTexColor(gl, null);
            }
            else
                UtilGL.drawTexture(gl, drawCenter, drawSize, sheet.GLtexID);

        }
        gl.glPopMatrix();

        return true;
    }
}
