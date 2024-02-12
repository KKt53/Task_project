package com.example.task_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;

import static com.example.task_project.Child_class.DBEntry_2;

import androidx.appcompat.widget.SwitchCompat;

public class Sub_ListAdapter extends SimpleCursorAdapter {

    private Context sub_context;
    private static Sub_ListAdapter instance = null;

    private DatabaseHelper helper = null;

    // コンストラクタ
    public Sub_ListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        sub_context = context;
    }

    // 指定データのビューを取得
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        int id = ((Cursor) getItem(position)).getInt(0);

        ContentValues values = new ContentValues();

        // 削除ボタン オブジェクトを取得
        ImageButton btnDel = (ImageButton) view.findViewById(R.id.button_delete);
        ImageButton btnUpd = (ImageButton) view.findViewById(R.id.setting);
        SwitchCompat toggleSwitch = (SwitchCompat) view.findViewById(R.id.toggle_switch);

        // ボタンにリスト内の位置を設定
        btnDel.setTag(position);
        btnUpd.setTag(position);
        toggleSwitch.setTag(position);

        helper = new DatabaseHelper(sub_context);

        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    values.put(DBEntry_2.flag, 1);
//                    Log.d("MainActivity", "ON");
                } else if(!isChecked) {
//                    Log.d("MainActivity", "OFF");
                    values.put(DBEntry_2.flag, 0);
                }
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    db.update(DBEntry_2.Table_Name, values, DBEntry_2._ID+" = ?", new String[] {String.valueOf(id)});
                }
            }
        });

        return view;
    }
}
