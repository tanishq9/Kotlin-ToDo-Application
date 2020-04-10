package com.boss.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boss.login.adapter.NotesAdapter
import com.boss.login.clickListeners.ItemClickListener
import com.boss.login.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyNotesActivity : AppCompatActivity() {

    var fullName: String? = null;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var arrayList: ArrayList<Note>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var notesAdapter: NotesAdapter
    lateinit var itemClickLister: ItemClickListener
    var tag: String = "Activity::Notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate called")
        setContentView(R.layout.activity_notes)
        bindView()
        setUpSharedPreference()
        getIntentData()
        floatingActionButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                setUpDialogBox()
            }
        })
        supportActionBar?.title = fullName
    }

    private fun setUpDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog_layout, null)
        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val alertDialog = AlertDialog.Builder(this@MyNotesActivity)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Add", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val title = editTextTitle.text.toString()
                        val description = editTextDescription.text.toString()
                        if (title.isNotEmpty() && description.isNotEmpty()) {
                            // create a new note
                            arrayList.add(Note(title, description))
                            // update the UI by modifying the adapter
                            updateUI()
                        } else {
                            Toast.makeText(this@MyNotesActivity, "Enter both title and description", Toast.LENGTH_SHORT).show()
                        }
                        p0?.dismiss()
                    }
                })
                .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()
                    }
                })
                .create()

        alertDialog.show()
    }

    private fun updateUI() {
        recyclerView.adapter = notesAdapter
    }

    private fun getIntentData() {
        if (intent.hasExtra(AppConstant.FULL_NAME)) {
            fullName = intent.getStringExtra(AppConstant.FULL_NAME)
        }
        if (fullName.isNullOrEmpty()) {
            fullName = sharedPreferences.getString(PrefConstant.FULL_NAME, "")
        }
    }

    private fun setUpSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun bindView() {
        floatingActionButton = findViewById(R.id.fabAddNote)
        recyclerView = findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(this@MyNotesActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        itemClickLister = object : ItemClickListener {
            override fun onClick(note: Note) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE, note.title)
                intent.putExtra(AppConstant.DESCRIPTION, note.description)
                startActivity(intent)
            }
        }
        arrayList = ArrayList<Note>()
        notesAdapter = NotesAdapter(arrayList, itemClickLister)
    }

}