package jp.ac.chiba_fjb.f.home;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by x15g010 on 2017/11/02.
 */

public  class TextDB extends SQlite{

    public TextDB(Context context) {
        //ここでデータベースのファイル名とバージョン番号を指定
        super(context, "TextDB.db",1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //初期テーブルの作成
        db.execSQL("create table TextDB(id integer primary key,name text);");
        db.execSQL("create table GomiDB(id integer primary key,name text);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //バージョン番号を変えた場合に呼び出される
    }
}
