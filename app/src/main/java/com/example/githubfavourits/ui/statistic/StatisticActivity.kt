package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.data.AppErrorHandler
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.repository.RepoRepository
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omega_r.base.errors.ErrorHandler
import com.omega_r.libs.extensions.context.getCompatColor
import com.omega_r.libs.extensions.date.getDateDayOfMonth
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import com.omega_r.libs.extensions.view.getCompatColor
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.util.*

class StatisticActivity : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {

    companion object {

        private const val EXTRA_NAME_USER = "nameUser"
        private const val EXTRA_REPO = "repo"
        private lateinit var myResources: Resources


        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    override val presenter: StatisticPresenter by providePresenter {
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }
    override var direction: RepoRepository.Period = RepoRepository.Period.YEAR
    override var structureDateList: MutableList<DateStatistic>? = null
    @RequiresApi(Build.VERSION_CODES.O)
    set(value) {
        if (value != null) {
            field = value
            getBarChartData()
        }
    }

    private val timeText: TextView by bind(R.id.textview_time)
    private val weekButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val previousButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextButton: ImageButton by bind(R.id.imagebutton_next)

    private var barArrayList = mutableListOf<BarEntry>()
    private val currentDate = Date()

    override var weekEndGlobal: String = "dayStart"
    override var weekStartGlobal: String = "dayEnd"
    override var year: Int = currentDate.getDateYear()
    override var month = currentDate.getDateMonth()
    override var day = currentDate.getDateDayOfMonth()
    override var dayOfYear = 0

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)
        val map = mapOf(
            RepoRepository.Period.WEEK to listOf(
                myResources.getString(R.string.Monday),
                myResources.getString(R.string.Tuesday),
                myResources.getString(R.string.Wednesday),
                myResources.getString(R.string.Thursday),
                myResources.getString(R.string.Friday),
                myResources.getString(R.string.Saturday),
                myResources.getString(R.string.Sunday)
            ),

            RepoRepository.Period.MONTH to listOf(
                myResources.getString(R.string.First),
                myResources.getString(R.string.Second),
                myResources.getString(R.string.Third),
                myResources.getString(R.string.Forth),
                myResources.getString(R.string.Fifth)
            ),

            RepoRepository.Period.YEAR to listOf(
                myResources.getString(R.string.January),
                myResources.getString(R.string.February),
                myResources.getString(R.string.March),
                myResources.getString(R.string.April),
                myResources.getString(R.string.May),
                myResources.getString(R.string.June),
                myResources.getString(R.string.July),
                myResources.getString(R.string.August),
                myResources.getString(R.string.September),
                myResources.getString(R.string.October),
                myResources.getString(R.string.November),
                myResources.getString(R.string.December)
            )
        )
        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {
                myResources = resources
                barchart.xAxis.setLabelCount(map[direction]!!.size, false)
                return map[direction]?.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() { // TODO check
            override fun getFormattedValue(value: Float): String {
                val v = value.toInt()
                return "$v"
            }
        }
        val barDataSet = BarDataSet(barArrayList, "")
        val barData = BarData(barDataSet)

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.axisDependency = YAxis.AxisDependency.LEFT

        barchart.data = barData
        barchart.setGridBackgroundColor(R.color.transparent)
        barchart.setTouchEnabled(true)
        barchart.animateY(1000)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f
        xAxis.textColor = (R.color.dark_greyish_blue)
        xAxis.gridColor = (R.color.transparent)

        YAxis.YAxisLabelPosition.INSIDE_CHART

        axisRight.isEnabled = false

        axisLeft.isEnabled = false
        axisLeft.setDrawAxisLine(false)
        axisLeft.setDrawGridLines(false)
        axisLeft.gridColor = (R.color.transparent)

        description.isEnabled = false

        barDataSet.setDrawIcons(false)
        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE
        barDataSet.color = getCompatColor(R.color.dark_greyish_blue)

        xAxis.valueFormatter = xFormatter
        axisLeft.valueFormatter = yFormatter

        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myResources = resources

        timeText.text = year.toString()

        presenter.getStarredDataList()

        weekButton.setOnClickListener {
            onPeriodClicked(RepoRepository.Period.WEEK)
        }

        monthButton.setOnClickListener {
            onPeriodClicked(RepoRepository.Period.MONTH)
        }

        yearButton.setOnClickListener {
            onPeriodClicked(RepoRepository.Period.YEAR)
        }

        previousButton.setOnClickListener {
            presenter.onPreviousClicked()

            if (direction == RepoRepository.Period.YEAR) {
                timeText.text = year.toString()

            }

            if (direction == RepoRepository.Period.MONTH) {
                timeText.text = "$month.$year"

            }

            if (direction == RepoRepository.Period.WEEK) {
                timeText.text = "$weekStartGlobal-$weekEndGlobal.$month.$year"
            }

            presenter.getStarredDataList()
            getBarChartData()
        }

        nextButton.setOnClickListener {
            presenter.onNextClicked()

            when (direction) {
                RepoRepository.Period.YEAR -> timeText.text = year.toString()
                RepoRepository.Period.MONTH -> timeText.text = "$month.$year"
                RepoRepository.Period.WEEK -> timeText.text =
                    "$weekStartGlobal-$weekEndGlobal.$month.$year"
            }

            getBarChartData()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun getBarChartData() {

        Log.d("StatisticActDateList", structureDateList.toString())

        barchart.clear()
        barArrayList.clear()

        structureDateList?.forEachIndexed { index, value ->
            barArrayList.add(BarEntry(index.toFloat(), value.userList.size.toFloat()))
        }

        Log.d("StatisticActBarList", barArrayList.toString())

        val barDataSet = BarDataSet(barArrayList, "")

        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F

        barchart.data = BarData(barDataSet)
        barchart.isScaleXEnabled = false
        barchart.isScaleYEnabled = false

        barDataSet.setDrawIcons(false)
        barDataSet.color = getCompatColor(R.color.dark_greyish_blue)

        barchart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun onPeriodClicked(period: RepoRepository.Period) {

        when (period) {
            RepoRepository.Period.WEEK -> timeText.text =
                "$weekStartGlobal-$weekEndGlobal.$month.$year"
            RepoRepository.Period.MONTH -> timeText.text = "$month.$year"
            RepoRepository.Period.YEAR -> timeText.text = year.toString()
        }

        presenter.onPeriodClicked(period)
        barchart.invalidate()
        getBarChartData()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(applicationContext, "$e coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Toast.makeText(applicationContext, "nothing", Toast.LENGTH_SHORT).show()
    }

}