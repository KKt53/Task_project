package com.example.task_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.task_project.Parent_class.DBEntry;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private LayoutInflater inflater;

    private DatabaseHelper helper = null;
    MainListAdapter sc_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Add_Task.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart(){

        super.onStart();


        mainListView = findViewById(R.id.mainList);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        helper = new DatabaseHelper(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("カテゴリー選択画面");
        }

        onShow();
    }


    // 削除ボタン　タップ時に呼び出されるメソッド
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        alertMake(id);
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new DatabaseHelper(this);

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBEntry.Table_Name, null, null,
                    null, null, null, null, null);
            //db.query(テーブル名, String[](カラム名の配列),条件(whereの内容),
            // 検索条件のパラメータに置換する値の配列,groupBy句,having句,orderBy句)

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry.Name};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.raw_main,cursor,from,to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainList);


            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView av, View view, int position, long id) {

                    //　クリックされた行のデータを取得
                    Cursor cursor = (Cursor)av.getItemAtPosition(position);

                    // テキスト登録画面 Activity へのインテントを作成
                    Intent intent  = new Intent(MainActivity.this, com.example.task_project.MainActivity2.class);

                    intent.putExtra(DBEntry._ID, cursor.getInt(0));
                    intent.putExtra(DBEntry.Name, cursor.getString(1));

                    // アクティビティを起動
                    startActivity(intent);
                }
            });
        }
    }

    private void alertMake(int id){
        String strMessage = "付随しているタスクも消えます";

        String sql = "DELETE From Child where Child.category = (SELECT Parent.Name from Parent where Parent._id = ?);";
        String[] selectionArgs = {Integer.valueOf(id).toString()};

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(strMessage);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do something
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    db.execSQL(sql,selectionArgs);
                }

                // データを削除
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    db.delete(DBEntry.Table_Name, DBEntry._ID+" = ?", new String[] {String.valueOf(id)});
                }

                // データを一覧表示
                onShow();
            }
        });

        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
    }

}