package com.gondragon.shoot2.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.gondragon.shoot2.enemy.EnemyData;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AccessOfEnemyDataTest {

    @Before
    public void setContext() {

        Context context = InstrumentationRegistry.getTargetContext();
        AccessOfEnemyData.setContext(context);
    }

    @Test
    public void setEnemyList(){

        ArrayList<EnemyData> enemyList = new ArrayList<>();
        AccessOfEnemyData.setEnemyList(enemyList);
    }

}