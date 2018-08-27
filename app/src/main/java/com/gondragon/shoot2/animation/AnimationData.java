package com.gondragon.shoot2.animation;

import com.gondragon.shoot2.vector.Double2Vector;

public class AnimationData{

    public enum RepeatAttribute {

        LOOP(0), ONCE(1), STOP(2);

        int id;

        RepeatAttribute(int id){

            this.id = id;
        }

        public static RepeatAttribute getFromID(int id){

            for(RepeatAttribute e: RepeatAttribute.values()){
                if(e.id == id) return e;
            }
            return null;
        }

        public int getID(){

            return this.id;
        }

    };

    public enum RotateAttribute{

        DEFAULT(0), TENDTOPLANE(1), CLOCKWISE(2), TENDAHEAD(3),
        TENDPARENTFACEON(4), SETPARENTFACEONANDCW(5);

        int id;

        RotateAttribute(int id){

            this.id = id;
        }

        public static RotateAttribute getFromID(int id){

            for(RotateAttribute e: RotateAttribute.values()){
                if(e.id == id) return e;
            }
            return null;
        }

        public int getID(){

            return this.id;
        }
    };

    public Double2Vector drawSize = new Double2Vector();
    public int textureID;
    public int frameOffset, frameNumber, frameInterval;
    public RepeatAttribute repeatAttribute;
    public RotateAttribute rotateAction;
    public int rotateOffset;
    public double angularVelocity;

    public AnimationData(){

        initialize();
    }

    public AnimationData(AnimationData src){

        this();
        this.copy(src);
    }

    public void initialize(){

        drawSize.set(0, 0);
        textureID = -1;
        frameOffset = frameNumber = frameInterval = rotateOffset =0;
        angularVelocity = 0;
        repeatAttribute = RepeatAttribute.LOOP;
        rotateAction = RotateAttribute.DEFAULT;
    }

    public void copy(AnimationData src){

        drawSize.copy(src.drawSize);
        textureID = src.textureID;
        frameOffset = src.frameOffset;
        frameNumber = src.frameNumber;
        frameInterval = src.frameInterval;
        rotateOffset = src.rotateOffset;
        angularVelocity = src.angularVelocity;
        repeatAttribute = src.repeatAttribute;
        rotateAction = src.rotateAction;
    }
}