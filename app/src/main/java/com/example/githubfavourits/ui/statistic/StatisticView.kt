package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.StarredRepository
import com.example.githubfavourits.ui.base.BaseView
import com.github.mikephil.charting.charts.BarChart

interface StatisticView: BaseView {

    var starredList: List<StarredRepository>

}