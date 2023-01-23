package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import com.example.githubfavourits.App.Companion.githubApi
import com.omega_r.base.mvp.presenters.OmegaPresenter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenter(): OmegaPresenter<SearchView>() {

    fun onSearch(repoName: String, userList: ArrayList<ResponseUser>, adapter: CustomRecyclerAdapter) {

//        item.name
//        launch{
//            val repos = githubApi!!.getUserData(item.login)
//        }

        var user = userList

        val repos = githubApi!!.getUserData(repoName)

        repos.enqueue(object : Callback<ArrayList<ResponseUser>>
        {

            override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {

                if (response.isSuccessful) {
                    user = (response.body())!!
                    adapter.setRepositories(user)
                } else {
                    println(response.errorBody())
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseUser>>, t: Throwable) {
                t.printStackTrace()
            }

        })


    }


}