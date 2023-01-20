package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter


class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    companion object{
        var userList = arrayListOf<ResponseUser>()
        var repositoryList = arrayListOf<ResponceReposytories>()
        val adapter = CustomRecyclerAdapter(userList, repositoryList)

        fun createLauncher() = createActivityLauncher()
    }

    override val presenter: SearchPresenter by providePresenter()

    private val textBar: EditText by bind(R.id.text_bar)
    private val buttonSearch: ImageButton by bind(R.id.button_search)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView: RecyclerView = findViewById(R.id.search_recycler_view)!!
        val location = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = location
        recyclerView.adapter = adapter

        buttonSearch.setOnClickListener {

            val name: String = textBar.text.toString()
            presenter.onButtonSearchClick(name)

        }


    }


}