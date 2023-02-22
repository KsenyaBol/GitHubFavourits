package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repo
import com.example.domain.entity.RepoData
import com.example.githubfavourits.ui.base.BaseView

interface SearchView : BaseView {

    var repoList: RepoData

}