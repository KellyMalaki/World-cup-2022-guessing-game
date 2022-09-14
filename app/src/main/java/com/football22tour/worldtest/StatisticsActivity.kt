package com.football22tour.worldtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.football22tour.worldtest.adapters.StatisticsAdapter
import com.football22tour.worldtest.dialogs.ConfirmResetDialog

class StatisticsActivity : AppCompatActivity() {
    private lateinit var names: Array<String>
    private lateinit var images: IntArray
    private lateinit var wins: IntArray
    lateinit var theIndex: IntArray
    val IMAGES_STATE = "IMAGE_STATE"
    val NAMES_STATE = "NAMES_STATE"
    val WINS_STATE = "WINS_STATE"
    val INDEX_STATE = "INDEX_STATE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val imageViewInnerImage = findViewById<ImageView>(R.id.imageView_statistics_the_winner)
        val txtWinnerNameAndPoints = findViewById<TextView>(R.id.txt_statistics_the_winner)
        val listView = findViewById<ListView>(R.id.listview_statistics)

        if(savedInstanceState == null){
            val bundle = this.intent.extras
            names = bundle!!.getStringArray("names") as Array<String>
            images = bundle.getIntArray("images")!!
            wins = TheDatabase(this).getStatisticsWins(this)
        }else{
            names = savedInstanceState.getStringArray(NAMES_STATE) as Array<String>
            images = savedInstanceState.getIntArray(IMAGES_STATE)!!
            wins = savedInstanceState.getIntArray(WINS_STATE)!!
            theIndex = savedInstanceState.getIntArray(INDEX_STATE)!!
        }

        imageViewInnerImage.setImageResource(images[theIndex[0]])
        txtWinnerNameAndPoints.text= "${names[theIndex[0]]}\n${wins[0]}"
        val theAdapter = StatisticsAdapter(applicationContext, names, images, wins, theIndex)
        listView.adapter = theAdapter
        val oneInstance: View? = theAdapter.getView(0, null, listView)
        oneInstance?.measure(0, 0)
        val theHeight = oneInstance?.measuredHeight
        val ourHeight = (theHeight!!*(names.size-0.5)).toInt()
        listView.layoutParams.height = ourHeight
        (findViewById<Button>(R.id.btnStatisticsReset)).setOnClickListener {
            ConfirmResetDialog(layoutInflater, this).displayWarning()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(IMAGES_STATE, images)
        outState.putStringArray(NAMES_STATE, names)
        outState.putIntArray(WINS_STATE, wins)
        outState.putIntArray(INDEX_STATE, theIndex)
    }
    fun reloadActivity(){
        finish()
        startActivity(intent)
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}