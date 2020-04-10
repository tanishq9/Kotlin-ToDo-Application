package com.boss.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boss.login.R
import com.boss.login.clickListeners.ItemClickListener
import com.boss.login.model.Note

class NotesAdapter(val list: ArrayList<Note>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.NoteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter_layout, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteHolder, position: Int) {
        val note: Note = list[position]
        val title = note.title
        val description = note.description
        holder.textViewTitle.text = title
        holder.textViewDescription.text = description
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                itemClickListener.onClick(note)
            }
        })
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById<TextView>(R.id.textViewDescription)
    }

}