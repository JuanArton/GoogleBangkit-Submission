package com.juanarton.githubapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.ID
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.LOGIN
import com.juanarton.githubapp.database.DatabaseContract.FavUserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {

    companion object {
        private const val DATABASE_NAME = "dbfavlist"
        private const val DATABASE_VERSION = 1
        private const val CREATE_FAV_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $LOGIN TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FAV_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}