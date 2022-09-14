package com.football22tour.worldtest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.provider.BaseColumns

class DataEntry(private val mainActivity: MainActivity) {
    fun runDataEntry()  {
        val theImages = arrayOf(R.drawable.qatar, R.drawable.usa, R.drawable.australia, R.drawable.saudi_arabia, R.drawable.japan, R.drawable.korea_republic, R.drawable.wales,
            R.drawable.canada, R.drawable.spain, R.drawable.cameroon, R.drawable.senegal, R.drawable.serbia, R.drawable.poland, R.drawable.switzerland, R.drawable.ecuador, R.drawable.netherlands,
            R.drawable.england, R.drawable.mexico, R.drawable.tunisia, R.drawable.morocco, R.drawable.denmark, R.drawable.ir_iran, R.drawable.costa_rica, R.drawable.ghana,
            R.drawable.argentina, R.drawable.brazil, R.drawable.germany, R.drawable.belgium, R.drawable.portugal, R.drawable.uruguay, R.drawable.france, R.drawable.croatia)
        if (!checkDataBase()) {
            //App opened for the very first time install database
            val db = TheDatabase(mainActivity.applicationContext).writableDatabase
            val theNames = arrayOf("Qatar", "USA", "Australia", "Saudi Arabia", "Japan", "Korea Republic", "Wales", "Canada",
                "Spain","Cameroon", "Senegal", "Serbia", "Poland", "Switzerland", "Ecuador", "Netherlands",
                "England", "Mexico", "Tunisia", "Morocco", "Denmark", "IR Iran", "Costa Rica", "Ghana",
                "Argentina", "Brazil", "Germany", "Belgium", "Portugal", "Uruguay", "France", "Croatia")
            val theLevels = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3)

            for(i in 0..31){
                val theData = ContentValues().apply {
                    put(Main.FeedEntry.NAME, theNames[i])
                    put(Main.FeedEntry.STRENGTH, theLevels[i])
                    put(Main.FeedEntry.WINS, 0)
                    put(Main.FeedEntry.IMAGES, theImages[i])
                }
                db.insert(Main.FeedEntry.TABLE_NAME, null, theData)
            }
            db.close()
        }else{
            //Just fix the images
            val db = TheDatabase(mainActivity.applicationContext).writableDatabase
            for(i in 0..31){
                val theData = ContentValues().apply {
                    put(Main.FeedEntry.IMAGES, theImages[i])
                }
                db.update(Main.FeedEntry.TABLE_NAME, theData, "${BaseColumns._ID} = ${i+1}", null)
            }
            db.close()
        }

        val ourSharedPrefs = "initial_shared_prefs"
        val sharedPrefsCorrectImages = "correct_images"

        val sharedPrefs = mainActivity.getSharedPreferences(ourSharedPrefs, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(sharedPrefsCorrectImages, true)
        editor.apply()
    }

    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            checkDB = SQLiteDatabase.openDatabase(mainActivity.getDatabasePath("football22tour_worldtest.db").toString(), null, SQLiteDatabase.OPEN_READONLY)
            checkDB.close()
        } catch (e: SQLiteException) {
            // database doesn't exist yet.
        }
        return checkDB != null
    }
}