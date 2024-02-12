package com.example.task_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.task_project.Parent_class.DBEntry;
import static com.example.task_project.Child_class.DBEntry_2;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Priority extends AppCompatActivity {

    private String name_p;
    Priority_adapter sc_adapter;
    private DatabaseHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        name_p = intent.getStringExtra("name_p");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("優先順位表示中");
        }

        onShow();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new DatabaseHelper(this);

        // データベースを検索する項目を定義
        String sql = "SELECT Child._id,Child.Name FROM Parent, Child WHERE Parent.Name = Child.category and Child.parent = '' and Child.category = ?  ORDER by case when date is NULL then '2' when date = '' then '1' else '0' end, date ASC;";
        String[] selectionArgs ={name_p};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.rawQuery(sql, selectionArgs);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry_2.Name};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new Priority_adapter(
                    this, R.layout.raw_pri,cursor,from,to,0);


            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.priList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}