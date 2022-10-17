package com.rehan.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rehan.newsapp.R
import com.rehan.newsapp.models.News

class NewsAdapter(private val listener: NewsItemClicked) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Create variable first to hold the position and in that variable set your text
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.ivNews)
        holder.tvTitle.text = currentItem.title
        holder.tvAuthor.text = currentItem.author
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updatedNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivNews: ImageView = itemView.findViewById(R.id.ivNews)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
    }

    interface NewsItemClicked{
        fun onItemClicked(item: News  )
    }
}