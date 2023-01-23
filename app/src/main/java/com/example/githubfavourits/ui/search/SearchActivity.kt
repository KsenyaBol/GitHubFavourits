package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import com.example.domain.objects.user.UserName
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.omega_r.adapters.OmegaAdapter
import com.omega_r.bind.adapters.OmegaAutoAdapter
import com.omega_r.bind.model.BindModel
import com.omega_r.bind.model.binders.bindCustom
import com.omega_r.bind.model.binders.bindString
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter


class SearchActivity : BaseActivity(R.layout.activity_search), SearchView {

    companion object{

        fun createLauncher() = createActivityLauncher()
    }

    override val presenter: SearchPresenter by providePresenter()

    private var userList = arrayListOf<ResponseUser>()
    private var repositoryList = arrayListOf<ResponceReposytories>()
    private var adapter = CustomRecyclerAdapter(userList, repositoryList)

//    private val adapter = OmegaAutoAdapter.create(R.layout.list_template, {repos ->
//        presenter.onSearch(repos)
//    })

    private val textBar: EditText by bind(R.id.text_bar)
    private val buttonSearch: ImageButton by bind(R.id.button_search)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView: RecyclerView by bind(R.id.search_recycler_view) // bind

        recyclerView.adapter = adapter

        buttonSearch.setOnClickListener {

            val name: String = textBar.text.toString()
            presenter.onSearch(name, userList, adapter)

        }

//        buttonRepos.setOnClickListener {
//
//        }
//
//        if (click_flg) {
////            presenter.onElementClick(userName, reposName)
//            val text = userName
//            val duration = Toast.LENGTH_SHORT
//            Toast.makeText(applicationContext, text, duration).show()
//            !click_flg
//        }


    }


}