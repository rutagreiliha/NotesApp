package com.rgnotes.notesapp.home_screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.utils.Note

class NoteListAdapter(private val noteTitleList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private lateinit var listener: onItemClickListener


    class ViewHolder(view: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.showtitle)
        val textViewBody: TextView = view.findViewById(R.id.showbody)
        val textViewDate: TextView = view.findViewById(R.id.showdate)

        init {
            view.setOnClickListener {
                clickListener.onItemClick((bindingAdapterPosition))
            }
        }
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        listener = clickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.notelistitem, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = noteTitleList[position].title.toString()
        viewHolder.textViewBody.text = noteTitleList[position].body.toString()
        viewHolder.textViewDate.text = formatDateTime(noteTitleList[position].dateTime)
    }

    override fun getItemCount() = noteTitleList.size

    private fun formatDateTime(date: String): String {
        val day = date.split("T")[0].split("-")[2]
        val month = date.split("T")[0].split("-")[1]
        val year = date.split("T")[0].split("-")[0]
        val hour = date.split("T")[1].split(":")[0]
        val minute = date.split("T")[1].split(":")[1]
        return "$hour:$minute $day/$month/$year"
    }
}