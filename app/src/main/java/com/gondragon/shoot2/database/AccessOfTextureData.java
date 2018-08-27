package com.gondragon.shoot2.database;

import com.gondragon.shoot2.texture.TextureSheet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccessOfTextureData {

    private static final String databaseDir = "C:/Users/Takahiro/workspace/textureComposer/texDataBase/";

    //private static final String databaseDir = ".\\texDataBase\\";
    private static final String databasePath = databaseDir + "texDB.db";
    private static final String imageDir = databaseDir + "image\\";

    private AccessOfTextureData(){

    }

    public static String getTexImageDir(){

        return imageDir;
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

        TextureSheet TextureSheet = new TextureSheet();

        setTextureSheet(TextureSheet, resultSet);

        return TextureSheet;
    }

    private static void setTextureSheet(TextureSheet textureSheet, ResultSet resultSet){

        try {
            textureSheet.textureID = resultSet.getInt("textureID");
            textureSheet.pictureName = resultSet.getString("pictureName");
            textureSheet.gridSizeX = resultSet.getInt("gridSizeX");
            textureSheet.gridSizeY = resultSet.getInt("gridSizeY");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
