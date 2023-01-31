package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.domain.entity.Repo
import com.example.domain.entity.StarredRepository
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
        private var direction = Entity.DAY

        private val weekMap =
            mapOf(
                Calendar.MONDAY to 0,
                Calendar.TUESDAY to 1,
                Calendar.WEDNESDAY to 2,
                Calendar.THURSDAY to 3,
                Calendar.FRIDAY to 4,
                Calendar.SATURDAY to 5,
                Calendar.SUNDAY to 6
            )

        private val monthMap =
            mapOf(
                Calendar.JANUARY to 0,
                Calendar.FEBRUARY to 1,
                Calendar.MARCH to 2,
                Calendar.APRIL to 3,
                Calendar.MAY to 4,
                Calendar.JUNE to 5,
                Calendar.JULY to 6,
                Calendar.AUGUST to 7,
                Calendar.SEPTEMBER to 8,
                Calendar.OCTOBER to 9,
                Calendar.NOVEMBER to 10,
                Calendar.DECEMBER to 11
            )

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    enum class Entity {
        DAY, MONTH, YEAR
    }

    override val presenter: StatisticPresenter by providePresenter{
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }

    private val timeText: TextView by bind(R.id.textview_time)
    private val dayButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    override var starredList: List<StarredRepository> = listOf()
    private var index: Int = 0
    private var barArrayList =  arrayListOf<BarEntry>()
    private val allDateBarList = arrayListOf<Date>()
    private val dayBarList = arrayListOf<Date>()
    private val monthBarList = arrayListOf<Date>()
    private var yearBarList = arrayListOf<Date>()
    private var nowDate = Calendar.getInstance()
    private var monthDate = nowDate.get(Calendar.MONTH)
    private var yearDate = nowDate.get(Calendar.YEAR)
    private var dayDate = nowDate.get(Calendar.DAY_OF_MONTH)
    private val currentDate = Date()
    private val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val dateText: String = dateFormat.format(currentDate)
    var quarters =  arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        getBarChartData()

        val quartersDay = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
        val quartersMonth = arrayOf("1st", "2nd", "3d", "4th")
        val quartersYear = arrayOf("Jn", "F", "Mr", "Ap", "My", "Jn", "Jl", "Au", "S", "O", "N", "D")

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (direction == Entity.DAY) {

                    nowDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    nowDate.add(Calendar.DAY_OF_MONTH, value.toInt())
                    val week = nowDate.get(Calendar.DAY_OF_WEEK)
                    val indexLocal = weekMap[week]!!
                    index = indexLocal
                    quarters = quartersDay
                }
               if (direction == Entity.MONTH) {

                   nowDate.set(Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_MONTH)
                   nowDate.add(Calendar.WEEK_OF_MONTH, value.toInt())
                   val month = nowDate.get(Calendar.WEEK_OF_MONTH)
                   val indexLocal = weekMap[month]!!
                   index = indexLocal
                   quarters = quartersMonth
               }

                if (direction == Entity.YEAR) {

                    nowDate.set(Calendar.MONTH, Calendar.JANUARY)
                    nowDate.add(Calendar.MONTH, value.toInt())
                    val year = nowDate.get(Calendar.MONTH)
                    val indexLocal = monthMap[year]!!
                    index = indexLocal
                    quarters = quartersYear
                }

                return quarters[index]
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val count = value.toInt()
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

    fun getBarChartData() {
        barArrayList = arrayListOf(
            BarEntry(0f,2f),
            BarEntry(1f,4f),
            BarEntry(2f,5f),
            BarEntry(3f,3f),
            BarEntry(4f,2f),
            BarEntry(5f,6f),
            BarEntry(6f,4f),
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        starredList.forEach { starred ->
            val dateStar = dateFormat.parse(starred.favouriteAt)
            allDateBarList.add(dateStar!!)
        }
        allDateBarList.forEach { date ->
            if (date.getDateYear() == yearDate) yearBarList.add(date)
        }
        yearBarList.forEach { date ->
            if (date.getDateMonth() == monthDate) monthBarList.add(date)
        }
        monthBarList.forEach{ date ->
            if (date.day == dayDate && date.day == dayDate - 1 && date.day == dayDate - 2 && date.day == dayDate - 3
                && date.day == dayDate - 4 && date.day == dayDate - 5 && date.day == dayDate - 6)
                dayBarList.add(date)
        }
//        dayBarList.sort()

        timeText.text = dateText

        presenter.getStarredDataList()

        dayButton.setOnClickListener {
            direction = Entity.DAY
            barchart.invalidate()
        }

        monthButton.setOnClickListener {
            direction = Entity.MONTH
            barchart.invalidate()

        }

        yearButton.setOnClickListener {
            direction = Entity.YEAR
            barchart.invalidate()

        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(applicationContext, "$e coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Toast.makeText(applicationContext, "nothing", Toast.LENGTH_SHORT).show()
    }


}