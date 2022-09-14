package com.football22tour.worldtest.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.football22tour.worldtest.R
import com.football22tour.worldtest.StatisticsActivity
import com.football22tour.worldtest.TheDatabase

class ConfirmResetDialog(private val inflater: LayoutInflater, private val activity: StatisticsActivity) {
    lateinit var dialog: AlertDialog

    fun displayWarning(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val viewAdded = inflater.inflate(R.layout.confirm_reset_dialog, null)
        builder.setView(viewAdded)
        builder.setCancelable(true)
        val btnConfirm = viewAdded.findViewById<Button>(R.id.btnConfirmReset)
        val btnReject = viewAdded.findViewById<Button>(R.id.btnDoNotReset)
        btnConfirm.setOnClickListener {
            TheDatabase(activity.applicationContext).resetStatistics()
            Toast.makeText(activity.applicationContext, "Statistics data has been reset successfully", Toast.LENGTH_SHORT).show()
            activity.reloadActivity()
        }
        btnReject.setOnClickListener {
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}