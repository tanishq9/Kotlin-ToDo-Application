package com.boss.login.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.boss.login.R
import com.boss.login.adapter.BlogAdapter
import com.boss.login.model.Data
import com.boss.login.model.JsonResponse


class BlogActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val TAG = "BlogActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        bindView()
        getBlogs()
    }

    private fun getBlogs() {
        AndroidNetworking.get("http://www.mocky.io/v2/5926ce9d11000096006ccb30")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(JsonResponse::class.java, object : ParsedRequestListener<JsonResponse> {
                    override fun onResponse(response: JsonResponse?) {
                        setUpRecyclerView(response)
                    }

                    override fun onError(anError: ANError?) {
                        Log.e(TAG, anError?.localizedMessage)
                    }
                })
    }

    private fun bindView() {
        recyclerView = findViewById(R.id.recyclerView)

    }

    private fun setUpRecyclerView(response: JsonResponse?) {
        val blogAdapter = BlogAdapter(response?.data as ArrayList<Data>)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = blogAdapter
    }


}
