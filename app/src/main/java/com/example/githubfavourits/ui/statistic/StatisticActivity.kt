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
import com.omega_r.libs.extensions.date.getDateDayOfMonth
import com.omega_r.libs.extensions.date.getDateMonth
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    override var direction: Enum<StatisticPresenter.DateValue> = StatisticPresenter.DateValue.YEAR
    override var structureDateList = arrayListOf<DateStatistic>()
    override var allDateBarList: ArrayList<DateStatistic>? = null
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
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    private var barArrayList = arrayListOf<BarEntry>()
    private val currentDate = Date()
    private var dateFormatForYear: DateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private var dateFormatForMonth: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
    private var dateFormatForDay: DateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private var quarters: List<String> = listOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")

    override var weekEndGlobal: String = "dayStart"
    override var weekStartGlobal: String = "dayEnd"
    override var year: Int = dateFormatForYear.format(currentDate).toInt()
    override var month = dateFormatForMonth.format(currentDate).toInt()
    override var day = dateFormatForDay.format(currentDate).toInt()
    override var dayOfYear = 0

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {

                if (direction == StatisticPresenter.DateValue.WEEK) {
                    quarters = listOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
                }
                if (direction == StatisticPresenter.DateValue.MONTH) {
                    quarters = listOf("1st", "2nd", "3d", "4th", "5th")
                }
                if (direction == StatisticPresenter.DateValue.YEAR) {
                    quarters = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
                }
                xAxis.setLabelCount(quarters.size, false)
                return quarters.getOrNull(value.toInt()) ?: value.toString()
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
        barDataSet.color = resources.getColor(R.color.dark_greyish_blue)

        xAxis.valueFormatter = xFormatter
        axisLeft.valueFormatter = yFormatter

        invalidate()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getBarChartData() {

        presenter.barChartDataCount()

        Log.d("ListSize", allDateBarList?.size.toString())
        Log.d("datalist", structureDateList.toString())

        barchart.clear()
        barArrayList.clear()

        structureDateList.forEach { value ->
            when(direction) {
                StatisticPresenter.DateValue.WEEK ->
                    barArrayList.add(BarEntry(value.starredAt.getDateDayOfMonth().toFloat(), value.userList.size.toFloat()))
                StatisticPresenter.DateValue.MONTH ->
                    barArrayList.add(BarEntry(value.starredAt.getDateDayOfMonth() / 7f, value.userList.size.toFloat()))
                StatisticPresenter.DateValue.YEAR ->
                    barArrayList.add(BarEntry(value.starredAt.getDateMonth().toFloat(), value.userList.size.toFloat()))
            }
        }
        Log.d("barlist1", barArrayList.toString())

        val barDataSet = BarDataSet(barArrayList, "")

        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.color = resources.getColor(R.color.dark_greyish_blue)

        barchart.data = BarData(barDataSet)
        barchart.isScaleXEnabled = false
        barchart.isScaleYEnabled = false

        barDataSet.setDrawIcons(false)
        barDataSet.color = resources.getColor(R.color.dark_greyish_blue)

        barchart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeText.text = year.toString()

        presenter.getStarredDataList()

        weekButton.setOnClickListener {
            timeText.text = "$weekStartGlobal-$weekEndGlobal.$month.$year"
            presenter.weekButtonClicked()
            barchart.invalidate()
            getBarChartData()
        }

        monthButton.setOnClickListener {
            timeText.text = "$month.$year"
            presenter.monthButtonClicked()
            barchart.invalidate()
            getBarChartData()
        }

        yearButton.setOnClickListener {

            timeText.text = year.toString()
            presenter.yearButtonClicked()
            barchart.invalidate()
            getBarChartData()
        }

        backImageButton.setOnClickListener {
            presenter.backImageButtonClick()

            if (direction == StatisticPresenter.DateValue.YEAR) {
                timeText.text = year.toString()
            }

            if (direction == StatisticPresenter.DateValue.MONTH) {
                timeText.text = "$month.$year"
            }

            if (direction == StatisticPresenter.DateValue.WEEK) {
                timeText.text = "$weekStartGlobal-$weekEndGlobal.$month.$year"
            }
            getBarChartData()
        }

        nextImageButton.setOnClickListener {
            presenter.nextImageButtonClick()

            if (direction == StatisticPresenter.DateValue.YEAR) {
                timeText.text = year.toString()
            }

            if (direction == StatisticPresenter.DateValue.MONTH) {
                timeText.text = "$month.$year"
            }

            if (direction == StatisticPresenter.DateValue.WEEK) {
                timeText.text = "$weekStartGlobal-$weekEndGlobal.$month.$year"
            }

            getBarChartData()
        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(applicationContext, "$e coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Toast.makeText(applicationContext, "nothing", Toast.LENGTH_SHORT).show()
    }


}