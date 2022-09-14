package com.football22tour.worldtest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

object Main{
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "main"
        const val  NAME = "name"
        const val STRENGTH = "strength"
        const val WINS = "wins"
        const val IMAGES = "images"
    }
}

object Groups{
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "group_stage"
        const val TOURNAMENT_SHARED_PREFS = "tournament_shared_prefs"
        const val  TEAM = "team"
        const val MP = "mp"
        const val W = "w"
        const val D = "d"
        const val L = "l"
        const val POINTS = "points"
        const val GROUP = "the_group"
        const val CURSOR = "shared_prefs_the_cursor_pointer"
        const val IS_KNOCKOUT = "shared_prefs_is_knockout"
    }
}

object GroupGames{
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "group_games"
        const val  TEAM_A = "team_a"
        const val  TEAM_B = "team_b"
        const val GROUP = "which_group"
    }
}

object KnockOut{
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "knockout"
        const val  A = "a"
        const val B = "b"
    }
}

object TempTeams{
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "knockout_temp_teams"
        const val  TEAM = "team"
    }
}

private const val SQL_CREATE_MAIN =
    "CREATE TABLE ${Main.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${Main.FeedEntry.NAME} TEXT," +
            "${Main.FeedEntry.STRENGTH} INT," +
            "${Main.FeedEntry.WINS} INT," +
            "${Main.FeedEntry.IMAGES} INT)"

private const val SQL_CREATE_GROUPS =
    "CREATE TABLE ${Groups.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${Groups.FeedEntry.TEAM} INT," +
            "${Groups.FeedEntry.MP} INT," +
            "${Groups.FeedEntry.W} INT," +
            "${Groups.FeedEntry.D} INT," +
            "${Groups.FeedEntry.L} INT," +
            "${Groups.FeedEntry.POINTS} INT," +
            "${Groups.FeedEntry.GROUP} INT)"

private const val SQL_CREATE_GROUP_GAMES =
    "CREATE TABLE ${GroupGames.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${GroupGames.FeedEntry.TEAM_A} INT," +
            "${GroupGames.FeedEntry.TEAM_B} INT," +
            "${GroupGames.FeedEntry.GROUP} INT)"

private const val SQL_CREATE_KNOCKOUT =
    "CREATE TABLE ${KnockOut.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${KnockOut.FeedEntry.A} INT," +
            "${KnockOut.FeedEntry.B} INT)"

private const val SQL_CREATE_KNOCKOUT_TEMP_TEAMS =
    "CREATE TABLE ${TempTeams.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${TempTeams.FeedEntry.TEAM} INT)"

private const val SQL_DELETE_MAIN =
    "DROP TABLE IF EXISTS  ${Main.FeedEntry.TABLE_NAME}"
private const val SQL_DELETE_GROUPS =
    "DROP TABLE IF EXISTS  ${Groups.FeedEntry.TABLE_NAME}"
private const val SQL_DELETE_GROUP_GAMES =
    "DROP TABLE IF EXISTS  ${GroupGames.FeedEntry.TABLE_NAME}"
private const val SQL_DELETE_KNOCKOUT =
    "DROP TABLE IF EXISTS  ${KnockOut.FeedEntry.TABLE_NAME}"
private const val SQL_DELETE_TEMP_TEAMS =
    "DROP TABLE IF EXISTS  ${TempTeams.FeedEntry.TABLE_NAME}"

class TheDatabase(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null , DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_MAIN)
        db?.execSQL(SQL_CREATE_GROUPS)
        db?.execSQL(SQL_CREATE_GROUP_GAMES)
        db?.execSQL(SQL_CREATE_KNOCKOUT)
        db?.execSQL(SQL_CREATE_KNOCKOUT_TEMP_TEAMS)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_MAIN)
        db?.execSQL(SQL_DELETE_GROUPS)
        db?.execSQL(SQL_DELETE_GROUP_GAMES)
        db?.execSQL(SQL_DELETE_KNOCKOUT)
        db?.execSQL(SQL_DELETE_TEMP_TEAMS)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onDowngrade(db, oldVersion, newVersion)
    }
    companion object{
        const val DATABASE_NAME = "football22tour_worldtest.db"
        const val DATABASE_VERSION = 1
    }
    fun completeData(mainActivity: MainActivity){
        val names = arrayListOf<String>()
        val images = arrayListOf<Int>()
        val db  = this.readableDatabase
        val queryString = "SELECT ${Main.FeedEntry.NAME}, ${Main.FeedEntry.IMAGES} FROM ${Main.FeedEntry.TABLE_NAME} ORDER BY ${Main.FeedEntry.STRENGTH} ASC;"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        if (R.drawable.qatar != cursor.getInt(1)){
            val sharedPrefsCorrectImages = "correct_images"
            val ourSharedPrefs = "initial_shared_prefs"
            val sharedPrefs = mainActivity.getSharedPreferences(ourSharedPrefs, Context.MODE_PRIVATE)
            val editor1 = sharedPrefs.edit()
            editor1.putBoolean(sharedPrefsCorrectImages, false)
            editor1.apply()
            mainActivity.restartActivity()
        }
        for (i in 0..31){
            names.add(cursor.getString(0))
            images.add(cursor.getInt(1))
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        mainActivity.teamNames = names.toTypedArray()
        mainActivity.teamImages = images.toIntArray()
    }
    fun getStatisticsWins(statisticsActivity: StatisticsActivity): IntArray{
        val wins = arrayListOf<Int>()
        val theIndex = arrayListOf<Int>()
        val db  = this.readableDatabase
        val queryString = "SELECT ${Main.FeedEntry.WINS}, ${BaseColumns._ID} FROM ${Main.FeedEntry.TABLE_NAME} ORDER BY ${Main.FeedEntry.WINS} DESC, ${Main.FeedEntry.NAME} ASC;"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        for (i in 0..31){
            wins.add(cursor.getInt(0))
            theIndex.add(cursor.getInt(1)-1)
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        statisticsActivity.theIndex = theIndex.toIntArray()
        return wins.toIntArray()
    }
    fun getTournamentWins(): IntArray{
        val wins = arrayListOf<Int>()
        val db  = this.readableDatabase
        val queryString = "SELECT ${Main.FeedEntry.WINS} FROM ${Main.FeedEntry.TABLE_NAME};"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        for (i in 0..31){
            wins.add(cursor.getInt(0))
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return wins.toIntArray()
    }
    fun resetSavedGameDetails(){
        val dbEditor = this.writableDatabase
        dbEditor.execSQL("delete from ${Groups.FeedEntry.TABLE_NAME}")
        dbEditor.execSQL("delete from ${GroupGames.FeedEntry.TABLE_NAME}")
        dbEditor.execSQL("delete from ${KnockOut.FeedEntry.TABLE_NAME}")
        dbEditor.execSQL("delete from ${TempTeams.FeedEntry.TABLE_NAME}")
        dbEditor.close()

    }
    fun getKnockoutGames(tournamentActivity: TournamentActivity){
        val theTeams1 = arrayListOf<IntArray>()
        var theTeams2: ArrayList<IntArray>? = null
        var theTeams3: ArrayList<IntArray>? = null
        var theTeams4: ArrayList<IntArray>? = null
        var top4: ArrayList<IntArray>? = null
        val db  = this.readableDatabase
        val queryString = "SELECT ${KnockOut.FeedEntry.A}, ${KnockOut.FeedEntry.B} FROM ${KnockOut.FeedEntry.TABLE_NAME};"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        val theSize = cursor.count
        if (theSize>=9){
            theTeams2 = arrayListOf()
        }
        if (theSize>=13){
            theTeams3 = arrayListOf()
        }
        if (theSize>=15){
            theTeams4 = arrayListOf()
        }
        if (theSize>=17){
            top4 = arrayListOf()
        }

        for (i in 0 until cursor.count){
            if (i>7){
                //It's not stage 1
                if (i>11){
                    //It's not stage 2
                    if (i>13){
                        //It's not stage 3 it's either 3rd position or finals
                        if (i>15){
                            top4!!.add(intArrayOf(cursor.getInt(0), cursor.getInt(1)))
                        }else{
                            //It's 3rd position
                            theTeams4!!.add(intArrayOf(cursor.getInt(0), cursor.getInt(1)))
                        }
                    }else{
                        //It's round of 4
                        theTeams3!!.add(intArrayOf(cursor.getInt(0), cursor.getInt(1)))
                    }
                }else{
                    //It's round of 8
                    theTeams2!!.add(intArrayOf(cursor.getInt(0), cursor.getInt(1)))
                }
            }else{
                //It's round of 16
                theTeams1.add(intArrayOf(cursor.getInt(0), cursor.getInt(1)))
            }
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        tournamentActivity.knockOutStageTeams1 = theTeams1.toTypedArray()
        if (theTeams2 != null){
            tournamentActivity.knockOutStageTeams2 = theTeams2.toTypedArray()
        }
        if (theTeams3 != null){
            tournamentActivity.knockOutStageTeams3 = theTeams3.toTypedArray()
        }
        if (theTeams4 != null){
            tournamentActivity.knockOutStageTeams4 = theTeams4.toTypedArray()
        }
        if (top4 != null){
            tournamentActivity.top4Arrangement = top4.toTypedArray()
        }
    }
    fun getKnockoutTempTeams(): ArrayList<Int>?{
        val db  = this.readableDatabase
        val theTeams = arrayListOf<Int>()
        val queryString = "SELECT ${TempTeams.FeedEntry.TEAM} FROM ${TempTeams.FeedEntry.TABLE_NAME};"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()

        if (cursor.count != 0){
        for(i in 0 until cursor.count){
            theTeams.add(cursor.getInt(0))
            cursor.moveToNext()
        }}else{
            cursor.close()
            db.close()
            return null
        }
        cursor.close()
        db.close()
        return theTeams
    }
    fun clearKnockout(){
        val dbEditor = this.writableDatabase
        dbEditor.execSQL("delete from ${KnockOut.FeedEntry.TABLE_NAME}")
    }
    fun clearTemp(){
        val dbEditor = this.writableDatabase
        dbEditor.execSQL("delete from ${TempTeams.FeedEntry.TABLE_NAME}")
    }
    fun resetStatistics(){
        val db = this.writableDatabase
        for(i in 0..31){
            val theData = ContentValues().apply {
                put(Main.FeedEntry.WINS, 0)
            }
            db.update(Main.FeedEntry.TABLE_NAME, theData, "${BaseColumns._ID} = ${i+1}", null)
        }
        db.close()
    }
    fun getGroupsAndTeams(): Array<Array<IntArray>>{
        val group0 = arrayListOf<IntArray>()
        val group1 = arrayListOf<IntArray>()
        val group2 = arrayListOf<IntArray>()
        val group3 = arrayListOf<IntArray>()
        val group4 = arrayListOf<IntArray>()
        val group5 = arrayListOf<IntArray>()
        val group6 = arrayListOf<IntArray>()
        val group7 = arrayListOf<IntArray>()
        val db  = this.readableDatabase
        val queryString = "SELECT ${Groups.FeedEntry.TEAM}, ${Groups.FeedEntry.MP}, ${Groups.FeedEntry.W}, ${Groups.FeedEntry.D}, ${Groups.FeedEntry.L}, ${Groups.FeedEntry.POINTS}, ${Groups.FeedEntry.GROUP} FROM ${Groups.FeedEntry.TABLE_NAME};"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        for (i in 0..31){
            when(cursor.getInt(6)){
                0 ->{
                    group0.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                1 ->{
                    group1.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                2 ->{
                    group2.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                3 ->{
                    group3.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                4 ->{
                    group4.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                5 ->{
                    group5.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                6 ->{
                    group6.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
                7 ->{
                    group7.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)))
                }
            }
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return arrayOf(group0.toTypedArray(), group1.toTypedArray(), group2.toTypedArray(), group3.toTypedArray(), group4.toTypedArray(), group5.toTypedArray(), group6.toTypedArray(), group7.toTypedArray())
    }
    fun getGroupGames(): Array<IntArray>{
        val theGames = arrayListOf<IntArray>()
        val db  = this.readableDatabase
        val queryString = "SELECT ${GroupGames.FeedEntry.TEAM_A}, ${GroupGames.FeedEntry.TEAM_B}, ${GroupGames.FeedEntry.GROUP} FROM ${GroupGames.FeedEntry.TABLE_NAME};"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        for (i in 0..47){
            theGames.add(intArrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)))
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return theGames.toTypedArray()
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}