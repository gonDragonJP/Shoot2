package com.gondragon.shoot2.stage;

import android.graphics.PointF;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.UtilGL;
import com.gondragon.shoot2.database.AccessOfTextureData;
import com.gondragon.shoot2.texture.TextureSheet;

import javax.microedition.khronos.opengles.GL10;

public class Background {

    private Background(){

    }

    private static final PointF drawCenter = new PointF(0,0);
    private static float screenCenterX, screenCenterY;
    private static PointF drawSize = new PointF();
    private static TextureSheet drawSheet;

    private static int pictureNumber, pictureLength;
    private static int scrollPosition, pictureIndex;

    public static void initialize(int bgPictureNumber){

        pictureNumber = bgPictureNumber;

        screenCenterX = Global.virtualScreenSize.x /2;
        screenCenterY = Global.virtualScreenSize.y /2;
        int width = Global.virtualScreenLimit.width();
        pictureLength = Global.virtualScreenLimit.height();

        drawSize.set(width, pictureLength);
        scrollPosition =0;
    }

    public static TextureSheet[] getBackgroundSheets(int stageNumber){

        TextureSheet[] sheets = new TextureSheet[pictureNumber];

        for(int i=0; i<pictureNumber; i++) {

            sheets[i] = getSheet(stageNumber, i);
            AccessOfTextureData.setAssetImage(sheets[i], true);
        }
        return sheets;
    }

    private static TextureSheet getSheet(int stageNumber, int textureId){

        TextureSheet sheet = new TextureSheet();

        sheet.textureID = textureId;
        sheet.frameNumberX = 1;
        sheet.frameNumberY = 1;
        sheet.pictureName = "bg_stg"+String.valueOf(stageNumber)+"_"+String.valueOf(textureId+1)+".png";

        return sheet;
    }

    public static void onDraw(GL10 gl, int scrollPoint){

        calcScroll(scrollPoint);

        //まず一枚描出
        drawSheet = StageData.backgroundSheets[pictureIndex];
        drawOnePicture(gl, scrollPosition);

        // 続くスクロールを上に描写
        int preIndex = (pictureIndex +1) == pictureNumber ? 0 : pictureIndex +1;
        drawSheet = StageData.backgroundSheets[preIndex];
        drawOnePicture(gl, scrollPosition - pictureLength);
    }

    private static void drawOnePicture(GL10 gl, int position){

        UtilGL.setTextureSTCoords(drawSheet.getSTRect(0));

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glTranslatef(screenCenterX, screenCenterY + position, 0);
            UtilGL.drawTexture(gl, drawCenter, drawSize, drawSheet.GLtexID);
        }
        gl.glPopMatrix();

    }

    private static void calcScroll(int scrollPoint){

        int wholePictureLength = pictureLength * pictureNumber;

        scrollPosition = scrollPoint % pictureLength;
        pictureIndex = (scrollPoint / pictureLength) % pictureNumber;
    }
}
