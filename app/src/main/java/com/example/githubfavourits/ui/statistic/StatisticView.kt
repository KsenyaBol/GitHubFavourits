package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.StarredRepoData
import com.example.domain.entity.StarredRepository
import com.example.githubfavourits.ui.base.BaseView
import kotlin.collections.ArrayList

interface StatisticView: BaseView {

    var allDateBarList: ArrayList<StarredRepoData>

}