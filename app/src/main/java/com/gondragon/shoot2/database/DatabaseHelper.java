package com.gondragon.shoot2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Android用SQLiteでアクセス出来るデータベースの作成クラス(アセットのデータベースをコピー作成します)

    private Context context;
    private File databasePath;
    private int databaseVersion;
    private String dbPathInAsset;

    private Logger logger = Logger.getLogger("DatabaseHelper");

    public DatabaseHelper
            (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);

        this.context = context;
        databasePath = context.getDatabasePath(name);
        databaseVersion = version;
        dbPathInAsset = "database/" + name + ".db";
    }

    public void createDatabaseFromAsset() throws IOException{

        if(!checkSameDatabaseExists()){// 既に同名同バージョンのDBが存在する場合スキップ

            getReadableDatabase();
            copyFromAsset();

            SQLiteDatabase checkDB = openWtitableDatabase();

            if(checkDB != null){

                logger.warning("new DB created!");

                checkDB.setVersion(databaseVersion);
                checkDB.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private SQLiteDatabase openWtitableDatabase(){

        String filePath = databasePath.getAbsolutePath();
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){ /* 当該データベースにアクセス出来ず(存在せず)*/ }

        return checkDB;
    }

    private boolean checkSameDatabaseExists(){

        SQLiteDatabase checkDB = openWtitableDatabase();

        if(checkDB == null) {

            logger.warning("the DB not exists");

            return false;
        }

        if(checkDB.getVersion() == databaseVersion){// 同じバージョンの同名データベースが存在する

            logger.warning("same version exists!");

            checkDB.close();
            return true;
        }

        logger.warning("deffernt version exists (deleted)");

        databasePath.delete();  //  同名のデータベースがあるがバージョンが違うので削除
        return false;
    }

    private void copyFromAsset() throws IOException{

        InputStream input = context.getAssets().open(dbPathInAsset);
        OutputStream out = new FileOutputStream(databasePath);

        byte[] buffer = new byte[1024];
        int size;

        while((size = input.read(buffer)) >0){ out.write(buffer, 0, size);}

        out.flush();
        input.close();
        out.close();
    }
}
