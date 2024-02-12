package com.example.task_project;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Priority_adapter extends SimpleCursorAdapter {
    // コンストラクタ
    public Priority_adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    // 指定データのビューを取得
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView number = (TextView) view.findViewById(R.id.number);

        number.setText(Integer.toString(position + 1));

        return view;
    }
}
