package com.gondragon.shoot2.texture;

import com.gondragon.shoot2.database.AccessOfTextureData;

import java.util.ArrayList;
import java.util.List;

public class TextureInitializer {

    private enum EnumTexture {

        MyPlane(0,"myplanesheet.png",4,4),
        Effect_0(1,"effect_sheet000.png",8,8),
        Effect_1(2,"effect_sheet001.png",8,8),
        Effect_2(3,"effect_sheet002.png",4,4),
        Effect_3(4,"effect_sheet003.png",8,8),
        Bullet_0(5,"bullet_sheet000.png",8,8),
        Bullet_1(6,"bullet_sheet001.png",8,8),
        Item(7,"item_sheet.png",8,8);

        int textureId, frameX, frameY;
        String pictureName;

        EnumTexture(int id, String name, int xc, int yc)
            { textureId = id; pictureName = name; frameX = xc; frameY = yc;};

        public TextureSheet getSheet(){

            TextureSheet sheet = new TextureSheet();

            sheet.textureID = textureId;
            sheet.frameNumberX = frameX;
            sheet.frameNumberY = frameY;
            sheet.pictureName = pictureName;

            return sheet;
        }
    }

    public static TextureSheet[] getEnumTexSheets(){

        TextureSheet[] sheets = new TextureSheet[EnumTexture.values().length];

        for(EnumTexture e : EnumTexture.values()) {

            sheets[e.textureId] = e.getSheet();
            AccessOfTextureData.setAssetImage(sheets[e.textureId], false);
        }
        return sheets;
    }

    public static TextureSheet getCharactersSheet(){

        TextureSheet sheet = new TextureSheet();

        sheet.textureID = 0;
        sheet.frameNumberX = 16;
        sheet.frameNumberY = 16;
        sheet.pictureName = "chr_sheet.png";

        AccessOfTextureData.setAssetImage(sheet, false);

        return sheet;
    }

    public static TextureSheet[] getStageEnemyTexSheets(int stageNumber){

        // 配列でsheetを扱う為にリストでdbから読みこんだsheetListは配列にセットした後、破棄します
        // 配列はdb上、indexが過不足無い序数順序で定義されていない可能性を考えて冗長に定義しています

        List<TextureSheet> texSheetList = new ArrayList<TextureSheet>();
        AccessOfTextureData.setTexDataList(texSheetList, stageNumber);
        if (texSheetList.size() ==0) return null;
        int maxIndex = getMaxIndex(texSheetList);

        TextureSheet[] sheets = new TextureSheet[maxIndex + 1];
        for(TextureSheet e: texSheetList){

            sheets[e.textureID] = new TextureSheet(e);
        }

        return sheets;
    }

    private static int getMaxIndex(List<TextureSheet> list){

        int result = 0;
        for(TextureSheet e: list){

            result = Math.max(result, e.textureID);
        }
        return result;
    }
}
