package com.boss.login.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.boss.login.NotesApp
import com.boss.login.R
import com.boss.login.adapter.NotesAdapter
import com.boss.login.clickListeners.ItemClickListener
import com.boss.login.db.Notes
import com.boss.login.db.NotesDatabase
import com.boss.login.utils.AppConstant
import com.boss.login.utils.PrefConstant
import com.boss.login.workmanager.MyWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.TimeUnit


class MyNotesActivity : AppCompatActivity() {

    var fullName: String? = null;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var arrayList: ArrayList<Notes>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var notesAdapter: NotesAdapter
    lateinit var itemClickLister: ItemClickListener
    val tag: String = "Activity::Notes"
    val ADD_NOTES_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate called")
        setContentView(R.layout.activity_notes)
        bindView()
        setUpSharedPreference()
        getIntentData()
        getDataFromDatabase()
        setUpWorkManager()

        floatingActionButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.e(tag, "FAB clicked")
                val intent = Intent(this@MyNotesActivity, AddNotesActivity::class.java)
                startActivityForResult(intent, ADD_NOTES_CODE)
                // setUpDialogBox()
                /*val intent = Intent(this@MyNotesActivity, AddNotesActivity::class.java)
                startActivity(intent)*
                We will use startActivityForResult since, we will use the info from the activity called
                 */
            }
        })
        supportActionBar?.title = fullName
    }

    private fun setUpWorkManager() {
        val constraint = Constraints.Builder()
//                .setRequiresBatteryNotLow(true)
//                .setRequiresCharging(false)
//                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // minimum time limit for a periodic task in 15 minutes, so even if we
        // pass 1 minutes, it will default it to 15 minutes (i.e 90000sec)
        // therefore we can't have duration of periodic task to be less than 15 minutes
        val request = PeriodicWorkRequest
                .Builder(MyWorker::class.java, 1, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()

        WorkManager.getInstance().enqueue(request)
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDB().notesDao()
        val listOfNotes = notesDao.getAll()
        // update the arrayList with all notes
        arrayList.addAll(listOfNotes)
        Log.e(tag, arrayList.size.toString())
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
                            // add this new note to arrayList
                            arrayList.add(note)
                            // add this new note to database
                            addNote(note)
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
        // insertion of notes in db
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDB().notesDao()
        notesDao.insert(note)
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
                Log.e(tag, note.isTaskCompleted.toString())
                val notesApp = applicationContext as NotesApp
                val notesDao = notesApp.getNotesDB().notesDao()
                notesDao.update(note)
            }
        }
        arrayList = ArrayList<Notes>()
        // setup the adapter with the arrayList
        notesAdapter = NotesAdapter(arrayList, itemClickLister)
        recyclerView.adapter = notesAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(tag, "onDestroy called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_NOTES_CODE) {
                val title = data?.getStringExtra(AppConstant.TITLE)
                val description = data?.getStringExtra(AppConstant.DESCRIPTION)
                val imagePath = data?.getStringExtra(AppConstant.IMAGE_PATH)
                val notes = Notes(title = title!!, description = description!!, imagePath = imagePath!!, isTaskCompleted = false)
                arrayList.add(notes)
                recyclerView.adapter?.notifyItemChanged(arrayList.size - 1)
                addNote(notes);
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.blog) {
            Log.e(tag, "Blog menu option clicked");
        }
        return super.onOptionsItemSelected(item);
    }
}