package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import com.example.githubfavourits.App
import com.example.githubfavourits.App.Companion.githubApi
import com.example.githubfavourits.ui.search.SearchActivity.Companion.adapter
import com.example.githubfavourits.ui.search.SearchActivity.Companion.repositoryList
import com.example.githubfavourits.ui.search.SearchActivity.Companion.userList
import com.omega_r.base.mvp.presenters.OmegaPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenter(): OmegaPresenter<SearchView>() {

    fun onButtonSearchClick(name: String) {

        val repos = githubApi!!.getUserData(name)

        repos.enqueue(object : Callback<ArrayList<ResponseUser>>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<ResponseUser>>, response: Response<ArrayList<ResponseUser>>) {
                if(response.isSuccessful) {

                    userList = response.body()!!
                    adapter.setReposytories(userList)

                } else {

                    println(response.errorBody())

                }

            }

            override fun onFailure(call: Call<ArrayList<ResponseUser>>, t: Throwable) {

                t.printStackTrace()

            }

        })


    }

    fun onElementClick(fullName: String) {

        val reposName = githubApi!!.getReposData(fullName)

        reposName.enqueue(object : Callback<ArrayList<ResponceReposytories>>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<ResponceReposytories>>, response: Response<ArrayList<ResponceReposytories>>) {
                if(response.isSuccessful) {

                    repositoryList = response.body()!!
                    adapter.getRepositories(repositoryList)

                } else {

                    println(response.errorBody())

                }

            }

            override fun onFailure(call: Call<ArrayList<ResponceReposytories>>, t: Throwable) {

                t.printStackTrace()

            }

        })

//        var userInfo = userList.forEach { reposytory ->
//            reposytory.id = id
//        }

    }

}