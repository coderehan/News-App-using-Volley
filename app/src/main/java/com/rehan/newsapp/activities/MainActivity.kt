package com.rehan.newsapp.activities

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.rehan.newsapp.R
import com.rehan.newsapp.adapters.NewsAdapter
import com.rehan.newsapp.databinding.ActivityMainBinding
import com.rehan.newsapp.models.News
import com.rehan.newsapp.network.VolleySingleton

class MainActivity : AppCompatActivity(), NewsAdapter.NewsItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.setHasFixedSize(true)
        fetchData()
        adapter = NewsAdapter(this)
        binding.rvNews.adapter = adapter

    }

    private fun fetchData(){
        // URL from API
        val url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=f16aae49aae8414d83bae4ad8a00b098"

        // Create the JsonObjectRequest and handle the response
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Do something with the successful response
                // First we have JsonArray in the api and inside the JsonArray, we have JsonObject. So first get JsonArray and then get JsonObject
                val newsJsonArray =  response.getJSONArray("articles")      // "articles" is the json array name in the above api
                val newsArrayList = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),      // title is the key name in the above api
                        newsJsonObject.getString("author"),     // author is the key name in the above api
                        newsJsonObject.getString("url"),        // url is the key name in the above api
                        newsJsonObject.getString("urlToImage")  // urlToImage is the key name in the above api
                    )
                    // Finally pass this news object into array list object
                    newsArrayList.add(news)
                }
                // Now pass the array to adapter
                adapter.updatedNews(newsArrayList)
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            })
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
        // Taking user from 1 activity to another activity
        // Using CustomTab to keep the browser inside the activity itself
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))

        // Giving color to action bar of browser activity
        val color = Color.parseColor("FF6200EE")
        builder.setToolbarColor(color)

    }
}