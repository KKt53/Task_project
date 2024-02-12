package com.example.task_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.icu.util.Calendar;
import static com.example.task_project.Child_class.DBEntry_2;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class Add_task_2 extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);

        ImageView imageView = findViewById(R.id.date_picker_actions);

        EditText editText_d = findViewById(R.id.date);
        EditText task_name = findViewById(R.id.Task_name);
        Button buttonSave_c = findViewById(R.id.Pri_button);

        Intent intent = getIntent();

        String name_p = intent.getStringExtra("name_p");
        int id = intent.getIntExtra("_id",0);
        int upd_flg = intent.getIntExtra("upd_flag",0);
        String name_m = intent.getStringExtra("name_m");
        String date = intent.getStringExtra("date");

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

        if(upd_flg == 1){
            editText_d.setText(date);
            task_name.setText(name_m);
            buttonSave_c.setText("更新");
        }
        else{
            buttonSave_c.setText("作成");
        }

        //EditTextにリスナーをつける
        imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                Calendar date = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Add_task_2.this,
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                editText_d.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );

                //dialogを表示
                datePickerDialog.show();

            }
        });

        buttonSave_c.setOnClickListener( v -> {

            DatabaseHelper helper = new DatabaseHelper(this);

            // エディットテキストのテキストを取得
            String date_s = editText_d.getText().toString();
            String date_n = task_name.getText().toString();

            // 書き込みモードでデータベースをオープン
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                // 入力されたタイトルとコンテンツをContentValuesに設定
                // ContentValuesは、項目名と値をセットで保存できるオブジェクト
                ContentValues cv = new ContentValues();
                cv.put(DBEntry_2.Name, date_n);
                cv.put(DBEntry_2.Date, date_s);
                cv.put(DBEntry_2.category, name_p);

                // データ新規登録
                if(upd_flg == 1){
                    ContentValues cvu = new ContentValues();
                    cvu.put(DBEntry_2.parent, date_n);
                    db.update(DBEntry_2.Table_Name, cvu, "Parent = (SELECT Child.Name from Child where Child._id = ?)", new String[]{String.valueOf(id)});
                    db.update(DBEntry_2.Table_Name, cv, "_id = ?", new String[]{String.valueOf(id)});
                }
                else{
                    db.insert(DBEntry_2.Table_Name, null, cv);
                }
                //db.insert(DBEntry_2.Table_Name, null, cv);
                //db.update(DBEntry_2.Table_Name, cv, "id = " + id, null);

                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("タスク作成画面");
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