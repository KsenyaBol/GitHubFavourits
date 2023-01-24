package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.ResponceRepositories
import com.example.domain.entity.Repositories
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.model.binders.bindCustom
import com.omega_r.bind.model.binders.bindString
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter


class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    companion object{

        fun createLauncher() = createActivityLauncher()
    }

    override val presenter: SearchPresenter by providePresenter()

    private val adapter = OmegaAutoAdapter.create(R.layout.list_template, callback = { repos: Repositories ->
// click
    }) {
        bindString(R.id.repository_name_text, ResponceRepositories::reposName)
        bindCustom(R.id.favourite_image) { imageFavourite: ImageButton, _: Repositories -> // bindClick
            setImageFavouriteState(imageFavourite)
        }
        bindCustom(R.id.container_for_repos){ reposButton: ImageButton, _: Repositories ->
            setReposButtonState(reposButton)
        }
    }
    private val recyclerView: RecyclerView by bind(R.id.search_recycler_view, adapter)

    private val textBar: EditText by bind(R.id.text_bar)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.adapter = adapter

        setClickListener(R.id.button_search) {
            val name: String = textBar.text.toString()
            presenter.onButtonSearchClicked(name)
        }


    }

    override fun setRepository(repositories: List<Repositories>) { // setRepoList
        initRecyclerView(repositories)
    }

    private fun initRecyclerView(repositories: List<Repositories>) {
        adapter.list = repositories
        recyclerView.adapter = adapter

    }

    private fun setImageFavouriteState(imageFavourite: ImageButton) {
        imageFavourite.setOnClickListener {
            imageFavourite.isSelected = !imageFavourite.isSelected
        }

    }

    private fun setReposButtonState(reposButton: ImageButton) {
        reposButton.setOnClickListener {
            Toast.makeText(applicationContext,"will be added", Toast.LENGTH_SHORT).show()
        }
    }


}