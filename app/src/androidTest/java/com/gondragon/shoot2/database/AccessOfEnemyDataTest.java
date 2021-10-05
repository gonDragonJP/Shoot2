package com.gondragon.shoot2.database;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import com.gondragon.shoot2.enemy.EnemyData;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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