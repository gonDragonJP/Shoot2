package com.gondragon.shoot2.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.texture.TextureSheet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccessOfTextureData {

    //  テクスチャのデータベースアクセスおよび画像読み込みシングルトンクラス
    //  注意！　使用の前にコンテキストのセット(setContext)が必要です

    private AccessOfTextureData(){

    }

    private static Context context;
    private static String databaseName = Global.textureDatabaseName;
    private static int databaseVersion = Global.textureDB_Version;
    private static String imageAssetsDir, stgTableSuffix;

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setTexDataList(List<TextureSheet> texSheetList, int stageNumber){

        SQLiteManager.initDatabase(context, databaseName, databaseVersion);
        stgTableSuffix = "_Stage_"+String.valueOf(stageNumber);

        Cursor cursor = SQLiteManager.getRowValuesWithOrder
                ("TextureTable"+stgTableSuffix, "textureID");
        //sql = "select * from TextureData order by textureID;";
        cursor.moveToFirst();

        do {

                texSheetList.add(generateTexSheet(cursor));

        } while(cursor.moveToNext());
        
        SQLiteManager.closeDatabase();
    }

    private static TextureSheet generateTexSheet(Cursor cursor){

        TextureSheet textureSheet = new TextureSheet();

        setFromDatabase(textureSheet, cursor);

        setImage(textureSheet, textureSheet.textureID >=1000);

        return textureSheet;
    }

    private static void setFromDatabase(TextureSheet textureSheet, Cursor cursor){

        textureSheet.textureID = cursor.getInt(cursor.getColumnIndex("textureID"));
        textureSheet.pictureName = cursor.getString(cursor.getColumnIndex("pictureName"));
        textureSheet.gridSizeX = cursor.getInt(cursor.getColumnIndex("gridSizeX"));
        textureSheet.gridSizeY = cursor.getInt(cursor.getColumnIndex("gridSizeY"));
    }

    private static void setImage(TextureSheet textureSheet, boolean isBackgroungSheet){

        imageAssetsDir = isBackgroungSheet ? "bgImage/" : "texImage/";

        textureSheet.texImage = getTexImage(textureSheet.pictureName);

        textureSheet.frameNumberX = textureSheet.texImage.getWidth() / textureSheet.gridSizeX;
        textureSheet.frameNumberY = textureSheet.texImage.getHeight() / textureSheet.gridSizeY;
    }

    private static Bitmap getTexImage(String pictureName){

        Bitmap texImage;
        AssetManager assetManager = context.getAssets();

        try {

            InputStream stream = assetManager.open(imageAssetsDir + pictureName);
            texImage = BitmapFactory.decodeStream(new BufferedInputStream(stream));

        } catch (IOException e) {

            e.printStackTrace();
            texImage = null;
        }

        return texImage;
    }

    public static void setAssetImage(TextureSheet sheet, boolean isBackgroungSheet){
        //イニシャライザーで列挙定義されたシートに画像を読み込む為のメソッドです
        //ToDo)いずれデータベース化してこのメソッドは破棄する

        imageAssetsDir = isBackgroungSheet ? "bgImage/" : "texImage/";

        sheet.texImage = getTexImage(sheet.pictureName);

        sheet.gridSizeX = sheet.texImage.getWidth() / sheet.frameNumberX;
        sheet.gridSizeY = sheet.texImage.getHeight() / sheet.frameNumberY;
    }
}
