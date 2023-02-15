package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.DateStatistic
import com.example.githubfavourits.ui.base.BaseView
import kotlin.collections.ArrayList

interface StatisticView: BaseView {

    var allDateBarList: ArrayList<DateStatistic>?
    var structureDateList: ArrayList<DateStatistic>
    var direction: Enum<StatisticPresenter.DateValue>
    var year: Int
    var month: Int
    var day: Int
    var dayOfYear: Int
    var weekStartGlobal: String
    var weekEndGlobal: String

}