package com.boss.login.view

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
import com.boss.login.NotesApp
import com.boss.login.R
import com.boss.login.adapter.NotesAdapter
import com.boss.login.clickListeners.ItemClickListener
import com.boss.login.db.Notes
import com.boss.login.utils.AppConstant
import com.boss.login.utils.PrefConstant
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyNotesActivity : AppCompatActivity() {

    var fullName: String? = null;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var arrayList: ArrayList<Notes>
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

        getDataFromDatabase()

        floatingActionButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                setUpDialogBox()
            }
        })
        supportActionBar?.title = fullName
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDB().notesDao()
        val listOfNotes = notesDao.getAll()
        // init the arrayList
        arrayList.addAll(listOfNotes)
        Log.e(tag, arrayList.size.toString())
        // setup the adapter of recycler view
        notesAdapter = NotesAdapter(arrayList, itemClickLister)
        recyclerView.adapter = notesAdapter
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
                        val titleText = editTextTitle.text.toString()
                        val descriptionText = editTextDescription.text.toString()
                        if (titleText.isNotEmpty() && descriptionText.isNotEmpty()) {
                            // create a new note
                            val note = Notes(title = titleText, description = descriptionText)
                            // add this new note to database
                            addNote(note)
                            // arrayList.add(Note(title, description))
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

    private fun addNote(note: Notes) {
        // insertion of notes in db and update the recycler view
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDB().notesDao()
        notesDao.insert(note)
        // reset the arrayList
        arrayList.clear()
        arrayList.addAll(notesDao.getAll())
        Log.e(tag, arrayList.size.toString())
        // setup the adapter of recycler view again
        recyclerView.adapter = notesAdapter
    }

    private fun updateUI() {

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
            override fun onClick(note: Notes) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE, note.title)
                intent.putExtra(AppConstant.DESCRIPTION, note.description)
                startActivity(intent)
            }

            override fun onUpdate(note: Notes) {

            }
        }
        arrayList = ArrayList<Notes>()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(tag, "onDestroy called")
    }

}