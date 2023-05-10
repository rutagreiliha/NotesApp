package com.rgnotes.notesapp.home_screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.Note

class NoteListAdapter(private val noteTitleList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private lateinit var listener: onItemClickListener


    class ViewHolder(view: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.showtitle)

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
    }

    override fun getItemCount() = noteTitleList.size
}