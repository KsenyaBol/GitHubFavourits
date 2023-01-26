package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.RepoBody
import com.example.domain.entity.Repo
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.model.binders.bindString
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter

class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    companion object {
        fun createLauncher() = createActivityLauncher()
    }

    override val presenter: SearchPresenter by providePresenter()

    private val adapter = OmegaAutoAdapter.create(R.layout.list_template, callback = { repos: Repo ->
        presenter.onReposButtonClicked(repos)
    }) {
        bindString(R.id.repository_name_text, RepoBody::reposName)
    }

    private val recyclerView: RecyclerView by bind(R.id.search_recycler_view, adapter)

    private val userNameEditText: EditText by bind(R.id.edittext_user_name)

    override var repoList: List<Repo> = listOf()
        set(value) {
            field = value
            adapter.list = value
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setClickListener(R.id.button_search) {
            val name: String = userNameEditText.text.toString()
            presenter.onButtonSearchClicked(name)
        }
    }


}