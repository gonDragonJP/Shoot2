package com.gondragon.shoot2.texture;

import com.gondragon.shoot2.database.AccessOfTextureData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextureInitializer {

    public enum EnumTexture {

        MyPlane(0,"myplanesheet.png",4,4),
        Effect_0(1,"effect_sheet000.png",8,8),
        Effect_1(2,"effect_sheet001.png",8,8),
        Effect_2(3,"effect_sheet002.png",4,4),
        Effect_3(4,"effect_sheet003.png",8,8),
        Bullet_0(5,"bullet_sheet000.png",8,8),
        Bullet_1(6,"bullet_sheet001.png",8,8),
        Item(7,"item_sheet.png",8,8),
        Meter(8,"meter.png",1,1);

        public int textureId, frameX, frameY;
        public String pictureName;

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

            if(e.textureID<1000) {
                sheets[e.textureID] = new TextureSheet(e);
            }
        }

        return sheets;
    }

    private static int getMaxIndex(List<TextureSheet> list){

        int result = 0;
        for(TextureSheet e: list){

            if(e.textureID<1000)	// index1000番台は背景用テクスチャです
                result = Math.max(result, e.textureID);
        }
        return result;
    }

    public static TextureSheet[] getBackgroundTexSheets(int stageNumber){

        ArrayList<TextureSheet> texSheetList = new ArrayList<>();
        AccessOfTextureData.setTexDataList(texSheetList, stageNumber);
        if (texSheetList.size() ==0) return null;

        Iterator<TextureSheet> it = texSheetList.iterator();
        while(it.hasNext()){
            if(it.next().textureID<1000) it.remove();
        }

        List<TextureSheet> sortList = getSortList(texSheetList);

        TextureSheet[] sheets = new TextureSheet[sortList.size()];
        for(int i=0; i<sortList.size(); i++){

            TextureSheet e = sortList.get(i);
            sheets[i] = new TextureSheet(e);
        }

        return sheets;
    }

    private static ArrayList<TextureSheet> getSortList(ArrayList<TextureSheet> argList){

        ArrayList<TextureSheet> sortList = new ArrayList<>();

        for(TextureSheet e: argList){

            if(sortList.size()==0) {sortList.add(e); continue;}

            for(int i=0; i<sortList.size(); i++){

                if(e.textureID < sortList.get(i).textureID) {
                    sortList.add(i , e);
                    break;
                }
                if(i == sortList.size()-1) {
                    sortList.add(e);
                    break;
                }
            }
        }
        return sortList;
    }
}
