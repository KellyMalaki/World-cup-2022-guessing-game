package com.football22tour.worldtest

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.football22tour.worldtest.dialogs.GameExistsDialog
import java.util.*

const val NAMES_STATE = "NAMES_STATE"
const val IMAGES_STATE = "IMAGES_STATE"
const val GAME_EXISTS_STATE = "GAME_STATE"
const val SHARED_PREFS_MAIN_ACTIVITY  = "shared_prefs_main_activity"
class MainActivity : AppCompatActivity() {
    lateinit var teamNames: Array<String>
    lateinit var teamImages: IntArray
    var gameExists: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            val theCurrentTime = Calendar.getInstance().timeInMillis
            if (theCurrentTime > 1663965352488){
                //Close app
                finish()
            }else {
                val sharedPrefsCorrectImages = "correct_images"
                val sharedPrefs = getSharedPreferences(SHARED_PREFS_MAIN_ACTIVITY, Context.MODE_PRIVATE)
                if (!sharedPrefs.getBoolean(sharedPrefsCorrectImages, false)) {
                    DataEntry(this).runDataEntry()
                }
                gameExists = sharedPrefs.getBoolean(GAME_EXISTS_STATE, false)
                TheDatabase(this).completeData(this)
            }
        }else{
            gameExists = savedInstanceState.getBoolean(GAME_EXISTS_STATE)
            teamNames = savedInstanceState.getStringArray(NAMES_STATE) as Array<String>
            teamImages = savedInstanceState.getIntArray(IMAGES_STATE)!!
        }
        //Start normal activities
        val btnAppDetails: ImageView = findViewById(R.id.btn_privacy_policies)
        val btnStartSimulator: Button = findViewById(R.id.btn_begin)
        val btnStatistics: Button = findViewById(R.id.btn_statistics)
        btnAppDetails.setOnClickListener{
            //We replace it with this toast message
            Toast.makeText(this, "This would show a pop up dialog with information about the app.", Toast.LENGTH_SHORT).show()
        }
        btnStartSimulator.setOnClickListener {
            if (gameExists){
               GameExistsDialog(layoutInflater, this).displayWarning()
            }else{
                newTournament()
            }
        }
        btnStatistics.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra("names", teamNames)
            intent.putExtra("images", teamImages)
            startActivity(intent)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(GAME_EXISTS_STATE, gameExists)
        outState.putIntArray(IMAGES_STATE, teamImages)
        outState.putStringArray(NAMES_STATE, teamNames)
    }
    fun restartActivity(){
        this.finishAffinity()
        startActivity(intent)
    }
    fun newTournament(){
        val intent = Intent(this, TournamentActivity::class.java)
        intent.putExtra("names", teamNames)
        intent.putExtra("images", teamImages)
        intent.putExtra("resume", false)
        gameExists = true
        startActivity(intent)
    }
    fun resumeTournament(){
        val intent = Intent(this, TournamentActivity::class.java)
        intent.putExtra("names", teamNames)
        intent.putExtra("images", teamImages)
        intent.putExtra("resume", true)
        startActivity(intent)
    }
}