package com.gondragon.shoot2.animation;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class AnimationSet{

    public enum AnimeKind {NORMAL(0), EXPLOSION(1), NODEACTION(2), SPECIAL(3);

        int id;

        AnimeKind(int id){ this.id = id; };

        public int getID(){ return id; };

        public static AnimeKind getFromID(int id){

            for(AnimeKind e: AnimeKind.values()){
                if (e.id == id) return e;
            }
            return null;
        }
    }

    public AnimationData normalAnime = new AnimationData();
    public AnimationData explosionAnime = new AnimationData();
    public Map<Integer, AnimationData> nodeActionAnime
            = new Hashtable<Integer, AnimationData>();
    // node変化をトリガーにしてアニメが変化するのでリストではダメです（indexがとんだ値になる）

    public AnimationSet(){

    }

    public void initialize(){

    }

    public AnimationSet(AnimationSet src){

        this();
        this.copy(src);
    }

    public AnimationData getAnime(AnimeKind kind, int nodeIndex){

        switch(kind){

            case NORMAL:	 return normalAnime;
            case EXPLOSION:  return explosionAnime;
            case NODEACTION: return getNodeActionAnime(nodeIndex);
        }
        return null;
    }

    public AnimationData getNodeActionAnime(int nodeIndex){

        return nodeActionAnime.get(nodeIndex);
    }

    public void copy(AnimationSet src){

        normalAnime.copy(src.normalAnime);
        explosionAnime.copy(src.explosionAnime);

        nodeActionAnime.clear();
        Set<Integer> keySet = src.nodeActionAnime.keySet();
        for(Integer e: keySet){
            AnimationData srcAnime = src.nodeActionAnime.get(e);
            nodeActionAnime.put(e, new AnimationData(srcAnime));
        }
    }
}
