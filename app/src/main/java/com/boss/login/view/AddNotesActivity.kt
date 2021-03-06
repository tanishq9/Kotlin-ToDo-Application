package com.boss.login.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.boss.login.BuildConfig
import com.boss.login.R
import com.boss.login.utils.AppConstant
import com.bumptech.glide.Glide
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.jar.Manifest

class AddNotesActivity : AppCompatActivity() {

    // either initialize it or use lateinit to initialize this widget later
    lateinit var editTextTile: EditText
    lateinit var editTextDescription: EditText
    lateinit var buttonSubmit: Button
    lateinit var imageView: ImageView
    val REQUEST_CODE_GALLERY = 2
    val REQUEST_CODE_CAMERA = 1
    val MY_PERMISSION_CODE = 1234
    var picturePath = ""
    val TAG = "Activity::AddNotes"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        Log.e(TAG, "AddNotesAct created")
        bindViews()
        clickListeners()
    }

    private fun clickListeners() {
        imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (checkAndRequestPermission()) {
                    setUpDialog()
                }
            }
        })

        buttonSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // pass the data from this activity to MyNotesActivity, since we are passing this activity using onActivityResult
                val intent = Intent()
                intent.putExtra(AppConstant.TITLE, editTextTile.text.toString())
                intent.putExtra(AppConstant.DESCRIPTION, editTextDescription.text.toString())
                intent.putExtra(AppConstant.IMAGE_PATH, picturePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        })

    }

    private fun checkAndRequestPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionNeeded = ArrayList<String>()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toTypedArray<String>(), MY_PERMISSION_CODE)
            return false
        }
        return true
    }

    // this function is used to handle the permission that we requested in the checkAndRequestPermission function
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    setUpDialog()
                }
            }
        }
    }

    private fun setUpDialog() {
        val view: View = LayoutInflater.from(this@AddNotesActivity).inflate(R.layout.dialog_selector, null)
        val textViewCamera: TextView = view.findViewById(R.id.textViewCamera)
        val textViewGallery: TextView = view.findViewById(R.id.textViewGallery)
        val dialog: AlertDialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create()

        // add click listeners for both the text views in the dialog
        textViewCamera.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var photoFile: File? = null
                // create a file
                photoFile = createImage()
                if (photoFile != null) {
                    // we are getting the local file path using file provider using the given content provider authority
                    val photoURI = FileProvider.getUriForFile(this@AddNotesActivity, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                    // we will add file provider (content provider) in the manifest file
                    //  A Content Provider is used to share and access data from a central repository.
                    //  Usually android applications keep data hidden from the other applications but sometimes,
                    //  it is useful to share data across them.
                    // that is what we have to mention in the manifest file as well otherwise app crashes
                    picturePath = photoFile.absolutePath
                    Log.e(TAG, picturePath)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    Log.e(TAG, "set imageView in onActivityResult()")
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
                    dialog.dismiss()
                }
            }
        })

        textViewGallery.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // to open a new app, we use implicit intent
                // earlier we were using explicit intent
                // since we were navigating in the same app
                // so instead of passing context in the Intent()
                // constructor, we will write the Action
                val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                // to open a gallery, we wont to go to gallery and get the data back on our app
                // i.e why we use startActivityForResult()
                startActivityForResult(intent, REQUEST_CODE_GALLERY)
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun createImage(): File? {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(java.util.Date())
        val fileName = "JPEG_" + timestamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "GOT RESULT")
            // if result code is equal to activity RESULT_OK, then this means
            // we have got a result from the activity that was called
            // since we called an external app using implicit intent
            // therefore the result will be what we get from that app
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    val selectedImage = data?.data
                    Log.e(TAG, data.toString())
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    Log.e(TAG, filePath.contentToString())
                    // content resolver helps us to get the access of different content providers
                    val c = contentResolver.query(selectedImage!!, filePath, null, null, null)
                    if (c != null) {
                        c.moveToFirst()
                        val columnIndexedValue = c.getColumnIndex(filePath[0])
                        picturePath = c.getString(columnIndexedValue)
                        c.close()
                        Log.e(TAG, picturePath)
                        Glide.with(this).load(picturePath).into(imageView)
                    }
                }

                REQUEST_CODE_CAMERA -> {
                    Glide.with(this).load(picturePath).into(imageView)
                }
            }
        }
    }

    private fun bindViews() {
        editTextTile = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        imageView = findViewById(R.id.imageView)
    }
}
