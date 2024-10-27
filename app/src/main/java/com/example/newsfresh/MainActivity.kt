package com.example.newsfresh

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity(), NewsItemclicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    };

    private fun fetchData() {
        val apikey = "0f24bc4c85e8c3d0ab573a99e1827e26"
        val url = "https://gnews.io/api/v4/search?q=example&lang=en&country=us&max=10&apikey=$apikey"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonObject = JSONObject(response)
                val newsJsonArray = jsonObject.getJSONArray("articles")
                val newsArray = ArrayList<News>()

                for (i in 0 until newsJsonArray.length()) {
                    val newsJSONObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("description"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("image")
                    )

                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            { error ->

            }
        )

        MySingleton.getInstance(this).addToRequestqueue(stringRequest)
    }


    override fun onItemClicked(item: News) {


        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))


    }

}