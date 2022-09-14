package com.football22tour.worldtest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.football22tour.worldtest.R
import com.football22tour.worldtest.TournamentActivity

class KnockoutFragment : Fragment() {
    private lateinit var tournamentActivity: TournamentActivity
    private var knockOutStageTeams1: Array<IntArray>? = null
    private var knockOutStageTeams2: Array<IntArray>? = null
    private var knockOutStageTeams3: Array<IntArray>? = null
    private var knockOutStageTeams4: IntArray? = null
    private var winner: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tournamentActivity = activity as TournamentActivity
        tournamentActivity.getKnockoutValues(this)
        logger("on create knockout")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_knockout, container, false)

        val match11 = view.findViewById<LinearLayout>(R.id.knockout_match11)
        val match12 = view.findViewById<LinearLayout>(R.id.knockout_match12)
        val match13 = view.findViewById<LinearLayout>(R.id.knockout_match13)
        val match14 = view.findViewById<LinearLayout>(R.id.knockout_match14)

        val match11b = view.findViewById<LinearLayout>(R.id.knockout_match11b)
        val match12b = view.findViewById<LinearLayout>(R.id.knockout_match12b)
        val match13b = view.findViewById<LinearLayout>(R.id.knockout_match13b)
        val match14b = view.findViewById<LinearLayout>(R.id.knockout_match14b)

        val match21 = view.findViewById<LinearLayout>(R.id.knockout_match21)
        val match22 = view.findViewById<LinearLayout>(R.id.knockout_match22)
        val match21b = view.findViewById<LinearLayout>(R.id.knockout_match21b)
        val match22b = view.findViewById<LinearLayout>(R.id.knockout_match22b)

        val match31 = view.findViewById<LinearLayout>(R.id.knockout_match31)
        val match31b = view.findViewById<LinearLayout>(R.id.knockout_match31b)

        val match4a = view.findViewById<LinearLayout>(R.id.knockout_match4a)
        val match4b = view.findViewById<LinearLayout>(R.id.knockout_match41b)

        val match5 = view.findViewById<LinearLayout>(R.id.knockout_match5)

            if(tournamentActivity.knockOutStageTeams1!= null){
                match11.addView(viewGenerator(0, 0))
                match12.addView(viewGenerator(1, 0))
                match13.addView(viewGenerator(2, 0))
                match14.addView(viewGenerator(3, 0))
                match11b.addView(viewGenerator(4, 0))
                match12b.addView(viewGenerator(5, 0))
                match13b.addView(viewGenerator(6, 0))
                match14b.addView(viewGenerator(7, 0))

                if (tournamentActivity.knockOutStageTeams2 != null){

                    match21.addView(viewGenerator(0, 1))
                    match22.addView(viewGenerator(1, 1))
                    match21b.addView(viewGenerator(2, 1))
                    match22b.addView(viewGenerator(3, 1))

                    if (tournamentActivity.knockOutStageTeams3 != null){

                        match31.addView(viewGenerator(0, 2))
                        match31b.addView(viewGenerator(1, 2))

                        if (tournamentActivity.knockOutStageTeams4 != null){
                            match4a.addView(viewGenerator(0, 3))
                            match4b.addView(viewGenerator(1, 3))
                            if (winner != null){
                                match5.addView(viewGenerator(0, 4))
                            }else{
                                match5.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                            }
                        }else{
                            match4a.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                            match4b.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                            match5.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                        }
                    }else{
                        match31.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                        match31b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                        match4a.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                        match4b.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                        match5.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                    }
                }else{
                    match21.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match22.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match21b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match22b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match31.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match31b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                    match4a.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                    match4b.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                    match5.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                }
        }else{
                match11.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match12.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match13.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match14.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match11b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match12b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match13b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match14b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match21.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match22.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match21b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match22b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match31.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match31b.addView(layoutInflater.inflate(R.layout.knockout_instance, null))
                match4a.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                match4b.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
                match5.addView(layoutInflater.inflate(R.layout.knockout_instance_2, null))
        }
        return view
    }
    private fun viewGenerator(whichOne: Int, whichStage: Int):View{
        var theView = layoutInflater.inflate(R.layout.knockout_instance, null)
        when(whichStage){
            0 ->{
                val imageA = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_A)
                val imageB = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_B)

                val textA = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_A)
                val textB = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_B)

                imageA.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams1!![whichOne][0]])
                imageB.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams1!![whichOne][1]])

                textA.text = tournamentActivity.allTeamNames[knockOutStageTeams1!![whichOne][0]]
                textB.text = tournamentActivity.allTeamNames[knockOutStageTeams1!![whichOne][1]]
            }
            1 ->{
                val imageA = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_A)
                val imageB = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_B)

                val textA = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_A)
                val textB = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_B)

                imageA.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams2!![whichOne][0]])
                imageB.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams2!![whichOne][1]])

                textA.text = tournamentActivity.allTeamNames[knockOutStageTeams2!![whichOne][0]]
                textB.text = tournamentActivity.allTeamNames[knockOutStageTeams2!![whichOne][1]]
            }
            2 ->{
                val imageA = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_A)
                val imageB = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_1_team_B)

                val textA = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_A)
                val textB = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_1_team_B)

                imageA.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams3!![whichOne][0]])
                imageB.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams3!![whichOne][1]])

                textA.text = tournamentActivity.allTeamNames[knockOutStageTeams3!![whichOne][0]]
                textB.text = tournamentActivity.allTeamNames[knockOutStageTeams3!![whichOne][1]]
            }
            3 ->{
                theView = layoutInflater.inflate(R.layout.knockout_instance_2, null)
                val image = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_2_team)

                val text = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_2_team)

                image.setImageResource(tournamentActivity.allTeamImages[knockOutStageTeams4!![whichOne]])
                text.text = tournamentActivity.allTeamNames[knockOutStageTeams4!![whichOne]]
            }
            4 ->{
                theView = layoutInflater.inflate(R.layout.knockout_instance_2, null)
                val image = theView.findViewById<ImageView>(R.id.img_KNOCKOUT_2_team)
                val text = theView.findViewById<TextView>(R.id.txt_KNOCKOUT_2_team)

                image.setImageResource(tournamentActivity.allTeamImages[winner!!])
                text.text = tournamentActivity.allTeamNames[winner!!]
            }
        }
        return theView
    }
    fun getKnockOutArrays(knockOutStageTeams1: Array<IntArray>?, knockOutStageTeams2: Array<IntArray>?, knockOutStageTeams3: Array<IntArray>?, knockOutStageTeams4: IntArray?, winner: Int?){
        if (knockOutStageTeams1 != null){
            this.knockOutStageTeams1 = knockOutStageTeams1
            if (knockOutStageTeams2 != null){
                this.knockOutStageTeams2 = knockOutStageTeams2
                if (knockOutStageTeams3 != null){
                    this.knockOutStageTeams3 = knockOutStageTeams3
                    if (knockOutStageTeams4 != null){
                        this.knockOutStageTeams4 = knockOutStageTeams4
                        if (winner != null){
                            this.winner = winner
                        }
                    }
                }
            }
        }
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}