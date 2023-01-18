package com.example.githubfavourits.ui.statistic

import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omegar.mvp.ktx.providePresenter

class StatisticActivity: BaseActivity(R.layout.activity_statistic), StatisticView, OnChartValueSelectedListener {

    override val presenter: StatisticPresenter by providePresenter()

    private val chart: LineChart by bind(R.id.chart) {
        setOnChartValueSelectedListener(this@StatisticActivity)
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }
}