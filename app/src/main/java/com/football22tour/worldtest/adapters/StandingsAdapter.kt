package com.football22tour.worldtest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.football22tour.worldtest.R

class StandingsAdapter(private val allNames: Array<String>, private val allImages: IntArray, private val groups: Array<Array<IntArray>>, private val theTeamIndex: Array<IntArray>): RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {
    private val groupNames = arrayOf("A", "B", "C", "D", "E", "F", "G", "H")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_standings_one_instance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.theGroup.text = "Group ${groupNames[position]}"

        holder.name1.text = allNames[groups[position][theTeamIndex[position][0]][0]]
        holder.name2.text = allNames[groups[position][theTeamIndex[position][1]][0]]
        holder.name3.text = allNames[groups[position][theTeamIndex[position][2]][0]]
        holder.name4.text = allNames[groups[position][theTeamIndex[position][3]][0]]

        holder.flag1.setImageResource(allImages[groups[position][theTeamIndex[position][0]][0]])
        holder.flag2.setImageResource(allImages[groups[position][theTeamIndex[position][1]][0]])
        holder.flag3.setImageResource(allImages[groups[position][theTeamIndex[position][2]][0]])
        holder.flag4.setImageResource(allImages[groups[position][theTeamIndex[position][3]][0]])

        holder.mp1.text = groups[position][theTeamIndex[position][0]][1].toString()
        holder.mp2.text = groups[position][theTeamIndex[position][1]][1].toString()
        holder.mp3.text = groups[position][theTeamIndex[position][2]][1].toString()
        holder.mp4.text = groups[position][theTeamIndex[position][3]][1].toString()

        holder.w1.text = groups[position][theTeamIndex[position][0]][2].toString()
        holder.w2.text = groups[position][theTeamIndex[position][1]][2].toString()
        holder.w3.text = groups[position][theTeamIndex[position][2]][2].toString()
        holder.w4.text = groups[position][theTeamIndex[position][3]][2].toString()

        holder.d1.text = groups[position][theTeamIndex[position][0]][3].toString()
        holder.d2.text = groups[position][theTeamIndex[position][1]][3].toString()
        holder.d3.text = groups[position][theTeamIndex[position][2]][3].toString()
        holder.d4.text = groups[position][theTeamIndex[position][3]][3].toString()

        holder.l1.text = groups[position][theTeamIndex[position][0]][4].toString()
        holder.l2.text = groups[position][theTeamIndex[position][1]][4].toString()
        holder.l3.text = groups[position][theTeamIndex[position][2]][4].toString()
        holder.l4.text = groups[position][theTeamIndex[position][3]][4].toString()

        holder.p1.text = groups[position][theTeamIndex[position][0]][5].toString()
        holder.p2.text = groups[position][theTeamIndex[position][1]][5].toString()
        holder.p3.text = groups[position][theTeamIndex[position][2]][5].toString()
        holder.p4.text = groups[position][theTeamIndex[position][3]][5].toString()
    }

    override fun getItemCount(): Int {
        return 8
    }

    class ViewHolder : RecyclerView.ViewHolder{
        val theGroup:TextView

        val name1: TextView
        val name2: TextView
        val name3: TextView
        val name4: TextView

        val flag1: ImageView
        val flag2: ImageView
        val flag3: ImageView
        val flag4: ImageView

        val mp1: TextView
        val mp2: TextView
        val mp3: TextView
        val mp4: TextView

        val w1: TextView
        val w2: TextView
        val w3: TextView
        val w4: TextView

        val d1: TextView
        val d2: TextView
        val d3: TextView
        val d4: TextView

        val l1: TextView
        val l2: TextView
        val l3: TextView
        val l4: TextView

        val p1: TextView
        val p2: TextView
        val p3: TextView
        val p4: TextView
        constructor(@NonNull itemView: View): super(itemView){
            this.theGroup = itemView.findViewById(R.id.txt_group_name)

            this.name1 = itemView.findViewById(R.id.soi_txt_1)
            this.name2 = itemView.findViewById(R.id.soi_txt_2)
            this.name3 = itemView.findViewById(R.id.soi_txt_3)
            this.name4 = itemView.findViewById(R.id.soi_txt_4)

            this.flag1 = itemView.findViewById(R.id.soi_image_1)
            this.flag2 = itemView.findViewById(R.id.soi_image_2)
            this.flag3 = itemView.findViewById(R.id.soi_image_3)
           this.flag4 = itemView.findViewById(R.id.soi_image_4)

           this.mp1 = itemView.findViewById(R.id.soi_mp_1)
           this.mp2 = itemView.findViewById(R.id.soi_mp_2)
           this.mp3 = itemView.findViewById(R.id.soi_mp_3)
           this.mp4 = itemView.findViewById(R.id.soi_mp_4)

           this.w1 = itemView.findViewById(R.id.soi_w_1)
           this.w2 = itemView.findViewById(R.id.soi_w_2)
           this.w3 = itemView.findViewById(R.id.soi_w_3)
           this.w4 = itemView.findViewById(R.id.soi_w_4)

           this.d1 = itemView.findViewById(R.id.soi_d_1)
           this.d2 = itemView.findViewById(R.id.soi_d_2)
           this.d3 = itemView.findViewById(R.id.soi_d_3)
           this.d4 = itemView.findViewById(R.id.soi_d_4)

           this.l1 = itemView.findViewById(R.id.soi_l_1)
           this.l2 = itemView.findViewById(R.id.soi_l_2)
           this.l3 = itemView.findViewById(R.id.soi_l_3)
           this.l4 = itemView.findViewById(R.id.soi_l_4)

           this.p1 = itemView.findViewById(R.id.soi_p_1)
           this.p2 = itemView.findViewById(R.id.soi_p_2)
           this.p3 = itemView.findViewById(R.id.soi_p_3)
           this.p4 = itemView.findViewById(R.id.soi_p_4)
        }

    }
}