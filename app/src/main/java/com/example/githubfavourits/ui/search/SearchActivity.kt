package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.example.domain.entity.Repo
import com.example.domain.entity.RepoData
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.model.binders.bindString
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter


class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    companion object {

        private const val PREVENTION_VALUE = 5

        fun createLauncher() = createActivityLauncher()
    }

    override val presenter: SearchPresenter by providePresenter()

    private val adapter = OmegaAutoAdapter.create(R.layout.list_template, callback = { repos: Repo ->
        presenter.onReposButtonClicked(repos)
    }) {
        bindString(R.id.repository_name_text, Repo::name)
    }

    private val recyclerView: OmegaRecyclerView by bind(R.id.search_recycler_view, adapter)

    private val userNameEditText: EditText by bind(R.id.edittext_user_name)
    private val page: Int = 1

    override var repoList: RepoData = RepoData(repoList = listOf(), false)
        set(value) {
            field = value
            adapter.list = value.repoList
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userNameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.onButtonSearchClicked(userNameEditText.text.toString(), recyclerView)
                    true
                } else false

        }

        setClickListener(R.id.button_search) {
            val name: String = userNameEditText.text.toString()
            presenter.onButtonSearchClicked(name, recyclerView)
        }
    }


}