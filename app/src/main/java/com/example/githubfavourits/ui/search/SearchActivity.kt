package com.example.githubfavourits.ui.search

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omegar.mvp.ktx.providePresenter

class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {



    override val presenter: SearchPresenter by providePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView: RecyclerView = findViewById(R.id.search_recycler_view)!!

        val repositoriesList = listOf(
            RepositoriesObject("SandboxGame", true),
            RepositoriesObject("SandboxGame", false),
            RepositoriesObject("SandboxGame", false),
            RepositoriesObject("SandboxGame", false),
        )
        val adapter = CustomRecyclerAdapter(repositoriesList)
        val location = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = location


        recyclerView.adapter = adapter

    }


}