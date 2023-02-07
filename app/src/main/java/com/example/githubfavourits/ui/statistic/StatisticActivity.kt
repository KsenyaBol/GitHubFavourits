package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import com.example.data.entities.RepoDateStatistic
import com.example.data.entities.StarredData
import com.example.domain.entity.DateStatistic
import com.example.domain.entity.Repo
import com.example.domain.entity.StarredRepoData
import com.example.domain.entity.User
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
import com.omega_r.libs.extensions.date.getDateYear
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
        private var direction = Entity.YEAR

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    enum class Entity() {
        WEEK, MONTH, YEAR
    }

    override val presenter: StatisticPresenter by providePresenter {
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }

    private val timeText: TextView by bind(R.id.textview_time)
    private val dayButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    override var allDateBarList: ArrayList<StarredRepoData>? = null
        set(value) {
            if (value != null) {
                field = value
                getBarChartData()
            }
        }

    private var structureDateList = arrayListOf<DateStatistic>()
    private var barArrayList = arrayListOf<BarEntry>()
    private val currentDate = Date()
    private var dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private var dateFormatForYear: DateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private var dateFormatForMonth: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
    private var dateFormatForDay: DateFormat = SimpleDateFormat("dd", Locale.getDefault())
    private val dateText: String = dateFormat.format(currentDate)
    var quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
    var year: Int = dateFormatForYear.format(currentDate).toInt()
    var month = dateFormatForMonth.format(currentDate).toInt()
    var day = dateFormatForDay.format(currentDate).toInt()
    var week = Calendar.WEEK_OF_YEAR

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {

                if (direction == Entity.WEEK) {
                    quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
                }
                if (direction == Entity.MONTH) {
                    quarters = arrayOf("1st", "2nd", "3d", "4th", "5th")
                }
                if (direction == Entity.YEAR) {
                    quarters = arrayOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
                }

                return quarters.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val count = 0
                return "$count"
            }
        }

        val barDataSet = BarDataSet(barArrayList, "")
        val barData = BarData(barDataSet)

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.setGradientColor(R.color.gray, R.color.dark_greyish_blue)
        barDataSet.axisDependency = YAxis.AxisDependency.LEFT

        barchart.data = barData
        barchart.setGridBackgroundColor(R.color.transparent)
        barchart.setTouchEnabled(true)
        barchart.animateY(1000)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f
        xAxis.textColor = (R.color.black)
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

        xAxis.valueFormatter = xFormatter
        axisLeft.valueFormatter = yFormatter

        invalidate()
    }


    @SuppressLint("SimpleDateFormat")
    private fun getBarChartData() {
        val calendar: Calendar = GregorianCalendar()
        calendar.clear()
        calendar[Calendar.YEAR] = Calendar.YEAR
        calendar[Calendar.WEEK_OF_YEAR] = Calendar.WEEK_OF_YEAR
        val weekStart = calendar.time
        calendar.add(day, 6)
        val weekEnd = calendar.time
        val period: ClosedRange<Date> = (weekStart..weekEnd)

        structureDateList.clear()

        if (direction == Entity.YEAR) {
            for (month in 0..11) {
                val list = arrayListOf<User>()
                if (allDateBarList != null) {
                    allDateBarList!!.forEach { date ->
                        if ((date.favouriteAt.year + 1900) == year && date.favouriteAt.month == month) {
                            list.add(date.user)
                        }
                    }
                }
                structureDateList.add(RepoDateStatistic(month, list))
            }
            barchart.clear()

        }

        if (direction == Entity.MONTH) {

            for (week in 0..4) {
                val list = arrayListOf<User>()
                if (allDateBarList != null) {
                    allDateBarList!!.forEach { date ->
                        if ((date.favouriteAt.year + 1900) == year && date.favouriteAt.month == month &&
                            (date.favouriteAt.getDateDayOfMonth() / 7 - 1) == week
                        ) {
                            list.add(date.user)

                        }
                    }
                }
                structureDateList.add(RepoDateStatistic(week, list))
            }
            barchart.clear()

        }

        if (direction == Entity.WEEK) {

            for (day in 0..6) {
                val list = arrayListOf<User>()
                if (allDateBarList != null) {
                    allDateBarList!!.forEach { date ->
                        if ((date.favouriteAt.year + 1900) == year && date.favouriteAt.month == month &&
                            date.favouriteAt in period && date.favouriteAt.day == day
                        ) {
                            list.add(date.user)
                        }
                    }
                }
                structureDateList.add(RepoDateStatistic(day, list))
            }
            barchart.clear()

        }

        Log.d("ListSize", allDateBarList?.size.toString())
        Log.d("datalist", structureDateList.toString())

        barchart.clear()
        barArrayList.clear()

        structureDateList.forEach { value ->
            barArrayList.add(BarEntry(value.favouriteAt.toFloat(), value.userList.size.toFloat()))
        }
        Log.d("barlist1", barArrayList.toString())

        val barDataSet = BarDataSet(barArrayList, "")
        barchart.invalidate()
        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.setGradientColor(R.color.gray, R.color.dark_greyish_blue)
        barchart.data = BarData(barDataSet)
        barDataSet.setDrawIcons(false)

        barchart.invalidate()


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        timeText.text = dateText

        presenter.getStarredDataList()

        dayButton.setOnClickListener {
            timeText.text = "$day 0$month $year"
            direction = Entity.WEEK
            barchart.invalidate()
            getBarChartData()
        }

        monthButton.setOnClickListener {
            timeText.text = "0$month $year"
            direction = Entity.MONTH
            barchart.invalidate()
            getBarChartData()
        }

        yearButton.setOnClickListener {

            timeText.text = year.toString()
            direction = Entity.YEAR
            barchart.invalidate()
            getBarChartData()
        }

        backImageButton.setOnClickListener {
            if (direction == Entity.YEAR) {
                year -= 1
                timeText.text = year.toString()
            }
            if (direction == Entity.MONTH) {
                month -= 1
                timeText.text = "0$month $year"
            }
            if (direction == Entity.WEEK) {
                day -= 6
                timeText.text = "$day 0$month $year"
            }
            getBarChartData()
        }

        nextImageButton.setOnClickListener {
            if (direction == Entity.YEAR) {
                val yearNow = Calendar.YEAR
                if (year > yearNow) {
                    year += 1
                    timeText.text = year.toString()
                }

            }
            if (direction == Entity.MONTH) {
                val monthNow = Calendar.MONTH
                if (month > monthNow) {
                    month += 1
                    timeText.text = "0$month $year"
                }

            }
            if (direction == Entity.WEEK) {
                day += 6
                timeText.text = "$day 0$month $year"

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