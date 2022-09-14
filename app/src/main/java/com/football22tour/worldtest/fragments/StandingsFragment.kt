package com.football22tour.worldtest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.football22tour.worldtest.R
import com.football22tour.worldtest.TournamentActivity
import com.football22tour.worldtest.adapters.IndexArraySorting
import com.football22tour.worldtest.adapters.StandingsAdapter
import java.util.*

class StandingsFragment : Fragment() {
    private lateinit var tournamentActivity: TournamentActivity
    private lateinit var theTeamsInGroups: Array<Array<IntArray>>
    private lateinit var allNames: Array<String>
    private lateinit var allFlags: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tournamentActivity = activity as TournamentActivity
        tournamentActivity.getStandingsValues(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_standings, container, false)
        val theRecyclerview = view.findViewById<RecyclerView>(R.id.recyclerView_standings)
        val theTeamIndexArray = arrayListOf<IntArray>()
        for(i in theTeamsInGroups.indices){
            theTeamIndexArray.add(getTheOrder(intArrayOf(theTeamsInGroups[i][0][5], theTeamsInGroups[i][1][5], theTeamsInGroups[i][2][5], theTeamsInGroups[i][3][5])))
        }
        val theAdapter = StandingsAdapter(allNames, allFlags, theTeamsInGroups, theTeamIndexArray.toTypedArray())
        theRecyclerview.layoutManager = LinearLayoutManager(context)
        theRecyclerview.adapter = theAdapter
        return view
    }
    fun getTheValues(theGroup: Array<Array<IntArray>>, names: Array<String>, flags: IntArray){
        theTeamsInGroups = theGroup
        allNames = names
        allFlags = flags
    }
    private fun getTheOrder(thePoints: IntArray): IntArray{
        val comparator = IndexArraySorting(thePoints)
        val theFinalArray: Array<Int> = comparator.createIndexArray()
        Arrays.sort(theFinalArray, comparator)
        return intArrayOf(theFinalArray[3], theFinalArray[2], theFinalArray[1], theFinalArray[0])
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}