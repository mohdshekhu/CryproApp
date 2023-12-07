package com.example.cryptoapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(private var context: Context)
    : SQLiteOpenHelper(context,"WatchListDATABASE",null,1) {

    val DB_NAME = "WatchListDATABASE"
    var DB_VERSION = 1
    val TABLE_NAME = "FavTable"
    val uId = "uniqueID"
    val id = "CoinID"
    val isFav = "IsFav"
    var query = "CREATE TABLE "+TABLE_NAME+"("+uId+" INTEGER PRIMARY KEY, "+id+" INTEGER UNIQUE, "+isFav+" BOOLEAN )"


    override fun onCreate(p0: SQLiteDatabase?) {

        p0!!.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        var s = "Drop TABLE IF EXISTS $TABLE_NAME"
        p0!!.execSQL(s)
        p0.execSQL(query)

    }

    fun addFav(id:Int,isFav:Boolean){
        val db= this.writableDatabase
        var cv=ContentValues()
        cv.put(this.id,id)
        cv.put(this.isFav,isFav)
        db.insert(this.TABLE_NAME,null , cv)
        db.close()
    }

    fun isAvail(id: Int):Boolean{
        val db = this.readableDatabase
        var curs = db.rawQuery("Select 1 from $TABLE_NAME where ${this.id} = $id" , null)
        return curs.moveToFirst()
    }

    fun delete(id : Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME where ${this.id} = $id"
        db.execSQL(query)
    }
}