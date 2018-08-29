package com.gondragon.shoot2.texture;

import com.gondragon.shoot2.database.AccessOfTextureData;

import java.util.ArrayList;
import java.util.List;

public class TextureInitializer {

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
