package com.football22tour.worldtest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.football22tour.worldtest.R
import com.football22tour.worldtest.TournamentActivity

class GameFragment : Fragment() {
    lateinit var tournamentActivity: TournamentActivity
    var isKnockout: Boolean? = null
    private var nameA: String? = null
    private var nameB: String? = null
    private var nameC: String? = null
    private var flagC: Int? = null
    private var nameD: String? = null
    private var flagD: Int? = null
    private var flagA: Int? = null
    private var flagB: Int? = null
    private var roundOrGroup: String? = null

    private lateinit var gameConstraint: ConstraintLayout
    private lateinit var winnerConstraint: ConstraintLayout
    private lateinit var teamAChoice: Button
    private lateinit var teamBChoice: Button
    private lateinit var teamAImage: ImageView
    private lateinit var teamBImage: ImageView
    private lateinit var theLevel: TextView
    private lateinit var btnDraw: Button
    private lateinit var txtWinner1: TextView
    private lateinit var txtWinner2: TextView
    private lateinit var txtWinner3: TextView
    private lateinit var txtWinner4: TextView
    private lateinit var imgwinner1: ImageView
    private lateinit var imgwinner2: ImageView
    private lateinit var imgwinner3: ImageView
    private lateinit var imgwinner4: ImageView
    private lateinit var btnMenu: Button
    private lateinit var btnAgain: Button
    private var gameOver = false
    private var created = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tournamentActivity = activity as TournamentActivity
        tournamentActivity.whichGameChecker(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        gameConstraint = view.findViewById(R.id.constraint_game)
        winnerConstraint = view.findViewById(R.id.constraint_winner)
        teamAChoice = view.findViewById(R.id.btn_teamA)
        teamBChoice = view.findViewById(R.id.btn_teamB)
        teamAImage = view.findViewById(R.id.img_teamA)
        teamBImage = view.findViewById(R.id.img_teamB)
        theLevel = view.findViewById(R.id.txtLevel)
        btnDraw = view.findViewById(R.id.btnDraw)
        txtWinner1 = view.findViewById(R.id.txtWinnerTeamName)
        txtWinner2 = view.findViewById(R.id.txt_winner_2)
        txtWinner3 = view.findViewById(R.id.txt_winner_3)
        txtWinner4 = view.findViewById(R.id.txt_winner_4)
        imgwinner1 = view.findViewById(R.id.img_winner_team)
        imgwinner2 = view.findViewById(R.id.img_winner_2)
        imgwinner3 = view.findViewById(R.id.img_winner_3)
        imgwinner4 = view.findViewById(R.id.img_winner_4)
        btnMenu = view.findViewById(R.id.btn_menu)
        btnAgain = view.findViewById(R.id.btn_again)
        updateTheView()
        teamAChoice.setOnClickListener {
            tournamentActivity.gamePlayed(0, this)
            showLoading()
        }
        teamBChoice.setOnClickListener {
            tournamentActivity.gamePlayed(1, this)
            showLoading()
        }
        btnDraw.setOnClickListener {
            tournamentActivity.gamePlayed(2, this)
            showLoading()
        }
        btnMenu.setOnClickListener {
            tournamentActivity.saveAndCloseActivity()
        }
        btnAgain.setOnClickListener {
            tournamentActivity.startNewGame()
            gameOver = false
            nameC = null
            flagC = null
            nameD = null
            flagD = null
            tournamentActivity.whichGameChecker(this)
        }
        created = true
        return view
    }
    private fun updateTheView(){
        if (gameOver){
            if (winnerConstraint.visibility == View.GONE){
                winnerConstraint.visibility = View.VISIBLE
                gameConstraint.visibility = View.GONE
            }
            txtWinner1.text = nameA
            txtWinner2.text = nameB
            txtWinner3.text = nameC
            txtWinner4.text = nameD
            imgwinner1.setImageResource(flagA!!)
            imgwinner2.setImageResource(flagB!!)
            imgwinner3.setImageResource(flagC!!)
            imgwinner4.setImageResource(flagD!!)
        }else{
            if (gameConstraint.visibility == View.GONE){
                gameConstraint.visibility = View.VISIBLE
                winnerConstraint.visibility = View.GONE
            }
            teamAImage.setImageResource(flagA!!)
            teamBImage.setImageResource(flagB!!)
            teamAChoice.text = nameA
            teamBChoice.text = nameB
            theLevel.text = roundOrGroup
            if (isKnockout!!){
                btnDraw.visibility = View.GONE
            }else{
                btnDraw.visibility = View.VISIBLE
            }
        }
    }
    fun updateTheValues(nameA: String, nameB: String, flagA: Int, flagB: Int, isKnockout: Boolean, roundOrGroup: String){
        gameOver = false
        this.flagA = flagA
        this.flagB = flagB
        this.nameA = nameA
        this.nameB = nameB
        this.roundOrGroup = roundOrGroup
        this.isKnockout = isKnockout
        if (created){
            updateTheView()
        }
        hideLoading()
    }
    fun gameOver(name1: String, flag1: Int, name2: String, flag2: Int, name3: String, flag3: Int, name4: String, flag4: Int){
        gameOver = true
        nameA = name1
        flagA = flag1
        nameB = name2
        flagB = flag2
        nameC = name3
        flagC = flag3
        nameD = name4
        flagD = flag4
        if (created){
            updateTheView()
        }
        hideLoading()
    }
    private fun showLoading(){}
    private fun hideLoading(){}
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}