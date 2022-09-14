package com.football22tour.worldtest.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.football22tour.worldtest.R

class StatisticsAdapter: BaseAdapter {
    private var context: Context
    private var names: Array<String>
    private var images: IntArray
    private var wins: IntArray
    var theIndex: IntArray
    private var inflater: LayoutInflater
    constructor(context: Context, names: Array<String>, images: IntArray, wins :IntArray, theIndex: IntArray){
        this.context = context
        this.names = names
        this.images = images
        this.wins = wins
        this.theIndex = theIndex
        this.inflater = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        return names.size-1
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return  1
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val theNumber = p0+1
        val theView: View? = inflater!!.inflate(R.layout.item_winner_recylerview_tab, null)
        val number = theView!!.findViewById<TextView>(R.id.txtStatisticsItemTheNo)
        val name = theView.findViewById<TextView>(R.id.txtStatisticsItemTeamName)
        val image = theView.findViewById<ImageView>(R.id.imageViewStatisticsItemTheImage)
        val points = theView.findViewById<TextView>(R.id.txtStatisticsItemThePoints)

        number.text = "#${theNumber+1}"
        name.text = names[theIndex[theNumber]]
        image.setImageResource(images[theIndex[theNumber]])
        points.text = wins[theNumber].toString()

        return theView
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}