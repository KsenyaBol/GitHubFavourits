package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.DateStatistic
import com.example.githubfavourits.ui.base.BaseView

interface StatisticView: BaseView {

    var allDateBarList: MutableList<DateStatistic>?
    var structureDateList: MutableList<DateStatistic>
    var direction: StatisticPresenter.Period
    var year: Int
    var month: Int
    var day: Int
    var dayOfYear: Int
    var weekStartGlobal: String
    var weekEndGlobal: String

}