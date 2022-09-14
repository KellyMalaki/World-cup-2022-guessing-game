package com.football22tour.worldtest.dialogs

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import com.football22tour.worldtest.R
import com.football22tour.worldtest.TournamentActivity
import com.football22tour.worldtest.fragments.GameFragment

class UndoResultsDialog(private val inflater: LayoutInflater, private val activity: TournamentActivity) {
    lateinit var dialog: AlertDialog

    fun displayDialog(unDoText: String, gameFragment: GameFragment){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val viewAdded = inflater.inflate(R.layout.back_button_dialog1, null)
        builder.setView(viewAdded)
        builder.setCancelable(true)
        val btnUndo = viewAdded.findViewById<Button>(R.id.btnBackButtonRedo)
        val btnCloseTournament = viewAdded.findViewById<Button>(R.id.btnBackButtonQuitTournament)
        btnUndo.text = unDoText
        btnUndo.setOnClickListener {
            activity.undoResults(gameFragment)
            dialog.dismiss()
        }
        btnCloseTournament.setOnClickListener {
            activity.saveAndCloseActivity()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}