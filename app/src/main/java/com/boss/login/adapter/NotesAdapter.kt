package com.boss.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boss.login.R
import com.boss.login.clickListeners.ItemClickListener
import com.boss.login.db.Notes

class NotesAdapter(val list: ArrayList<Notes>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.NoteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter_layout, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteHolder, position: Int) {
        val note: Notes = list[position]
        val title = note.title
        val description = note.description
        holder.textViewTitle.text = title
        holder.textViewDescription.text = description
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                itemClickListener.onClick(note)
            }
        })
        holder.checkBoxMarkStatus.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                note.isTaskCompleted = p1
                itemClickListener.onUpdate(note)
            }
        })
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById<TextView>(R.id.textViewDescription)
        val checkBoxMarkStatus: CheckBox = itemView.findViewById<CheckBox>(R.id.checkboxMarkStatus)
    }

}