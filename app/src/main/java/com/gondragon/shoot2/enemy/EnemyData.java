package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.collision.CollisionRegion;
import com.gondragon.shoot2.vector.Int2Vector;

import java.util.ArrayList;

public class EnemyData {

    public enum EnemyCategory {

        FLYING(0),
        GROUND(1),
        MAKER(2),
        BULLET(3),
        PARTS(4);

        int id;

        EnemyCategory(int id){

            this.id = id;
        }

        public int getID(){

            return this.id;
        }

        public static EnemyCategory getFromID(int id){

            EnemyCategory result = null;

            for(EnemyCategory c: EnemyCategory.values()){

                if(c.id == id) {
                    result = c;
                    break;
                }
            }
            return result;
        }
    };

    public enum StartPositionAtrib{

        DEFAULT(0),
        PLANE_SIDE(1),
        COUNTERPLANE_SIDE(2),
        POS_PLANE(3);

        int id;

        StartPositionAtrib(int id){

            this.id = id;
        }

        public static StartPositionAtrib getFromID(int id){

            StartPositionAtrib result = null;

            for(StartPositionAtrib c: StartPositionAtrib.values()){

                if(c.id == id){
                    result = c;
                    break;
                }
            }
            return result;
        }
    };

    public enum StartVectorAtrib{

        DEFAULT(0),
        ADOPT_TO_CENTER(1),
        ADOPT_TO_COUNTERCENTER(2),
        TEND_TO_PLANE(3),
        TEND_PARENT_AHEAD(4),
        ADOPT_TO_PLANE(5),
        ADOPT_TO_COUNTERPLANE(6),
        TEND_PARENT_FACEON(7);

        int id;

        StartVectorAtrib(int id){

            this.id = id;
        }

        public static StartVectorAtrib getFromID(int id){

            StartVectorAtrib result = null;

            for(StartVectorAtrib c: StartVectorAtrib.values()){

                if(c.id == id) {
                    result = c;
                    break;
                }
            }
            return result;
        }
    };

    public enum MutableCategory{

        MOVING, GENERATOR, COLLISION, NODE_ACTION_ANIME
    }

    public String name;
    public boolean isDerivativeType;
    public int objectID;
    public int explosiveObjectID;
    public int hitPoints;
    public int atackPoints;
    public Int2Vector startPosition;
    public Int2Vector startPosAttrib;

    public ArrayList<MovingNode> node;
    public ArrayList<GeneratingChild> generator;
    public ArrayList<CollisionRegion> collision;
    public AnimationSet animationSet;

    public EnemyData(){

        startPosition = new Int2Vector();
        startPosAttrib = new Int2Vector();
        node = new ArrayList<MovingNode>();
        generator = new ArrayList<GeneratingChild>();
        collision = new ArrayList<CollisionRegion>();
        animationSet = new AnimationSet();

        initialize();
    }

    public void initialize(){

        name = "";
        isDerivativeType = false;
        objectID = -1;
        explosiveObjectID = -1;

        hitPoints = 0;
        atackPoints = 0;

        startPosition.set(0, 0);
        startPosAttrib.set(0, 0);
        node.clear();
        generator.clear();
        collision.clear();
        animationSet.initialize();
    }

    public void copy(EnemyData src){

        initialize();

        this.name = src.name;
        this.isDerivativeType = src.isDerivativeType;
        this.objectID = src.objectID;
        this.explosiveObjectID = src.explosiveObjectID;

        this.hitPoints = src.hitPoints;
        this.atackPoints = src.atackPoints;

        this.startPosition.copy(src.startPosition);
        this.startPosAttrib.copy(src.startPosAttrib);

        node.clear();
        generator.clear();
        collision.clear();

        for(MovingNode e: src.node){
            this.node.add(new MovingNode(e));
        }
        for(GeneratingChild e: src.generator){
            this.generator.add(new GeneratingChild(e));
        }
        for(CollisionRegion e: src.collision){
            this.collision.add(new CollisionRegion(e));
        }
        this.animationSet.copy(src.animationSet);
    }

    public EnemyCategory getCategory(){

        return EnemyCategory.getFromID(objectID / 1000);
    }

    @SuppressWarnings("unchecked")
    public <T> T generateNewNode(MutableCategory category){

        Object result = null;

        switch(category){

            case MOVING:
                result = new MovingNode();
                node.add((MovingNode)result);
                break;
            case GENERATOR:
                result = new GeneratingChild();
                generator.add((GeneratingChild)result);
                break;
            case COLLISION:
                result = new CollisionRegion();
                collision.add((CollisionRegion)result);
                break;
            default:
        }
        return (T)result;
    }

    public AnimationData generateNewNodeActionAnime(int keyNode){

        AnimationData result = null;

        if(!animationSet.nodeActionAnime.containsKey(keyNode)){

            result = new AnimationData();
            animationSet.nodeActionAnime.put(keyNode, result);
        }
        return result;
    }

    public int getMinFreeKeyNode(){

        int key = 0;
        while(animationSet.nodeActionAnime.containsKey(key)) key++;

        return key;
    }

    public void deleteNode(MutableCategory category, int index){

        int keyNode = index;//NodeActionAnimeの削除の場合indexはkeyと読み変えます

        switch(category){

            case MOVING:
                node.remove(index);
                break;
            case GENERATOR:
                generator.remove(index);
                break;
            case COLLISION:
                collision.remove(index);
                break;
            case NODE_ACTION_ANIME:
                animationSet.nodeActionAnime.remove(keyNode);
            default:
        }
    }
}
