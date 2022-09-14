package com.football22tour.worldtest.dialogs

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import com.football22tour.worldtest.MainActivity
import com.football22tour.worldtest.R

class GameExistsDialog(private val inflater: LayoutInflater, private val activity: MainActivity) {
    lateinit var dialog: AlertDialog

    fun displayWarning(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val viewAdded = inflater.inflate(R.layout.continue_or_start_new_dialog, null)
        builder.setView(viewAdded)
        builder.setCancelable(true)
        val btnNewTournament = viewAdded.findViewById<Button>(R.id.btnStartNewTournament)
        val btnResumeTournamet = viewAdded.findViewById<Button>(R.id.btnContinueTournament)
        btnNewTournament.setOnClickListener {
            activity.newTournament()
            dialog.dismiss()
        }
        btnResumeTournamet.setOnClickListener {
            activity.resumeTournament()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}