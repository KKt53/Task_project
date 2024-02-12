package com.example.task_project;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.task_project.Parent_class.DBEntry;
import static com.example.task_project.Child_class.DBEntry_2;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity3 extends AppCompatActivity {
    private DatabaseHelper helper = null;
    Sub_ListAdapter sc_adapter;


    private String name_c;
    private String name_p;
//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        name_c = intent.getStringExtra("name_c");
        name_p = intent.getStringExtra(DBEntry_2.Name);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Add_task_3.class);
                intent.putExtra("name_p",name_p);
                intent.putExtra("name_c",name_c);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart(){

        super.onStart();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name_p + "の子タスク選択画面");
        }

        helper = new DatabaseHelper(this);
        onShow();
    }

    // アクティビティの再開処理
    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        onShow();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new DatabaseHelper(this);

        // データベースを検索する項目を定義
        String sql = "SELECT Child._id,Child.Name,Child.flag FROM Child WHERE Child.Parent = ?;";
        String[] selectionArgs ={name_p};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.rawQuery(sql, selectionArgs);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry_2.Name,DBEntry_2.flag};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title, R.id.toggle_switch};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new Sub_ListAdapter(
                    this, R.layout.raw_sub,cursor,from,to,0);

            sc_adapter.setViewBinder((View view, Cursor c, int columnIndex) -> {
                if(columnIndex == c.getColumnIndex(DBEntry_2.flag)){
                    ((SwitchCompat)view).setChecked(c.getInt(columnIndex) == 1);
                    return true;
                }
                else{
                    return false;
                }

            });

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

        }
    }


    // 削除ボタン　タップ時に呼び出されるメソッド
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();
        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データを削除
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBEntry_2.Table_Name, DBEntry_2._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データを一覧表示
        onShow();
    }

    public void btnUpd_onClick(View view){
        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);
        String name_m = ((Cursor) sc_adapter.getItem(pos)).getString(1);

        Intent intent  = new Intent(getApplication(), Add_task_3.class);
        intent.putExtra("_id",id);
        intent.putExtra("name_m", name_m);
        intent.putExtra("name_p", name_p);
        intent.putExtra("upd_flag",1);

        startActivity(intent);
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
