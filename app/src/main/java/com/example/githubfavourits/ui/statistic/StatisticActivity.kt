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
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StatisticActivity : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {


    companion object {

        private const val EXTRA_NAME_USER = "nameUser"
        private const val EXTRA_REPO = "repo"
        private var direction = Entity.WEEK

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    enum class Entity(quantityValue: Int) {
        WEEK(7), MONTH(4), YEAR(12)
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

    override var allDateBarList = arrayListOf<StarredRepoData>()
        set(value) {
            field = value
            getBarChartData()
        }
    private var structureDateList = arrayListOf<DateStatistic>()
    private var barArrayList = arrayListOf<BarEntry>()
    private val weekBarList = arrayListOf<StarredRepoData>()
    private val monthBarList = arrayListOf<StarredRepoData>()
    private var yearBarList = arrayListOf<StarredRepoData>()
    private val currentDate = Date()
    private val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val dateText: String = dateFormat.format(currentDate)
    var quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
    val year: Int = 2021
    val month = Calendar.MONTH

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {

                if (direction == Entity.WEEK) {
                    quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
                }
                if (direction == Entity.MONTH) {
                    quarters = arrayOf("1st", "2nd", "3d", "4th")
                }
                if (direction == Entity.YEAR) {
                    quarters = arrayOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
                }

                return quarters.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                var count = 0

                if (direction == Entity.WEEK) {
                    count = weekBarList.size
                }
                if (direction == Entity.MONTH) {
                    count = monthBarList.size
                }
                if (direction == Entity.YEAR) {
                    count = yearBarList.size

                    yearBarList.forEach { date ->

                    }
                }

                return "$count"
            }
        }
        getBarChartData()

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

        Log.d("ListSize", allDateBarList.size.toString())

        val calendar: Calendar = GregorianCalendar()
        calendar.clear()
        calendar[Calendar.YEAR] = Calendar.YEAR
        calendar[Calendar.WEEK_OF_YEAR] = Calendar.WEEK_OF_YEAR
        val weekStart = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val weekEnd = calendar.time
        val period: ClosedRange<Date> = (weekStart..weekEnd)

        for (month in 0..11) {
            val list = arrayListOf<User>()
            allDateBarList.forEach { date ->
                if (date.favouriteAt.getDateYear() == year) {
                    if (date.favouriteAt.month == month) list.add(date.user)
                }
                structureDateList.add(RepoDateStatistic(date.favouriteAt.month, list))

            }
        }



        Log.d("structureDateList", structureDateList.toString())

        allDateBarList.forEach { date ->
            if (date.favouriteAt.getDateYear() == year) yearBarList.add(date)
            Log.d("yearList", yearBarList.toString())
        }
        allDateBarList.forEach { date ->
            if (date.favouriteAt.getDateYear() == year && date.favouriteAt.getDateMonth() == month)
                monthBarList.add(date)
        }
        allDateBarList.forEach { date ->
            if (date.favouriteAt.getDateYear() == year && date.favouriteAt.getDateMonth() == month &&
                date.favouriteAt in period
            ) weekBarList.add(date)
            Log.d("weekList", weekBarList.toString())
        }

        if (direction == Entity.WEEK) {

            barArrayList = arrayListOf(
                BarEntry(0f, 2f),
                BarEntry(1f, 4f),
                BarEntry(2f, 5f),
                BarEntry(3f, 3f),
                BarEntry(4f, 2f),
                BarEntry(5f, 6f),
                BarEntry(6f, 4f),
            )
            barchart.invalidate()
        }

        if (direction == Entity.MONTH) {

            barArrayList = arrayListOf(
                BarEntry(0f, 2f),
                BarEntry(1f, 4f),
                BarEntry(2f, 5f),
                BarEntry(3f, 3f),
            )
            barchart.invalidate()
        }

        if (direction == Entity.YEAR) {

//            var list: List<Float> = listOf()
//            structureDateList.forEach { date ->
//                if(date.favouriteAt) == month) {
//
//                }
//            }

            structureDateList.forEach { date ->
                (date.favouriteAt == month)
            }

            barArrayList = arrayListOf(
                BarEntry(0f, 2f),
                BarEntry(1f, 4f),
                BarEntry(2f, 5f),
                BarEntry(3f, 3f),
                BarEntry(4f, 2f),
                BarEntry(5f, 6f),
                BarEntry(6f, 4f),
                BarEntry(7f, 5f),
                BarEntry(8f, 8f),
                BarEntry(9f, 1f),
                BarEntry(10f, 3f),
                BarEntry(11f, 5f),
            )
            barchart.invalidate()
        }


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeText.text = dateText

        presenter.getStarredDataList()

        dayButton.setOnClickListener {
            direction = Entity.WEEK
            barchart.invalidate()
            getBarChartData()
        }

        monthButton.setOnClickListener {
            direction = Entity.MONTH
            barchart.invalidate()
            getBarChartData()

        }

        yearButton.setOnClickListener {
            direction = Entity.YEAR
            barchart.invalidate()
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

//allDateBarList.forEach { date ->
//    if (date.favouriteAt.getDateYear() == year) {
//        for (month in 0..11) {
//            val list = arrayListOf<User>()
//            if (date.favouriteAt.getDateMonth() == month) {
//                list.add(date.user)
//            }
//            structureDateList.add(RepoDateStatistic(month, list))
//        }
//    }
//}