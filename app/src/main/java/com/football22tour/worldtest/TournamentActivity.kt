package com.football22tour.worldtest

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.football22tour.worldtest.adapters.IndexArraySorting
import com.football22tour.worldtest.dialogs.OnKnockOutDialog
import com.football22tour.worldtest.dialogs.UndoResultsDialog
import com.football22tour.worldtest.fragments.GameFragment
import com.football22tour.worldtest.fragments.KnockoutFragment
import com.football22tour.worldtest.fragments.StandingsFragment
import java.util.*
import kotlin.collections.ArrayList

const val ALL_NAMES = "tournament_names"
const val ALL_TEAM_IMAGES = "all_team_images"
const val ALL_GROUPS = "all_groups"
const val ALL_GAMES = "all_games"
const val GAME_POINTER = "game_pointer"
const val IS_KNOCKOUT = "is_knockout"
const val LATE_RESULTS = "late_results"
const val LATE_RESULTS_EXISTS = "late_results_exists"
private const val PREVIOUS_VALUE_A = "previous_value_a"
private const val PREVIOUS_VALUE_B = "previous_value_b"
private const val PREVIOUS_TEAMS_INDEX = "previous_teams_index"

const val KNOCKOUT_STAGE_TEAMS_1 = "knockout_stage_teams_1"
const val KNOCKOUT_STAGE_TEAMS_2 = "knockout_stage_teams_2"
const val KNOCKOUT_STAGE_TEAMS_3 = "knockout_stage_teams_3"
const val KNOCKOUT_STAGE_TEAMS_4 = "knockout_stage_teams_4"
const val TOP_4_ARRANGEMENT = "knockout_stage_4"
const val KNOCKOUT_WINNERS = "knockout_winners"
const val KNOCKOUT_LOSERS = "knockout_losers"
const val MAIN_POINTS_ADDITION = "main_points_addition"

class TournamentActivity : AppCompatActivity() {
    private val groupNames = arrayOf("A", "B", "C", "D", "E", "F", "G", "H")
    private lateinit var navView: BottomNavigationView

    lateinit var allTeamNames: Array<String>
    lateinit var allTeamImages: IntArray
    lateinit var allGroups: Array<Array<IntArray>>
    lateinit var allGames: Array<IntArray>

    var knockOutStageTeams1: Array<IntArray>? = null
    var knockOutStageTeams2: Array<IntArray>? = null
    var knockOutStageTeams3: Array<IntArray>? = null
    var knockOutStageTeams4: Array<IntArray>? = null
    var top4Arrangement: Array<IntArray>? = null
    private var lastKnockoutTeams: Array<Int>? = null

    private var knockoutStageWinners = arrayListOf<Int>()
    private var knockoutStageLosers = arrayListOf<Int>()

    private var previousValueA: Array<Int>? = null
    private var previousValueB: Array<Int>? = null
    private var previousTeamsIndex: IntArray? = null
    var gamePointer = 0
    var isKnockOut = false
    var lastResults: Int? = null

    private var mainPointsAddition = IntArray(32)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            val bundle = this.intent.extras
            allTeamNames = bundle!!.getStringArray("names") as Array<String>
            allTeamImages = bundle.getIntArray("images")!!
            val resumeGame = bundle.getBoolean("resume")
            if (resumeGame){
                val dbInstance = TheDatabase(this)
                allGroups = dbInstance.getGroupsAndTeams()
                allGames = dbInstance.getGroupGames()
                val sharedPrefs = getSharedPreferences(Groups.FeedEntry.TOURNAMENT_SHARED_PREFS, Context.MODE_PRIVATE)
                gamePointer = sharedPrefs.getInt(Groups.FeedEntry.CURSOR, 0)
                isKnockOut = sharedPrefs.getBoolean(Groups.FeedEntry.IS_KNOCKOUT, false)
                if (isKnockOut){
                    val ourDB = TheDatabase(this)
                    ourDB.getKnockoutGames(this)
                    val theOutput = ourDB.getKnockoutTempTeams()
                    if (theOutput!= null){
                        if(gamePointer >=60){
                            val theMinimum = theOutput.size-3
                            for (i in 0 until theOutput.size){
                                if (i >= theMinimum){
                                    knockoutStageLosers.add(theOutput[i])
                                }else{
                                    knockoutStageWinners.add(theOutput[i])
                                }
                            }
                        }else{
                            knockoutStageWinners = theOutput
                        }
                    }
                }
            }else{
                startNewGame()
            }
        }else{
            allTeamNames = savedInstanceState.getStringArray(ALL_NAMES)!!
            allTeamImages = savedInstanceState.getIntArray(ALL_TEAM_IMAGES)!!
            allGroups = savedInstanceState.getSerializable(ALL_GROUPS) as Array<Array<IntArray>>
            allGames = savedInstanceState.getSerializable(ALL_GAMES) as Array<IntArray>
            mainPointsAddition = savedInstanceState.getIntArray(MAIN_POINTS_ADDITION)!!
            gamePointer = savedInstanceState.getInt(GAME_POINTER)
            isKnockOut = savedInstanceState.getBoolean(IS_KNOCKOUT)
            if (savedInstanceState.getBoolean(LATE_RESULTS_EXISTS)){
                lastResults = savedInstanceState.getInt(LATE_RESULTS)
                previousValueA = savedInstanceState.getIntArray(PREVIOUS_VALUE_A)?.toTypedArray()
                previousValueB = savedInstanceState.getIntArray(PREVIOUS_VALUE_B)?.toTypedArray()
                previousTeamsIndex = savedInstanceState.getIntArray(PREVIOUS_TEAMS_INDEX)
            }

            if (isKnockOut){
                knockOutStageTeams1 = savedInstanceState.getSerializable(KNOCKOUT_STAGE_TEAMS_1) as Array<IntArray>
                knockOutStageTeams2 = savedInstanceState.getSerializable(KNOCKOUT_STAGE_TEAMS_2) as Array<IntArray>
                knockOutStageTeams3 = savedInstanceState.getSerializable(KNOCKOUT_STAGE_TEAMS_3) as Array<IntArray>
                knockOutStageTeams4 = savedInstanceState.getSerializable(KNOCKOUT_STAGE_TEAMS_4) as Array<IntArray>
                top4Arrangement = savedInstanceState.getSerializable(TOP_4_ARRANGEMENT) as Array<IntArray>
                knockoutStageWinners = savedInstanceState.getIntegerArrayList(KNOCKOUT_WINNERS) as ArrayList<Int>
                knockoutStageLosers = savedInstanceState.getIntegerArrayList(KNOCKOUT_LOSERS) as ArrayList<Int>
            }
        }
        //We first create our fragments and insert the appropriate data
        setContentView(R.layout.activity_tournament)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_tournament)
        navView.setupWithNavController(navController)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isKnockOut){
            outState.putSerializable(KNOCKOUT_STAGE_TEAMS_1, knockOutStageTeams1)
            outState.putSerializable(KNOCKOUT_STAGE_TEAMS_2, knockOutStageTeams2)
            outState.putSerializable(KNOCKOUT_STAGE_TEAMS_3, knockOutStageTeams3)
            outState.putSerializable(KNOCKOUT_STAGE_TEAMS_4, knockOutStageTeams4)
            outState.putSerializable(TOP_4_ARRANGEMENT, top4Arrangement)
            outState.putSerializable(KNOCKOUT_WINNERS, knockoutStageWinners)
            outState.putSerializable(KNOCKOUT_LOSERS, knockoutStageLosers)
        }
        if (lastResults != null){
            outState.putBoolean(LATE_RESULTS_EXISTS, true)
            outState.putInt(LATE_RESULTS, lastResults!!)
            outState.putIntArray(PREVIOUS_VALUE_A, previousValueA?.toIntArray())
            outState.putIntArray(PREVIOUS_VALUE_B, previousValueB?.toIntArray())
            outState.putIntArray(PREVIOUS_TEAMS_INDEX, previousTeamsIndex)
        }else{
            outState.putBoolean(LATE_RESULTS_EXISTS, false)
        }
        outState.putStringArray(ALL_NAMES, allTeamNames)
        outState.putIntArray(ALL_TEAM_IMAGES, allTeamImages)
        outState.putSerializable(ALL_GROUPS, allGroups)
        outState.putSerializable(ALL_GAMES, allGames)
        outState.putInt(GAME_POINTER, gamePointer)
        outState.putBoolean(IS_KNOCKOUT, isKnockOut)
        outState.putIntArray(MAIN_POINTS_ADDITION, mainPointsAddition)
    }
    override fun onPause() {
        if (lastResults != null){
            updateGroupStageMainAdditions(lastResults!!, gamePointer-1)
            lastResults = null
            previousTeamsIndex = null
            previousValueA = null
            previousValueB = null
        }
        updateSavedPointerToSharedPref()
        updateMainPoints()
        if (!isKnockOut){
            updateGroupStageOnDatabase()
        }else{
            updateKnockoutOnDatabase()
        }
        mainPointsAddition = IntArray(32)
        super.onPause()
    }
    override fun onBackPressed() {
        if (lastResults != null){
            if (navView.selectedItemId == R.id.navigation_game){
                //Ask for user if they would like to undo
                val theFragment = (findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_tournament)).getFragment<GameFragment>()
                val lastPointer = gamePointer-1
                UndoResultsDialog(layoutInflater, this).displayDialog("Undo Results for ${allTeamNames[allGames[lastPointer][0]]} vs ${allTeamNames[allGames[lastPointer][1]]}", theFragment)
            }else{
                //Save first then close activity
                saveAndCloseActivity()
            }
        }else{
            updateSavedPointerToSharedPref()
            updateMainPoints()
            if (!isKnockOut){
                updateGroupStageOnDatabase()
            }else{
                updateKnockoutOnDatabase()
            }
            mainPointsAddition = IntArray(32)
            super.onBackPressed()
        }
    }
    fun gamePlayed(theResults: Int, gameFragment: GameFragment){
        if (!isKnockOut){
            if (lastResults == null){
                lastResults = theResults
            }else{
                updateGroupStageMainAdditions(lastResults!!, gamePointer-1)
                lastResults = theResults
            }
            updateChanges(gamePointer,theResults)
        }else {
            if(gamePointer< 64){
                if (gamePointer<62){
                    if (gamePointer<60){
                        if (gamePointer<56){
                            //48-55
                            knockoutStageWinners.add(knockOutStageTeams1!![gamePointer - 48][theResults])
                        }else{
                            //56-59
                            knockoutStageWinners.add(knockOutStageTeams2!![gamePointer - 56][theResults])
                        }
                    }else{
                        //60 and 61
                        val theLoser = if (theResults == 0){1}else{0}
                        knockoutStageWinners.add(knockOutStageTeams3!![gamePointer - 60][theResults])
                        knockoutStageLosers.add(knockOutStageTeams3!![gamePointer - 60][theLoser])
                    }
                }else{
                    //62 and 63
                    val theLoser = if (theResults == 0){1}else{0}
                    if (gamePointer == 62){
                        //62
                        top4Arrangement = arrayOf(intArrayOf(knockoutStageLosers[theLoser], knockoutStageLosers[theResults]))
                    }else{
                        //63
                        top4Arrangement = arrayOf(top4Arrangement!![0], intArrayOf(knockoutStageWinners[theLoser], knockoutStageWinners[theResults]))
                    }
                }
            }else{
                //Game ended
            }
                //This is where we update main database
                if (theResults == 0){
                    mainPointsAddition[lastKnockoutTeams!![0]]++
                }else{
                    mainPointsAddition[lastKnockoutTeams!![1]]++
                }
        }
        gamePointer++
        updatePointer(gamePointer)
        whichGameChecker(gameFragment)
    }
    fun whichGameChecker(gameFragment: GameFragment){
        if (gamePointer <=47){
            //Group stage game
            val roundOrGroupText = "Group ${groupNames[allGames[gamePointer][2]]} match"
            gameFragment.updateTheValues(allTeamNames[allGames[gamePointer][0]], allTeamNames[allGames[gamePointer][1]], allTeamImages[allGames[gamePointer][0]], allTeamImages[allGames[gamePointer][1]], false, roundOrGroupText)
        }else{
            //Knockout game
            if (gamePointer <= 55){
                //Knockout stage 1
                if (gamePointer == 48){
                    generateKnockoutStage1()
                }
                val roundOrGroupText = "1/16"
                val currentPointer = gamePointer - 48
                getKnockoutTeamInfo(knockOutStageTeams1!![currentPointer][0], knockOutStageTeams1!![currentPointer][1], roundOrGroupText, gameFragment)
            }else{
                if (gamePointer <= 59){
                    //Knockout stage 2
                    if (gamePointer == 56){
                        generateKnockoutStage2()
                    }
                    val roundOrGroupText = "1/8"
                    val currentPointer = gamePointer - 56
                    getKnockoutTeamInfo(knockOutStageTeams2!![currentPointer][0], knockOutStageTeams2!![currentPointer][1], roundOrGroupText, gameFragment)
                }else{
                    if(gamePointer <= 61){
                        //Knockout stage 3
                        if (gamePointer == 60){
                            generateKnockoutStage3()
                        }
                        val roundOrGroupText = "1/4"
                        val currentPointer = gamePointer - 60
                        getKnockoutTeamInfo(knockOutStageTeams3!![currentPointer][0], knockOutStageTeams3!![currentPointer][1], roundOrGroupText, gameFragment)
                    }else{
                        if (gamePointer == 62){
                            //Third Position
                            generateKnockoutStage4()
                            val roundOrGroupText = "3rd Position"
                            getKnockoutTeamInfo(knockOutStageTeams4!![0][0], knockOutStageTeams4!![0][1], roundOrGroupText, gameFragment)
                        }else{
                        if(gamePointer == 63){
                            //Finals
                            val roundOrGroupText = "Finals"
                            getKnockoutTeamInfo(knockOutStageTeams4!![1][0], knockOutStageTeams4!![1][1], roundOrGroupText, gameFragment)
                        }else{
                            //Tournament ended
                            gameOver(gameFragment)
                        }}
                    }
                }
            }
        }
    }
    private fun updatePointer(latestOne: Int){
        val sharedPrefs = getSharedPreferences(Groups.FeedEntry.TOURNAMENT_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putInt(Groups.FeedEntry.CURSOR, latestOne)
        editor.apply()
    }
    private fun updateIsKnockoutToTrue(){
        val sharedPrefs = getSharedPreferences(Groups.FeedEntry.TOURNAMENT_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(Groups.FeedEntry.IS_KNOCKOUT, true)
        editor.apply()
    }
    private fun revertChanges(){
            if (previousTeamsIndex != null){
                allGroups[previousTeamsIndex!![0]][previousTeamsIndex!![1]] = previousValueA!!.toIntArray()
                allGroups[previousTeamsIndex!![0]][previousTeamsIndex!![2]] = previousValueB!!.toIntArray()
            }
            previousTeamsIndex = null
            previousValueA = null
            previousValueB = null
    }
    private fun updateChanges(thePointer: Int, theResults: Int){
        var teamAIndex = 0
        var teamBIndex = 0
        val teamA = allGames[thePointer][0]
        val teamB = allGames[thePointer][1]
        val theGroup = allGames[thePointer][2]

        for (i in 0..3){
            if (teamA == allGroups[theGroup][i][0]){
                teamAIndex = i
            }
            if (teamB == allGroups[theGroup][i][0]){
                teamBIndex = i
            }
        }
            updateStandingsValues(theGroup, teamAIndex, teamBIndex, theResults)
    }
    private fun updateStandingsValues(group: Int, teamAIndex: Int, teamBIndex: Int, results: Int){
        previousValueA = allGroups[group][teamAIndex].toTypedArray()
        previousValueB = allGroups[group][teamBIndex].toTypedArray()
        allGroups[group][teamAIndex][1]++
        allGroups[group][teamBIndex][1]++
        previousTeamsIndex = intArrayOf(group, teamAIndex, teamBIndex)
        when(results){
            0->{
                allGroups[group][teamAIndex][2]++
                allGroups[group][teamBIndex][4]++
                allGroups[group][teamAIndex][5]+=3
            }
            1->{
                allGroups[group][teamBIndex][2]++
                allGroups[group][teamAIndex][4]++
                allGroups[group][teamBIndex][5]+=3
            }
            2->{
                allGroups[group][teamAIndex][5]++
                allGroups[group][teamBIndex][5]++
                allGroups[group][teamAIndex][3]++
                allGroups[group][teamBIndex][3]++
            }
        }
    }
    private fun updateGroupStageMainAdditions(theData: Int, thePointer: Int){
        if (theData !=2){
            //This is where we update main database
            if (theData == 0){
                mainPointsAddition[allGames[thePointer][0]]++
            }else{
                mainPointsAddition[allGames[thePointer][1]]++
            }
        }
    }
    fun saveAndCloseActivity(){
        if (!isKnockOut){
            if (lastResults != null){
                updateGroupStageMainAdditions(lastResults!!, gamePointer-1)
                lastResults = null
            }
            updateGroupStageOnDatabase()
        }else{
            updateKnockoutOnDatabase()
        }
        updateSavedPointerToSharedPref()
        updateMainPoints()
        mainPointsAddition = IntArray(32)
        finish()
    }
    fun undoResults(gameFragment: GameFragment){
        gamePointer--
        lastResults = null
        updateSavedPointerToSharedPref()
        whichGameChecker(gameFragment)
        revertChanges()
    }
    private fun updateSavedPointerToSharedPref(){
        val sharedPrefs = getSharedPreferences(Groups.FeedEntry.TOURNAMENT_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putInt(Groups.FeedEntry.CURSOR, gamePointer)
        editor.apply()
    }
    private fun generateKnockoutStage1(){
        val theSucceedingTeams = arrayListOf<Int>()
        if (lastResults != null){
            updateGroupStageMainAdditions(lastResults!!, gamePointer-1)
            lastResults = null
        }
        for (i in 0..7){
            val comparator = IndexArraySorting(intArrayOf(allGroups[i][0][5], allGroups[i][1][5], allGroups[i][2][5], allGroups[i][3][5]))
            val theFinalArray: Array<Int> = comparator.createIndexArray()
            Arrays.sort(theFinalArray, comparator)
            val myTempArray = arrayOf(allGroups[i][0][0], allGroups[i][1][0], allGroups[i][2][0], allGroups[i][3][0])
            theSucceedingTeams.add(myTempArray[theFinalArray[3]])
            theSucceedingTeams.add(myTempArray[theFinalArray[2]])
        }
        knockOutStageTeams1 = arrayOf(intArrayOf(theSucceedingTeams[0], theSucceedingTeams[15]), intArrayOf(theSucceedingTeams[2], theSucceedingTeams[5]), intArrayOf(theSucceedingTeams[6], theSucceedingTeams[9]), intArrayOf(theSucceedingTeams[10], theSucceedingTeams[13]), intArrayOf(theSucceedingTeams[14], theSucceedingTeams[11]), intArrayOf(theSucceedingTeams[1], theSucceedingTeams[4]), intArrayOf(theSucceedingTeams[3], theSucceedingTeams[8]), intArrayOf(theSucceedingTeams[7], theSucceedingTeams[12]))
        if (knockoutStageWinners.isNotEmpty()){
            knockoutStageWinners.clear()
        }
        if (knockoutStageLosers.isNotEmpty()){
            knockoutStageLosers.clear()
            logger("Clear 1")
        }
        OnKnockOutDialog(layoutInflater, this).displayWarning()
        updateGroupStageOnDatabase()
        isKnockOut = true
        updateIsKnockoutToTrue()
    }
    private fun generateKnockoutStage2(){
        knockOutStageTeams2 = arrayOf(intArrayOf(knockoutStageWinners[0], knockoutStageWinners[1]), intArrayOf(knockoutStageWinners[2], knockoutStageWinners[3]), intArrayOf(knockoutStageWinners[4], knockoutStageWinners[5]), intArrayOf(knockoutStageWinners[6], knockoutStageWinners[7]))
        if (knockoutStageWinners.isNotEmpty()){
            knockoutStageWinners.clear()
        }
        if (knockoutStageLosers.isNotEmpty()){
            knockoutStageLosers.clear()
            logger("Clear 2")
        }
    }
    private fun generateKnockoutStage3(){
        knockOutStageTeams3 = arrayOf(intArrayOf(knockoutStageWinners[0], knockoutStageWinners[1]), intArrayOf(knockoutStageWinners[2], knockoutStageWinners[3]))
        if (knockoutStageWinners.isNotEmpty()){
            knockoutStageWinners.clear()
        }
        if (knockoutStageLosers.isNotEmpty()){
            knockoutStageLosers.clear()
            logger("Clear 3")
        }
    }
    private fun generateKnockoutStage4(){
        logger("Game pointer is $gamePointer")
        logger("Size is ${knockoutStageLosers.size}}")
        knockOutStageTeams4 = arrayOf(intArrayOf(knockoutStageLosers[0], knockoutStageLosers[1]), intArrayOf(knockoutStageWinners[0], knockoutStageWinners[1]))
    }
    private fun getKnockoutTeamInfo(theTeam1: Int, theTeam2: Int, roundOrGroup: String, gameFragment: GameFragment){
        val itsName1 = allTeamNames[theTeam1]
        val itsFlag1 = allTeamImages[theTeam1]
        val itsName2 = allTeamNames[theTeam2]
        val itsFlag2 = allTeamImages[theTeam2]
        lastKnockoutTeams = arrayOf(theTeam1, theTeam2)
        gameFragment.updateTheValues(itsName1, itsName2, itsFlag1, itsFlag2, true, roundOrGroup)
    }
    fun startNewGame(){
        TheDatabase(this).resetSavedGameDetails()
        val groupStageData = GroupStageGenerator()
        allGroups = groupStageData.generateGroups(this)
        allGames = groupStageData.generateGames(this)
        gamePointer = 0
        isKnockOut = false
        resetOurKnockout()
        val sharedPrefs = getSharedPreferences(Groups.FeedEntry.TOURNAMENT_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor1 = sharedPrefs.edit()
        editor1.putBoolean(Groups.FeedEntry.IS_KNOCKOUT, isKnockOut)
        editor1.putInt(Groups.FeedEntry.CURSOR, gamePointer)
        editor1.apply()
        val sharedPrefsMainActivity = getSharedPreferences(SHARED_PREFS_MAIN_ACTIVITY, Context.MODE_PRIVATE)
        val mainActivityEditor = sharedPrefsMainActivity.edit()
        mainActivityEditor.putBoolean(GAME_EXISTS_STATE, true)
        mainActivityEditor.apply()
    }
    private fun resetOurKnockout(){
        if(knockOutStageTeams1 != null){
            knockOutStageTeams1 = null
            knockOutStageTeams2 = null
            knockOutStageTeams3 = null
            knockOutStageTeams4 = null
            top4Arrangement = null
            knockoutStageWinners.clear()
            knockoutStageLosers.clear()
        }
    }
    private fun gameOver(gameFragment: GameFragment){
        gameFragment.gameOver(allTeamNames[top4Arrangement!![1][1]], allTeamImages[top4Arrangement!![1][1]], allTeamNames[top4Arrangement!![1][0]], allTeamImages[top4Arrangement!![1][0]], allTeamNames[top4Arrangement!![0][1]], allTeamImages[top4Arrangement!![0][1]], allTeamNames[top4Arrangement!![0][0]], allTeamImages[top4Arrangement!![0][0]])
    }
    fun getStandingsValues(standingsFragment: StandingsFragment){
        standingsFragment.getTheValues(allGroups, allTeamNames, allTeamImages)
    }
    fun getKnockoutValues(knockoutFragment: KnockoutFragment){
        knockoutFragment.getKnockOutArrays(knockOutStageTeams1, knockOutStageTeams2, knockOutStageTeams3, if(knockOutStageTeams4!= null){if (knockOutStageTeams4!!.size == 2){knockOutStageTeams4!![1]}else{null}}else{null}, if(top4Arrangement != null){if (top4Arrangement!!.size == 2){top4Arrangement!![1][1]}else{null}}else{null})
    }
    private fun updateMainPoints(){
        val ourDBForUpdates = TheDatabase(this)
        val currentPoints = ourDBForUpdates.getTournamentWins()
        val dbEditor = ourDBForUpdates.writableDatabase
        for (i in 0..31){
            if (mainPointsAddition[i] != 0){
                val theUpdate = ContentValues().apply {
                    put(Main.FeedEntry.WINS, mainPointsAddition[i]+currentPoints[i])
                }
                dbEditor.update(Main.FeedEntry.TABLE_NAME, theUpdate, "${BaseColumns._ID} = ${i+1}", null)
            }
        }
        dbEditor.close()
    }
    private fun updateGroupStageOnDatabase(){
        val ourDBForUpdates = TheDatabase(this).writableDatabase
        for (i in 0..7){
                for (j in 0..3){
                    val theUpdate = ContentValues().apply {
                        put(Groups.FeedEntry.MP, allGroups[i][j][1])
                        put(Groups.FeedEntry.W, allGroups[i][j][2])
                        put(Groups.FeedEntry.D, allGroups[i][j][3])
                        put(Groups.FeedEntry.L, allGroups[i][j][4])
                        put(Groups.FeedEntry.POINTS, allGroups[i][j][5])
                    }
                    ourDBForUpdates.update(Groups.FeedEntry.TABLE_NAME, theUpdate, "${Groups.FeedEntry.TEAM} = ${allGroups[i][j][0]}", null)
                }
        }
        ourDBForUpdates.close()
    }
    private fun updateKnockoutOnDatabase(){
        val db = TheDatabase(this)
        db.clearKnockout()
        db.clearTemp()
        val ourDBForUpdates = db.writableDatabase
        if (knockOutStageTeams1 != null){
            for (i in knockOutStageTeams1!!.indices){
                val theData = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, knockOutStageTeams1!![i][0])
                    put(KnockOut.FeedEntry.B, knockOutStageTeams1!![i][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        if (knockOutStageTeams2 != null){
            for (i in knockOutStageTeams2!!.indices){
                val theData = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, knockOutStageTeams2!![i][0])
                    put(KnockOut.FeedEntry.B, knockOutStageTeams2!![i][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        if (knockOutStageTeams3 != null){
            for (i in knockOutStageTeams3!!.indices){
                val theData = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, knockOutStageTeams3!![i][0])
                    put(KnockOut.FeedEntry.B, knockOutStageTeams3!![i][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        if (knockOutStageTeams4 != null){
            for (i in knockOutStageTeams4!!.indices){
                val theData = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, knockOutStageTeams4!![i][0])
                    put(KnockOut.FeedEntry.B, knockOutStageTeams4!![i][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        if (top4Arrangement != null){
                val theData = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, top4Arrangement!![0][0])
                    put(KnockOut.FeedEntry.B, top4Arrangement!![0][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData)
            if (top4Arrangement!!.size == 2){
                val theData2 = ContentValues().apply {
                    put(KnockOut.FeedEntry.A, top4Arrangement!![1][0])
                    put(KnockOut.FeedEntry.B, top4Arrangement!![1][1])
                }
                ourDBForUpdates.insert(KnockOut.FeedEntry.TABLE_NAME, null, theData2)
            }
        }
        if (knockoutStageWinners.isNotEmpty()){
            for (i in knockoutStageWinners.indices){
                val theData = ContentValues().apply {
                    put(TempTeams.FeedEntry.TEAM, knockoutStageWinners[i])
                }
                ourDBForUpdates.insert(TempTeams.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        if (knockoutStageLosers.isNotEmpty()){
            for (i in knockoutStageLosers.indices){
                val theData = ContentValues().apply {
                    put(TempTeams.FeedEntry.TEAM, knockoutStageLosers[i])
                }
                ourDBForUpdates.insert(TempTeams.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        ourDBForUpdates.close()
    }
    private fun logger(thedata: String){
        Log.d("tournamentActivityLog", thedata)
    }
}