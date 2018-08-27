package com.gondragon.shoot2.stage;

import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.enemy.CollisionRegion;
import com.gondragon.shoot2.enemy.EnemyData;
import com.gondragon.shoot2.enemy.GeneratingChild;
import com.gondragon.shoot2.enemy.MovingNode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileAccess {

    public static void main(String[] arg) {

        FileAccess access = new FileAccess();

        access.setEventList(new ArrayList<EventData>(), 1);
    }

    EventData eventData;
    ArrayList<EventData> eventList;

    EnemyData enemyData;
    ArrayList<EnemyData> enemyList;

    public void setEventList(ArrayList<EventData> list,int stageNumber){

        String[] eventFileName ={

                "event_stage1.txt",
                "event_stage1.txt",
                "event_stage1.txt",
                "event_stage1.txt",
                "event_stage1.txt",
                "event_stage1.txt",
                "event_stage1.txt"
        };

        eventList = list;
        eventList.clear();

        try {

            readFile(eventFileName[stageNumber-1]);

        } catch (IOException e) {

            e.printStackTrace();
        }

        interpretText();
    }

    public void setEnemyList(ArrayList<EnemyData> list,int stageNumber){

        String[] enemyFileName ={

                "enemy_stage1.txt",
                "enemy_stage1.txt",
                "enemy_stage1.txt",
                "enemy_stage1.txt",
                "enemy_stage1.txt",
                "enemy_stage1.txt",
                "enemy_stage1.txt"
        };

        enemyList = list;
        enemyList.clear();

        try {

            readFile(enemyFileName[stageNumber-1]);

        } catch (IOException e) {

            e.printStackTrace();
        }

        interpretText();
    }

    enum Token{	// "Generating"　"Node" "Collision"が重複してるので順番かえられないです

        NormalAnime("NormalAnime"),
        ExplosionAnime("ExplosionAnime"),
        NodeActionAnime("NodeActionAnime"),
        EnemyAppearance("EnemyAppearance"),

        _EnemyData("/EnemyData"), EnemyData("EnemyData"),
        _MovingNode("/MovingNode"), MovingNode("MovingNode"),
        _GeneratingChild("/GeneratingChild"), GeneratingChild("GeneratingChild"),
        _CollisionRegion("/CollisionRegion"), CollisionRegion("CollisionRegion"),
        _Sight("/Sight"), Sight("Sight"),

        Node("Node"), Collision("Collision"),

        _EventData("/EventData"), EventData("EventData"),
        Start("Start"), Generating("Generating"),

        Default("");

        String tokenStr;

        Token(String tokenStr){

            this.tokenStr = tokenStr;
        }
    };

    ArrayList<String> fileText = new ArrayList<String>();

    int phraseLine, readPoint; //<>で囲まれたphraseのどこを読んでいるかがreadPointです
    String phrase;


    private void readFile(String fileName) throws IOException {

        fileText.clear();

        InputStream in = null;

        try {

            in = new FileInputStream(fileName);

            InputStreamReader streamReader = new InputStreamReader(in);

            BufferedReader reader = new BufferedReader(streamReader);

            String buffer;

            while((buffer = reader.readLine()) !=null){

                fileText.add(buffer);
            }

            reader.close();

        } catch (FileNotFoundException e) {

            System.out.print("ファイルが見つかりません");

        } finally {

            if(in != null){

                in.close();
            }
        }
    }

    private void interpretText(){

        int textLine = fileText.size();

        for(phraseLine=0; phraseLine<textLine; phraseLine++){

            if(extractPhrase()){

                Token type = getPhraseType();
                //System.out.println("("+ type.tokenStr + "- start)");
                readParameter(type);
                //System.out.println("("+ type.tokenStr + "- finished)");
            }
        }
    }

    private boolean extractPhrase(){

        String lineText = fileText.get(phraseLine);

        //System.out.println("_" + lineText);

        int startBracket = lineText.indexOf('<');
        int endBracket   = lineText.indexOf('>');

        if(startBracket<endBracket){

            phrase = lineText.substring(startBracket + 1, endBracket);
            return true;
        }
        return false;
    }

    private Token getPhraseType(){

        Token type = Token.Default;

        for(Token t : Token.values()){

            if(phrase.indexOf(t.tokenStr,0) !=-1){

                type = t;
                break;
            }
        }
        return type;
    }

    private void readParameter(Token type){

        readPoint = 0;

        switch(type){

            case _EnemyData:
                enemyList.add(enemyData);
                break;

            case EnemyData:
                enemyData = new EnemyData();
                enemyData.initialize();
                enemyData.name = getStrValue();
                enemyData.objectID = getIntValue();
                enemyData.isDerivativeType = (getIntValue() == 1)? true : false;
                break;

            case Start:
                enemyData.startPosition.x = (int)getDblValue();
                enemyData.startPosition.y = (int)getDblValue();
                enemyData.startPosAttrib.x = getIntValue();
                enemyData.startPosAttrib.y = getIntValue();
                break;

            case _MovingNode:
                break;

            case MovingNode:
                break;

            case _GeneratingChild:
                break;

            case GeneratingChild:
                break;

            case _CollisionRegion:
                break;

            case CollisionRegion:
                enemyData.hitPoints = getIntValue();
                enemyData.atackPoints = getIntValue();
                break;

            case _Sight:
                break;

            case Sight:
                break;

            case NormalAnime:
                addNormalAnime();
                break;

            case ExplosionAnime:
                addExplosionAnime();
                break;

            case NodeActionAnime:
                addNodeActionAnime();
                break;

            case Node:
                addNode();
                break;

            case Generating:
                addGenerating();
                break;

            case Collision:
                addCollision();
                break;

            case _EventData:
                eventList.add(eventData);
                break;

            case EventData:
                readEventPhrase();
                break;

            case EnemyAppearance:
                eventData.eventObjectID = getIntValue();
                break;

            default:
        }

        return;
    }

    private String getStrValue(){

        String valueText = extractValueText();

        return valueText;
    }

    private int getIntValue(){

        String valueText = extractValueText();
        if(valueText==null) return -1;

        return Integer.valueOf(valueText);
    }

    private double getDblValue(){

        String valueText = extractValueText();
        if(valueText==null) return -1;

        return Double.valueOf(valueText);
    }

    private String extractValueText(){

        String valueText="";

        try{

            int startWquotation = phrase.indexOf('\"',readPoint);
            if(startWquotation ==-1) {
                throw new Exception("error: can't find start quotation.");
            }
            readPoint = startWquotation + 1;

            int endWquotation = phrase.indexOf('\"',readPoint);
            if(endWquotation ==-1) {
                throw new Exception("error: can't find end quotation.");
            }
            readPoint = endWquotation + 1;

            valueText = phrase.substring(startWquotation+1, endWquotation);

        }catch(Exception e){

            e.printStackTrace();

            valueText = null;
        }

        //System.out.println(valueText);

        return valueText;
    }

    private void readEventPhrase(){

        eventData = new EventData();
        eventData.initialize();
        eventData.scrollPoint =  getIntValue();
        eventData.eventCategory
                = EventData.EventCategory.getFromID(getIntValue());
    }

    private void addNode(){

        MovingNode node = enemyData.generateNewNode(EnemyData.MutableCategory.MOVING);

        node.startVelocity.x = getDblValue();
        node.startVelocity.y = getDblValue();
        node.startAcceleration.x = getDblValue();
        node.startAcceleration.y = getDblValue();
        node.homingAcceleration.x = getDblValue();
        node.homingAcceleration.y = getDblValue();
        node.nodeDurationFrame = getIntValue();
        node.startVelAttrib.x = getIntValue();
        node.startVelAttrib.y = getIntValue();
        node.startAccAttrib.x = getIntValue();
        node.startAccAttrib.y = getIntValue();
    }

    private void addGenerating(){

        GeneratingChild gen = enemyData.generateNewNode(EnemyData.MutableCategory.GENERATOR);

        gen.objectID = getIntValue();
        gen.repeat = getIntValue();
        gen.startFrame = getIntValue();
        gen.intervalFrame = getIntValue();
        gen.centerX = getIntValue();
        gen.centerY = getIntValue();
    }

    private void addCollision(){

        CollisionRegion col = enemyData.generateNewNode(EnemyData.MutableCategory.COLLISION);

        col.centerX = getIntValue();
        col.centerY = getIntValue();
        col.size = getIntValue();
        col.collisionShape
                = CollisionRegion.CollisionShape.getFromID(getIntValue());
    }

    private void addNormalAnime(){

        readAnimePhrase(enemyData.animationSet.normalAnime);
    }

    private void addExplosionAnime(){

        readAnimePhrase(enemyData.animationSet.explosionAnime);
    }

    private void addNodeActionAnime(){

        int keyNode = getIntValue();
        AnimationData animeData = enemyData.generateNewNodeActionAnime(keyNode);

        readAnimePhrase(animeData);
    }

    private void readAnimePhrase(AnimationData animeData){

        animeData.drawSize.x = getIntValue();
        animeData.drawSize.y = getIntValue();
        animeData.textureID  = getIntValue();
        animeData.frameOffset = getIntValue();
        animeData.frameNumber = getIntValue();
        animeData.frameInterval = getIntValue();
        animeData.repeatAttribute = AnimationData.RepeatAttribute.getFromID(getIntValue());
        animeData.rotateAction = AnimationData.RotateAttribute.getFromID(getIntValue());
        animeData.rotateOffset = getIntValue();
        animeData.angularVelocity = getDblValue();
    }
}
