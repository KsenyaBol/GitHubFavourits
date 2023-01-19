package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.ResponceReposytories
import com.example.githubfavourits.App.Companion.githubApi
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omegar.mvp.ktx.providePresenter
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    override val presenter: SearchPresenter by providePresenter()

    private var repositoriesList = arrayListOf<ResponceReposytories>()
//    private var searchbar: SearchView by bind(R.id.search_bar)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView: RecyclerView = findViewById(R.id.search_recycler_view)!!


        val adapter = CustomRecyclerAdapter(repositoriesList)
        val location = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = location


        recyclerView.adapter = adapter

        val repos: Call<ArrayList<ResponceReposytories>> = githubApi!!.getUserData("KsenyaBol")

        repos.enqueue(object : Callback<ArrayList<ResponceReposytories>>
        {

            override fun onResponse(call: Call<ArrayList<ResponceReposytories>>, response: Response<ArrayList<ResponceReposytories>>) {
                if(response.isSuccessful) {

                    repositoriesList = response.body()!!
                    adapter.notifyDataSetChanged()
                    adapter.setReposytories(repositoriesList)

                } else {

                    println(response.errorBody())

                }
            }
            override fun onFailure(call: Call<ArrayList<ResponceReposytories>>, t: Throwable) {
                t.printStackTrace()
            }

        })


    }


}