package com.example.githubfavourits.ui.statistic

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.ktx.providePresenter

class StatisticActivity(private val createAt: String) : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {


    companion object {

        private var direction = Entity.DAY

        fun createLauncher() = createActivityLauncher()
    }

    enum class Entity {
        DAY, MONTH, YEAR
    }

    override val presenter: StatisticPresenter by providePresenter()

    private val timeText: TextView by bind(R.id.time_text)
    private val dayButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)

    private val chart: BarChart by bind(R.id.linechart) {
        setOnChartValueSelectedListener(this@StatisticActivity)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val nowDay = Calendar.DATE
//        var repositoryAge = nowDay - createAt.toInt()

        dayButton.setOnClickListener {
            direction = Entity.DAY
        }

        monthButton.setOnClickListener {
            direction = Entity.MONTH
        }

        yearButton.setOnClickListener {
            direction = Entity.YEAR
        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }
}