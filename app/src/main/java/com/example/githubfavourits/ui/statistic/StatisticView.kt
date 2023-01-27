package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.StarredRepository
import com.example.githubfavourits.ui.base.BaseView

interface StatisticView: BaseView {

var starredList: List<StarredRepository>

}