package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
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
import com.omega_r.libs.extensions.context.getCompatColor
import com.omega_r.libs.extensions.date.getDateYear
import com.omega_r.libs.extensions.view.getCompatColor
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.text.SimpleDateFormat
import java.util.*

class StatisticActivity : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {

    companion object {

        private const val EXTRA_NAME_USER = "nameUser"
        private const val EXTRA_REPO = "repo"

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    override val presenter: StatisticPresenter by providePresenter {
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }
    override var direction: StatisticPresenter.Period = StatisticPresenter.Period.YEAR
    override var structureDateList = mutableListOf<DateStatistic>()
    override var allDateBarList: MutableList<DateStatistic>? = null
        set(value) {
            if (value != null) {
                field = value
                getBarChartData()
            }
        }

    private val map = mapOf(
        StatisticPresenter.Period.WEEK to listOf(
            applicationContext.getString(R.string.Monday),
            applicationContext.getString(R.string.Tuesday),
            applicationContext.getString(R.string.Wednesday),
            applicationContext.getString(R.string.Thursday),
            applicationContext.getString(R.string.Friday),
            applicationContext.getString(R.string.Saturday),
            applicationContext.getString(R.string.Sunday)
        ),

        StatisticPresenter.Period.MONTH to listOf(
            applicationContext.getString(R.string.First),
            applicationContext.getString(R.string.Second),
            applicationContext.getString(R.string.Third),
            applicationContext.getString(R.string.Forth),
            applicationContext.getString(R.string.Fifth)
        ),

        StatisticPresenter.Period.YEAR to listOf(
            applicationContext.getString(R.string.January),
            applicationContext.getString(R.string.February),
            applicationContext.getString(R.string.March),
            applicationContext.getString(R.string.April),
            applicationContext.getString(R.string.May),
            applicationContext.getString(R.string.June),
            applicationContext.getString(R.string.July),
            applicationContext.getString(R.string.August),
            applicationContext.getString(R.string.September),
            applicationContext.getString(R.string.October),
            applicationContext.getString(R.string.November),
            applicationContext.getString(R.string.December)
        )
    )

    private val timeText: TextView by bind(R.id.textview_time)
    private val weekButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    private var barArrayList = mutableListOf<BarEntry>()
    private val currentDate = Date()

    override var weekEndGlobal: String = "dayStart"
    override var weekStartGlobal: String = "dayEnd"
    override var year: Int = SimpleDateFormat("yyyy", Locale.getDefault()).format(currentDate).toInt()
    override var month = SimpleDateFormat("MM", Locale.getDefault()).format(currentDate).toInt()
    override var day = SimpleDateFormat("dd", Locale.getDefault()).format(currentDate).toInt()
    override var dayOfYear = 0

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {
                xAxis.setLabelCount(map[direction]!!.size, false)
                return map[direction]!!.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
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

    @SuppressLint("SimpleDateFormat")
    private fun getBarChartData() {

        presenter.getDateCount()

        Log.d("ListSize", allDateBarList?.size.toString())
        Log.d("datalist", structureDateList.toString())

        barchart.clear()
        barArrayList.clear()

        structureDateList.forEachIndexed { index, value ->
            barArrayList.add(BarEntry(index.toFloat(), value.userList.size.toFloat()))
        }

        Log.d("barlist1", barArrayList.toString())

        val barDataSet = BarDataSet(barArrayList, "")

        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.color = getCompatColor(R.color.dark_greyish_blue)

        barchart.data = BarData(barDataSet)
        barchart.isScaleXEnabled = false
        barchart.isScaleYEnabled = false

        barDataSet.setDrawIcons(false)
        barDataSet.color = getCompatColor(R.color.dark_greyish_blue)

        barchart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeText.text = year.toString()

        presenter.getStarredDataList()

        weekButton.setOnClickListener {
            onPeriodClicked(StatisticPresenter.Period.WEEK)
        }

        monthButton.setOnClickListener {
            onPeriodClicked(StatisticPresenter.Period.MONTH)
        }

        yearButton.setOnClickListener {
            onPeriodClicked(StatisticPresenter.Period.YEAR)
        }

        backImageButton.setOnClickListener {
            presenter.onPreviousClicked()

            val firstElement = allDateBarList!!.first().starredAt.getDateYear()

            if (direction == StatisticPresenter.Period.YEAR) {
                timeText.text = year.toString()
                if (firstElement == year - 1) {
                    presenter.getStarredListRepeatedly()
                }
            }

            if (direction == StatisticPresenter.Period.MONTH) {
                timeText.text = "$month.$year"
                if (firstElement == year - 1 && month == 0) {
                    presenter.getStarredListRepeatedly()
                }
            }

            if (direction == StatisticPresenter.Period.WEEK) {
                timeText.text = "$weekStartGlobal-$weekEndGlobal.$month.$year"
                if (firstElement == year - 1 &&
                    weekStartGlobal.toInt() <= 7 && month == 0
                ) {
                    presenter.getStarredListRepeatedly()
                }
            }
            getBarChartData()
        }

        nextImageButton.setOnClickListener {
            presenter.onNextClicked()

            when (direction) {
                StatisticPresenter.Period.YEAR -> timeText.text = year.toString()
                StatisticPresenter.Period.MONTH -> timeText.text = "$month.$year"
                StatisticPresenter.Period.WEEK -> timeText.text =
                    "$weekStartGlobal-$weekEndGlobal.$month.$year"
            }

            getBarChartData()
        }

    }

    @SuppressLint("SetTextI18n")
    fun onPeriodClicked(period: StatisticPresenter.Period) {

        when (period) {
            StatisticPresenter.Period.WEEK -> timeText.text =
                "$weekStartGlobal-$weekEndGlobal.$month.$year"
            StatisticPresenter.Period.MONTH -> timeText.text = "$month.$year"
            StatisticPresenter.Period.YEAR -> timeText.text = year.toString()
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