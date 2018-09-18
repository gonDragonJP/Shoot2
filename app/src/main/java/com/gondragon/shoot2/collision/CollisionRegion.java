package com.gondragon.shoot2.collision;

public class CollisionRegion {

    public enum CollisionShape {

        CIRCLE(0),
        RECTANGLE(1);

        int id;

        CollisionShape(int id){

            this.id = id;
        }

        public int getID(){

            return id;
        }

        public static CollisionShape getFromID(int id){

            CollisionShape result = null;

            for(CollisionShape c: CollisionShape.values()){

                if(c.id == id) {
                    result = c;
                    break;
                }
            }
            return result;
        }
    };

    public int centerX;
    public int centerY;
    public int size;
    public CollisionShape collisionShape = CollisionShape.CIRCLE;

    public CollisionRegion(){

    }

    public CollisionRegion(CollisionRegion src){

        copy(src);
    }

    public void copy(CollisionRegion src){

        centerX = src.centerX;
        centerY = src.centerY;
        size = src.size;
        collisionShape = src.collisionShape;
    }

    public boolean checkCollision(CollisionRegion object){

        int dx = (centerX - object.centerX);
        int dy = (centerY - object.centerY);
        int r = (size + object.size);

        return (dx * dx + dy * dy) < r * r;
    }
}
