package com.example.githubfavourits.ui.statistic

import com.example.domain.entity.DateStatistic
import com.example.domain.repository.RepoRepository
import com.example.githubfavourits.ui.base.BaseView

interface StatisticView: BaseView {

    var structureDateList: MutableList<DateStatistic>?
    var direction: RepoRepository.Period
    var year: Int
    var month: Int
    var day: Int
    var dayOfYear: Int
    var weekStartGlobal: String
    var weekEndGlobal: String

}