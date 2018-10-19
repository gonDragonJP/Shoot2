package com.gondragon.shoot2.enemy;

public interface EnemyCommunicable {

    Enemy getGeneratingChild (Enemy parent);
    void generateExplosiveObject (Enemy parent);
}
