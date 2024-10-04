package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.moduel.volunter_structure

class volunter_adapter(students: List<volunter_structure>) : RecyclerView.Adapter<volunter_adapter.viewholder>() {


   var studentdata = ArrayList<volunter_structure>()

  init {
    studentdata.addAll(students)
  }


  class viewholder(itemview: View):RecyclerView.ViewHolder(itemview) {

    var studentname: TextView =itemview.findViewById(R.id.studentname)
    var enrollment: TextView =itemview.findViewById(R.id.enrollment)
    var instcode: TextView =itemview.findViewById(R.id.instcode)
    var instname: TextView =itemview.findViewById(R.id.instname)
    var district: TextView =itemview.findViewById(R.id.district)
    var gendre: TextView =itemview.findViewById(R.id.gendre)
    var dateOfBirth: TextView =itemview.findViewById(R.id.dateOfBirth)
    var cast: TextView =itemview.findViewById(R.id.cast)
    var branch: TextView =itemview.findViewById(R.id.branch)
    var mainsubject: TextView =itemview.findViewById(R.id.mainsubject)
    var email: TextView =itemview.findViewById(R.id.email)
    var phone: TextView =itemview.findViewById(R.id.phone)

  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
    val itemview= LayoutInflater.from(parent.context).inflate(R.layout.volunter_item,parent,false)
    return viewholder(itemview)
  }

  override fun getItemCount(): Int {
    return studentdata.size
  }

  override fun onBindViewHolder(holder: viewholder, position: Int) {
    holder.studentname.text=studentdata[position].studentname
    holder.enrollment.text=studentdata[position].enrollmentNo
    holder.instcode.text=studentdata[position].instCode
    holder.instname.text=studentdata[position].instName
    holder.district.text=studentdata[position].district
    holder.gendre.text=studentdata[position].gender
    holder.dateOfBirth.text=studentdata[position].dateOfBirth
    holder.cast.text=studentdata[position].cast
    holder.branch.text=studentdata[position].branch
    holder.mainsubject.text=studentdata[position].mainSubject
    holder.email.text=studentdata[position].email
    holder.phone.text=studentdata[position].phoneNo


  }
}