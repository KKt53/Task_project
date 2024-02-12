package com.example.task_project;

import android.provider.BaseColumns;

public class Parent_class {

    private Parent_class(){}

    public static class DBEntry implements BaseColumns{
        public static final String Table_Name = "Parent";
        public static final String Name = "Name";
    }

}
