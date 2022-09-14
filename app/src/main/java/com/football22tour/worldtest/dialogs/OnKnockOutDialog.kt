package com.football22tour.worldtest.dialogs

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import com.football22tour.worldtest.MainActivity
import com.football22tour.worldtest.R
import com.football22tour.worldtest.TournamentActivity

class OnKnockOutDialog(private val inflater: LayoutInflater, private val activity: TournamentActivity) {
    lateinit var dialog: AlertDialog

    fun displayWarning(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val viewAdded = inflater.inflate(R.layout.on_knockout_dialog, null)
        builder.setView(viewAdded)
        builder.setCancelable(false)
        val btnOk = viewAdded.findViewById<Button>(R.id.btn_on_knockout_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}