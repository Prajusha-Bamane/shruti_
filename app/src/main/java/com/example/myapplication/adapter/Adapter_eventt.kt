package com.example.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.contact
import com.example.myapplication.event.Update_event
import com.example.myapplication.event.event_for_mam
import com.example.myapplication.moduel.event_structure

class adapter_contact(
    private val onDeleteClickListener: (eventId: String) -> Unit,
    private val isEventForMam: Boolean
):RecyclerView.Adapter<adapter_contact.viewholder>(){

    private var eventlist = ArrayList<event_structure>()





    class viewholder(itemview:View):RecyclerView.ViewHolder(itemview){
        val text:TextView=itemview.findViewById(R.id.eventTitle)
        val date:TextView=itemview.findViewById(R.id.eventDate)
        val description:TextView=itemview.findViewById(R.id.eventDescription)
        val image:ImageView=itemview.findViewById(R.id.eventImage)



    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val eventId = eventlist[position].id
        holder.text.text = eventlist[position].title
        holder.date.text = eventlist[position].date
        holder.description.text = eventlist[position].description
        Glide.with(holder.itemView.context).load(eventlist[position].image).into(holder.image)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (context is event_for_mam) { // Check if the context is event_for_mam
                val intent = Intent(context, Update_event::class.java)
                intent.putExtra("eventId", eventId)
                context.startActivity(intent)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (isEventForMam) {
                if (eventId != null) {
                    onDeleteClickListener(eventId)
                }
                true
            } else {
                false }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)
        return viewholder(itemview)
    }

    override fun getItemCount(): Int {
        return eventlist.size
    }

    fun update_event(eventlist: List<event_structure>){
        this.eventlist.clear()
        this.eventlist.addAll(eventlist)
        notifyDataSetChanged()
    }
}