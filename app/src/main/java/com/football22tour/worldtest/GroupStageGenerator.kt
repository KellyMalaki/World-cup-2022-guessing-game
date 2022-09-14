package com.football22tour.worldtest

import android.content.ContentValues
import android.util.Log

class GroupStageGenerator {
    lateinit var games: Array<IntArray>
    lateinit var allTeams: Array<Array<IntArray>>
    fun generateGroups(tournamentActivity: TournamentActivity): Array<Array<IntArray>>{
        val stage3Teams = listOf(0,1,2,3,4,5,6,7).shuffled()
        val stage4Teams = listOf(8,9,10,11,12,13,14,15).shuffled()
        val stage5Teams = listOf(16,17,18,19,20,21,22,23).shuffled()
        val stage6Teams = listOf(24,25,26,27,28,29,30,31).shuffled()

        val allGroups:ArrayList<Array<IntArray>> = arrayListOf()
        val db = TheDatabase(tournamentActivity).writableDatabase
        for (i in 0..7){
            val eachGroup = arrayListOf<IntArray>()
            val teams = listOf(stage3Teams[i],stage4Teams[i],stage5Teams[i],stage6Teams[i]).shuffled().toIntArray()
            for (j in 0..3){
                val eachTeam = IntArray(6)
                eachTeam[0] = teams[j]
                eachGroup.add(eachTeam)
                //We add it to the database too
                val theData = ContentValues().apply {
                    put(Groups.FeedEntry.TEAM, teams[j])
                    put(Groups.FeedEntry.MP, 0)
                    put(Groups.FeedEntry.W, 0)
                    put(Groups.FeedEntry.D, 0)
                    put(Groups.FeedEntry.L, 0)
                    put(Groups.FeedEntry.POINTS, 0)
                    put(Groups.FeedEntry.GROUP, i)
                }
                db.insert(Groups.FeedEntry.TABLE_NAME, null, theData)
            }
            allGroups.add(eachGroup.toTypedArray())
        }
        db.close()
        allTeams = allGroups.toTypedArray()
            return allTeams
    }
    fun generateGames(tournamentActivity: TournamentActivity): Array<IntArray>{
        val allGames: ArrayList<IntArray> = arrayListOf()
        val gamesSkeleton = arrayOf(arrayOf(0,1), arrayOf(2, 3), arrayOf(1,2), arrayOf(0,3), arrayOf(2, 0), arrayOf(3, 1))
        val db = TheDatabase(tournamentActivity).writableDatabase
        //First from each group
        for(i in 0..5){
            //First from each skeleton
            for (j in 0..7){
                //Second from each group
                val teamA = allTeams[j][gamesSkeleton[i][0]][0]
                val teamB = allTeams[j][gamesSkeleton[i][1]][0]
                allGames.add(arrayOf(teamA, teamB,j).toIntArray())

                //We add it to the database too
                val theData = ContentValues().apply {
                    put(GroupGames.FeedEntry.TEAM_A, teamA)
                    put(GroupGames.FeedEntry.TEAM_B, teamB)
                    put(GroupGames.FeedEntry.GROUP, j)
                }
                db.insert(GroupGames.FeedEntry.TABLE_NAME, null, theData)
            }
        }
        db.close()
        games = allGames.toTypedArray()
        return games
    }
    private fun logger(thedata: String){
        Log.d("tryaad", thedata)
    }
}