package com.example.task_project;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import static com.example.task_project.Parent_class.DBEntry;
import static com.example.task_project.Child_class.DBEntry_2;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity2 extends AppCompatActivity {
    private DatabaseHelper helper = null;
    Sub_ListAdapter sc_adapter;


    private String name_p;
//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button Pri_button = findViewById(R.id.Pri_button);



        Intent intent = getIntent();


        name_p = intent.getStringExtra(DBEntry.Name);

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
                Intent intent = new Intent(getApplication(), Add_task_2.class);
                intent.putExtra("name_p",name_p);
                intent.putExtra("upd_flag",0);
                startActivity(intent);
            }
        });

        Pri_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), Priority.class);
                intent.putExtra("name_p",name_p);
                startActivity(intent);
            }
        });
        onShow();
    }

    @Override
    public void onStart(){

        super.onStart();



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name_p + "のタスク選択画面");
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
        String sql = "SELECT Child._id,Child.Name,Child.flag,Child.Date FROM Parent, Child WHERE Parent.Name = Child.category and Child.parent = '' and Child.category = ?;";
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

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView av, View view, int position, long id) {

                    //　クリックされた行のデータを取得
                    Cursor cursor = (Cursor)av.getItemAtPosition(position);

                    // テキスト登録画面 Activity へのインテントを作成
                    Intent intent  = new Intent(MainActivity2.this, com.example.task_project.MainActivity3.class);

                    intent.putExtra(DBEntry_2._ID, cursor.getInt(0));
                    intent.putExtra(DBEntry_2.Name, cursor.getString(1));
                    intent.putExtra("name_c",name_p);

                    // アクティビティを起動
                    startActivity(intent);
                }
            });
        }
    }


    // 削除ボタン　タップ時に呼び出されるメソッド
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        alertMake(id);
    }

    public void btnUpd_onClick(View view){
        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);
        String name_m = ((Cursor) sc_adapter.getItem(pos)).getString(1);
        String date = ((Cursor) sc_adapter.getItem(pos)).getString(3);

        Intent intent  = new Intent(getApplication(), Add_task_2.class);
        intent.putExtra("_id",id);
        intent.putExtra("name_m", name_m);
        intent.putExtra("date", date);
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

    private void alertMake(int id){
        String strMessage = "付随しているタスクも消えます";

        String sql = "DELETE From Child where Child.Parent = (SELECT Child.Name from Child where Child._id = ?);";
        String sql_2 = "DELETE From Child where Child._id = ?;";
        String[] selectionArgs = {Integer.valueOf(id).toString()};


        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(strMessage);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // データを削除
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    db.execSQL(sql,selectionArgs);
                }


                // データを削除
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    db.execSQL(sql_2,selectionArgs);
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