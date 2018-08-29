package com.gondragon.shoot2.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    private static Context context;

    private static final String databaseAssetsDir = "database/";
    private static final String databasePath = databaseAssetsDir + "texDB.db";
    private static final String imageAssetsDir = "texImage/";

    private AccessOfTextureData(){

    }

    public static void setContext(Context arg){

        context = arg;
    }

    public static void setTexDataList(List<TextureSheet> texSheetList, int stageNumber){

        SQLiteManager.initDatabase(databasePath);

        String sql;
        ResultSet resultSet;

        sql = "select * from TextureData order by textureID;";
        resultSet = SQLiteManager.getResultSet(sql);

        try {
            while(resultSet.next()){

                texSheetList.add(generateTexSheet(resultSet));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        SQLiteManager.closeDatabase();
    }

    private static TextureSheet generateTexSheet(ResultSet resultSet){

        TextureSheet textureSheet = new TextureSheet();

        setFromDatabase(textureSheet, resultSet);

        setImage(textureSheet);

        return textureSheet;
    }

    private static void setFromDatabase(TextureSheet textureSheet, ResultSet resultSet){

        try {
            textureSheet.textureID = resultSet.getInt("textureID");
            textureSheet.pictureName = resultSet.getString("pictureName");
            textureSheet.gridSizeX = resultSet.getInt("gridSizeX");
            textureSheet.gridSizeY = resultSet.getInt("gridSizeY");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static void setImage(TextureSheet textureSheet){

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
}
