package com.example.task_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.task_project.Child_class.DBEntry_2;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Add_task_3 extends AppCompatActivity {
    private DatabaseHelper helper = null;
    private EditText editText;
    private String name_p;
    private String name_c;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task3);
        editText = findViewById(R.id.Task_Name);
        Button buttonSave = findViewById(R.id.Task_make);
        Intent intent = getIntent();
        String name_p = intent.getStringExtra("name_p");
        String name_c = intent.getStringExtra("name_c");
        int id = intent.getIntExtra("_id", 0);
        String name_m = intent.getStringExtra("name_m");
        int upd_flg = intent.getIntExtra("upd_flag",0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        // データベースヘルパーを準備
        helper = new DatabaseHelper(this);

        if(upd_flg == 1){
            editText.setText(name_m);
            buttonSave.setText("更新");
        }
        else{
            buttonSave.setText("作成");
        }


        // lambda
        buttonSave.setOnClickListener( v -> {
            DatabaseHelper helper = new DatabaseHelper(this);

            // エディットテキストのテキストを取得
            String text = editText.getText().toString();

            // 書き込みモードでデータベースをオープン
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                // 入力されたタイトルとコンテンツをContentValuesに設定
                // ContentValuesは、項目名と値をセットで保存できるオブジェクト
                ContentValues cv = new ContentValues();
                cv.put(DBEntry_2.Name, text);
                cv.put(DBEntry_2.parent, name_p);
                cv.put(DBEntry_2.category, name_c);

                if(upd_flg == 1){
                    db.update(DBEntry_2.Table_Name, cv, "_id = ?", new String[]{String.valueOf(id)});
                }
                else{
                    // データ新規登録
                    db.insert(DBEntry_2.Table_Name, null, cv);
                }

                finish();
            }

        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("子タスク作成画面");
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
