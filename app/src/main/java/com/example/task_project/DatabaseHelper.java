package com.example.task_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.task_project.Parent_class.DBEntry;
import static com.example.task_project.Child_class.DBEntry_2;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final private int VERSION = 2;

    static final private String DBNAME = "Task.db";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        // テーブルを作成
        db.execSQL(
                "CREATE TABLE "+ DBEntry.Table_Name +
                        " (" +
                        DBEntry._ID +
                        " INTEGER PRIMARY KEY, "  +
                        DBEntry.Name +
                        " TEXT default 'カテゴリー名'" +
                        " ) "
        );

        db.execSQL(
                "CREATE TABLE "+ DBEntry_2.Table_Name +
                        " (" +
                        DBEntry_2._ID +
                        " INTEGER PRIMARY KEY, " +
                        DBEntry_2.Name +
                        " TEXT default 'タスク名', " +
                        DBEntry_2.flag +
                        " INTEGER  default '0'," +
                        DBEntry_2.category +
                        " TEXT default '', " +
                        DBEntry_2.parent +
                        " TEXT default '', " +
                        DBEntry_2.child +
                        " TEXT default '', " +
                        DBEntry_2.Date +
                        " INTEGER DEFAULT '', " +
                        DBEntry_2.Data_flag +
                        " INTEGER default '0', " +
                        DBEntry_2.ranking +
                        " INTEGER default '0', " +
                        DBEntry_2.prerequisite +
                        " TEXT default '前提条件' " +
                        " ) "
        );

        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBEntry.Table_Name +
                        " BEGIN "+
                        " UPDATE " + DBEntry.Table_Name + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");
    }
    // データベースをバージョンアップした時、テーブルを削除してから再作成
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.Table_Name);
        onCreate(db);
    }
}