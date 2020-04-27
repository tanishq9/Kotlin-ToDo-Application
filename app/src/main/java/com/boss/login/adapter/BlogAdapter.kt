package com.boss.login.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boss.login.R
import com.boss.login.model.Data
import com.bumptech.glide.Glide

class BlogAdapter(val list: ArrayList<Data>) : RecyclerView.Adapter<BlogAdapter.BlogHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogAdapter.BlogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_layout, parent, false)
        return BlogHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BlogHolder, position: Int) {
        val blog = list.get(position)
        Glide.with(holder.itemView).load(blog.blog_url).into(holder.imageView)
        holder.textViewTitle.text = blog.title
        holder.textViewDescription.text = blog.description
    }

    inner class BlogHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = itemView.findViewById<TextView>(R.id.textViewDescription)
    }
}