package com.example.task_project;

import android.provider.BaseColumns;

public class Child_class {

    private Child_class(){}

    public static class DBEntry_2 implements BaseColumns{
        public static final String Table_Name = "Child";
        public static final String Name = "Name";
        public static final String flag = "Flag";
        public static final String category = "Category";
        public static final String parent = "Parent";
        public static final String child = "Child";
        public static final String Date = "Date";
        public static final String Data_flag = "Data_flag";
        public static final String prerequisite = "prerequisite";
        public static final String ranking = "ranking";
    }

}
